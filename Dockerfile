FROM centos
ENV APP_FILE target/couchsvc11-0.0.1-SNAPSHOT.jar
ENV APP_HOME /usr/app
EXPOSE 8080
EXPOSE 8090
EXPOSE 8091
COPY . $APP_HOME/
WORKDIR $APP_HOME
RUN yum install maven -y
RUN mvn -s Maven/setting clean install  -Djacoco.percentage.instruction=0.01
CMD java -javaagent:/root/.m2/repository/io/prometheus/jmx/jmx_prometheus_javaagent/0.11.0/jmx_prometheus_javaagent-0.11.0.jar=8089:/usr/app/sample_config.yml -jar /usr/app/$APP_FILE