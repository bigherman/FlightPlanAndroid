package dk.bigherman.android.hello;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PopulateMapArea extends AsyncTask<ArrayList<OverlayItem>, Void, ArrayList<OverlayItem>>
{
	private ProgressDialog dialog;
	private MapView myMap;
	private MapArea mapArea; 
	private AirfieldItemizedOverlay itemizedlist;
	
	public PopulateMapArea(MapView myMap, MapArea mapArea, AirfieldItemizedOverlay itemizedlist)
	{
		this.myMap = myMap;
		this.mapArea = mapArea;
		this.itemizedlist = itemizedlist;
	}
	
    @Override
    protected void onPreExecute()
    {
        dialog = new ProgressDialog(this.myMap.getContext());
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        
        myMap.getOverlays().clear();
    }
	
	@Override
	protected ArrayList<OverlayItem> doInBackground(ArrayList<OverlayItem>... args)
	{
		DataBaseHelper myDbHelper = new DataBaseHelper(this.myMap.getContext());
		Log.i("airfields", "Start db load");
        try {
        	myDbHelper.createDataBase();
        	myDbHelper.openDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	} catch(SQLException sqle) {
	 		throw sqle;
	 	}
	 
	 	ArrayList<Airfield> airfields = myDbHelper.airfieldsInArea(
	 			mapArea.getMinRow(),
	 			mapArea.getMaxRow(),
	 			mapArea.getMinCol(),
	 			mapArea.getMaxCol()
	 	);
        myDbHelper.close();
        
		for (int i=0; i<airfields.size();i++) {
			String icao = airfields.get(i).getIcaoCode();
			Log.i("airfields", "Next airfield call, ICAO=" + airfields.get(i).getIcaoCode());

			int lat = (int)(airfields.get(i).getLat()*(double)1E6);
			int lng = (int)(airfields.get(i).getLng()*(double)1E6);
			Log.i("airfields", "lat=" + lat + ",long=" + lng);
			String metar = "No metar available";
			String stationName = airfields.get(i).getName();

			GeoPoint point = new GeoPoint(lat,lng);
			OverlayItem overlayitem = new OverlayItem(point, stationName+" (" + icao + ")", metar);
			args[0].add(overlayitem);
		}

		for (OverlayItem item : args[0]) {
			this.itemizedlist.addOverlay(item);
		}
		
		Log.i("PopulateMapArea", "Returning data");
		
		return args[0];
	}
	
	protected void onPostExecute(ArrayList<OverlayItem> result)
	{
		Log.i("PopulateMapArea", "onPostExecute, result size=" + result.size());
		
		this.myMap.getOverlays().add(this.itemizedlist);
		this.myMap.invalidate();
		
        this.dialog.dismiss();
    }
}
