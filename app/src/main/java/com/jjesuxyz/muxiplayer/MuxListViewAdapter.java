package com.jjesuxyz.muxiplayer;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;



/**
 * MuxListViewAdapter class is used by the ListView widget to define the layout
 * of each row in the ListView. In its constructor this class receive as
 * parameter a reference to the main UI and to the activities hosting the
 * fragments that also uses ListView widgets to display data to the user. It
 * also receives an ArrayList the contains all the MP3 file info that will be
 * displayed in the ListView.
 *
 * Created on 12/5/2020 2:35 pm.
 */

public class MuxListViewAdapter extends BaseAdapter {
                                  //ArrayList holding MP3 file path info
    private ArrayList<String> listMP3RowNumber;
    private ArrayList<String> listMP3FilePath;
    private LayoutInflater inflater;




    /**
     * MuxListViewAdapter() constructor of this class. This is the only
     * constructor that this class uses. It receives the list of mp3 file paths
     * that are in the device sdcard. It also receives the pointer to the main
     * class managing the window user interface. It calls another function to
     * fill another ArrayList holding the numbers mp3 files that the device
     * contains.
     *
     * @param activity type Activity
     * @param mp3FilePath type ArrayList
     */
    public MuxListViewAdapter(Activity activity, ArrayList<String> mp3FilePath){
        super();

        listMP3FilePath = new ArrayList<>();
        listMP3FilePath.addAll(mp3FilePath);
        inflater = activity.getLayoutInflater();
        createHashMap();

    }   //End of MuxListViewAdapter() function



    /**
     * getData() function is used to returns just the ArrayList holding MP3 file
     * paths.
     *
     * @return type ArrayList
     */
    public ArrayList<String> getData(){
        return listMP3FilePath;
    }



    /**
     * createHashMap() function is used to fill an ArrayList of number,
     * 0->number of mp3 files in the device. This list of numbers is used to
     * customize each row in the ListView since each row contains one number
     * and the mp3 file path string.
     *
     */
    public void createHashMap(){
        listMP3RowNumber = new ArrayList<>();
        for(Integer i = 0; i < listMP3FilePath.size(); i++){
            listMP3RowNumber.add(i.toString());
        }
    }   //End of createHashMap() function



    /**
     * getCount() function is used to return the number of mp3 files that are
     * saved in the device sdcard.
     *
     * @return type int
     */
    @Override
    public int getCount() {
        return listMP3FilePath.size();
    }   //End of getCount() function




    /**
     * getItem() this function is used to return the file path of an mp3 file.
     * It needs the ArrayList position of the mp3 file.
     *
     * @param position type int
     * @return type Object
     */
    @Override
    public Object getItem(int position) {
        return listMP3FilePath.get(position);
    }   //End of getItem() function



    /**
     * getItemId() function is not used by the programmer coding this project.
     *
     * @param position int
     * @return type long
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }   //End of getItemId() function



    /**
     * getView() function is used to customize each row of the ListView that
     * show users the list of mp3 files that the device sdcard contains. Each
     * row contains two TextView elements that are set to by this function.
     * One TextView shows the mp3 name, and the other shows the mp3 file number
     * within the ArrayList. This function also customizes the background color
     * of adjacent row to different colors.
     *
     * @param position type int
     * @param convertView type View
     * @param parent type ViewGroup
     * @return type void
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

                                  //TextView used to set/change the row text
        TextView txtvwRowNumber;
        TextView txtvwMP3FilePath;

                                  //Fist time that this function is called by
                                  //the system
        if(convertView == null) {
                                  //Getting the layout file used in each row
                                  //Por debugging:
                                  //inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.row_layout, parent, false);
        }
                                  //Getting actual TextViews used in each row
        txtvwRowNumber = convertView.findViewById(R.id.txtvwRowNumber);
        txtvwMP3FilePath = convertView.findViewById(R.id.txtvwMP3FilePath);
                                  //Setting each row text
        txtvwRowNumber.setText(listMP3RowNumber.get(position ) + "-> ");
                                  //Getting the song name from the file path
        String[] str = listMP3FilePath.get(position).split("/");
        String strTmp = str[str.length -1];
                                  //Removing '/storage/sdcard0' from mp3
                                  //file absolute path
        txtvwMP3FilePath.setText(strTmp);
                                  //Setting adjacent row background color
        if((position % 2) == 0){
            int miOtroAzul = Color.parseColor("#010445");
            convertView.setBackgroundColor(miOtroAzul);
        }
        else{
            int miAzul = Color.parseColor("#01042A");
            convertView.setBackgroundColor(miAzul);
        }

        return convertView;
    }   //End of getView() function



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


}   //End of Class MuxListViewAdapter



/********************END OF FILE MuxListViewAdapter.java***********************/