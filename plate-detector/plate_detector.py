import base64
import os
import re
import sys

import cv2
import easyocr
import numpy as np
import torch

sys.path.insert(1, 'yolov5')

from models.experimental import attempt_load
from utils.datasets import letterbox
from utils.general import check_img_size, non_max_suppression, \
    scale_coords, xyxy2xywh
from utils.torch_utils import select_device

IOU_THRES = 0.45
CONF_THRES = 0.4
IMG_SIZE = 640


class Detector:
    def __init__(self):
        self.device = select_device(os.getenv('DEVICE', ''))
        self.half = self.device.type != 'cpu'
        self.model = attempt_load(os.environ['WEIGHTS_PATH'], map_location=self.device)
        self.reader = easyocr.Reader(['es'])
        self.stride = int(self.model.stride.max())
        self.imgsz = check_img_size(IMG_SIZE, s=self.stride)
        if self.half:
            self.model.half()

    def detect(self, img0s):
        img = letterbox(img0s, self.imgsz, stride=self.stride)[0]
        img = img[:, :, ::-1].transpose(2, 0, 1)  # BGR to RGB, to 3x416x416
        img = np.ascontiguousarray(img)

        img = torch.from_numpy(img).to(self.device)
        img = img.half() if self.half else img.float()
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        pred = self.model(img, augment=False)[0]
        pred = non_max_suppression(pred, CONF_THRES, IOU_THRES, classes=None, agnostic=False)
        primary_bb = extract_biggest_detection(img0s, img, pred)
        cropped = crop_image(img0s, primary_bb)
        # cv2.imwrite(f"archivo.jpg", cropped)

        return self.get_license_plate(cropped), cropped

    def get_license_plate(self, cropped):
        text_detections = self.reader.readtext(cropped)
        max_area = 0
        license_plate = ''
        for text in text_detections:
            area = (int(text[0][1][0]) - int(text[0][0][0])) * (int(text[0][3][1]) - int(text[0][1][1]))
            if area > max_area:
                max_area = area
                license_plate = text[1]
        return license_plate


def data_uri_to_cv2_img(encoded_data):
    np_array = np.frombuffer(base64.b64decode(encoded_data), np.uint8)
    img_read = cv2.imdecode(np_array, cv2.IMREAD_COLOR)
    return img_read


def get_area(xywh):
    return xywh[2] * xywh[3]


def crop_image(img0s, primary_bb):
    h, w, _ = img0s.shape
    x1 = int(primary_bb[0] * w) - int(primary_bb[2] * w / 2)
    y1 = int(primary_bb[1] * h) - int(primary_bb[3] * h / 2)
    x2 = int(primary_bb[2] * w) + x1
    y2 = int(primary_bb[3] * h) + y1
    cropped = img0s[y1:y2, x1:x2]
    return cropped


def extract_biggest_detection(org_img, img, pred):
    max_area = 0
    primary_bb = tuple()
    for i, det in enumerate(pred):
        gn = torch.tensor(org_img.shape)[[1, 0, 1, 0]]
        if len(det):
            det[:, :4] = scale_coords(img.shape[2:], det[:, :4], org_img.shape).round()
            for *xyxy, conf, cls in reversed(det):
                xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()
                area = get_area(xywh)
                if area > max_area:
                    max_area = area
                    primary_bb = xywh
    return primary_bb


if __name__ == '__main__':
    with open(sys.argv[1], "rb") as image_file:
        encoded_string = base64.b64encode(image_file.read())
        image = data_uri_to_cv2_img(encoded_string)
        plate, plate_image = Detector().detect(image)
        cv2.imwrite('test.png', plate_image)
        s = re.sub(r'\W+', '', plate)

        pattern = re.compile("[A-Z]{1,3}[0-9]{2,5}[A-Z]?")
        print(pattern.match(s))
        print(s)
