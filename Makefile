run:
	mvn clean package
	mv target/jflash-browser-1.0-jar-with-dependencies.jar jflash.jar
	java -jar jflash.jar


clean:
	mvn clean
	rm jflash.jar
