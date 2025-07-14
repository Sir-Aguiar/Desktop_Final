@echo off
echo ====================================
echo    Diario Digital - Compilacao
echo ====================================
echo.

echo Compilando o projeto...
javac -cp "src/main/java;target/classes" -d "target/classes" "src/main/java/org/example/*.java" "src/main/java/org/example/entities/*.java" "src/main/java/org/example/utils/*.java" "src/main/java/org/example/gui/*.java"

if %errorlevel% neq 0 (
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo Compilacao concluida com sucesso!
echo.
echo Executando o sistema...
java -cp "target/classes" org.example.Main

pause
