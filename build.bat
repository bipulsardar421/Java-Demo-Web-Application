@echo off

for /r .\WEB-INF\classes %%f in (*.class) do del "%%f"

set CLASSPATH=.;.\lib\servlet-api.jar;..\src;%CLASSPATH%

cd src\servlet

javac -d "../../WEB-INF/classes" -cp "../../lib/*;../../src" *.java

cd ..\..\


python -u ".\server.py"