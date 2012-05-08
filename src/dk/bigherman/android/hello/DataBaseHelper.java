package dk.bigherman.android.hello;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

public class DataBaseHelper extends SQLiteOpenHelper
{		 
	    //The Android's default system path of your application database.
	    private static String DB_PATH = "/data/data/dk.bigherman.android.hello/databases/";
	    private static String DB_NAME = "airfields.rdb";
	 
	    private SQLiteDatabase myDataBase; 
	 
	    private Context myContext;
	 
	    /**
	     * Constructor
	     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	     * @param context
	     */
	    public DataBaseHelper(Context context) 
	    {
	    	super(context, DB_NAME, null, 1);
	        this.myContext = context;
	    }	
	 
	    /**
	     * Creates a empty database on the system and rewrites it with your own database.
	     * 
	    */
	    public void createDataBase() throws IOException
	    {
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist)
	    	{
	    		//do nothing - database already exist
	    	}
	    	else
	    	{
	    		//By calling this method an empty database will be created into the default system path
	            //of your application so we are going to be able to overwrite that database with our database.
	        	this.getReadableDatabase();
	 
	        	try
	        	{
	    			copyDataBase();
	    		}
	        	catch (IOException e)
	        	{
	        		throw new Error("Error copying database");
	        	}
	    	}
	    }
	 
	    /**
	     * Check if the database already exist to avoid re-copying the file each time you open the application.
	     * @return true if it exists, false if it doesn't
	    */
	    private boolean checkDataBase()
	    {
	    	SQLiteDatabase checkDB = null;
	 
	    	try
	    	{
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    	}
	    	catch(SQLiteException e)
	    	{
	    		//database doesn't exist yet.
	    	}
	 
	    	if(checkDB != null)
	    	{
	    		checkDB.close();
	    	}
	 
	    	boolean existDB;
	    	if (checkDB != null)
	    		existDB = true;
	    	else
	    		existDB = false;
	    	
	    	checkDB = null;
	    	
	    	return existDB;
	    }
	 
	    /**
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDataBase() throws IOException
	    {
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	 
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ( (length = myInput.read(buffer)) > 0)
	    	{
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	 
	    }
	 
	    public void openDataBase() throws SQLException
	    {
	    	//Open the database
	        String myPath = DB_PATH + DB_NAME;
	    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    }
	 
	    @Override
		public void close()
	    {
    	    if(myDataBase != null)
    	    {
    		    myDataBase.close();
    	    }
    	    super.close();
    	    this.myContext = null;
    	    this.myDataBase = null;
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
	 
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
	 
		}
		
		// Add your public helper methods to access and get content from the database.
	    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	    // to you to create adapters for your views.
		
		public String airfieldName(String icaoCode)
		{
			String airfieldName;
			
			Cursor myCursor = myDataBase.rawQuery("SELECT name FROM airfields WHERE icao ='" + icaoCode +"'", null);
			
			myCursor.moveToFirst();
			airfieldName = myCursor.getString(0);
			
			myCursor.close();
			
			return airfieldName;
		}
		
		public ArrayList<Airfield> airfieldsInArea(int minRow, int maxRow, int minCol, int maxCol)
		{
			ArrayList<Airfield> airfieldsInArea = new ArrayList<Airfield>();
			Airfield airfield;
			
			Cursor myCursor = myDataBase.rawQuery("SELECT icao, lat, long, name FROM airfields "
					+ "WHERE tilerow >" + minRow +" AND tileRow<" + maxRow + " AND tilecol>" + minCol + " AND tilecol<" + maxCol, null);
			
			myCursor.moveToNext();
			
			while (!myCursor.isAfterLast()) {
				airfield = new Airfield (myCursor.getString(0), myCursor.getDouble(1), myCursor.getDouble(2), myCursor.getString(3));
				airfieldsInArea.add(airfield);
				myCursor.moveToNext();
			}
			
			myCursor.close();
			
			return airfieldsInArea;
		}
}
