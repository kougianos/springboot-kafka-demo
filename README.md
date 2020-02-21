This repository contains 2 spring boot microservices that communicate with Kafka. The first microservice exposes a REST API and receives JSON messages as input, and then produces a Kafka message to a specific topic. The second microservice contains a Kafka listener which is a subscriber to the aforementioned topic, takes the message, transforms it and sends it back to Kafka to a different topic. The first microservice contains a listener to this topic and receives the transformed message.

## Running locally
A kafka installation is required to run the project locally.
