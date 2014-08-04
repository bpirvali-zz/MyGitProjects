#!/bin/sh
# STOP TOMCAT
echo "stoping tomcat..."
/Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/bin/shutdown.sh

# package
#echo "mvn package..."
#mvn clean package -DskipTests

# RE-DEPLOY
echo "re-deploy..."
rm /Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/webapps/bs.war
rm -rf /Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/webapps/bs
cp "./target/BookService2WebApp-0.0.1-SNAPSHOT.war" /Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/webapps/bs.war

rm /Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/logs/*

# START TOMCAT
echo "starting tomcat..."
/Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/bin/startup.sh

