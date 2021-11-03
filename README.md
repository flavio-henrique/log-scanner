# log-scanner App

It's a Comand line App based on Java version 8.


## Run Locally
Pre-requisites:
  - Open JDK version 16 
  
Run this commands below in a linux terminal or Git Bash:

Run `./mvnw clean package`

Run `java -jar logscan-1.0-SNAPSHOT.jar <inputFile>` or `java -jar logscan-1.0-SNAPSHOT.jar <inputFile> > <outputFile>`

Example: `java -jar logscan-1.0-SNAPSHOT.jar C:/Users/E858989/Downloads/server/server.log > report.json`
