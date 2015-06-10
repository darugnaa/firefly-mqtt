# Firefly MQTT
Firefly MQTT is a small "traffic sniffer" for MQTT protocol. It displays received messages in a table, with details lik Retained flag, QoS. Firefly supports decoding [Eclipse Kura](http://www.eclipse.org/kura/) payload format, both regular and gzipped.

<img src="http://i.imgur.com/ueWVwN5.png?1">

## Compile!
Install the required jgoodies forms jar into local maven repository

    cd Dependencies
    mvn install:install-file -DgroupId=com.jgoodies -DartifactId=forms -Dversion=1.3.0 -Dpackaging=jar -Dfile=forms-1.3.0.jar
    cd ..
 
Now build with maven

    cd firefly
    mvn clean verify
    
You should find the firefly jar into target/ folder.

## Develop!

Firefly MQTT is developed in [Eclipse 4.4 Luna](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr2) with [WindowBuilder](http://www.eclipse.org/windowbuilder/download.php).
The project is compiled and packed using [Maven 3.0.5](https://maven.apache.org/download.cgi).

## TO DO
* Right click popup menu on JTable with options "Copy" topic/payload
* Right click popup menu on JList add option to "Copy" topic
* Re-subscribe on connect
* Client-id in MqttSettings
* Fix concurrentmodificationexception on table model
* Counter with msgs/sec (add status bar to UI?)
* MqttSetting on a "per server" basis

