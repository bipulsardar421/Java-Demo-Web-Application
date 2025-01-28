@echo off

for /r .\WEB-INF\classes %%f in (*.class) do del "%%f"

set CLASSPATH=.;.\lib\servlet-api.jar;..\src;%CLASSPATH%

cd src\dbmodel\login

javac -d "../../../WEB-INF/classes" -cp "../../../lib/*;../../../src" LoginDBmodel.java

cd ..\..\..\

