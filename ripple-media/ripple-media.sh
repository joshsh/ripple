#!/bin/bash

# Find Java.
if [ "$JAVA_HOME" = "" ] ; then
        JAVA="java"
else
        JAVA="$JAVA_HOME/bin/java"
fi

# Set Java options.
if [ "$JAVA_OPTIONS" = "" ] ; then
        JAVA_OPTIONS="-Xms32M -Xmx512M"
fi

JAR=target/ripple-media*-standalone.jar

# Launch Ripple.
$JAVA $JAVA_OPTIONS -jar $JAR $*

# Return the appropriate exit code.
exit $?

