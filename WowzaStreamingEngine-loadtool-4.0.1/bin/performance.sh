#!/bin/sh

. ./setenv.sh
mode=standalone

ulimit -n 20000 > /dev/null 2>&1

WMSTUNE_OPTS=`$WMSAPP_HOME/bin/tune.sh $mode`

$_EXECJAVA $WMSTUNE_OPTS -Dcom.wowza.wms.Logging="$WMSCONFIG_HOME/conf/log4j-performance.properties" -Dcom.wowza.wms.runmode="$mode" -Dcom.wowza.wms.native.base="linux" -Dcom.wowza.wms.AppHome="$WMSAPP_HOME" -Dcom.wowza.wms.ConfigURL="$WMSCONFIG_URL" -Dcom.wowza.wms.ConfigHome="$WMSCONFIG_HOME" -cp $WMSAPP_HOME/bin/wms-bootstrap.jar com.wowza.wms.bootstrap.Bootstrap runTest $1
