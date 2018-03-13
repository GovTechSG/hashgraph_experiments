FROM maven:3.3-jdk-8 AS hg-build

RUN mkdir  -p /opt/maven/hashgraph-experiments
ADD  ./IPOSDemo /opt/maven/hashgraph-experiments/IPOSDemo
COPY ./exo-config.json /opt/maven/hashgraph-experiments/exo-config.json
COPY ./swirlds.jar /opt/maven/hashgraph-experiments/swirlds.jar
COPY ./config.txt /opt/maven/hashgraph-experiments/config.txt
COPY ./data /opt/maven/hashgraph-experiments/data

WORKDIR /opt/maven/hashgraph-experiments/IPOSDemo

RUN mvn clean install -e

ENTRYPOINT ["java", "-jar", "/opt/maven/hashgraph-experiments/IPOSDemo/IPOSAPP/target/IPOSApp-0.0.1-SNAPSHOT.jar"]

