This project was developped for a project in University of Athens.

Android Application used to determine collision when placed in front of the user. The point of this app is to determine collisions through 3 sensors found in android devices (Linear Acceleration, Light sensor, Proximity sensor). Real time measurements take place to check if the thresholds are surpassed and then alert the user with sound and text warnings. It is built to work in offline mode and do the calculations locally but it was also extended to go online and connect to an MQTT broker (mosquitto) which hosts the server with a Java application to control the data and control many devices simultaneously. In short:

Offline Mode:
Home screen shows the sensors' data in a text view and also contains a menu to access the settings and change the frequency and the thresholds of the sensors. Also, the collision message is shown through a heads-up notification that can be clicked to return to the app's main screen. 

Online Mode:
All offline calculations stop during online mode and WiFi and GPS are turned on. There are available settings to configue the broker's IP and port and a button to start transmission. When the thresholds are surpassed the server sends a toast back to the device. The server handles all the load of the android device thus making it more reliable while also informing the user if there is a chance for collision with another user.
