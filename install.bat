set WGET_HOME=%CD%\lib\wget-1.11.4-1-bin
set Path=%Path%;%WGET_HOME%;%WGET_HOME%\bin
call mvn install:install-file -Dfile=./fitnesse-standalone.jar -DgroupId=org.fitnesse -DartifactId=fitnesse-standalone -Dversion=20150226 -Dpackaging=jar
call mvn install:install-file -Dfile=./lib/JmeterBundle.jar -DgroupId=JMeterBundle -DartifactId=JMeterBundle -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
call mvn install:install-file -Dfile=./lib/sqljdbc4.jar -DgroupId=sqljdbc4 -DartifactId=sqljdbc4 -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/synthuse.jar -DgroupId=synthuse -DartifactId=synthuse -Dversion=1.1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/sikuli-java.jar -DgroupId=org.sikuli -DartifactId=sikuli-java -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./plugins/oasisplugin.jar -DgroupId=oasis -DartifactId=oasis-plugin -Dversion=1.0 -Dpackaging=jar
call mvn install:install-file -Dfile=./lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar
