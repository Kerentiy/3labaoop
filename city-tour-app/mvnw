#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Startup Script
# ----------------------------------------------------------------------------

MAVEN_OPTS="%MAVEN_OPTS%"
MVNW_REPOURL="https://repo.maven.apache.org/maven2"

WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER="org.apache.maven.wrapper.MavenWrapperMain"

if [ ! -d ".mvn/wrapper" ]; then
    mkdir -p .mvn/wrapper
fi

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading maven-wrapper.jar..."
    curl -o "$WRAPPER_JAR" "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.3.0/maven-wrapper-3.3.0.jar"
fi

exec "$JAVA_HOME/bin/java" $MAVEN_OPTS -classpath "$WRAPPER_JAR" $WRAPPER_LAUNCHER "$@"