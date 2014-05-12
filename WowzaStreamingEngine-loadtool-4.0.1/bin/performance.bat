@echo off

set runmode=standalone

call setenv.bat
call wowzaversion.bat

if not %WMSENVOK% == "true" goto end

set _WINDOWNAME="Wowza Streaming Engine 4: Load Test Tool"
set _EXESERVER=

set CLASSPATH="%WMSAPP_HOME%\bin\wms-bootstrap.jar"
set CLASSPATH=%CLASSPATH:\=/%
set CLASSPATH=%CLASSPATH://=/%
   
set TUNEPATH="%WMSAPP_HOME%\bin\tune.bat"
set TUNEPATH=%TUNEPATH:\\=\%
set TUNEPATH=%TUNEPATH:\=/%

for /f "delims=" %%i in ('%TUNEPATH%') do set WMSTUNE_OPTS=%%i

echo Tuning: %WMSTUNE_OPTS%

"%_EXECJAVA%" %WMSTUNE_OPTS% -Dcom.wowza.wms.Logging="%WMSCONFIG_HOME%/conf/log4j-performance.properties" -Dcom.wowza.wms.runmode="%runmode%" -Dcom.wowza.wms.native.base="win" -Dcom.wowza.wms.ConfigURL="%WMSCONFIG_URL%" -cp %CLASSPATH% com.wowza.wms.bootstrap.Bootstrap runTest %1

:end