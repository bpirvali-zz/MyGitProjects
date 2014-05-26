#!/bin/sh
# STOP TOMCAT
echo "stoping tomcat..."
/Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/bin/shutdown.sh

rm /Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/logs/*

# START TOMCAT
echo "starting tomcat..."
/Users/bpirvali/Documents/MyDocs/Java/Tools/Tomcat/bin/startup.sh

