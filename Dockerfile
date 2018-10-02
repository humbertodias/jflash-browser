FROM ubuntu:16.04

# System deps
RUN apt update && apt install make xvfb default-jdk maven -y
# jxbrowser dependencies
RUN apt install libgconf-2-4 libssl-dev libxss-dev libcrypto++-dev -y
RUN ln -s /usr/lib/x86_64-linux-gnu/libcrypto.so /usr/lib/x86_64-linux-gnu/libcrypto.so.1.0.0 \
    ln -s /lib/x86_64-linux-gnu/libcrypt.so.1 /lib/x86_64-linux-gnu/libcrypt.so.11 \
    ln -s /lib/x86_64-linux-gnu/libudev.so.1 /lib/x86_64-linux-gnu/libudev.so.0

#RUN useradd -ms /bin/bash jflash
#USER jflash
#WORKDIR /home/jflash

WORKDIR /jflash
ADD . /jflash
RUN make package

EXPOSE 5900