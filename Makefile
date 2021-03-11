VERSION:=v0.4.0

all: config
	javac -Xlint:deprecation -d ./build/ ./mc4k/*.java
	cd build && jar cfm Minecraft4K.jar manifest_minecraft4k.txt mc4k/* res/*

config:
	mkdir -p ./build/mc4k/
	mkdir -p ./build/res/
	cp ./res/manifest_minecraft4k.txt ./build/
	cp ./res/*.png ./build/res/
	cp ./res/*.dat ./build/res/

