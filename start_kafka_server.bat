@echo off
REM Starting Zookeeper and Kafka

set KAFKA_HOME=C:\kafka

echo Starting Zookeeper...
pushd "%KAFKA_HOME%\bin\windows"
start cmd /k .\zookeeper-server-start.bat ..\..\config\zookeeper.properties
popd


REM Wait for Zookeeper to start up (adjust the time if needed)
timeout /t 30 /nobreak

echo Starting Kafka...
pushd "%KAFKA_HOME%\bin\windows"
start cmd /k .\kafka-server-start.bat ..\..\config\server.properties
popd

pause