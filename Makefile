package:
	mvn clean package
	mv target/jflash-browser-1.0-jar-with-dependencies.jar jflash.jar

run: package
	java -jar jflash.jar

clean:
	mvn clean
	rm -rf jflash.jar log/* swf/*

docker_build:
	docker build --no-cache -t jflash-browser .

headless: package
	Xvfb :99 -ac -screen 0 1024x768x16 & export DISPLAY=":99" & java -jar jflash.jar

x11vnc:
	x11vnc -display :99 -localhost

docker_server: headless x11vnc

vncviewer:
	vncviewer :0