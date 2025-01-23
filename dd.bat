@echo off
:: Check if the user provided a directory as an argument
if "%~1"=="" (
    echo Usage: %~nx0 ^<directory^>
    exit /b 1
)

:: Store the target directory
set "TARGET_DIR=%~1"

:: Check if the provided argument is a valid directory
if not exist "%TARGET_DIR%" (
    echo Error: %TARGET_DIR% is not a valid directory.
    exit /b 1
)

echo Displaying directory tree for: %TARGET_DIR%
echo ----------------------------------------

:: Display the directory tree
tree "%TARGET_DIR%" /f
