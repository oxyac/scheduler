FROM openjdk:18-buster
ARG JAR_FILE=build/libs/*.jar

#RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip
#RUN mkdir /opt/gradle
#RUN unzip -d /opt/gradle gradle-7.6-bin.zip
#RUN chmod +x /opt/gradle
#RUN /opt/gradle build
COPY ${JAR_FILE} app.jar
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.26.0/geckodriver-v0.26.0-linux64.tar.gz
RUN tar -xvf geckodriver-v0.26.0-linux64.tar.gz
RUN mv geckodriver /usr/bin/
RUN chmod +x /usr/bin/geckodriver
ENTRYPOINT ["java", \
"-Dspring.datasource.password=${BOT_DB_PASSWORD}", \
#"-Dbot.name=${BOT_NAME}", \
#"-Dbot.token=${BOT_TOKEN}", \
"-Dspring.datasource.username=${BOT_DB_USERNAME}", \
 "-jar", "app.jar"]
