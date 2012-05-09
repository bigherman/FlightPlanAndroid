package dk.bigherman.android.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PopulateMap extends AsyncTask<HelloItemizedOverlay, Void, HelloItemizedOverlay>
{
	private ProgressDialog dialog;
	private Context myMap;
	private ArrayList<Airfield> airfields; 
	
	public PopulateMap(Context myMap, ArrayList<Airfield> airfields)
	{
		this.myMap = myMap;
		this.airfields = airfields;
	}
    @Override
    protected void onPreExecute()
    {
        dialog = new ProgressDialog(myMap);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
    }
	
	@Override
	protected HelloItemizedOverlay doInBackground(HelloItemizedOverlay... itemizedoverlay)
	{
		XmlPullParserFactory factory = null;
		XmlPullParser xpp = null;
		try {
		factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        xpp = factory.newPullParser();
	   
	       for (int i=0; i<this.airfields.size();i++) {
	        	Log.i("airfields", "Next airfield call, ICAO=" + airfields.get(i).getIcaoCode());
	        	
			    URL url = new URL("http://api.geonames.org/weatherIcao?ICAO=" + airfields.get(i).getIcaoCode() + "&username=bigherman");
			    
			    int lat = 0;
			    int lng = 0;
			    
			    BufferedReader in = new BufferedReader(
			            new InputStreamReader(
			            url.openStream()), 6*1024);
		
		        String line;
		        StringBuffer result = new StringBuffer();
		        while ((line = in.readLine()) != null) {
		        	result.append(line);
		        }
		        in.close();
		
		        xpp.setInput( new StringReader (result.toString()));
		        String elementContent = null;
		        String metar = "No metar available";
		        System.out.println(airfields.get(i).getName());
		        System.out.println(airfields.get(i).getIcaoCode());
		        String stationName = "";
		        
		        int eventType = xpp.getEventType();
		        String tag = null;
		        
		        do {    
			        xpp.next();  
			        eventType = xpp.getEventType();  
			        switch(eventType) {
			        	case XmlPullParser.TEXT: 
				        	elementContent = xpp.getText();
				        	
				        	if(tag.equals("lat")) {
					        	lat = (int)(Double.parseDouble(elementContent)*1e6);
					        	
					        	System.out.println(lat);
					        	//Log.d("text", String.valueOf(lat));
					        } else if (tag.equals("lng")) {
					        	lng = (int)(Double.parseDouble(elementContent)*1e6); 
					        	
					        	System.out.println(lng);
					        	//Log.d("text", String.valueOf(lng));
					        } else if (tag.equals("observation")) {
					        	metar = elementContent;
					        } else if (tag.equals("stationName")) {
					        	stationName = elementContent;
					        }
				        	break;
			        	
			        	case XmlPullParser.START_TAG:
			        		tag = xpp.getName();
			        		//Log.d("tag", tag);
			        		break;
			        	
			        	default:
			        		break;
			        }  
		        } while (eventType != XmlPullParser.END_DOCUMENT); 
		        
		        if(lat != 0 && lng != 0) {
			        GeoPoint point = new GeoPoint(lat,lng);
			        OverlayItem overlayitem = new OverlayItem(point, stationName, metar);
			        itemizedoverlay[0].addOverlay(overlayitem);
		        }
		    }
	       
	       Log.i("PopulateMap", "Out of loop..");    
	   } catch (Exception e) {
		   Log.e("exception", e.getMessage());
	   }
		Log.i("PopulateMap", "Returning data");
		return itemizedoverlay[0];
	}
	
	protected void onPostExecute(HelloItemizedOverlay result)
	{
		Log.i("PopulateMap", "onPostExecute, result size=" + result.size());
        this.dialog.dismiss();
    }
}
