# On part de Tomcat 11
FROM tomcat:11.0-jdk17

# 1. On nettoie les apps par défaut
RUN rm -rf /usr/local/tomcat/webapps/*

# 2. On télécharge le driver PostgreSQL directement dans Tomcat
ADD https://jdbc.postgresql.org/download/postgresql-42.7.3.jar /usr/local/tomcat/lib/postgresql.jar

# 3. On copie le WAR
COPY target/CodingTickets-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/CodingTickets.war

# 4. On expose le port
EXPOSE 8080

# 5. Démarrage
CMD ["catalina.sh", "run"]