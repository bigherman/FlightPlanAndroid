package dk.bigherman.android.hello;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PopulateMapDialog extends AsyncTask<String, Void, String>
{
	//private Builder dialog;
	//private MyMap myMap
	private Context mapView;
	private ProgressDialog dialog;
	private Builder alertDialog;
	
	public PopulateMapDialog(Context mapView, Builder alertDialog)
	{
		this.mapView = mapView;
		this.alertDialog = alertDialog;
	}
	
	@Override
	protected void onPreExecute()
	{
        dialog = new ProgressDialog(mapView);
        dialog.setTitle("Loading...");
        dialog.setMessage("Retrieving observations...");
        dialog.setIndeterminate(true);
        dialog.show();
	}

	@Override
	protected String doInBackground(String... params)
	{
		String icao;
		// Following isn't an optimal way of retrieving the ICAO code of the clicked airfield. Am aware..
		Pattern p = Pattern.compile("^(?:.*)(?:\\((.*)\\))$");
		Matcher m = p.matcher(params[0]);
		
		if (m.find()) {
			icao = m.group(1);
		} else {
			return "ICAO code missing(??)";
		}
		
		// XML parsing code..
		
		try {
			URL url = new URL("http://api.geonames.org/weatherIcao?ICAO=" + icao + "&username=bigherman");
			
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document)builder.build(url);
			Element rootNode = document.getRootElement();
			Element observation = rootNode.getChild("observation");
			//Log.i("PopulateMapDialog", observation.getChildText("observation"));
			return observation.getChildText("observation");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		this.alertDialog.setMessage(result);
		this.dialog.dismiss();
		this.alertDialog.show();
	}
}
