# Firefly MQTT
Firefly MQTT is a small "traffic sniffer" for MQTT protocol developed in Java7. It displays received messages in a table, with details like Retained flag and QoS. Firefly supports decoding [Eclipse Kura](http://www.eclipse.org/kura/) payload format, both regular and gzipped. In this first release all subscriptions have QoS 2.

<img src="http://i.imgur.com/ueWVwN5.png?1">

MQTT settings like broker, username etc are configurable and saved into a JSON configuration file in the same folder. Please be sure to have write permissions to the working folder where you run Firefly.  
If you find this tool useful I would like to hear you opinions. If you find a bug or want a feature [open an issue](https://github.com/darugnaa/firefly-mqtt/issues).

## Download
You can download the jar [from here](https://drive.google.com/file/d/0B0tptNwKwCF_WkdXNUl6V01hQVU/view?usp=sharing) and just double-click it to start! If you wish to run it from command line just `java -jar firefly-0.0.1.jar` and you'll see all the nice debug output.

**Important**: I released a shaded jar (aka uber jar) with all dependencies inside for convenience. See [Libraries section](#libraries) for the list of libraries included.

## Compile!
Clone the repository to your local machine

    git clone https://github.com/darugnaa/firefly-mqtt.git
    cd firefly-mqtt
    
Install the required jgoodies forms jar into local Maven repository

    cd Dependencies
    mvn install:install-file -DgroupId=com.jgoodies -DartifactId=forms -Dversion=1.3.0 -Dpackaging=jar -Dfile=forms-1.3.0.jar
    cd ..
 
Now build with Maven

    cd firefly
    mvn clean verify
    
You should find the firefly jar into `target/` folder.

## Develop!
Firefly MQTT is developed in [Eclipse 4.4 Luna](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/lunasr2) with [WindowBuilder](http://www.eclipse.org/windowbuilder/download.php). Import the project in Eclipse with "File -> Import", select "General -> Existing projects into Workspace" and open the inner `firefly` folder.  
The project is compiled and packed using [Maven 3.0.5](https://maven.apache.org/download.cgi).

## TO DOs
* Right click popup menu on JTable with options "Copy" topic/payload
* Right click popup menu on JList add option to "Copy" topic
* Re-subscribe on connect
* Fix concurrentmodificationexception on table model
* Counter with msgs/sec (add status bar to UI?)
* MqttSetting on a "per server" basis
* Warn the user if broker address is invalid
* Warn the user on connection issues
* Limit the maximum of messages that the table can hold
* Persist subscriptions settings
* Persist settings into user and os specific folders (/home/user/.firefly on linux/osx and %APPDATA% on windows)

## Libraries
A complete list of libraries used in this project can be found [in the pom.xml](https://github.com/darugnaa/firefly-mqtt/blob/master/firefly/pom.xml#L31). I included in my sources the classes from [Eclipse Kura source code](https://github.com/eclipse/kura) required to decode Kura payloads.
