import base64
import os
import re
import sys
from pathlib import Path

import easyocr
import numpy as np
import torch
import torch.backends.cudnn as cudnn

from models.common import DetectMultiBackend
from utils.dataloaders import IMG_FORMATS, VID_FORMATS, LoadImages, LoadStreams
from utils.general import (LOGGER, check_file, check_img_size, check_imshow, cv2, increment_path, non_max_suppression,
                           scale_coords, xyxy2xywh)
from utils.plots import Annotator, colors, save_one_box
from utils.torch_utils import select_device, time_sync

FILE = Path(__file__).resolve()
ROOT = FILE.parents[0]
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))
ROOT = Path(os.path.relpath(ROOT, Path.cwd()))


class Detector:
    def __init__(self):
        self.weights = os.getenv('WEIGHTS_PATH', default='')
        self.device = select_device()
        self.model = DetectMultiBackend(os.environ['WEIGHTS_PATH'], device=self.device, dnn=False, fp16=False)
        stride = self.model.stride
        self.imgsz = check_img_size((640, 640), s=stride)
        gpu = self.device.type != 'cpu'
        self.reader = easyocr.Reader(['es'], gpu=gpu)

    def detect(self,
               source,
               imgsz=(640, 640),
               conf_thres=0.25,
               iou_thres=0.45,
               max_det=1000,
               view_img=False,
               save_txt=False,
               save_conf=False,
               save_crop=False,
               nosave=True,
               classes=None,
               agnostic_nms=False,
               augment=False,
               visualize=False,
               project=ROOT / 'runs/detect',
               name='exp',
               exist_ok=True,
               line_thickness=3,
               hide_labels=False,
               hide_conf=False):

        global cropped
        source = str(source)
        save_img = not nosave and not source.endswith('.txt')  # save inference images
        is_file = Path(source).suffix[1:] in (IMG_FORMATS + VID_FORMATS)
        is_url = source.lower().startswith(('rtsp://', 'rtmp://', 'http://', 'https://'))
        webcam = source.isnumeric() or source.endswith('.txt') or (is_url and not is_file)
        if is_url and is_file:
            source = check_file(source)  # download

        # Directories
        if save_img or save_crop or save_conf or save_conf:
            save_dir = increment_path(Path(project) / name, exist_ok=exist_ok)  # increment run
            (save_dir / 'labels' if save_txt else save_dir).mkdir(parents=True, exist_ok=True)  # make dir

        stride, names, pt = self.model.stride, self.model.names, self.model.pt

        model = self.model

        # Dataloader
        if webcam:
            view_img = check_imshow()
            cudnn.benchmark = True  # set True to speed up constant image size inference
            dataset = LoadStreams(source, img_size=imgsz, stride=stride, auto=pt)
            bs = len(dataset)  # batch_size
        else:
            dataset = LoadImages(source, img_size=imgsz, stride=stride, auto=pt)
            bs = 1  # batch_size

        # Run inference
        model.warmup(imgsz=(1 if pt else bs, 3, *imgsz))  # warmup
        dt, seen = [0.0, 0.0, 0.0], 0
        for path, im, im0s, vid_cap, s in dataset:
            t1 = time_sync()
            im = torch.from_numpy(im).to(self.device)
            im = im.half() if model.fp16 else im.float()  # uint8 to fp16/32
            im /= 255  # 0 - 255 to 0.0 - 1.0
            if len(im.shape) == 3:
                im = im[None]  # expand for batch dim
            t2 = time_sync()
            dt[0] += t2 - t1

            # Inference
            visualize = increment_path(save_dir / Path(path).stem, mkdir=True) if visualize else False
            pred = model(im, augment=augment, visualize=visualize)
            t3 = time_sync()
            dt[1] += t3 - t2

            # NMS
            pred = non_max_suppression(pred, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)
            dt[2] += time_sync() - t3

            for i, det in enumerate(pred):  # per image
                seen += 1
                if webcam:  # batch_size >= 1
                    p, im0, frame = path[i], im0s[i].copy(), dataset.count
                    s += f'{i}: '
                else:
                    p, im0, frame = path, im0s.copy(), getattr(dataset, 'frame', 0)
                if save_img or save_crop or save_conf or save_conf:
                    p = Path(p)  # to Path
                    save_path = str(save_dir / p.name)  # im.jpg
                    txt_path = str(save_dir / 'labels' / p.stem) + (
                        '' if dataset.mode == 'image' else f'_{frame}')  # im.txt

                s += '%gx%g ' % im.shape[2:]  # print string
                gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
                imc = im0.copy() if save_crop else im0  # for save_crop
                annotator = Annotator(im0, line_width=line_thickness, example=str(names))
                max_area = 0
                if len(det):

                    det[:, :4] = scale_coords(im.shape[2:], det[:, :4], im0.shape).round()

                    for c in det[:, -1].unique():
                        n = (det[:, -1] == c).sum()  # detections per class
                        s += f"{n} {names[int(c)]}{'s' * (n > 1)}, "  # add to string

                    # Write results
                    for *xyxy, conf, cls in reversed(det):
                        xywh = (xyxy2xywh(torch.tensor(xyxy).view(1, 4)) / gn).view(-1).tolist()  # normalized xywh
                        if save_txt:  # Write to file
                            line = (cls, *xywh, conf) if save_conf else (cls, *xywh)  # label format
                            with open(f'{txt_path}.txt', 'a') as f:
                                f.write(('%g ' * len(line)).rstrip() % line + '\n')

                        area = get_area(xywh)
                        if area > max_area:
                            cropped = save_one_box(xyxy, imc, BGR=True, save=False)
                            max_area = area

                        if save_img or save_crop or view_img:  # Add bbox to image
                            c = int(cls)  # integer class
                            label = None if hide_labels else (names[c] if hide_conf else f'{names[c]} {conf:.2f}')
                            annotator.box_label(xyxy, label, color=colors(c, True))
                        if save_crop:
                            save_one_box(xyxy, imc, file=save_dir / 'crops' / names[c] / f'{p.stem}.jpg',
                                         BGR=True)

                im0 = annotator.result()
                if view_img:
                    cv2.imshow(str(p), im0)
                    cv2.waitKey(1000)  # 1 millisecond

                if save_img:
                    if dataset.mode == 'image':
                        cv2.imwrite(save_path, im0)

            LOGGER.info(f'{s}Done. ({t3 - t2:.3f}s)')

        return self.get_license_plate(cropped), cropped

    def get_license_plate(self, cropped_img):
        text_detections = self.reader.readtext(cropped_img)
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


if __name__ == '__main__':
    with open(sys.argv[1], "rb") as image_file:
        plate, plate_image = Detector().detect(sys.argv[1])
        cv2.imwrite('test.png', plate_image)
        s = re.sub(r'\W+', '', plate)

        pattern = re.compile("[A-Z]{1,3}[0-9]{2,5}[A-Z]?")
        print(pattern.match(s))
        print(s)
