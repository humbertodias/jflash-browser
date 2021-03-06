UNAME := $(shell uname)

package:
	mvn package
	mv target/jflash-browser-1.0-jar-with-dependencies.jar jflash.jar

backtime:
ifeq ($(UNAME), Linux)
	sudo timedatectl set-ntp 0
	sudo date --set="20181001 00:00:00"
endif
ifeq ($(UNAME),Darwin)
	#date {month}{day}{hour}{minute}{year}
	sudo date 100100002018
endif

curtime:
ifeq ($(UNAME), Linux)
	sudo timedatectl set-ntp 1
endif
ifeq ($(UNAME),Darwin)
	sudo sntp -sS time.apple.com
endif

run: package	backtime
	java -jar jflash.jar
	make curtime

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
