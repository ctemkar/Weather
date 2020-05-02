# Weather
Use Google specified architecture with the latest tools

Uses best practices for Android apps suggested by Google
Based on Guide to app architecture by Google using Activity/Fragment -> ViewModel -> Repository -> Remote Data source (retrofit), Model (Room)

Doesn't ask for background location because location is not critical in this app. Only uses location when app is active.

Gets the last location and also runs a service to periodically update the location. Service only runs when app is active though, again because this is not a critical app.

Uses Stetho so network traffic can be monitored using the Chrome browser and chrome://inspect

Location design is kept simple. Asks to turn on location if location is off and gets the location. More error handling will be added later

Allows you to go back a year in time to see weather and also choose another date for weather

Uses Retrofit with Kotlin co-routines for network access



