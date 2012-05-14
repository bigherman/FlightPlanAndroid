package dk.bigherman.android.hello;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMap extends MapActivity 
{
	private MapView mapView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true); 
        mapView.displayZoomControls(true);
        mapView.getZoomButtonsController().setAutoDismissed(false);
              
        initMap();

        System.out.println("All done");
    }

	/**
	 * Initialise the map and adds the zoomcontrols to the LinearLayout.
	 */
	private void initMap()
	{
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.icn_empty);
        AirfieldItemizedOverlay itemizedoverlay = new AirfieldItemizedOverlay(drawable, this);
		
		mapView = (MapView) findViewById(R.id.mapView);
	    mapView.displayZoomControls(true);
	    GeoPoint aarhus = new GeoPoint(56500000, 10300000);
	    mapView.getController().setCenter(aarhus);
	    mapView.getController().setZoom(8);

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