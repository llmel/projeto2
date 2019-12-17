@echo off
@echo Instalador TCM
pause
@echo Para executar nosso programa voce precisa ter o java instalado na sua maquina, por isso vamos verificar se ja o possui na sua maquina
where java >nul 2>nul
if %errorlevel%==1 (
    @echo Java nao encontrado.
    pause
) 
  pause
  @echo Voce possui o Java instalado em sua maquina.
  set /p resposta=Iniciar programa? [s/n]:
  
  if %resposta%==s (
    @echo Programa sera iniciado...
    
    java -jar oshi-sprint-1.0-SNAPSHOT-jar-with-dependencies.jar
  )