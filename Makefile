all:
	mkdir -p ./build/mc4k/
	mkdir -p ./build/res/
	javac -d ./build/ ./mc4k/MCApplet.java
	javac -d ./build/ ./mc4k/MCTerrainGenerator.java
	javac -d ./build/ ./mc4k/MCPlayer.java
	javac -d ./build/ ./mc4k/Minecraft4K.java
	cp ./res/Manifest.txt ./build/
	cp ./res/textures.dat ./build/res/
	cp ./res/icon.png ./build/res/
	cd build && jar cfm Minecraft4K.jar Manifest.txt mc4k/* res/*
