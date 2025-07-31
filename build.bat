@REM !!! Warning !!!

@REM Do Not Change any thing, this Bat file First removes all the existing Class Files, set the classpath with the Required libraries, """"want to add libraries ?? add in the lib folder, and if it is a Tomcat Dependency then add it in the Tomcat Root Lib folder too.""""  Next this script will compile all the java files in the src folder and store the classes to WEB-INF/classess folder, next it will run a python script which will start the Tomcat Server.

@REM !!! Warning !!!

@echo off

for /r .\WEB-INF\classes %%f in (*.class) do del "%%f"

set CLASSPATH=.;.\lib\servlet-api.jar;..\src;%CLASSPATH%

cd src\servlet

javac -d "../../WEB-INF/classes" -cp "../../lib/*;../../src" *.java

cd ..\..\

python -u ".\server.py"

@REM server.bat