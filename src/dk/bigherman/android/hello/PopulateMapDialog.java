package dk.bigherman.android.hello;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PopulateMapDialog extends AsyncTask<String, Void, String>
{
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
		// TODO: Following code isn't an optimal way of retrieving the ICAO code of the clicked airfield.
		Pattern p = Pattern.compile("^(?:.*)(?:\\((.*)\\))$");
		Matcher m = p.matcher(params[0]);
		
		if (m.find()) {
			icao = m.group(1);
		} else {
			return "ICAO code missing(??)";
		}
		
		try {
			URL url = new URL("http://api.geonames.org/weatherIcao?ICAO=" + icao + "&username=bigherman");
			
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document)builder.build(url);
			Element rootNode = document.getRootElement();
			List<Element> children = rootNode.getChildren("observation");
			Log.i("ob", "Children: " + rootNode.getChildren("observation").size());
			if (children.size() > 0) {
				Element observation = children.get(0);

				String singleObservation = observation.getChildText("observation");
				Log.i("ob", "Observation='" + singleObservation + "'");
				return observation.getChildText("observation");
			} else {
				return "No observation found.";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		this.alertDialog.setMessage(result);
		this.dialog.dismiss();
		this.alertDialog.show();
	}
}
