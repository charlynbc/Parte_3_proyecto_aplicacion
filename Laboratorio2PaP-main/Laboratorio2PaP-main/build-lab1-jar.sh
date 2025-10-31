#!/bin/bash
# Script to build Laboratorio1.jar

echo "Removing Laboratorio2.war..."
IF EXIST C:apache-tomcat-10.1.36/webapps/Laboratorio2PaP.war DEL C:apache-tomcat-10.1.36/webapps/Laboratorio2PaP.war

echo "Copying Laboratorio2.war..."
cp C:/Users/alvxd/git/Laboratorio2PaP/target/Laboratorio2PaP.war C:apache-tomcat-10.1.36/webapps

echo "Initializing Tomcat server..."
C:apache-tomcat-10.1.36/bin/startup.bat