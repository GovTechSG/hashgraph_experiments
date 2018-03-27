FROM maven:3.3-jdk-8 AS hg-build


RUN apt-get update && apt-get -y install haveged && update-rc.d haveged defaults

RUN mkdir  -p /opt/maven/hashgraph-experiments
ADD  ./IPOSDemo /opt/maven/hashgraph-experiments/IPOSDemo
COPY ./swirlds.jar /opt/maven/hashgraph-experiments/swirlds.jar
COPY ./config.txt /opt/maven/hashgraph-experiments/config.txt
COPY ./data /opt/maven/hashgraph-experiments/data
COPY ./exo-config-docker.json /opt/maven/hashgraph-experiments/exo-config.json
COPY ./entrypoint.sh /opt/maven/hashgraph-experiments/entrypoint.sh
WORKDIR /opt/maven/hashgraph-experiments

RUN cd IPOSDemo && mvn clean install -e


FROM hg-build
EXPOSE 52204-52207
ENTRYPOINT ["/opt/maven/hashgraph-experiments/entrypoint.sh"]