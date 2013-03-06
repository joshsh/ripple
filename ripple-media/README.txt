================================================================================
Overview
================================================================================

This package is an extension to the Ripple scripting language which includes a
few additional, 'multimedia' primitives.  It is documented here:

    https://github.com/joshsh/ripple/wiki/media-library


================================================================================
Generating Maven artifacts for the media extension
================================================================================

mvn install:install-file -DgroupId=edu.cmu -DartifactId=sphinx -Dversion=4 -Dpackaging=jar -Dfile=/home/josh/opt/comp_/lang_/java_/speech_/sphinx4/lib/sphinx4.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/freetts.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmu_us_kal -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmu_us_kal.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-en_us -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/en_us.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmudict04 -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmudict04.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmulex -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmulex.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmu_time_awb -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmu_time_awb.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmutimelex -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmutimelex.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-jsapi -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/jsapi.jar

