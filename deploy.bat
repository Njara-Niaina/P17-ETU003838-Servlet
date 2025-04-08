@echo off
setlocal enabledelayedexpansion

:: Set base directories
set "BASE_DIR=%~dp0"
set "SRC_DIR=%BASE_DIR%src\main"
set "BUILD_DIR=%BASE_DIR%build"
set "LIB_DIR=%BASE_DIR%lib"
set "WEBAPP_DIR=%SRC_DIR%\webapp"
set "TEMP_DIR=%BASE_DIR%temp"

:: Set project name (can be changed as needed)
set "PROJECT_NAME=ETU003838"

:: Set deployment directory (customize as needed)
set "DEPLOY_DIR=C:\Tomcat 10.1\webapps"

:: Clean and recreate build directory
echo ===== Cleaning directories =====
if exist "%BUILD_DIR%" rd /s /q "%BUILD_DIR%"
if exist "%TEMP_DIR%" rd /s /q "%TEMP_DIR%"

echo ===== Creating fresh directories =====
mkdir "%BUILD_DIR%\WEB-INF\classes"
mkdir "%BUILD_DIR%\WEB-INF\lib"
mkdir "%TEMP_DIR%"

:: Build classpath with all JARs in lib directory
echo ===== Building classpath =====
set "CLASSPATH=%BUILD_DIR%\WEB-INF\classes"
for %%f in ("%LIB_DIR%\*.jar") do set "CLASSPATH=!CLASSPATH!;%%f"
echo Classpath: !CLASSPATH!

:: Detect packages and create a dependency order
echo ===== Detecting Java packages =====
set "PACKAGES_FILE=%TEMP_DIR%\packages.txt"
dir /b /s /ad "%SRC_DIR%\java" > "%PACKAGES_FILE%"

:: Create a list of all Java files
echo ===== Creating Java files list =====
set "FILES_LIST=%TEMP_DIR%\files_list.txt"
dir /b /s "%SRC_DIR%\java\*.java" > "%FILES_LIST%"

:: Count total files for progress display
set /a TOTAL_FILES=0
for /f %%a in ('type "%FILES_LIST%" ^| find /c /v ""') do set TOTAL_FILES=%%a
echo Total files to compile: %TOTAL_FILES%

:: Try to compile in appropriate order - compile each file individually until all succeed
echo ===== Compiling Java files with dependency resolution =====
set /a COMPILED_FILES=0
set /a MAX_PASSES=10
set /a CURRENT_PASS=1

:compilation_pass
echo Starting compilation pass %CURRENT_PASS% of %MAX_PASSES%
set /a COMPILED_THIS_PASS=0

for /f "tokens=*" %%f in ('type "%FILES_LIST%"') do (
    :: Skip already compiled files by checking for class file
    set "JAVA_FILE=%%f"
    set "RELATIVE_PATH=!JAVA_FILE:%SRC_DIR%\java\=!"
    set "CLASS_PATH=%BUILD_DIR%\WEB-INF\classes\!RELATIVE_PATH:.java=.class!"
    
    if not exist "!CLASS_PATH!" (
        echo [Pass %CURRENT_PASS%] Compiling: !RELATIVE_PATH!
        javac -d "%BUILD_DIR%\WEB-INF\classes" -cp "!CLASSPATH!" "%%f" >nul 2>nul
        
        if not errorlevel 1 (
            set /a COMPILED_FILES+=1
            set /a COMPILED_THIS_PASS+=1
        )
    )
)

echo Compilation pass %CURRENT_PASS% completed. Compiled %COMPILED_THIS_PASS% files in this pass.
echo Total compiled: %COMPILED_FILES% of %TOTAL_FILES%

:: If we compiled something this pass and haven't compiled everything yet, do another pass
if %COMPILED_THIS_PASS% gtr 0 (
    if %COMPILED_FILES% lss %TOTAL_FILES% (
        set /a CURRENT_PASS+=1
        if %CURRENT_PASS% leq %MAX_PASSES% goto compilation_pass
    )
)

:: Check if we've compiled all files
if %COMPILED_FILES% lss %TOTAL_FILES% (
    echo WARNING: Could not compile all files after %MAX_PASSES% passes.
    echo There might be compilation errors or unresolved dependencies.
    
    echo ===== Attempting final pass with all errors shown =====
    :: Final attempt with errors shown
    for /f "tokens=*" %%f in ('type "%FILES_LIST%"') do (
        set "JAVA_FILE=%%f"
        set "RELATIVE_PATH=!JAVA_FILE:%SRC_DIR%\java\=!"
        set "CLASS_PATH=%BUILD_DIR%\WEB-INF\classes\!RELATIVE_PATH:.java=.class!"
        
        if not exist "!CLASS_PATH!" (
            echo Final attempt: !RELATIVE_PATH!
            javac -d "%BUILD_DIR%\WEB-INF\classes" -cp "!CLASSPATH!" "%%f"
        )
    )
    
    echo Build might be incomplete. Continuing with available compiled classes...
) else (
    echo All Java files compiled successfully!
)

:: Copy web resources
echo ===== Copying webapp resources =====
xcopy "%WEBAPP_DIR%" "%BUILD_DIR%" /E /I /Y
if errorlevel 1 (
    echo Failed to copy webapp resources!
    goto cleanup_and_exit
)

:: Copy required JAR libraries
echo ===== Copying JAR libraries =====
xcopy "%LIB_DIR%\*.jar" "%BUILD_DIR%\WEB-INF\lib\" /Y
if errorlevel 1 (
    echo Failed to copy JAR libraries!
    goto cleanup_and_exit
)

:: Create WAR file
echo ===== Creating WAR file =====
pushd "%BUILD_DIR%"
jar -cvf "%BASE_DIR%\%PROJECT_NAME%.war" *
popd

:: Always deploy to Tomcat (removed conditional check)
echo ===== Deploying to Tomcat =====
if not exist "%DEPLOY_DIR%" (
    echo Warning: Deployment directory does not exist!
) else (
    copy /Y "%BASE_DIR%\%PROJECT_NAME%.war" "%DEPLOY_DIR%\"
    if not errorlevel 1 echo Successfully deployed to Tomcat
)

echo ===== Build completed successfully! =====

:cleanup_and_exit
:: Clean up temporary files
if exist "%TEMP_DIR%" rd /s /q "%TEMP_DIR%"

endlocal