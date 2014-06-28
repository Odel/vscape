@echo off
echo Compiling...
javac -d bin -cp libs/*; -sourcepath src src/com/rs2/*.java src/com/rs2/cache/*.java src/com/rs2/model/*.java src/com/rs2/net/*.java src/com/rs2/pf/*.java src/com/rs2/task/*.java src/com/rs2/util/*.java src/com/rs2/net/packet/*.java src/com/rs2/net/packet/packets/*.java src/com/rs2/model/content/*.java src/com/rs2/model/content/combat/*.java src/com/rs2/model/content/skills/*.java
@echo Finished.
pause