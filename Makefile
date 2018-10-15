package:
	mvn package
	mv target/jflash-browser-1.0-jar-with-dependencies.jar jflash.jar

run: package
	java -jar jflash.jar

clean:
	mvn clean
	rm -rf jflash.jar log/* download/*

docker_build:
	docker build --no-cache -t jflash-browser .

docker_run:
	docker run -ti -p 5900:5900 jflash-browser make serve

headless: package
	export DISPLAY=":99" && Xvfb :99 -ac -screen 0 1024x768x16 & xvfb-run -a java -jar jflash.jar &

x11vnc:
	mkdir ~/.x11vnc
	x11vnc -storepasswd password ~/.x11vnc/passwd
	x11vnc -display :99

serve: headless x11vnc

vncviewer:
	vncviewer :99