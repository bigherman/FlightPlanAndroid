package dk.bigherman.android.hello;

public class MapArea {
	private int minRow;
	private int minCol;
	private int maxRow;
	private int maxCol;
	
	public MapArea(int minRow, int maxRow, int minCol, int maxCol)
	{
		this.minRow = minRow;
		this.minCol = minCol;
		this.maxRow = maxRow;
		this.maxCol = maxCol;
	}
	
	protected int getMinRow()
	{
		return minRow;
	}
	
	protected void setMinRow(int minRow)
	{
		this.minRow = minRow;
	}
	
	protected int getMinCol()
	{
		return minCol;
	}
	
	protected void setMinCol(int minCol)
	{
		this.minCol = minCol;
	}
	
	protected int getMaxRow()
	{
		return maxRow;
	}
	
	protected void setMaxRow(int maxRow)
	{
		this.maxRow = maxRow;
	}
	
	protected int getMaxCol()
	{
		return maxCol;
	}
	
	protected void setMaxCol(int maxCol)
	{
		this.maxCol = maxCol;
	}
	

}
