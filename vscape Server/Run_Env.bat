@echo off
echo /v/scape
java -Xms512M -Xmx1024M -cp bin;libs/gson-2.3.1.jar;libs/commons-compress-1.9.jar;libs/javac++.jar;libs/jdom-2.0.6.jar;libs/jruby.jar;libs/mysql-connector-java-5.1.34-bin.jar;libs/xpp3-1.1.4c.jar;libs/xstream-1.4.8.jar;build/classes/model/content/skills/magic/; com.rs2.Server
pause