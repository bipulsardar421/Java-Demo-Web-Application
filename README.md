# Guide

Hi, I will guide you through testing this app.

## Building the Project

Download Tomcat: Go to https://tomcat.apache.org and download the version 8 (e.g., Tomcat 8).

Extract Archive: Unzip the downloaded .zip to a preferred location.

Deploy App: You have to clone this repo, either by just downloading Zip of this repo, or using GIT CLI to clone this repo, to webapps/ directory in your Tomcat folder.

Now Open the Recently Cloned Application Directory, here you will find build.bat script file, double click on it to compile and start the server.
Note:. you will be needing to install python as well to run the server, please do so.

# Use Browser to test

Open this url -> http://localhost:8080/Java-Demo-Web-Application/index

## Setting up DB

In order to run this app you need Database, download mySql community server from Oracle Site.
Next Open WorkBench -> Open a SQL editor -> copy all the SQL Scripts from https://github.com/bipulsardar421/Java-Demo-Web-Application/blob/main/DB/Dump20250731.sql -> Execute.


# Get the API Routes in the Root Directory inside ./src/servlet/ Every Files is responisible for a specific task, and every task have a endpoint url. 
## Application URL + AppName(Java-Demo-Web-Application) + ServletStartingPoint(Inside each servlet check @WebServlet("/ServletStartingPoint/*")) + Endpoint(the string value mentioned in the Switch-Case)


## This is a backend application used in a project called Inventory Control System, https://github.com/bipulsardar421/Java-Demo-Web-Application/blob/main/POC_Details_Document/InventoryControlSystem.doc
