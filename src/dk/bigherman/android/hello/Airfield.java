package dk.bigherman.android.hello;

public class Airfield 
{
	private String icaoCode;
	private double lat;
	private double lng;
	private int row;
	private int col;
	private String name;
	
	Airfield(String icao, double lat, double lng, int col, int row, String name)
	{
		this.icaoCode = icao;
		this.lat = lat;
		this.lng = lng;
		this.name = name;
		this.col = col;
		this.row = row;
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

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}
