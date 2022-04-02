package com.jjesuxyz.muxiplayer.modelo;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.jjesuxyz.muxiplayer.modelo.DBData.DBAccess;
import com.jjesuxyz.muxiplayer.modelo.DBData.DBAccessHelper;

import java.io.File;
import java.util.ArrayList;



/**
 * ELModelo public class is used to get access to the local database to insert,
 * retrieve and delete records into or from its tables. In theory all the user
 * interface activities and user interface fragments must use this class to
 * access the local database. This theory is Not totally done/accomplished yet.
 * This class manages any data manipulation before sending that data to the
 * classes that asked for that data.
 *
 * Created on 12/5/2020 12:10 pm.
 */

public class ElModelo {
                                  //Object to be pass to the class accessing
                                  //the local DB.
    private Context context;
                                  //ArrayList to hold MP3 file paths obtained
                                  //from SD Card.
    private ArrayList<String> arrListFiles;
    private ArrayList<String> arrayListPLayList;
                                  //Variable to access local database.
    private DBAccess dbAccess;




    /**
     * Public class constructor is used to perform some global class variables
     * initialization.  It receives a Context object as the only parameter it
     * needs.
     *
     * @param context type Context
     */
    public ElModelo(Context context){

        arrListFiles = new ArrayList<>();
        this.context = context;

    }   //End of class constructor.




    /**
     * insertMultipleRecordsIntoPlayListTable(ArrayList) function is used to
     * insert multiple MP3 records into the local database play list table.
     * It inserts into the database table all the records it receives as an
     * ArrayList parameter.
     * It does not returns any value.
     *
     * @param arrayListFilePath type ArrayList
     */
    public void insertMultipleRecordsIntoPlayListTable(ArrayList<String> arrayListFilePath){
                                  //Checking that ArrayList has been
                                  //instantiated before????.
        if (arrayListFilePath != null) {
                                  //Getting access to the local database.
            dbAccess = new DBAccess(context);
                                  //Inserting multiple records into the database.
            dbAccess.insertMultipleRecordsIntoPlayListTable(arrayListFilePath);
            dbAccess.close();
            dbAccess = null;
        }
        else {
                                  //Code just for debugging purposes.
            l("Function insertMultipleRecordsIntoPlayListTable(AL<Str> => NULL) ");
        }

    }   //End of insertMultipleRecordsIntoPlayListTable() function.




    /**
     * insertMultipleRecordsIntoFullListTable(ArrayList) function is used to
     * insert multiple MP3 records into the local database full list table. It
     * inserts into the database table all the records it receives as an
     * ArrayList parameter.
     * It does not returns any value.
     *
     * @param arrayListFilePath type ArrayList
     */
    public void insertMultipleRecordsIntoFullListTable(ArrayList<String> arrayListFilePath){
                                  //Checking that ArrayList has been
                                  //instantiated before????.
        if (arrayListFilePath != null) {
                                  //Getting access to the local database.
            dbAccess = new DBAccess(context);
                                  //Inserting multiple records into the database
                                  //table.
            dbAccess.insertMultipleRecordsIntoFullListTable(arrayListFilePath);
            dbAccess.close();
            dbAccess = null;
        }
        else {
                                  //Code just for debugging purposes.
            l("Function insertMultipleRecordsIntoPlayListTable(AL<Str> => NULL) ");
        }

    }   //End of insertMultipleRecordsIntoPlayListTable() function.




    /**
     * insertMultipleRecordsIntoSingerListTable(ArrayList) function is used to
     * insert multiple singer names records into the local database singer list
     * table. It inserts into the database table all the records it receives as
     * an ArrayList parameter.
     * It does not returns any value.
     *
     * @param arrayListSingerName type ArrayList
     */
    public void insertMultipleRecordsIntoSingerListTable(ArrayList<String> arrayListSingerName){
                                        //Checking that ArrayList has been
                                        //instantiated before????.
        if (arrayListSingerName != null) {
                                        //Getting access to the local database.
            dbAccess = new DBAccess(context);
                                        //Inserting multiple records into the
                                        //database table.
            dbAccess.insertMultipleRecordsIntoSingerListTable(arrayListSingerName);
            dbAccess.close();
            dbAccess = null;
        }
        else {
                                        //Code just for debugging purposes.
            l("Function insertMultipleRecordsIntoSingerListTable(AL<Str> => NULL) ");
        }

    }   //End of insertMultipleRecordsIntoSingerListTable() function.




    /**
     * getArrayListFromPlayListTable() function is used to get all the records
     * from the local database play list table. It returns all those records
     * after extracting them the Cursor object it receives. from the database.
     * It just calls another function in another class that actually access the
     * database tables to complete the get data action.
     *
     * @return type ArrayList
     */
    public ArrayList<String> getArrayListFromPlayListTable(){
                                  //ArrayList to hold all MP3 records.
        arrayListPLayList = new ArrayList<>();
                                  //Instantiating object to access local DB.
        dbAccess = new DBAccess(context);
                                  //Getting all MP3 records from local DB.
        Cursor cursor = dbAccess.getAllMP3FilePathsFromDBPlaylistTable();
        int index = cursor.getColumnIndex(DBAccessHelper.TABLE_PLAY_LIST);
                                  //Extracting MP3 records from Cursor.
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
            arrayListPLayList.add(str);
        }
                                  //Closing local DB.
        dbAccess.close();
                                  //Returning all MP3 files gotten from local Db.
        return arrayListPLayList;

    }   //End of getArrayListFromPlayListTable() function.




    /**
     * getAllMP3FilePathFromDBFulllistTable(String) function is used to get all
     * the records from the local database full list table. It returns all those
     * records after extracting them the Cursor object it receives. It just calls
     * another function in another class that actually access the database tables
     * to complete the get data action.
     *
     * Note: The String parameter it receives it is never used.
     *
     * @param strParam type String
     * @return type ArrayList
     */
    public ArrayList<String> getAllMP3FilePathFromDBFulllistTable(String strParam) {
                                  //ArrayList to hold all MP3 records.
        arrayListPLayList = new ArrayList<>();
                                  //Instantiating object to access local DB.
        dbAccess = new DBAccess(context);
                                  //Getting all MP3 records from local DB.
        Cursor cursor = dbAccess.getAllMP3FilePathsFromDBFullListTable();
        int index = cursor.getColumnIndex(DBAccessHelper.TABLE_FULL_LIST);
                                  //Extracting MP3 records from Cursor.
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
            arrayListPLayList.add(str);
        }
                                  //Closing local DB.
        dbAccess.close();
                                  //Returning all MP3 files gotten from local Db.
        return arrayListPLayList;

    }   //End of getAllMP3FilePathFromDBFulllistTable() function.




    /**
     * getAllSingerNamesFromDBSingerListTable(String) function is used to get
     * all the records from the local database singer list table. It returns
     * all those records after extracting them from the Cursor object it
     * receives from DB. It just calls another function in another class that
     * actually access the database tables to complete the get data action.
     *
     * Note: The String parameter it receives it is never used.
     *
     * @param strParam type String
     * @return type ArrayList
     */
    public ArrayList<String> getAllSingerNamesFromDBSingerListTable(String strParam) {
                                  //ArrayList to hold all singer names.
        arrayListPLayList = new ArrayList<>();
                                  //Instantiating object to access local DB.
        dbAccess = new DBAccess(context);
                                  //Getting all singer names from local DB.
        Cursor cursor = dbAccess.getAllSingerNamesFromDBSingerListTable();
        int index = cursor.getColumnIndex(DBAccessHelper.TABLE_SINGER_LIST);
                                  //Extracting singer names from Cursor.
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
            arrayListPLayList.add(str);
        }
                                  //Closing local DB.
        dbAccess.close();
                                  //Returning all singer names gotten from
                                  //local Db.
        return arrayListPLayList;

    }   //End of getAllSingerNamesFromDBSingerListTable() function.




    /**
     * deleteOneRowOnPlayListTable(String) function is used to deleted one
     * record from the local database play list table. The string parameter it
     * receives is the record that will be deleted from the database play list
     * table.  It returns the number of records deleted which are 1 or 0. It
     * just calls another function in another class that actually access the
     * database tables to complete the delete action.
     *
     * @param strTmp String
     * @return type int
     */
    public int deleteOneRowOnPlayListTable(String strTmp){
                                  //Variable to hold the number of deleted
                                  //records.
        int delRecordResult;
                                  //Instantiating DBAccess object to actually
                                  //access the DB.
        dbAccess = new DBAccess(context);
                                  //Deleting one record from database play
                                  //list table.
        delRecordResult = dbAccess.deleteOneRowsFromPlaylistTable(strTmp);
                                  //if for debugging purposes.
        if (delRecordResult <= 0){
            l("Funcion deleteOneRowOnPlayListTable(String ERROR " + delRecordResult);
        }

        return delRecordResult;

    }   //End of deleteOneRowOnPlayListTable() function.




    /**
     * deleteAllRecordsOnPlayListTable() function is used to delete all records
     * from the database play list table. It returns the number of deleted records.
     * It just calls another function in another class that actually access the
     * database tables to complete the delete action.
     *
     * @return type int
     */
    public int deleteAllRecordsOnPlayListTable(){
                                  //Variable to hold the number of deleted
                                  //records.
        int delRecordResult;
                                  //Instantiating DBAccess to actually access the
                                  //local DB.
        dbAccess = new DBAccess(context);
                                  //Deleting all records from database play list
                                  //table.
        delRecordResult = dbAccess.deleteAllRowsFromPlaylistTable();
        if (delRecordResult <= 0){
            l("Deleted records number with Function deleteAllRowsFromPlayListTable()" + delRecordResult);
        }
                                  //Returning number of deleted records from play
                                  //list table.
        return delRecordResult;

    }   //End of deleteAllRecordsOnPlayListTable() function.




    /**
     * deleteAllRecordsOnFullListTable() function is used to delete all records
     * from the database full list table. It returns the number of deleted records.
     * It just calls another function in another class that actually access the
     * database tables to complete the delete action.
     *
     * @return type int
     */
    public int deleteAllRecordsOnFullListTable(){
                                  //Variable to hold the number of deleted
                                  //records.
        int delRecordResult;
                                  //Instantiating DBAccess to actually access the
                                  //local DB.
        dbAccess = new DBAccess(context);
                                  //Deleting all records from database full list
                                  //table.
        delRecordResult = dbAccess.deleteAllRowsFromFulllistTable();
        if (delRecordResult <= 0){
            l("Deleted records number with Function deleteAllRowsFromFullListTable()" + delRecordResult);
        }
                                  //Returning number of deleted records from full
                                  //list table.
        return delRecordResult;

    }   //End of deleteAllRecordsOnFullListTable() function.




    /**
     * deleteAllRecordsOnSingerListTable() function is used to delete all
     * records from the database singer list table. It returns the number of
     * deleted records.  It just calls another function in another class that
     * actually access the database tables to complete the delete action.
     *
     * @return type int
     */
    public int deleteAllRecordsOnSingerListTable(){
                                  //Variable to hold the number of deleted
                                  //records.
        int delRecordResult;
                                  //Instantiating DBAccess to actually access
                                  //the local DB.
        dbAccess = new DBAccess(context);
                                  //Deleting all records from database singer
                                  //list table.
        delRecordResult = dbAccess.deleteAllRowsFromSingerListTable();
        if (delRecordResult <= 0){
            l("Deleted records number with Function deleteAllRowsFromSingerListTable()" + delRecordResult);
        }
                                  //Returning number of deleted records from
                                  //full list table.
        return delRecordResult;

    }   //End of deleteAllRecordsOnFullListTable() function.




    /**
     * getArrayListOfFiles() public function is used to send the list of mp3
     * files to another class of this project. It returns an ArrayList that
     * holds the list of mp3 files. It returns the variable even if  the
     * ArrayList size is zero or it is null;
     *
     * @return type ArrayList
     */
    public ArrayList<String> getArrayListOfFiles(){

        getAllMP3FilesFromSDCard(null, 1);

        return arrListFiles;

    }   //End of getArrayListOfFiles() function.




    /**
     * getAllMP3Files(File, int) function is used to traverse the SD Card
     * directory looking for MP3 files. When it traverse the directory it pushes
     * all the mp3 file it finds into an ArrayList of file paths.  All this mp3
     * files paths are later inserted into the db full list table.
     *
     * @param file type File
     * @param level type int
     */
    private void getAllMP3FilesFromSDCard(File file, int level){
        int aSDKVersion = 0;
                                  //This line is for debugging.
        String aStrName =  Build.MODEL + " - " + Build.MANUFACTURER + " - " + Build.HOST ;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                  //Checking if it is the first function call
            if (file == null && level <= 1) {
                Toast.makeText(context, "BaseDirectorioLength:  recursion 0", Toast.LENGTH_SHORT).show();
                                  //Switch block to detect Android version, and
                                  //manufacturer, and define the SD Card path
                                  //Getting the SDK Version.
                aSDKVersion = Build.VERSION.SDK_INT;

                switch (aSDKVersion) {
                                  //Android 4.4 SDK 19 KitKat.
                    case 19:
                        aStrName = "KitKat";
                        break;
                                  //Android 5 SDK 21 Lillipop.
                    case 21:
                        aStrName = "Lillipop";
                        break;
                                  //Android 5.1 SDK 22 Lillipop.
                    case 22:
                        aStrName = "Lillipop";
                        break;
                                  //Android 6 SDK 23 Marshmallow.
                    case 23:
                        aStrName = "Marshmallow";
                        break;
                                  //Android 7 SDK 24 Nougat.
                    case 24:
                        aStrName = "Nougat";
                        file = new File("/storage/78C9-1509/Music/");
                        break;
                                  //Android 7.1 SDK 25 Nougat.
                    case 25:
                        aStrName = "Nougat";
                                  //ZTE Android 7.1.1
                        file = new File("/storage/3061-6433/Music/");
                        break;
                                  //Android 8 SDK 2 Oreo.
                    case 26:
                        aStrName = "Oreo";
                        break;
                                  //Android 8.1 SDK 27 Oreo.
                    case 27:
                        aStrName = "Oreo";
                        if (Build.MANUFACTURER.equalsIgnoreCase("LGE")) {
                            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                        }
                        if (Build.MANUFACTURER.equalsIgnoreCase("google"))
                            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                        break;
                                  //Android 9 SDK 28 Pie.
                    case 28:
                        aStrName = "Pie";
                        break;
                                  //Android 10 SDK 29 Q.
                    case 29:
                        aStrName = "Q";
                        if (Build.MANUFACTURER.equalsIgnoreCase("motorola"))
                            file = new File("/storage/78C9-1509/");
                        else if (Build.MANUFACTURER.equalsIgnoreCase("google"))
                            file = new File("/storage/1E11-2C11/");
                        break;
                                  //Android 11 SDK 30.
                    case 30:
                        aStrName = "Nombre-30";
                        break;
                                  //Android 12 SDK 31-32.
                    case 31:
                        aStrName = "Nombre-31";
                        break;
                }   // End of switch block.

                                  //For debugging.
                l("SDK Version " + String.valueOf(aSDKVersion) + " - " + aStrName);

                Toast.makeText(context, "BaseDirectorio:  " + file.getPath(), Toast.LENGTH_SHORT).show();
                getAllMP3FilesFromSDCard(file, ++level);

            }   //End of if(file==null && level <= 1)
            else {
                                  //Making sure file is not null.
                if (file != null) {
                                  //Pushing mp3 files into the ArrayList.
                    if (file.isFile()) {
                        if (isFileMP3(file.getAbsolutePath())) {
                            arrListFiles.add(file.getAbsolutePath());
                        }
                    }
                                  //Moving into another directory, deeper level.
                    else if (file.isDirectory() && level <= 5) {
                        level = level + 1;
                        File[] arrFiles = file.listFiles();
                                  //Traversing directories.
                        if (arrFiles != null) {
                            for (int i = 0; i < arrFiles.length; i++) {
                                  //This recursive function is calling itself.
                                getAllMP3FilesFromSDCard(arrFiles[i], level);
                            }
                        }
                        else {
                            l("Directory is possible empty. Array of files is null:  ");
                        }
                    }
                }
                else {
                    l("Error: File object to access SD Card directory is null");
                }
            }   //End of if(file==null && level <= 1)//else

        }   //End of if(Environment.

    }   //End of getAllMP3Files() function.




    /**
     * isFileMP3() function is used to check if the file name it receives as a
     * parameter is an MP3 file or a file of any other type. It returns true if
     * file name is an MP3 file, false otherwise.
     *
     * @param pathFile type String
     * @return type boolean
     */
    private boolean isFileMP3(String pathFile){
        if(pathFile.endsWith(".mp3") || pathFile.endsWith(".MP3")){
            return true;
        }
        else {
            return false;
        }

    }   //End of isFileMP3() function.




    /**
     * isTableEmpty(String) function is used to check if a database table, whose
     * name it receives as a parameter, is empty.  It returns true if table is
     * empty false otherwise.
     *
     * @param strTableName type String
     * @return type boolean
     */
    public boolean isTableEmpty(String strTableName) {
                                  //Getting access to the local database.
        DBAccess dbAccess = new DBAccess(context);
                                  //Calling another function that actually access
                                  //the DB.
        boolean blIsTbEmpty = dbAccess.isTableEmpty(strTableName);
                                  //Closing the database.
        dbAccess.close();

        return blIsTbEmpty;

    }   //End of isTableEmpty() function.




    /**
     * getSingerNames(String) function is used to find all the songs that are
     * sang by the singer whose name this function receives as a parameter. The
     * search is done on the data that is in the database full list table.
     *
     * @param singerNombre type String
     * @return type ArrayList
     */
    public ArrayList<String> getSingerNames(String singerNombre) {
                                  //ArrayList to hold the MP3 music file sang by
                                  //the singer.
        ArrayList<String> arrayListPLayList = new ArrayList<>();
                                  //Getting access to local database.
        DBAccess dbAccess = new DBAccess(context);
                                  //Retrieving MP3 record info from database
                                  //table
        Cursor cursor = dbAccess.getAllMP3FilePathsFromDBFullListTable();
        int index = cursor.getColumnIndex(DBAccessHelper.TABLE_FULL_LIST);
                                  //Traversing cursor searching MP3 file with
                                  //singer name equal to the singerNombre
                                  //parameter.
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
                                  //Comparing singer names
            if (str.toLowerCase().contains(singerNombre)) {
                arrayListPLayList.add(str);
            }
        }
                                  //Closing local DB.
        dbAccess.close();
                                  //Returning all singer names gotten from
                                  //local Db.
        return arrayListPLayList;

    }   //End of getSingerNames function.




    /**
     * The l(String) function is used only to debug this class. It uses the
     * Log.d() function to pass the information to the Android Monitor window.
     * This information contains the class name and some information about the
     * error or data about the debugging process.
     *
     * @param str type String
     */
    private void l(String str){
                                  //Local variable for debugging.
        final String TAG = "NICKY";

        Log.d(TAG, this.getClass().getSimpleName() + " -> " + str);

    }   //End of l() function.


}   //End of Class ElModelo.



/********************END OF FILE ElModelo.java*********************************/

