@echo off
setlocal enabledelayedexpansion

set MAVEN_OPTS=%MAVEN_OPTS%
set MVNW_REPOURL=https://repo.maven.apache.org/maven2

set WRAPPER_JAR=".mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

set "JAVA_HOME=%JAVA_HOME%"
if "%JAVA_HOME%" == "" (
  echo JAVA_HOME is not set. Please set JAVA_HOME environment variable.
  exit /b 1
)

"%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% -classpath %WRAPPER_JAR% %WRAPPER_LAUNCHER% %*