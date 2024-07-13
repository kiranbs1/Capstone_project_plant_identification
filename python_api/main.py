import base64
import os
import heapq
import random
from http.client import HTTPException

import numpy as np
import tensorflow as tf
from flask import Flask, request, jsonify
from flask_restful import Api, Resource
from PIL import Image
from io import BytesIO
import logging
import traceback

app = Flask(__name__)
api = Api(app)
MODEL = tf.keras.models.load_model("../python_model/models/final 105 flower model")

pathToImages = "../python_model/training data/modified105Flowers/"
class_names_package = "../python_model/training data/modified105Flowers/"
class_names = os.listdir(class_names_package)

logging.basicConfig(level=logging.DEBUG)
@app.errorhandler(Exception)
def handle_exception(e):
    """
    Global error handler to catch exceptions and return appropriate responses.
    """
    if isinstance(e, HTTPException):
        return e

    traceback.print_exc()
    return jsonify(error=str(e)), 500


def get_images(directory, num_of_images):
    """
    Load images from a directory and encode them into base64 format.
    """
    base64_images = []
    contents = os.listdir(directory)
    for i in range(num_of_images):
        image = contents[random.randrange(0, len(contents))]
        with open(directory + image, "rb") as image_file:
            data = base64.b64encode(image_file.read())
            data_to_return = str(data)[2:-1]
            base64_images.append(data_to_return)
    return base64_images


class Classification(Resource):
    """
    Resource class to handle image classification requests.
    """
    def post(self):
        """
        Handle POST requests for image classification.
        """
        try:
            image_base_64 = base64.b64decode(request.form.get('image'))
            pil_image = Image.open(BytesIO(image_base_64))
            pil_image = pil_image.resize(size=[256, 256])
            image = np.array(pil_image)
            img_batch = np.expand_dims(image, 0)

            predictions = MODEL.predict(img_batch)
            classified_indexes = heapq.nlargest(3, range(len(predictions[0])), key=predictions[0].__getitem__)
            predicted_class = class_names[classified_indexes[0]]
            predicted_class_two = class_names[classified_indexes[1]]
            predicted_class_three = class_names[classified_indexes[2]]

            predicted_class_rating = predictions[0][classified_indexes[0]]
            predicted_class_two_rating = predictions[0][classified_indexes[1]]
            predicted_class_three_rating = predictions[0][classified_indexes[2]]

            predicted_class_images = get_images(pathToImages + predicted_class + "/", 3)
            predicted_class_images_two = get_images(pathToImages + predicted_class_two + "/", 3)
            predicted_class_images_three = get_images(pathToImages + predicted_class_three + "/", 3)
            to_return = {
                "class_one": predicted_class,
                "class_one_rating": float(predicted_class_rating),
                "class_one_images": predicted_class_images,
                "class_two": predicted_class_two,
                "class_two_rating": float(predicted_class_two_rating),
                "class_two_images": predicted_class_images_two,
                "class_three": predicted_class_three,
                "class_three_rating": float(predicted_class_three_rating),
                "class_three_images": predicted_class_images_three
            }
            return jsonify(to_return)
        except Exception as e:
            logging.exception(e)


class RequestRandomImages(Resource):
    """
    Resource class to handle requests for random images.
    """
    def get(self, num_of_images):
        """
        Handle GET requests for random images.
        """
        requests_to_return = []
        for i in range(num_of_images):
            r = random.randrange(0, len(class_names))
            random_class = class_names[r]
            image = get_images(pathToImages + random_class + "/", 1)
            data_to_return = {
                "name": random_class,
                "image": image
            }
            requests_to_return.append(data_to_return)
        to_return = {
            "plants": requests_to_return
        }
        return jsonify(to_return)


api.add_resource(Classification, "/classify")
api.add_resource(RequestRandomImages, "/random_images/<int:num_of_images>")


if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5000')
