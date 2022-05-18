import re

from flask import Flask, request, Response

from yolov5.plate_detector import data_uri_to_cv2_img, Detector

app = Flask(__name__)
detector = Detector()
license_plate_pattern = re.compile("[A-Z]{1,3}[0-9]{2,5}[A-Z]?")


@app.route('/', methods=["POST"])
def hello_world():
    raw_data = request.get_data()
    image = data_uri_to_cv2_img(raw_data)
    plate, _ = detector.detect(image)
    s = re.sub(r'\W+', '', plate)
    if license_plate_pattern.match(s):
        return Response(s, status=200, mimetype='text/plain')
    return Response("plate-not-found", status=403, mimetype='text/plain')


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8080)
