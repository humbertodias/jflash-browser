FROM ubuntu:16.04

# System deps
RUN apt update && apt install make xvfb x11vnc xfonts-base default-jdk maven -y
# jxbrowser dependencies
RUN apt install libgconf-2-4 libssl-dev libxss-dev libcrypto++-dev -y

RUN useradd -ms /bin/bash jflash
USER jflash
WORKDIR /home/jflash

WORKDIR /jflash
ADD . /jflash
RUN make package

EXPOSE 5900