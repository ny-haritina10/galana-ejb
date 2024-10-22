@echo off

set WILDFLY_HOME=D:\Executable\wildfly-26.1.2.Final

echo Starting WildFly...
cd %WILDFLY_HOME%\bin
standalone.bat

pause