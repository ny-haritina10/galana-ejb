@echo off

set "root=D:\Studies\ITU\S5\INF301_Architechture-Logiciel\projet\gestion-station\S5 p16\galana-ejb"
set "target_dir=D:\Studies\ITU\S5\INF301_Architechture-Logiciel\projet\gestion-station\S5 p16\galana-war\lib"

set "temp=%root%\temp"
set "src=%root%\src"
set "lib=%root%\lib"
set "bin=%root%\bin"
set "jar_name=galana-ejb"

:: Create temp and bin directories if they don't exist
if not exist "%temp%" mkdir "%temp%"
if not exist "%bin%" mkdir "%bin%"

:: copy all java files to temp directory
for /r "%src%" %%f in (*.java) do (
    xcopy "%%f" "%temp%"
)

:: move to temp to compile all java file
cd "%temp%"
javac -d "%bin%" -cp "%lib%\*" *.java

:: move to bin to create jar
cd "%bin%"
jar -cvf "%jar_name%.jar" .

:: copy jar to target directory
copy "%jar_name%.jar" "%target_dir%"

:: Clean up
cd "%root%"
rmdir /s /q "%temp%"