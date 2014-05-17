#!/bin/bash

# find Java
if [ "$JAVA_HOME" = "" ] ; then
	JAVA="java"
else
	JAVA="$JAVA_HOME/bin/java"
fi

# set Java options
if [ "$JAVA_OPTIONS" = "" ] ; then
	JAVA_OPTIONS="-Xms32M -Xmx512M"
fi

LINK=`readlink $0`
if [ "$LINK" ]; then
    DIR=`dirname $LINK`
else
    DIR=`dirname $0`
fi

$JAVA $JAVA_OPTIONS -cp $DIR/target/classes:$DIR/"target/dependency/*" net.fortytwo.ripple.Demo $*

# return the application's exit code
exit $?
