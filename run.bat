@echo off
title Running VuongNgocHue_bai3_tuan06
echo ========================================================
echo [1/3] Building WAR package with Maven...
echo ========================================================
call mvn package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================================
echo [2/3] Deploying api-app.war to Tomcat 10...
echo ========================================================
copy /Y "target\api-app.war" "E:\apache-tomcat-10.1.59\webapps\api-app.war"

echo.
echo ========================================================
echo [3/3] Starting Apache Tomcat 10...
echo ========================================================
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
set "CATALINA_HOME=E:\apache-tomcat-10.1.59"
start "" "E:\apache-tomcat-10.1.59\bin\startup.bat"

echo.
echo ========================================================
echo Server is starting! Opening browser in 5 seconds...
echo - Web UI:  http://localhost:8080/api-app/products
echo - Cart:    http://localhost:8080/api-app/cart
echo - REST:    http://localhost:8080/api-app/api/products
echo ========================================================
timeout /t 5 /nobreak >nul
start http://localhost:8080/api-app/products
