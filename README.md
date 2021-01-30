This repository contains 2 spring boot microservices that communicate with Kafka. The first microservice exposes a REST API and receives JSON messages as input, and then produces a Kafka message to a specific topic. The second microservice contains a Kafka listener which is a subscriber to the aforementioned topic, takes the message, transforms it and sends it back to Kafka to a different topic. The first microservice contains a listener to this topic and receives the transformed message.

## Running locally
A Zookeeper/Kafka installation is required to run the project locally.\
More information on how to install on windows 10:\
https://dzone.com/articles/running-apache-kafka-on-windows-os \
https://towardsdatascience.com/running-zookeeper-kafka-on-windows-10-14fc70dcc771 \

Maven installation is also required:\
https://howtodoinjava.com/maven/how-to-install-maven-on-windows/ \

Sample JSON to POST on receiver microservice (at **localhost:8081/postPerson**) \
```json
{
  "name": "nikos",
  "dateOfBirth": 234216745100,
  "pets": [
    {
      "name": "max",
      "breed": "pitbull"
    },
    {
      "name": "ira",
      "breed": "bull-terrier"
    }
  ]
}
```
The project has been tested with JDK11 on Windows 10, using Intellij IDEA.\
Steps to run it from a command line (assuming zookeeper and kafka are up and running):\
`git clone https://github.com/kougianos/springboot-kafka-demo.git`\
`cd springboot-kafka-demo/receiver`\
`mvn install` -> BUILD SUCCESS\
`java -jar target/receiver-0.0.1-SNAPSHOT.jar` -> You should see receiver application starting on port 8081\
`cd ../transformer`\
`mvn install` -> BUILD SUCCESS\
`java -jar target/transformer-0.0.1-SNAPSHOT.jar` -> You should see transmitter application starting on port 8082\
\
POST the above JSON on localhost:8081/postPerson using Postman or whatever you like and voila!
