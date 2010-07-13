@echo off
setlocal
set JAVA_HOME=F:\Documents\jdk1.6.0_20
REM set JAVA_HOME=F:\Documents\jre
set ANT_HOME=F:\Documents\apache-ant-1.8.1
set PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin

IF "%1"=="" GOTO default
IF "%1"=="run"
java -jar dist\Twitz.jar
GOTO done
IF "%1"=="build"
ant jar
GOTO done
IF "%1"=="prompt"
cmd.exe
GOTO done
REM java -cp "F:\Documents\Twitz\thirdparty\AppFramework.jar;F:\Documents\Twitz\thirdparty\twitter4j-core-2.1.2.jar;F:\Documents\Twitz\thirdparty\swingx-0.9.4.jar;F:\Documents\Twitz\thirdparty\substance-tools.jar;F:\Documents\Twitz\thirdparty\substance-tst.jar;F:\Documents\Twitz\thirdparty\substance.jar;F:\Documents\Twitz\thirdparty\trident.jar;F:\Documents\Twitz\thirdparty\commons-logging-1.1.1.jar;F:\Documents\Twitz\${libs.swing-layout.classpath};F:\Documents\Twitz\${libs.absolutelayout.classpath};F:\Documents\Twitz\thirdparty\log4j-1.2.14.jar;F:\Documents\Twitz\dist\Twitz.jar" twitz.TwitzApp

:default
cmd.exe
:done