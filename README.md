# GardenHelper
Android Plant database for gardeners.

Final Project

Import Application using Android studio
Build the project and run on emulator. API 8 or API 11

Icon is Green with white leaf on it.
Garden helper app
This is an app that helps users know more about their plants and
help them get the right environment for their plants.

There is a daily weather forecast for the next 5 days at the top
of the main activity that is updated based on user's location using
the gps.

If user does not provide location then there won't be any weather information.

Under it users can look up a plant they want to know more about and click search.
Users can then select the plant and it will be stored locally.

In the overflow menu of the App toolbar, there are 3 options that
users can select for getting air humidity, pressure, and temperature.

When clicked it will get the current data from the sensors of the phone.
It will also show you the locally stored plant's recommended environment 
as well to help users know if their plant is in a suitable environment.

App is locked vertically.

APIs used
Openweathermap api 	//For weather
Trefle api		//For plants database

Hardware features used
GPS location to update weather.
Environment sensors for air humidity, air pressure, and ambient temperature.

Current Bugs that I couldn't figure out yet: 
RecyclerView has weird spacing prob with fragments.
Search button needs to hit twice to show a list of plants.
When user types in new plants, it won't refresh to new plants.

Later features to add:
Soil humidity. More weather detail. Bug fixes. Different orientations.

Credits
Zybooks
Android developer tools/guides
Openweathermap.org
Trefle.io
Postman
Background from LiuBov Ilchuk on Unsplash
© 2022 GitHub, Inc.
Terms
Privacy
Security
Status
Docs
Contact GitHub
Pricing
API
Training
Blog
About
