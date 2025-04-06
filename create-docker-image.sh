#!/bin/bash

# Variables
IMAGE_NAME="cedric10101980/cp-order-service"
TAG="1.0.0"

# Build the Docker image
docker build -t ${IMAGE_NAME}:${TAG} .

# Tag the Docker image
docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest

# Push the Docker image to Docker Hub
docker push ${IMAGE_NAME}:${TAG}
docker push ${IMAGE_NAME}:latest