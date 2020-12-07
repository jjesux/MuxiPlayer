package com.jjesuxyz.muxiplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;




/**
 * NKRadio class is used to received and play live stream audio data from radio
 * stations. So far it works fine, but only the stop and play buttons works as
 * expected. The other two buttons Presets and equalizer do not work because
 * their implementation is no completed. In the future this button may be
 * implemented but with a new design and functionality.
 *
 * Also at this date 12/5/2020 some of the radio station url are broken, and
 * the connection cannot be established.
 *
 * Created on 12/5/2020 4:21 pm.
 */

public class NKRadio extends Activity {

                                  //Set of variables used in this project
    private MediaPlayer mdPlayer;
                                  //Button and listener variables
    private Button btnRadioPlay;
    private final int BTN_RADIOPLAY_ID = R.id.btnRadioPlay;
    private Button btnRadioStop;
    private final int BTN_RADIOSTOP_ID = R.id.btnRadioStop;
    private Button btnRadioPresets;
    private final int BTN_RADIOPRESETS_ID = R.id.btnRadioPreset;
    private Button btnRadioEquali;
    private final int BTN_RADIOEQUALI_ID = R.id.btnRadioEquali;

    private RadioButtonOptionsListeners radioButtonOptionsListeners;

                                  //RadioButton  and listener variables
    private RadioButton rdBtn0;
    private final int RDBUTTON0_ID = R.id.rdbtnStation0;
    private RadioButton rdBtn1;
    private final int RDBUTTON1_ID = R.id.rdbtnStation1;
    private RadioButton rdBtn2;
    private final int RDBUTTON2_ID = R.id.rdbtnStation2;
    private RadioButton rdBtn3;
    private final int RDBUTTON3_ID = R.id.rdbtnStation3;
    private RadioButton rdBtn4;
    private final int RDBUTTON4_ID = R.id.rdbtnStation4;
    private RadioButton rdBtn5;
    private final int RDBUTTON5_ID = R.id.rdbtnStation5;

    private RadioButtonPresetsListener radioButtonPresetsListener;

                                  //Variables to hold list of preset station
    private ArrayList<String> listPresetsIP = null;
    private ArrayList<String> listPresetsNames = null;
                                  //Variables holding radio station playing
    private String strRdStationPlayingIP = null;
    private String strRdStationPlayingInfo = null;
                                  //TextView variables hold radio station info
    private TextView txtvwStationIP = null;
    private TextView txtvwStationInfo = null;





    /**
     * onCreate() function is used to start running the application. It does
     * all the initialization of variables needed to set the window app as well
     * as all the button listeners. These listener react to user events. These
     * events are only Button and RadioButton events.
     *
     * @param savedInstanceState type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nk_radio_layout);
                                  //Setting listeners
        radioButtonOptionsListeners = new RadioButtonOptionsListeners();
        radioButtonPresetsListener = new RadioButtonPresetsListener();
                                  //Getting list of radio station info
        listPresetsIP = new ArrayList<>();
        listPresetsNames = new ArrayList<>();
                                  //Button and listener initialization
        initalizeRadioFeatureButtons();
        setRadioFeatureButtonListeners();
                                  //RadioButton and listener initialization
        initializeRadioPresetButtons();
        setRadioPresetButtonsListeners();
                                  //Reading radio station presets from a file
        getRadioButtonTextFromPresetFile(1);
                                  //Setting the RadioButton text
        setRadioButtonsText();
                                  //Getting last radio station selected
        setRadioStationToPlayFromFile();

    }   //End of onCreate() function



    /**
     * setRadioStationToPLayFromFile() function is used to set the radio
     * station to play when the radio player app is started, run. This info is
     * read from a file save in the internal storage. When a radio station is
     * selected from the preset list, the radio info is saved in this file. In
     * this way the app can remember which radio station was playing when the
     * app was turn off.
     *
     */
    private void setRadioStationToPlayFromFile(){
                                  //Reading files to get radio station info
        readRadioStationSelectedToFile();
                                  //Getting a pointer to TextView holding radio
                                  //station
                                  //playing info
        txtvwStationIP =   findViewById(R.id.txtvwStationIP);
        txtvwStationInfo = findViewById(R.id.txtvwStationInfo);
                                  //Setting TextView text
        txtvwStationIP.setText(strRdStationPlayingIP);
        txtvwStationInfo.setText(strRdStationPlayingInfo);

    }   //End of setRadioStationToPLayFromFile() function





    /**************************************************************************
     *  VARIABLE INITIALIZATION CODE SECTION                                  *
     **************************************************************************/

    /**
     * initializeRadioPresetButtons() function is used to get pointers to the
     * Buttons that are used in the app. These buttons are used to implement the
     * off, play, preset, and equalizer features of the app.
     *
     */
    private void initializeRadioPresetButtons(){
        rdBtn0 =  findViewById(R.id.rdbtnStation0);
        rdBtn1 =  findViewById(R.id.rdbtnStation1);
        rdBtn2 =  findViewById(R.id.rdbtnStation2);
        rdBtn3 =  findViewById(R.id.rdbtnStation3);
        rdBtn4 =  findViewById(R.id.rdbtnStation4);
        rdBtn5 =  findViewById(R.id.rdbtnStation5);

    }   //End of initializeRadioPresetButtons() function



    /**
     * setRadioPresetButtonsListeners() function is used to get pointers to the
     * RadioButtons that are used in this app. These RadioButtons are used to
     * represent the set of radio station that are preset. Clicking any of these
     * RadioButtons change the radio station been played.
     *
     */
    private void setRadioPresetButtonsListeners(){
        rdBtn0.setOnClickListener(radioButtonPresetsListener);
        rdBtn1.setOnClickListener(radioButtonPresetsListener);
        rdBtn2.setOnClickListener(radioButtonPresetsListener);
        rdBtn3.setOnClickListener(radioButtonPresetsListener);
        rdBtn4.setOnClickListener(radioButtonPresetsListener);
        rdBtn5.setOnClickListener(radioButtonPresetsListener);

    }   //End of setRadioPresetButtonsListeners() function



    /**
     * setRadioButtonsText() function is used to set the names of the radio
     * station that are in the list of preset radio station.
     *
     */
    private void setRadioButtonsText(){
        rdBtn0.setText(listPresetsNames.get(0));
        rdBtn1.setText(listPresetsNames.get(1));
        rdBtn2.setText(listPresetsNames.get(2));
        rdBtn3.setText(listPresetsNames.get(3));
        rdBtn4.setText(listPresetsNames.get(4));
        rdBtn5.setText(listPresetsNames.get(5));

    }   //End of setRadioButtonsText() function



    /**
     * initalizeRadioFeatureButtons() function is used to initialize the
     * variables that hold pointers to the window buttons that change the
     * state of the radio player.
     */
    private void initalizeRadioFeatureButtons(){
        btnRadioPlay =    findViewById(R.id.btnRadioPlay);
        btnRadioStop =    findViewById(R.id.btnRadioStop);
        btnRadioPresets = findViewById(R.id.btnRadioPreset);
        btnRadioEquali =  findViewById(R.id.btnRadioEquali);

    }   //End of initalizeRadioFeatureButtons() function



    /**
     * setRadioFeatureButtonListeners() function is used to set the listeners
     * of the radio button option features.
     *
     */
    private void setRadioFeatureButtonListeners(){
        btnRadioPlay.setOnClickListener(radioButtonOptionsListeners);
        btnRadioStop.setOnClickListener(radioButtonOptionsListeners);
        btnRadioPresets.setOnClickListener(radioButtonOptionsListeners);
        btnRadioEquali.setOnClickListener(radioButtonOptionsListeners);

    }   //End of setRadioFeatureButtonListeners() function


    /**************************************************************************
     *  END OF VARIABLE INITIALIZATION CODE SECTION                           *
     **************************************************************************/





    /***************************************************************************
     *  WRITING AND READING FILES CODE SECTION                                 *
     ***************************************************************************/

    /**
     * writeRadioStationSelectedToFile() function is used to write or save radio
     * station info into a text file saved in the internal memory of this app.
     * Right it just writes one line of info, corresponding to only one radio
     * station. In the future it will be used to save all the radio stations
     * that this app may keep in memory.
     * The String parameter it receives holds the radio station info: IP address
     * and human readable name.
     *
     * @param radioStation type String
     */
    private void writeRadioStationSelectedToFile(String radioStation){
        File file = new File(NKRadio.this.getFilesDir(), "miFilemon");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(radioStation.getBytes());
            fileOutputStream.close();

        }
                                  //Exceptions and debugging code
        catch (FileNotFoundException fnfex){
            fnfex.printStackTrace();
            dbgFunc(fnfex.getMessage(), 0);
        }
        catch (IOException ioex){
            ioex.printStackTrace();
            dbgFunc(ioex.getMessage(), 0);
        }

    }   //End of writeRadioStationSelectedToFile() function



    /**
     * readRadioStationSelectedToFile() function is used to read a file that
     * holds the radio station info of the radio station that was being played
     * before the radio player was turned off. Kind of memory.
     *
     */
    private void readRadioStationSelectedToFile(){

        try {
            FileInputStream fileInputStream = NKRadio.this.openFileInput("miFilemon");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String strTmp;
                                  //Reading just one line of radio station info
            while((strTmp = bufferedReader.readLine()) != null){
                                  //Internet ip address
                strRdStationPlayingIP = (strTmp.split(">>"))[0];
                                  //Radio station info for humans
                strRdStationPlayingInfo = (strTmp.split(">>"))[1];
            }
                                  //Closing streams
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

        }
                                  //Exceptions and debugging code
        catch (FileNotFoundException fnfex){
            fnfex.printStackTrace();
            dbgFunc(fnfex.getMessage(), 0);
        }
        catch (IOException ioex){
            ioex.printStackTrace();
            dbgFunc(ioex.getMessage(), 0);
        }

    }   //End of readRadioStationSelectedToFile() function



    /**
     * getRadioButtonTextFromPresetFile() function is used to read a file from
     * the raw directory of the app. This function will not be used in the
     * future because it was intended to modify the list of radio station that
     * this app may hold. Right the radio station are saved in a file in this
     * raw directory. It cannot be modified by this function.
     *
     * @param rdPresetList type int
     */
    private void getRadioButtonTextFromPresetFile(int rdPresetList){
                                  //Stream variables used to open and read a
                                  //text file
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
                                  //Open a file with only one radio station
                                  //info to play
        if(rdPresetList == 0){
            inputStream = NKRadio.this.getResources().openRawResource(R.raw.radio_estation_play);
        }
                                  //Open a file to read all the preset radio
                                  //station
        else {
            inputStream = NKRadio.this.getResources().openRawResource(R.raw.estaciones);
        }

        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String strTmp;
                                  //Actually reading radio station info name, IP
                                  //address
            while ((strTmp = bufferedReader.readLine()) != null){
                                  //reading only one line to play
                if(rdPresetList == 0){
                    strRdStationPlayingIP = (strTmp.split(">>"))[0];
                    strRdStationPlayingInfo = (strTmp.split(">>"))[1];
                }
                                  //Reading list of preset radio stations
                else {
                    listPresetsIP.add((strTmp.split(">>"))[0]);
                    listPresetsNames.add((strTmp.split(">>"))[1]);
                }
            }
                                  //Closing streams
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

        }
                                  //Exceptions and debugging code
        catch(FileNotFoundException fnfex){
            fnfex.printStackTrace();
            dbgFunc(fnfex.getMessage(), 0);
        }
        catch(IOException ioex){
            ioex.printStackTrace();
            dbgFunc(ioex.getMessage(), 0);
        }

    }   //End of getRadioButtonTextFromPresetFile() function



    /**************************************************************************
     *  END OF WRITING AND READING FILES CODE SECTION                         *
     **************************************************************************/





    /**************************************************************************
     *  MEDIA PLAYER LOGISTIC CODE SECTION                                    *
     **************************************************************************/

    /**
     * stopPlayingRadio() function is used to shut off the radio player if it
     * is playing any audio. It also resets it and releases resources to the
     * system, and it also nulls the radio player.
     *
     */
    private void stopPlayingRadio(){
        if(mdPlayer != null){
            mdPlayer.stop();
            mdPlayer.reset();
            mdPlayer.release();
            mdPlayer = null;
        }else{
            dbgFunc("Radio Player is OFF", 0);
        }

    }   //End of stopPlayingRadio() function



    /**
     * playRadio() function is used to actually play the radio station selected
     * by the user. It gets the radio information and IP address from a list of
     * radio station that are saved in a local text file. There are two list of
     * radio info and IP address that this function uses to connect to the radio
     * station and play it.
     *
     */
    private void playRadio(){
                                  //If radio player is not been played
        if(mdPlayer == null) {
            mdPlayer = new MediaPlayer();
            mdPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                                  //if radio station info is already in memory
                if(strRdStationPlayingIP != null) {
                    mdPlayer.setDataSource(strRdStationPlayingIP);
                }
                else{
                                  //Radio station info is new with another
                                  //radio station
                    strRdStationPlayingIP = listPresetsIP.get(0);
                    strRdStationPlayingInfo = listPresetsNames.get(0);
                    mdPlayer.setDataSource(strRdStationPlayingIP);
                                  //Setting TextView text with radio
                                  //station info
                    txtvwStationInfo.setText(strRdStationPlayingInfo);
                    txtvwStationIP.setText(strRdStationPlayingIP);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // mdPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                  //Getting radio player ready asynchronously
            mdPlayer.prepareAsync();
            mdPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                                  //Actually playing the radio station
                                  //selected
                    mdPlayer.start();
                }
            });
        }
                                  //Helping user with info about the radio
                                  //station state
        else{
            dbgFunc("MdPlayer is trying to connect - Or turn it off first", 0);
        }

    }   //End of playRadio() function


    /**************************************************************************
     *  END OF MEDIA PLAYER LOGISTIC CODE SECTION                             *
     **************************************************************************/



    /**************************************************************************
     *  LISTENER CLASSES CODE SECTION                                         *
     **************************************************************************/

    /**
     *  RadioButtonOptionsListener inner class is used to handle all the events
     *  that generated by the Buttons when users click on any of them. It has
     *  only one function, onClick(), that is overrode.
     */
    private class RadioButtonOptionsListeners implements View.OnClickListener {


        /**
         * onClick() function is used to handle all the events generated by user
         * when a Button is clicked. It uses a switch block of cases to change
         * the state of the radio player.  These states are OFF, play a radio
         * station, show preset of radio stations, and show the equalizer. It
         * just calls other functions to perform that task that is required by
         * the user.
         *
         * @param view type View
         */
        @Override
        public void onClick(View view){
                                  //switch block beginning
            switch (view.getId()){
                                  //Connect to a radio station and play it.
                case BTN_RADIOPLAY_ID:
                    stopPlayingRadio();
                    playRadio();
                    break;
                                  //Stopping or turning off the radio player
                case BTN_RADIOSTOP_ID:
                    stopPlayingRadio();
                    break;
                                  //Show or refresh the preset radio stations
                case BTN_RADIOPRESETS_ID:
                    getRadioButtonTextFromPresetFile(1);
                    setRadioButtonsText();
                    break;
                                  //Still working on this case
                case BTN_RADIOEQUALI_ID:
                    dbgFunc("Boton EQUALIZER", 0);
                                  //STIILL DEBUGGING THIS CASE
                    // writeRadioStationSelectedToFile();
                    dbgFunc("XXXXXXXXXXXXXXX", 0);
                    readRadioStationSelectedToFile();
                    break;
            }   //End of switch block

        }   //End of onClick() function


    }   //End of inner class  RadioButtonOptionsListeners





    /**
     *  RadioButtonPresetsListener inner class is used to handle all the events
     *  that generated by the RadioButtons when users click on any of them. It
     *  has only one function, onClick(), that is overrode.
     */
    private class RadioButtonPresetsListener implements View.OnClickListener {

        /**
         * onClick() function is used to handle all the events generated by user
         * when a RadioButton is clicked. It uses a switch block of cases to get
         * the radio station info that a user want to listen After that it sets
         * this radio station info into the TextViews, saved the radio station
         * info to a file, and start playing the radio station.
         *
         * @param view type View
         */
        @Override
        public void onClick(View view){
            int index;
                                  //Index of the radio station selected from
                                  //the preset
            index = listPresetsNames.indexOf(((RadioButton)view).getText().toString());
                                  //Just checking for out of bound error
            if(index < 0 || index > listPresetsIP.size()){
                dbgFunc("ERROR ERROR", 0);
                return;
            }

                                  //switch block beginning
            switch ((view).getId()){
                                  //Radio station one
                case RDBUTTON0_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;
                                  //Radio station two
                case RDBUTTON1_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;
                                  //Radio station three
                case RDBUTTON2_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;
                                  //Radio station four
                case RDBUTTON3_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;
                                  //Radio station five
                case RDBUTTON4_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;

                case RDBUTTON5_ID:
                    strRdStationPlayingIP = listPresetsIP.get(index);
                    strRdStationPlayingInfo = listPresetsNames.get(index);
                    break;
            }
                                  //Setting TextView text with radio station info
            txtvwStationInfo.setText(strRdStationPlayingInfo);
            txtvwStationIP.setText(strRdStationPlayingIP);
                                  //Saving radio station info to remember in next
                                  //run
            writeRadioStationSelectedToFile(strRdStationPlayingIP + ">>" + strRdStationPlayingInfo);
                                  //Playing radio station selected
            stopPlayingRadio();
            playRadio();

        }   //End of onClick() function

    }   //End of inner class RadioButtonPresetsListener



    /*********************************************************************************************
     *  END OF LISTENER CLASSES CODE SECTION                                                     *
     *********************************************************************************************/




    /**
     * This function is override to stop the MediaPLayer if is playing audio
     * when the back button of the device is clicked. Otherwise it keeps
     * playing the audio.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopPlayingRadio();

    }   //End of onDestroy() function



    /**
     * dbgFunc(String str) is used to do some debug tasks. These tasks may be
     * checking string values or number values.
     *
     * @param str type String
     * @param tiempo type int
     */
    private void dbgFunc(String str, int tiempo){
        if(tiempo <= 0) {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        }

    }   //End of dbgFunc() function



}   //End of NKRadio  class



/**************************END OF FILE NKRadio.java****************************/

