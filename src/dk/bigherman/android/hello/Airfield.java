package dk.bigherman.android.hello;

public class Airfield 
{
	private String icaoCode;
	private double lat;
	private double lng;
	private String name;
	
	Airfield(String icao, double lat, double lng, String name)
	{
		this.icaoCode = icao;
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}
	
	public double getLat()
	{
		return lat;
	}
	
	public double getLng()
	{
		return lng;
	}
	
	public String getIcaoCode()
	{
		return icaoCode;
	}
	
	public String getName()
	{
		return name;
	}
}
