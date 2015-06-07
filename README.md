# Firefly MQTT

Install the required jgoodies forms jar into local maven repository

    cd Dependencies
    mvn install:install-file -DgroupId=com.jgoodies -DartifactId=forms -Dversion=1.3.0 -Dpackaging=jar -Dfile=forms-1.3.0.jar
    cd ..
 
Now build with maven

    cd firefly
    mvn clean verify
    
You should find the firefly jar into target/ folder.
