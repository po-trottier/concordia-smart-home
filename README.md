# Smart Home Simulator

This simulator was developed as a requirement for the SOEN343 course given at Concordia University. The simulator is meant to replicate a Smart Home envrionment to allow for easy testing of Smart Home Systems without the hassle related to setting up physical systems. The simulator should also provide more consistent and predictable results leading to an overall better testing environment.

__The Team__

- Qandeel Arshad *(40041524)*
- Luigi Besani Urena *(40030054)*
- William Morin-Laberge *(40097269)*
- Ashwin Philip *(40096505)*
- Pierre-Olivier Trottier *(40059235)*

## Documentation

__Default accounts (username, password):__  
- parent, parent  
- child, child  
- guest, guest  
- stranger, stranger

### Technologies Used

The project is built using the Java version of the Android SDK. Gradle is used to sync depdencies and build the project.

Deployment is done automatically when using Android Studio or IntelliJ. In the case of a production-ready application, we could simply build an APK using the IDE and upload it to the Google Play Store for worldwide distribution, but since this is a development application, this will not be necessary.

### How to run the project

To run the project, simply follow the following steps:
1) Clone the repository on your local development machine
1) Open the project in IntelliJ
1) Build the project using the IDE's built-in tools
1) Run the project on a physical (through ADB) or virtual device by choosing from the IDE's dropdown menu and clicking the "Play" button.
