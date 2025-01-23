@echo off
echo Restructuring directories...
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\controller"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\dao"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\model"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\util"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\webapp\css"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\webapp\js"
mkdir "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\webapp\images"
move "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\JdbcApp.java" "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\controller\JdbcApp.java"
move "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\dbModel\Login.java" "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\src\model\Login.java"
move "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\WEB-INF\web.xml" "C:\XAMPP\TOMCAT\WEBAPPS\JDBC-TEST\WEB-INF\web.xml"
echo Restructuring complete.
pause
