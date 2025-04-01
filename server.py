import os
import time
import subprocess

TOMCAT_HOME = r"C:\xampp\tomcat"
# TOMCAT_HOME = r"G:\Download\apache-tomcat-9.0.99-windows-x64\apache-tomcat-9.0.99"

TOMCAT_BIN = os.path.join(TOMCAT_HOME, "bin")
JAVA_HOME = r"C:\Program Files\Java\jdk-22"

START_TOMCAT_CMD = os.path.join(TOMCAT_BIN, "catalina.bat start")
STOP_TOMCAT_CMD = os.path.join(TOMCAT_BIN, "catalina.bat stop")

os.environ["CATALINA_HOME"] = TOMCAT_HOME
os.environ["JAVA_HOME"] = JAVA_HOME

def stop_tomcat():
    """Stop Tomcat properly using catalina.bat."""
    print("Stopping Tomcat...")
    subprocess.run(STOP_TOMCAT_CMD, shell=True, env=os.environ)
    time.sleep(5)

def start_tomcat():
    """Start Tomcat using catalina.bat after setting JAVA_HOME."""
    print("Starting Tomcat...")
    subprocess.run(START_TOMCAT_CMD, shell=True, env=os.environ)

if __name__ == "__main__":
    stop_tomcat()
    start_tomcat()

