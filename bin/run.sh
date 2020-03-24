#!/bin/sh

WORKING_DIR="$(pwd)"
cd ..
HOME_DIR="$(pwd)"
echo 'Home directory:' $HOME_DIR
FILE_DIR=$HOME_DIR/files
echo 'Input file directory: ' $FILE_DIR

echo '############### SCRIPT EXECUTION STARTS ###################';

echo 'Starting zookeeper server'
$HOME_DIR/apache-zookeeper-3.6.0-bin/bin/zkServer.sh start &
sleep 4

echo 'Starting Kafka server'
$HOME_DIR/kafka_2.12-2.4.1/bin/kafka-server-start.sh $HOME_DIR/kafka_2.12-2.4.1/config/server.properties &
sleep 10

echo 'Starting client-transaction-management project'
$HOME_DIR/apache-maven-3.6.3/bin/mvn clean
$HOME_DIR/apache-maven-3.6.3/bin/mvn spring-boot:run -Dspring-boot.run.arguments="--client-transaction.input.file-path="$FILE_DIR &