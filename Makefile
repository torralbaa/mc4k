VERSION:=v0.3.0

all: config
	javac -d ./build/ ./mc4k/MCApplet.java
	javac -d ./build/ ./mc4k/MCTerrainGenerator.java
	javac -d ./build/ ./mc4k/MCPlayer.java
	javac -d ./build/ ./mc4k/Minecraft4K.java
	cd build && jar cfm Minecraft4K.jar manifest_minecraft4k.txt mc4k/* res/*

config:
	mkdir -p ./build/mc4k/
	mkdir -p ./build/res/
	cp ./res/manifest_minecraft4k.txt ./build/
	cp ./res/textures.dat ./build/res/
	cp ./res/icon.png ./build/res/
