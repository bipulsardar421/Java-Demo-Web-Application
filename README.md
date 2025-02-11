# Guide

Hi, I will guide you through testing this app.


# Cloning this Repo!
First you have to clone this repo, either by just downloading Zip of this repo, or using GIT CLI to clone this repo, once it is done let's move to the next step.


## Setting up DB

In order to run this app you need Database, download mySql community server from Oracle Site, create a db and change the configuration in \src\controller\JdbcApp.java.
It is the entry program which will be having all the credential for your DB, it is not recommended to keep all the credential like this, you can go for .env or using context.xml Configuration, but for feasibility I have used this.

## Setting up Tomcat Server
Install Xampp Server from this https://www.apachefriends.org/download.html (Choose correct Installer based on your OS)
Once Installed Go To this location C:\xampp\tomcat\webapps in your system, in case of mac it may be different please refer manual for xampp for mac.
Move your whole repo(the repo which you have cloned) including the root directory and paste it to webapps folder.



## Building the Project
Open Command Promt, Paste this ""cd C:\xampp\tomcat\webapps\Java-Demo-Web-Application"", remove the double quotes before pasting.
Once in CMD your path looks like this C:\xampp\tomcat\webapps\Java-Demo-Web-Application> type build.bat
The build. bat is a batch file containing all the instruction to build the project, if you want to do it manually then, put this line in the cmd
->>> cd src\dbmodel\login
then
->>> javac -d "../../WEB-INF/classes" -cp "../../lib/*;../../src" *.java
This command will store the class file to the classes folder in the WEB-INF.


## Start Server

Open Xampp control panel, start Apache then start Tomcat.

## Use Post-Man to verify

Use this url - http://localhost:8080/Java-Demo-Web-Application/login
select body and body type will be x-www-form-urlencoded
Give the Key "username" and value as the user name you kept in the login table

Create a login table with these attribute {id (int), username(varchar), password(varchar)} in the same db you mentioned in the JdbcApp.java


# Use Browser to test

Open this url -> http://localhost:8080/Java-Demo-Web-Application/index
Currently if it doesn't work check the code once again I may have updated few servlets
Give the User Name in the input box and click the button
