#JAR_DIR=/mnt/work
JAR_DIR=/home/git/jar

.PHONY:doc

CLASSPATH="${JAR_DIR}/mail-1.4.jar;${JAR_DIR}/jedis-2.9.0.jar;${JAR_DIR}/gson-2.8.2.jar;bin"


all:
	javac -cp ${CLASSPATH}  -d bin src/*.java

run:

	java -cp ${CLASSPATH} -Djava.util.logging.SimpleFormatter.format='%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n' \
	-Dcom.sun.management.jmxremote.port=8008 \
	-Dcom.sun.management.jmxremote.authenticate=false \
	-Dcom.sun.management.jmxremote.ssl=false \
	JHaServer


jar:
	jar cf jha.jar bin/*.class

doc:
	javadoc -cp ${CLASSPATH} -d doc src/*.java
