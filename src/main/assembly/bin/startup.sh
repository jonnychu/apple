#!/bin/bash
export BASE_PATH=/app/apple
export APPLICATION="apple"
export APPLICATION_JAR="${APPLICATION}-0.0.1-SNAPSHOT.jar"
export LOG_PATH="${BASE_PATH}/log"

export JAVA_OPT="-server -Xms256m -Xmx256m -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=256m -XX:-OmitStackTraceInFastThrow"

nohup java ${JAVA_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} > ${LOG_PATH} 2>&1 &