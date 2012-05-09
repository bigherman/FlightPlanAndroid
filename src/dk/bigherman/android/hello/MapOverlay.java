package dk.bigherman.android.hello;

import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay
{
    public boolean onTouchEvent(MotionEvent event, MapView mapView)
    {
    	if (event.getAction() == MotionEvent.ACTION_UP) {
    		GeoPoint geoPoint = mapView.getProjection().fromPixels(0,0);
    		double minLong = (double)geoPoint.getLongitudeE6()/(double)1E6;
    		double minLat  = (double)geoPoint.getLatitudeE6()/(double)1E6;
    		double maxLong = minLong+(double)mapView.getLongitudeSpan()/(double)1E6;
    		double maxLat  = minLat+(double)mapView.getLatitudeSpan()/(double)1E6;

    		int minGridLong = (int)(Math.floor(minLong+180));
    		int minGridLat  = (int)(Math.floor(minLat+90));
    		int maxGridLong = (int)(Math.ceil(maxLong+180));
    		int maxGridLat  = (int)(Math.ceil(maxLat+90));

    		Log.i("Map", "minLong=" + minLong + ",maxLong=" + maxLong + ",minLat=" + minLat + ",maxLat=" + maxLat);
    		Log.i("Map - Grid", "minGridLong=" + minGridLong + ",maxGridLong=" + maxGridLong + ",minGridLat=" + minGridLat + ",maxGridLat=" + maxGridLat);
    	}
		return false;
    }
}
