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

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem>
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public HelloItemizedOverlay(Drawable defaultMarker) 
	{
		super(boundCenterBottom(defaultMarker));
		populate();
	}
	
	public HelloItemizedOverlay(Drawable defaultMarker, Context context) 
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
		//System.out.println(item);
		//System.out.println(mContext);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		
		//System.out.println(item.getTitle());
		//System.out.println(item.getSnippet());
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		
		//dialog.setTitle("This is a test!");
		dialog.show();
		
		return true;
	}

}
