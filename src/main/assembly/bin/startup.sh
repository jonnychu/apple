#!/bin/bash
export APP_HOME=/app/apple
export APP_NAME="apple"
export APP_NAME_JAR="${APP_NAME}-0.0.1-SNAPSHOT.jar"
export CONFIG_DIR=${APP_HOME}/config

#export JAVA_OPT="-server -XX:+HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError -XX:+AggressiveOpts -XX:+UseG1GC -XX:NewRatio=3 -verbose:gc -XX:+PrintGCCause -XX:+PrintGCDateStamps -XX:+PrintGCAPP_NAMEStoppedTime -Djava.net.preferIPv4Stack=true -XX:MaxDirectMemorySize=256M"


nohup java -DAPP_HOME=${APP_HOME} -DAPP_NAME=${APP_NAME} -jar ${APP_HOME}/boot/${APP_NAME_JAR}  & >> ${APP_HOME}/log/${APP_NAME}.out 2>&1