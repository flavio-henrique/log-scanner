# Log Scanner Tool

It's a Comand line App based on Java version 8.


## Run Locally
Pre-requisites:
  - Open JDK version 16 
  
Run this commands below in a linux terminal or Git Bash:

Run `./mvnw clean package`

Run `java -jar target/logscan-1.0-SNAPSHOT.jar <inputFile>` or `java -jar target/logscan-1.0-SNAPSHOT.jar <inputFile> > <outputFile>`

Example: `java -jar target/logscan-1.0-SNAPSHOT.jar C:/Users/E858989/Downloads/server/server.log > report.json`


## Running unit tests

Run `./mvnw test` to execute the unit tests.

## Running mutation tests

Run `./mvnw pitest:mutationCoverage` to execute the mutation tests.

![image](https://user-images.githubusercontent.com/12549950/140185348-304a76ec-5413-4632-9adb-a6d1929d5706.png)
