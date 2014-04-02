@echo off
echo /v/scape
set lib=%lib%bin;
set lib=%lib%libs/gson-2.2.4.jar;
set lib=%lib%libs/commons-compress-1.0.jar;
set lib=%lib%libs/javac++.jar;
set lib=%lib%libs/jdom-2.0.2.jar;
set lib=%lib%libs/jruby.jar;
set lib=%lib%libs/mysql-connector-java-5.1.20-bin.jar;
set lib=%lib%libs/xpp3-1.1.4c.jar;
set lib=%lib%libs/xstream-1.4.1.jar;
set lib=%lib%build/classes/model/content/skills/magic/;


java -cp %lib% com.rs2.Server
pause