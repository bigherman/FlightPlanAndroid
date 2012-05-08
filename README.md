# FlightPlanAndroid
## Build instructions
You will need to create the file res/values/maps_apikey.xml with following contents:
```<?xml version="1.0" encoding="utf-8"?>
<resources>
	<string name="maps_apikey">[productionApiKey]</string>
</resources>```

Remember to replace [productionApiKey] with your own Google Maps API key - obtained from https://developers.google.com/android/maps-api-signup