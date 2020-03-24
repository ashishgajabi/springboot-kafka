# Client Transaction Management Application

This is a sample Java / Maven / Spring Boot based application which **continuously poll the directory files** and if **Input.txt** is found
then it process the file by sending each transaction(line) of file on Kafka topic. Kafka listener receives the message, process 
the message and saves it to database.

Once the 'Input.txt' file is processed, file is moved to **archive** folder.

Application uses Spring-boot with Spring-Kafka, Spring-data-jpa along with Apache commons-csv to generate CSV file with in-memory database H2.

Make sure you are using JDK 1.8

## How to run

### Description
bin/run.sh bash script which starts the Zookeeper server first followed by Kafka server, once they are up and running, 
it starts the spring-boot application(using command like below) using maven command.

```
mvn spring-boot:run -Dspring-boot.run.arguments="--client-transaction.input.file-path="$FILE_DIR
```

Application log files get generated in "logs" folder.

### Start the App

run below command:

**
```
./bin/run.sh
```
If requires, give execution permission to above bash script.

Sample input file is at **files** folder. Once Input.txt file is processed, you could see something like this in application logs,

```
2020-03-24 22:08:46,780 INFO au.com.easynebula.clienttransactionmanagement.scheduler.InputFileProcessingScheduler [scheduling-1] Input file found. Processing started...
2020-03-24 22:08:46,877 INFO au.com.easynebula.clienttransactionmanagement.scheduler.InputFileProcessingScheduler [scheduling-1] Total number of Records processed: [717]
2020-03-24 22:08:46,877 INFO au.com.easynebula.clienttransactionmanagement.scheduler.InputFileProcessingScheduler [scheduling-1] Input file processing finished
```

#### To process Input.txt file again, put new Input.txt file in 'files' folder

To download daily transaction summary report, hit below url in browser

**http://localhost:9000/clienttransactionmanagement/dailyReport**

Sample report is available in **output-file** folder

H2 database console can be reached at below url

http://localhost:9000/clienttransactionmanagement/h2-console with jdbc url as "jdbc:h2:mem:testdb" and user as 'sa'


