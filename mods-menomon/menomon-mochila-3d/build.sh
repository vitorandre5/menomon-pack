#!/bin/bash
# uso: build.sh  -> gera menomon-mochila-3d-<versao do res/fabric.mod.json>.jar
set -e; cd "$(dirname "$0")"
J=/home/vitor/Downloads/jdk-21/bin; CP="/home/vitor/.technic/modpacks/menomon/.fabric/remappedJars/minecraft-1.21.1-0.18.4/client-intermediary.jar:/home/vitor/.technic/libraries/net/fabricmc/fabric-loader/0.18.4/fabric-loader-0.18.4.jar:$(pwd)/libs/fabric-rendering-v1-0.116.12.jar:/home/vitor/.technic/libraries/com/mojang/datafixerupper/8.0.16/datafixerupper-8.0.16.jar:/home/vitor/.technic/libraries/com/mojang/brigadier/1.3.10/brigadier-1.3.10.jar:/home/vitor/.technic/libraries/org/joml/joml/1.10.5/joml-1.10.5.jar"
rm -rf out; cp -r res out; $J/javac --release 21 -proc:none -cp "$CP" -d out src/menomon/mochila3d/MochilaCores.java
V=$(python3 -c "import json;print(json.load(open('res/fabric.mod.json'))['version'])")
(cd out && $J/jar cf ../menomon-mochila-3d-$V.jar .); echo OK $V
