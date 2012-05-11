package dk.bigherman.android.hello;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class AirfieldItemizedOverlay extends ItemizedOverlay<OverlayItem>
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	//private int curLong = 0;
	//private int curLat = 0;
	private long lastTouchTime = -1;
	public AirfieldItemizedOverlay(Drawable defaultMarker) 
	{
		super(boundCenterBottom(defaultMarker));
		populate();
	}
	
	public AirfieldItemizedOverlay(Drawable defaultMarker, Context context) 
	{
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		  populate();
	}
	
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);
	    this.populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) 
	{
	  return mOverlays.get(i);
	}
	
	@Override
	public int size() 
	{
	  return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) 
	{
		System.out.println(index);
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		
		dialog.setTitle(item.getTitle());
		new PopulateMapDialog(mContext, dialog).execute(item.getTitle());
		
		return true;
	}
	
    @SuppressWarnings("unchecked")
    @Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView)
    {
    	Log.i("onTouchEvent", ""+mapView.getZoomLevel());
    	super.onTouchEvent(event, mapView);
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    	    long thisTime = System.currentTimeMillis();
    	    if (thisTime - lastTouchTime < 250) {
    	    	GeoPoint geoPoint = mapView.getProjection().fromPixels(0,0);
    	    	double minLong = (double)geoPoint.getLongitudeE6()/(double)1E6;
    	    	double maxLat  = (double)geoPoint.getLatitudeE6()/(double)1E6;
    	    	double maxLong = minLong+(double)mapView.getLongitudeSpan()/(double)1E6;
    	    	double minLat  = maxLat-(double)mapView.getLatitudeSpan()/(double)1E6;

    	    	int minGridLong = (int)(Math.floor(minLong+180));
    	    	int minGridLat  = (int)(Math.floor(minLat+90));
    	    	int maxGridLong = (int)(Math.ceil(maxLong+180));
    	    	int maxGridLat  = (int)(Math.ceil(maxLat+90));

    	    	MapArea mapArea = new MapArea(minGridLat, maxGridLat, minGridLong, maxGridLong);

    	    	Log.i("Map", "minLong=" + minLong + ",maxLong=" + maxLong + ",minLat=" + minLat + ",maxLat=" + maxLat);
    	    	Log.i("Map - Grid", "minGridLong=" + minGridLong + ",maxGridLong=" + maxGridLong + ",minGridLat=" + minGridLat + ",maxGridLat=" + maxGridLat);

    	    	ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();

    	    	new PopulateMapArea(mapView, mapArea, this).execute(overlayItemList);

    	    	for (OverlayItem item : overlayItemList) {
    	    		this.addOverlay(item);
    	    	}
    	    	Log.i("airfields", "Item count" + this.mOverlays.size());  

    	    	lastTouchTime = -1;
    		} else {
    			lastTouchTime = thisTime;
    		}
        }

    	
		return false;
    }
}
