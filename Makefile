JAR_DIR=/mnt/work
JAR_DIR=/home/git/lib


all:
	javac -cp ${JAR_DIR}/jedis-2.9.0.jar -d bin src/*.java

run:

	java -cp "${JAR_DIR}/jedis-2.9.0.jar;bin" -Djava.util.logging.SimpleFormatter.format='%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n' JHaServer
