package dk.bigherman.android.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import com.google.android.maps.*;

import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView.LayoutParams;  
import android.view.View;
import android.widget.LinearLayout;

import org.xmlpull.v1.*;

public class MyMap extends MapActivity 
{
	private MapView mapView;
    MapController mc;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapView);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
        
        try
        {
        	initMap();
        }
        catch (IOException ex)
        {
        	System.out.println("Map Init Error");
        }
        catch (XmlPullParserException ex)
        {
        	System.out.println("XML Parser Error");
        }
        System.out.println("All done");
    }

	/**
	 * Initialise the map and adds the zoomcontrols to the LinearLayout.
	 */
	private void initMap() throws XmlPullParserException, IOException
	{
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		Log.i("airfields", "Start db load");
        try 
        {
        	myDbHelper.createDataBase();
	 	} 
        catch (IOException ioe) 
        {
	 		throw new Error("Unable to create database");
	 	}
	 
	 	try 
	 	{
	 		myDbHelper.openDataBase();
	 	}
	 	catch(SQLException sqle)
	 	{
	 		throw sqle;
	 	}
	 	
	 	ArrayList<Airfield> airfields = myDbHelper.airfieldsInArea(140, 150, 175, 192);
        myDbHelper.close();
        
        Log.i("airfields", "End db load");
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.icn_empty);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
		
		mapView = (MapView) findViewById(R.id.mapView);
	    mapView.displayZoomControls(true);
	    GeoPoint aarhus = new GeoPoint(56500000, 10300000);
	    mapView.getController().setCenter(aarhus);
	    mapView.getController().setZoom(8);
	    
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
	    
	    Log.i("airfields",String.valueOf(airfields.size()));
        for (int i=0; i<airfields.size();i++)
	    {
        	Log.i("airfields", "Next airfield call");
        	
		    URL url = new URL("http://api.geonames.org/weatherIcao?ICAO=" + airfields.get(i).getIcaoCode() + "&username=bigherman");
		    
		    int lat = 0;
		    int lng = 0;
		    
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(
		            url.openStream()));
	
	        String line;
	        StringBuffer result = new StringBuffer();
	        while ((line = in.readLine()) != null) 
	        {
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
	        
	        do
	        {    
		        xpp.next();  
		        eventType = xpp.getEventType();  
		        switch(eventType)
		        {
		        	case XmlPullParser.TEXT: 
			        	elementContent = xpp.getText();
			        	
			        	if(tag.equals("lat"))
				        {
				        	lat = (int)(Double.parseDouble(elementContent)*1e6);
				        	
				        	System.out.println(lat);
				        	//Log.d("text", String.valueOf(lat));
				        }
				        else if (tag.equals("lng"))
				        {
				        	lng = (int)(Double.parseDouble(elementContent)*1e6); 
				        	
				        	System.out.println(lng);
				        	//Log.d("text", String.valueOf(lng));
				        }
				        else if (tag.equals("observation"))
				        {
				        	metar = elementContent;
				        }
				        else if (tag.equals("stationName"))
				        {
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
	        
	        if(lat != 0 && lng != 0)
	        {
		        GeoPoint point = new GeoPoint(lat,lng);
		        OverlayItem overlayitem = new OverlayItem(point, stationName, metar);
		        itemizedoverlay.addOverlay(overlayitem);
	        }
	    }
        
        mapOverlays.add(itemizedoverlay);
        mapView.invalidate();
        Log.i("airfields","Map Invalidated");
	}

    @Override
    protected boolean isRouteDisplayed()
    {
    	return false;
    }
}