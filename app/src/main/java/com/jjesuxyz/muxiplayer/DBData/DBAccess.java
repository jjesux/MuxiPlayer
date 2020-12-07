package com.jjesuxyz.muxiplayer.DBData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;





/**
 * The DBAccess class is used to get access and manipulate the local database.
 * It opens it in writable mode and provide functions and methods to insert and
 * extract data from the database tables. It also provides functions to delete
 * database table records. It also contains some functions that were used to
 * debug this class.
 *
 * Created on 12/5/2020 2:44 pm.
 *
 */

public class DBAccess {
                                  //Global class variable declaration
    private Context contexto;
                                  //Variable to access database
    private SQLiteDatabase database = null;
    private DBAccessHelper dbAccessHelper;




    /**
     * Class constructor, DBAccess(Context), used to create an object to access
     * the local database, and to open that database in a writable mode. It
     * receives one parameter that is a link to one of the user interfaces that
     * made the request.
     *
     * @param contexto type Context
     */
    public DBAccess(Context contexto){

        this.contexto = contexto;
                                  //Getting access to the database class helper
        dbAccessHelper = new DBAccessHelper(this.contexto);
        try{
                                  //Opening the local database in a writable mode
            open();
        }
        catch(SQLException sqle){
                                  //if any error
            sqle.printStackTrace();
        }

    }//End of constructor



    /**
     * The open() function is used to get a link to the local database and open
     * it in a writable mode.
     *
     * @throws SQLException threw when there is a problem to get access to the
     * local DB.
     */
    private void open() throws SQLException{

        database = dbAccessHelper.getWritableDatabase();
                                  //Just debugging la connection to DB
        if(database == null){
            l("DB is NULL DBAccess->open()");
        }
    }//End of function



    /**
     * This function is used to close the local database and release resources.
     * It does not receive any parameter nor returns any value.
     *
     */
    public void close(){
                                  //Database connection object
        if(database != null){
            database.close();
        }
                                  //Database connection helper object
        if(dbAccessHelper != null){
            dbAccessHelper.close();
        }

    }//End of function



    /**
     * The getDatabaseStructure() function is used to show the structure of the
     * local DB in the android monitor window. It shows all the tables in the
     * DB and all the columns in each table as well as all the rows in each
     * column.
     * It does not receive any parameter nor returns any value.
     *
     */
    public void getDatabaseStructure() {
        ArrayList<String[]> result = new ArrayList<>();
        int i;
                                  //SQLite command to get db structure, tables
                                  //and columns
        Cursor c = database.rawQuery("SELECT name FROM SQLite_master WHERE type='table'", null);

        result.add(c.getColumnNames());
                                  //for loop
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
                                  //for loop to visit every table in the DB
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);

                Cursor c1 = database.rawQuery("SELECT * FROM "+temp[i], null);
                c1.moveToFirst();
                String[] COLUMNS = c1.getColumnNames();
                                  //for loop to visit every column in each table
                for(int j = 0; j < COLUMNS.length; j++) {
                    c1.move(j);
                    //l("    COLUMN - " + COLUMNS[j]);
                }                 //End of inner-inner for loop

            }                     //End of inner for loop

            result.add(temp);
        }                         //End of outer for loop

    }//End of function



    /**
     * The isTableEmpty(String) function is used to check if the dabase tables
     * are empty.  It returns true if the databse table being analyzed is NOT
     * empty. It returns false if the database table being analyzed is empty.
     * It receives one string parameter. This parameter is the name of the
     * table to be checked.
     * It returns a boolean value.
     *
     * @param tableName type String
     * @return type boolean
     */
    public boolean isTableEmpty(String tableName){

        String strTableName = tableName;
        boolean empty = false;
        Cursor cursor;
                                  //Querying db table
        cursor = database.rawQuery("SELECT COUNT(*) FROM " + strTableName, null);
                                  //Checking if cursor is empty or null
        if(cursor != null && cursor.moveToFirst() && cursor.getInt(0) == 0){
            empty = true;
        }
                                  //Closing the cursor.
        cursor.close();

        return empty;

    }//End of function



    /**
     * The insertRecordIntoFullListTable(String, String) function is used to
     * insert a record into the local database table. It creates the record
     * column values and it passes this values to the insert function.  This
     * insert function actually insert these column values into the local
     * database.
     * It returns the id row number that is unique. If there is any error,
     * it will return -1.
     *
     * @param my_ID type String
     * @param fullSongPath type String
     * @return type long
     */
    public long insertRecordIntoFullListTable(String my_ID, String fullSongPath){
        long insertedRowId;
                                  //Setting the column values into a
                                  //ContentValues obj.
        ContentValues valuesFullList = new ContentValues();

        valuesFullList.put(DBAccessHelper.COLUMN_FILE_PATH, fullSongPath);
                                  //Inserting values into the database table
        insertedRowId = database.insert(DBAccessHelper.TABLE_FULL_LIST, null, valuesFullList);
                                  //Code for debugging purposes
        if(insertedRowId < 0){
            l("Error inserting record: class-DBAccess->createAndInsertRecordFullListTable()");
        }

        return insertedRowId;

    }//End of function



    /**
     * The insertMultipleRecordsIntoFullListTable(ArrayList) function is used to
     * insert multiple entries or records into the database full list table.
     * These records, mp3 files, may be all the files that exist in the device
     * external storage.  It needs one parameter of type ArrayList<String>. This
     * ArrayList contains mp3 files full path address.
     * It does not need the table name since it is used to insert records only
     * into the full list table.
     * It returns the id of the record inserted or -1 if there was an error.
     *
     * @param arrayListFilePathsSDCard type ArrayList<String>
     */
    public void insertMultipleRecordsIntoFullListTable(ArrayList<String> arrayListFilePathsSDCard){
                                  //Variable for debugging purposes
        long insertRowId;
                                  //Setting the column values into a
                                  //ContentValues obj.
        ContentValues valuesFullList = new ContentValues();

        for(int i = 0; i < arrayListFilePathsSDCard.size(); i++){
            valuesFullList.put(DBAccessHelper.COLUMN_FILE_PATH, arrayListFilePathsSDCard.get(i));
                                  //Inserting values into the database table
            insertRowId = database.insert(DBAccessHelper.TABLE_FULL_LIST, null, valuesFullList);
            valuesFullList.clear();
                                  //Code for debugging purposes
            if(insertRowId < 0){
                l("Error inserting records: class-DBAccess->insertMultipleRecordsIntoFullListTable()");
            }
        }

    }//End of function




    /**
     * The insertMultipleRecordsIntoSingerListTable(ArrayList) function is used
     * to insert multiple entries or records into the database singer list table.
     * These records, singer names, may be all the singers that may exist in the
     * device external storage.  It needs one parameter of type ArrayList<String>.
     * This ArrayList contains singer names info.
     * It does not need the table name since it is used to insert records only
     * into the singer list table.
     * It returns the id of the record inserted or -1 if there was an error.
     *
     * @param arrayListFilePathsSDCard type ArrayList<String>
     */
    public void insertMultipleRecordsIntoSingerListTable(ArrayList<String> arrayListFilePathsSDCard){
                                  //Variable for debugging purposes
        long insertRowId;
                                  //Setting the column values into a ContentValues obj.
        ContentValues valuesFullList = new ContentValues();

        for(int i = 0; i < arrayListFilePathsSDCard.size(); i++){
            valuesFullList.put(DBAccessHelper.COLUMN_SINGERLIST_NAMES, arrayListFilePathsSDCard.get(i));
                                  //Inserting values into the database table
            insertRowId = database.insert(DBAccessHelper.TABLE_SINGER_LIST, null, valuesFullList);
            valuesFullList.clear();
                                  //Code for debugging purposes
            if(insertRowId < 0){
                l("Error inserting records: class-DBAccess->insertMultipleRecordsIntoSingerListTable()");
            }
        }

    }//End of function




    /**
     * The insertRecordIntoPlaylistTable(String) function is used to insert one
     * entry or record into the database play list table.  It needs one parameter
     * of type string. This string is the full path of the mp3 file.  It does not
     * need the table name since it is used to insert records only into the play
     * list table.
     * It returns the id of the record inserted or -1 if there was an error.
     *
     * @param fullSongPath type String
     * @return type long
     */
    public long insertRecordIntoPlaylistTable(String fullSongPath){
        long insertedRowId;
                                  //Setting the column values into a ContentValues obj
        ContentValues valuesPlaylist = new ContentValues();
        valuesPlaylist.put(DBAccessHelper.COLUMN_PLAYLIST_SONGS, fullSongPath);
                                  //Inserting values into the database table
        insertedRowId = database.insert(DBAccessHelper.TABLE_PLAY_LIST, null, valuesPlaylist);
                                  //Code for debugging purposes
        if(insertedRowId < 0){
            l("Error inserting record class-DBAccess->insertRecordIntoPlaylistTable()");
        }

        return insertedRowId;

    }//End of function



    /**
     * insertMultipleRecordsIntoPlayListTable(ArrayList) function is used to
     * insert multiple records into the local database play list table. All
     * the records to be inserted are received in the ArrayList that the
     * function receives as a parameter. It loops the ArrayList calling the
     * Database insert method to insert one record at a time. If there is any
     * error, it let user know this with a Toast widget (No yet implemented)
     *
     * @param arrayListFilePaths type ArrayList
     */
    public void insertMultipleRecordsIntoPlayListTable(ArrayList<String> arrayListFilePaths){
                                  //Variable used to check for db insertion error
        long insertRowId;
                                  //ContentValue to hold record to be inserted
        ContentValues valuesFullList = new ContentValues();
                                  //Iterating the ArrayList to insert records
                                  //into DB table
        for(int i = 0; i < arrayListFilePaths.size(); i++){
                                  //Setting records into the ContentValue object
            valuesFullList.put(DBAccessHelper.COLUMN_PLAYLIST_SONGS, arrayListFilePaths.get(i));
                                  //Inserting the record
            insertRowId = database.insert(DBAccessHelper.TABLE_PLAY_LIST, null, valuesFullList);
            valuesFullList.clear();
                                  //Checking for errors
            if(insertRowId < 0){
                l("Error inserting records: class-DBAccess->insertMultipleRecordsIntoPlayListTable()");
            }
        }

    }//End of function



    /**
     * The insertRecordIntoSingerListTable(String) function is used to insert
     * one entry or record into the database singer list table.  It needs one
     * parameter of type string. This string is the full path of the mp3 file.
     * It does not need the table name since it is used to insert records only
     * into the play list table.
     * It returns the id of the record inserted or -1 if there was an error.
     *
     * @param singerFullPath type String
     * @return type long
     */
    public long insertRecordIntoSingerListTable(String singerFullPath){
                                  //Varaible used for debugging purposes
        long insertedRowId;
                                  //ContentValue used to help to make the insertion
        ContentValues valuesSingerList = new ContentValues();
        valuesSingerList.put(DBAccessHelper.COLUMN_SINGERLIST_NAMES, singerFullPath);
                                  //Making the insertion
        insertedRowId = database.insert(DBAccessHelper.TABLE_SINGER_LIST, null, valuesSingerList);
                                  //Debugging block
        if(insertedRowId < 0){
            l("Error inserting record clase-DBAccess->insertRecordIntoSingerListTable()");
        }
        return insertedRowId;

    }//End of function



    /**
     * The getAllMP3FilePathsFromDBFullListTable() function is used to get all
     * the records from the local database full list table.  It calls the
     * function query to execute the query.  It gets a cursor from this function
     * call querying the local database.
     * This function returns a cursor holding the data from the database full
     * list table.
     *
     *
     * @return type Cursor
     */
    public Cursor getAllMP3FilePathsFromDBFullListTable(){
        String[] columns = {DBAccessHelper.COLUMN_FILE_PATH};

        Cursor cur = database.query(DBAccessHelper.TABLE_FULL_LIST, columns, null, null, null,
                                                                                    null, null);

        return cur;

    }//End of function



    /**
     * The function getAllMP3FilePathsFromDBPlaylistTable(String) is used to
     * read all the records from the database play list table.  It does not need
     * any parameter like the name of the table since it is used to read data
     * only from the play list table.
     * It returns a cursor containing all the record in the play list table.
     *
     * @return type Cursor
     */
    public Cursor getAllMP3FilePathsFromDBPlaylistTable(){
        String[] columns = {DBAccessHelper.COLUMN_PLAYLIST_SONGS};

        Cursor cursor = null;
        cursor = database.query(DBAccessHelper.TABLE_PLAY_LIST, columns, null, null, null, null, null);
        return cursor;

    }//End of function



    /**
     * The function getAllSingerNamesFromDBSingerListTable() is used to read
     * all the records from the database singer list table.  It does not need
     * any parameter like the name of the table since it is used to read data
     * only from the singer list table.
     * It returns a cursor containing all the record in the singer list table.
     *
     * @return type Cursor
     */
    public Cursor getAllSingerNamesFromDBSingerListTable(){
        String[] columns = {DBAccessHelper.COLUMN_SINGERLIST_NAMES};

        Cursor cursor = database.query(DBAccessHelper.TABLE_SINGER_LIST, columns, null, null, null,
                                                                                        null, null);
        return cursor;

    }//End of function



    /**
     * The deleteAllRowsFromFulllistTable() function is used to delete all
     * records in the local database. It receives none parameter since it is
     * used to delete records in the database full list table.
     * It just calls a another function to execute the delete request.  It
     * gets the number of rows affected by this request.
     * This function returns the number of deleted records to the function
     * that made the call.
     *
     * @return type int
     */
    public int deleteAllRowsFromFulllistTable(){
        int deletedRows = 0;
                                  //Executing the delete request
        try{
            deletedRows = database.delete(DBAccessHelper.TABLE_FULL_LIST, "1", null);
        }
        catch(Exception e){		  //Checking for errors
            Toast.makeText(contexto, "Error: " + e.getMessage() , Toast.LENGTH_LONG).show();
        }

        return deletedRows;

    }//End of function



    /**
     * The deleteAllRowsFromPlaylistTable() function is used to deleted all the
     * records in the database play list table. It does not need any parameter
     * since it is used to delete records in the play list table.
     * It returns the number of records deleted.
     *
     * @return type int
     */
    public int deleteAllRowsFromPlaylistTable(){
        int deletedRows = 0;
                                  //Executing the delete request
        try{
            deletedRows = database.delete(DBAccessHelper.TABLE_PLAY_LIST, "1", null);
            database.close();
        }
        catch(Exception e){		  //Checking for errors
            Toast.makeText(contexto, "Error: " + e.getMessage() , Toast.LENGTH_LONG).show();
        }

        return deletedRows;

    }//End of function



    /**
     * The deleteOneRowsFromPlaylistTable(Integer, String) function is used to
     * delete only one record in the database play list table.  It needs the
     * file path as a parameter. It does not need the table name since it is used
     * to delete record in the play list table.
     * It returns the number of records deleted.
     *
     * @param strFilePath type String
     * @return type int
     */
    public int deleteOneRowsFromPlaylistTable(String strFilePath){
        String[] whereArgs = new String[]{strFilePath};
        int deletedRows = 0;
                                  //Executing the delete request
        try{
            deletedRows = database.delete(DBAccessHelper.TABLE_PLAY_LIST,
                                            DBAccessHelper.COLUMN_PLAYLIST_SONGS + " = ?", whereArgs);
            database.close();
        }
        catch(Exception e){		  //Checking for errors
            Toast.makeText(contexto, "Error: " + e.getMessage() , Toast.LENGTH_LONG).show();
        }

        return deletedRows;

    }//End of function



    /**
     * The deleteAllRowsFromSingerListTable() function is used to delete all
     * records in the local database. It receives none parameter since it is
     * used to delete records in the database singer list table.
     * It just calls a another function to execute the delete request.  It
     * gets the number of rows affected by this request.
     * This function returns the number of deleted records to the function that
     * made the call.
     *
     * @return type int
     */
    public int deleteAllRowsFromSingerListTable(){
        int deletedRows = 0;
                                  //Executing the delete request
        try{
            deletedRows = database.delete(DBAccessHelper.TABLE_SINGER_LIST, "1", null);
        }
        catch(Exception e){		  //Checking for errors
            Toast.makeText(contexto, "Error: " + e.getMessage() , Toast.LENGTH_LONG).show();
        }

        return deletedRows;

    }//End of function



    /**
     * The l(String) function is used only to debug this class. It uses the
     * Log.d() function to pass the information to the Android Monitor window.
     * This information contains the class name and some information about the
     * error or data about the debugging process.
     *
     * @param str type String
     */
    private void l(String str){
        Log.d("NIKO", this.getClass().getSimpleName() + " -> " + str);
    }




}   //End of Class DBAccess.java



/*************************END OF FILE DBAccess.java****************************/
