================================================================================
Overview
================================================================================

This package is an extension to the Ripple scripting language which includes a
few additional, 'multimedia' primitives.  It is an experimental work in progress
and is therefore undocumented.  Feel free to send mail to josh@fortytwo.net if
you'd like to know more.




java -cp target/ripple-media-*-standalone.jar net.fortytwo.ripple.audio.HelloDigits
java -cp target/ripple-media-*-standalone.jar net.fortytwo.ripple.audio.Test kevin
java -cp target/ripple-media-*-standalone.jar net.fortytwo.ripple.Demo

mvn install:install-file -DgroupId=edu.cmu -DartifactId=sphinx -Dversion=4 -Dpackaging=jar -Dfile=/home/josh/opt/comp_/lang_/java_/speech_/sphinx4/lib/sphinx4.jar

mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/freetts.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmu_us_kal -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmu_us_kal.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-en_us -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/en_us.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmudict04 -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmudict04.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmulex -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmulex.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmu_time_awb -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmu_time_awb.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-cmutimelex -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/cmutimelex.jar
mvn install:install-file -DgroupId=com.sun.speech -DartifactId=freetts-jsapi -Dversion=1.2.1 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true -Dfile=lib/jsapi.jar



java -jar bin/FreeTTSHelloWorld.jar kevin







<http://dbpedia.org/resource/Innsbruck>
    rdfs:comment >>
    dup >> lang >> "en" equal >>
        id scrap branch >>
    speak >> .



$ unzip -l lib/en_us.jar|grep CMULexicon
     6402  06-18-07 14:05   com/sun/speech/freetts/en/us/CMULexicon.class

