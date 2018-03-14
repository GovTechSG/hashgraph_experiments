FROM maven:3.3-jdk-8 AS hg-build

RUN mkdir  -p /opt/maven/hashgraph-experiments
ADD  ./IPOSDemo /opt/maven/hashgraph-experiments/IPOSDemo
COPY ./swirlds.jar /opt/maven/hashgraph-experiments/swirlds.jar
COPY ./config.txt /opt/maven/hashgraph-experiments/config.txt
COPY ./data /opt/maven/hashgraph-experiments/data
COPY ./exo-config-docker.json /opt/maven/hashgraph-experiments/exo-config.json

WORKDIR /opt/maven/hashgraph-experiments

RUN cd IPOSDemo && mvn clean install -e


FROM hg-build
EXPOSE 51200-51299
EXPOSE 50200-50299
EXPOSE 52200-52299
ENTRYPOINT ["java", "-jar", "swirlds.jar"]