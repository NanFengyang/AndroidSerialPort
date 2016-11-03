#!/bin/sh

javac ../src/com/safone/comdetector/SerialPort.java
javac ../src/com/safone/comdetector/SafoneCom.java
javah -o SerialPort.h -jni -classpath ../src com.ihop.comdetector.SerialPort
javah -o SafonePtt.h -jni -classpath ../src com.ihop.comdetector.SafoneCom
