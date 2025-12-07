# On part de Tomcat 11
FROM tomcat:11.0-jdk17

# 1. On nettoie les apps par défaut
RUN rm -rf /usr/local/tomcat/webapps/*

# 2. SOLUTION RADICALE : On télécharge le driver PostgreSQL directement dans Tomcat
# Cela garantit qu'il est présent, peu importe si ton WAR est bien construit ou non.
ADD https://jdbc.postgresql.org/download/postgresql-42.7.3.jar /usr/local/tomcat/lib/postgresql.jar

# 3. On copie ton WAR (ton code à toi)
COPY target/CodingTickets-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/CodingTickets.war

# 4. On expose le port
EXPOSE 8080

# 5. Démarrage
CMD ["catalina.sh", "run"]