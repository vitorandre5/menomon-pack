#!/bin/bash
# Compila o jar sem Gradle/Loom (alvos ja em nomes intermediary), igual aos outros menomon-*.
set -e
cd "$(dirname "$0")"
J=/home/vitor/Downloads/jdk-21/bin
P=~/.technic/modpacks/menomon
CP=$P/.fabric/remappedJars/minecraft-1.21.1-0.18.4/client-intermediary.jar
CP=$CP:$HOME/.technic/libraries/net/fabricmc/fabric-loader/0.18.4/fabric-loader-0.18.4.jar
CP=$CP:$(ls ~/Documents/curseforge/minecraft/Install/libraries/net/fabricmc/sponge-mixin/0.17.0+mixin.0.8.7/*.jar | head -1)
CP=$CP:$(find ~/.technic/libraries -name 'joml-*.jar' | head -1)
CP=$CP:$(find ~/.technic/libraries ~/Documents/curseforge/minecraft/Install/libraries -name "slf4j-api*.jar" | head -1)
for g in datafixerupper brigadier authlib guava; do CP=$CP:$(find ~/.technic/libraries -name "$g-*.jar" | head -1); done
V=$(python3 -c "import json;print(json.load(open('res/fabric.mod.json'))['version'])")
rm -rf out && mkdir -p out
$J/javac --release 21 -proc:none -Xlint:-options -cp "$CP" -d out $(find src -name '*.java')
cp -r res/* out/
rm -f menomon-mochila-fix-*.jar
(cd out && $J/jar cf ../menomon-mochila-fix-$V.jar .)
echo OK: $(pwd)/menomon-mochila-fix-$V.jar
