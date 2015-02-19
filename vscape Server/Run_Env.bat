@echo off
echo /v/scape
java -Xms1024M -Xmx1024M -cp bin;libs/gson-2.2.4.jar;libs/commons-compress-1.0.jar;libs/javac++.jar;libs/jdom-2.0.2.jar;libs/jruby.jar;libs/mysql-connector-java-5.1.20-bin.jar;libs/xpp3-1.1.4c.jar;libs/xstream-1.4.1.jar;build/classes/model/content/skills/magic/; com.rs2.Server
pause