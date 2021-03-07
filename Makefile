all:
	mkdir -p ./build/mc4k/
	mkdir -p ./build/res/
	javac -d ./build/ ./mc4k/Minecraft4K.java
	cp ./res/Manifest.txt ./build/
	cp ./res/textures.dat ./build/res/
	cd build && jar cfm Minecraft4K.jar Manifest.txt mc4k/* res/*
