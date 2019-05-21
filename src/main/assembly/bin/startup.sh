#!/bin/bash
export BASE_PATH=/app/apple
export APPLICATION="apple"
export APPLICATION_JAR="${APPLICATION}-0.0.1-SNAPSHOT.jar"

export JAVA_OPT="-server -XX:+HeapDumpOnOutOfMemoryError -XX:+ExitOnOutOfMemoryError -XX:+AggressiveOpts -XX:+UseG1GC -XX:NewRatio=3 -verbose:gc -XX:+PrintGCCause -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -Djava.net.preferIPv4Stack=true -XX:MaxDirectMemorySize=256M"

nohup java ${JAVA_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} & >> ${BASE_PATH}/log/${APPLICATION}.out 2>&1