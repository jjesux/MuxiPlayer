package com.jjesuxyz.muxiplayer;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjesuxyz.muxiplayer.control.ElControl;
import com.jjesuxyz.muxiplayer.modelo.ElModelo;

import java.io.File;
import java.util.ArrayList;


/**
 * MainActivity class is the entry point of this app. It is used to create the
 * main UI, ask for permission access storage, get data from the DB play list
 * table, set that data in the ListView. It also sets and synchronizes the
 * SeekBar widget with the song to be played. It also ask the class controller
 * to synchronize its ArrayList data from the db play list table.
 *
 *
 ** Created on 12/5/2020 12:41 pm.
 */
public class MainActivity extends ListActivity {

                                  //Context passed to other classes.
    private Context contexto = this;
                                  //Class to control MP3 file playing.
    private ElControl elControl = null;
    private MediaPlayer mdPlayer = null;
                                  //Set TextView to show info about song playing.
    private TextView txtvwPlayingSongName;
    private TextView txtvwSongCurrentTime;
    private TextView txtvwSongTotalTime;
                                  //Set of main window buttons and ID's variables.
    private Button btnPlay;
                                  //ID's are used in a switch block.
    private final int BTN_PLAY_ID = R.id.btnPlayID;
    private Button btnNext;
    private final int BTN_PLAYNEXT_ID = R.id.btnNextID;
    private Button btnPrevious;
    private final int BTN_PLAYPREVIOUS_ID = R.id.btnPreviousID;
    private Button btnStop;
    private final int BTN_STOPPLAY_ID = R.id.btnStopID;
    private Button btnPause;
    private final int BTN_PAUSEPLAY_ID = R.id.btnPauseID;
    private Button btnLoop;
    private final int BTN_LOOP_ID = R.id.btnLoopID;
    boolean bBtnLoopState = false;
                                  //Button to display music library.
    private Button btnMusLib;
    private final int BTN_MUS_LIB_ID = R.id.btnMusLibID;
                                  //Button to turn the radio on.
    private Button btnRadio;
    private final int BTN_RADIO_ID = R.id.btnRadioID;
                                  //Button to turn the equalizer on.
    private Button btnSoundEqualizer;
    private final int BTN_SOUNDEQUALIZER_ID = R.id.btnSoundEqualizerID;
                                  //Class to handle buttons events.
    private UIBotonListener uiBtnEventMngmt;
                                  //List of song in the device sdcard.
    private ArrayList<String> arrListPlaylistFromDB;
                                  //ListView to show list of song.
    private ListView lView;
                                  //ListView Adapter.
    MuxListViewAdapter muxListViewAdapter;
                                  //Represent the song being played progress.
    private SeekBar seekBar;
                                  //State of the pause button.
    private boolean bBtnPauseState = false;
                                  //Variable set to manage permission to access
                                  //device storage.
    private final int PERMISION_ACCESS_CODE = 3456;
    private boolean bPermissionsState = false;

    private final int RESULT_FROM_ACTIVITY = 20;

    public static final String UPDATE_NEEDED = "UPDATE_MUS_LIB";
    public static final String PLAYLIST_EMPTY = "PLAYLIST_IS_EMPTY";
                                  //Variable to get data from DB play list table.
    ElModelo elModelo;


                                  //This set of variables are used to created
                                  //AlertDialog to manage the equalizer UI.

                                  //List of equalizer preset names.
    private ArrayList<String> presetNamesList;
                                  //Variable holding the user preset selection.
    private short presetSelectedNumber = -1;

    AlertDialog alertDialog = null;
    AlertDialog.Builder alertDiaBuilder = null;

                                  //A variable reference to this class.
    MainActivity mainActivity;

                                  //Variable to hold the random playing mode
                                  //state, ON or OFF.
    private boolean bRandonPlay = false;
                                  //Variable used to change the loop button
                                  //text between LOOP and RAND.
    private int iSecCounter = 1;





    /**
     * onCreate(Bundle) function is used to check storage access, initialize
     * variables, get data from DB and display this data to the user. It also
     * set the ListView click listener class and the ListView adapter. It also
     * set the SeekBar widget change listener.
     *
     * @param savedInstanceState type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                                  //Just getting a reference to this class to be
                                  //used when 'this' reference is not valid.
        mainActivity = this;
                                  //Checking and asking permission to access
                                  //storage.
        checkStorageAccessPermission();

                                  //TextView variables initialization.
        txtvwPlayingSongName = findViewById(R.id.txtvwPlayingSongNameID);
        txtvwSongCurrentTime = findViewById(R.id.txtvwSongCurrentTimeID);
        txtvwSongTotalTime = findViewById( R.id.txtvwSongTotalTimeID);

                                  //Button variables, & listener initialization.
        setButtons();
                                  //Buttons Click event listener.
        uiBtnEventMngmt = new UIBotonListener();
        setButtonsClickListener();

                                  //ListView Setting.
        elModelo = new ElModelo(contexto);
        arrListPlaylistFromDB = elModelo.getArrayListFromPlayListTable();
        muxListViewAdapter = new MuxListViewAdapter(this, arrListPlaylistFromDB);
                                  //Music controller setting.
        elControl = new ElControl(MainActivity.this);
        elControl.setArrayListPlaylist(arrListPlaylistFromDB);

        lView = getListView();
                                  //Setting the ListView Adapter.
        lView.setAdapter(muxListViewAdapter);
                                  //ListView listener implementation.
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvw = view.findViewById(R.id.txtvwMP3FilePath);
                                  //Playing song by clicking one in the ListView
                                  //of songs.
                elControl.playSongFromList(position);
                btnPlay.setTextColor(ContextCompat.getColor(contexto, R.color.colorVerde));
                                  //Activating the pause button.
                btnPause.setEnabled(true);
                                  //Setting the boolean pause var to true.
                bBtnPauseState = true;
                                  //Managing the pause button color to let user
                                  //know if this button was activated.
                setBtnPauseColorState();
                                  //Checking if loop button should be enabled
                                  //and change text color to yellow.
                if (bBtnLoopState == false) {
                    btnLoop.setEnabled(true);
                    btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                }
            }
        });
                                  //End of ListView code.

                                  //Setting seek bar to show the progress of
                                  //song being played SeekBar var initialization.
        seekBar = findViewById(R.id.seekBID);
                                  //SeekBar listener implementation.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                  //if block to change the loop btn text between
                                  //LOOP to RAND every two seconds. SeekBar
                                  //widget progress is used for this.
                if(bRandonPlay == true) {
                    iSecCounter++;
                    if (iSecCounter >= 4) {
                        btnLoop.setText("RAND");
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde2));
                        iSecCounter = 0;
                    } else if (iSecCounter == 2) {
                        btnLoop.setText("LOOP");
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
                    }
                }


                if(fromUser) {
                    mdPlayer = elControl.getMdPlayer();
                                  //Checking if MP is playing a song.
                    if(mdPlayer != null && progress < elControl.getSongTotalTime()) {
                                  //Synchronizing MP and SeekBar progress.
                        mdPlayer.seekTo(progress * 1000);
                    }
                    else{         //Song ended or it is not being played.
                        seekBar.setProgress(0);
                    }
                }

            }
                                  //Function part of Listener, not implemented
                                  //in this app.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
                                  //Function part of Listener, not implemented
                                  //in this app.
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });                       //End of SeekBar implementation.

    }   //End of onCreate() function.




    /**
     * getMyListView() function is used just to return the main window ListView.
     *
     * @return lView type ListView
     */
    public ListView getMyListView(){
        return lView;

    }   //End of getMyListView() function.




    /**
     * setBtnSoundEqualizerState() function is used to change the text on the
     * equalizer button. This text is on or off depending if the equalizer is
     * on or off.
     *
     * @param stateONOFF type String
     */
    public void setBtnSoundEqualizerState(String stateONOFF){
        btnSoundEqualizer.setText(stateONOFF);

    }   //End of setBtnSoundEqualizerState() function.




    /**
     * setTxtvwPlayingSongName() function is used to set the text on the
     * TextView that contains the name of the song being played.
     *
     * @param actualPlayingSongName type String
     */
    public void setTxtvwPlayingSongName(String actualPlayingSongName){
        File file = new File(actualPlayingSongName);
                                  //Displaying song name being played.
        txtvwPlayingSongName.setText(file.getName());

    }   //End of setTxtvwPlayingSongName() function.




    /**
     * setTxtvwSongTotalTime() function is used to set the text on the
     * TextView that contains time that the song being played last.
     *
     * @param strTiempoTotal type String
     */
    public void setTxtvwSongTotalTime(String strTiempoTotal){
        txtvwSongTotalTime.setText(strTiempoTotal);

    }   //End of setTxtvwSongTotalTime() function.




    /**
     * setTxtvwSongCurrentTime() function is used to set the current progress
     * time of the song being played.
     *
     * @param strCurrentTime type String
     */
    public void setTxtvwSongCurrentTime(String strCurrentTime){
        txtvwSongCurrentTime.setText(strCurrentTime);

    }   //End of setTxtvwSongCurrentTime() function.




    /**
     * setSeekBarProgress() function is used to set the SeekBar progress
     * of the song being playing.
     *
     * @param progreso type int
     */
    public void setSeekBarProgress(int progreso){
        seekBar.setProgress(progreso);

    }   //End of setSeekBarProgress() function.




    /**
     * setSeekBarMax() function is used to set the maximum value that the
     * SeekBar will handle.
     *
     * @param songTiempoTotal type int
     */
    public void setSeekBarMax(int songTiempoTotal){
        seekBar.setMax(songTiempoTotal);

    }   //End of setSeekBarMax() function.




    /**
     * setPresetSelectedNumber(short) function is used to set the value of the
     * variable holding the equalizer preset number selected by the user.
     *
     * @param presetSelectedNumberPar
     */
    public void setPresetSelectedNumber(short presetSelectedNumberPar) {
        presetSelectedNumber = presetSelectedNumberPar;

    }   //End of setPresetSelectedNumber(short) function.





    /**
     * getPresetSelectedNumber() function is used to get the value of the
     * variable holding the equalizer preset number selected by the user.
     *
     * @return short
     */
    public short getPresetSelectedNumber() {
        return presetSelectedNumber;

    }   //End of getPresetSelectedNumber() function.




    /**
     * setBtnPlayTextColor(boolean) function is used to the play button text
     * color. This color is green when the app is playing a song, and it is
     * yellow when the app is not playing any song.
     *
     * @param bBtnOnOff
     */
    public void setBtnPlayTextColor(boolean bBtnOnOff) {
        if (bBtnOnOff == false) {
            btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLetras));
        }
        else {
            btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
        }

    }   //End of setBtnPlayTextColor(boolean) function




    /**
     * setButtons() function is used to get all the buttons that are in the main
     * UI window. All these buttons are defined in the main_activity layout file.
     *
     */
    private void setButtons(){
        btnPlay =   findViewById(R.id.btnPlayID);
        btnNext =   findViewById(R.id.btnNextID);
        btnPrevious = findViewById(R.id.btnPreviousID);
        btnStop =   findViewById(R.id.btnStopID);
        btnPause =  findViewById(R.id.btnPauseID);
        btnLoop =   findViewById(R.id.btnLoopID);
        btnMusLib = findViewById(R.id.btnMusLibID);
        btnRadio =  findViewById(R.id.btnRadioID);
        btnSoundEqualizer = findViewById(R.id.btnSoundEqualizerID);

    }   //End of setButtons() function




    /**
     * setButtonsClickListener function is used to set the event listener
     * class that will handle the events of all these buttons.
     *
     */

    private void setButtonsClickListener(){
        btnPlay.setOnClickListener(uiBtnEventMngmt);
        btnNext.setOnClickListener(uiBtnEventMngmt);
        btnPrevious.setOnClickListener(uiBtnEventMngmt);
        btnPause.setOnClickListener(uiBtnEventMngmt);
        btnStop.setOnClickListener(uiBtnEventMngmt);
        btnLoop.setOnClickListener(uiBtnEventMngmt);
        btnMusLib.setOnClickListener(uiBtnEventMngmt);
        btnRadio.setOnClickListener(uiBtnEventMngmt);
        btnSoundEqualizer.setOnClickListener(uiBtnEventMngmt);


                                  // Implementing long click on loop button to
                                  //activate random mode playing.
        btnLoop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                                  //Random is OFF and it is turned ON.
                if (bRandonPlay == false) {
                    bRandonPlay = true;
                                  //If loop mode is OFF, turn it ON.
                    if(bBtnLoopState == false) {
                        btnLoop.performClick();
                    }
                }
                                  //Random is ON and it is turned OFF.
                else {            //if bRandomPlay == true.
                    bRandonPlay = false;
                    btnLoop.setText("LOOP");
                    if (bBtnLoopState == true){
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
                    }
                }

                String str = Boolean.toString(bRandonPlay);
                Toast.makeText(getApplicationContext(), "RANDOM MODE IS " + str, Toast.LENGTH_LONG).show();
                                  //Setting random mode ON-OFF on ElControl
                                  //class.
                elControl.setbRandomPlay(bRandonPlay);

                return true;
            }
        });

    }   //End of setButtonsClickListener() function.




    /**
     *  performAStopBtnClick() function is used to perform a click event
     *  programmatically on the loop button. So far this function is mainly
     *  used by the ElControl class.
     */
    public void performAStopBtnClick(){
        btnStop.performClick();

    }   //End of performAStopBtnClick() function.




    /**
     * UIBotonListener is the only inner class that the main class contains. It
     * is used to handle all the buttons reaction to user clicks on these
     * buttons.  This class contains only one function onClick(View vw). This
     * function handles all the buttons reaction.
     */
    private class UIBotonListener implements View.OnClickListener{

        /**
         * onClick function used to handle all button click events. These
         * events are play audio, play previous or next audio, stop or pause
         * audio, set continuous mode, activate the system equalizer to improve
         * sound.  All these events are handle in the ElControl class.
         *
         * @param vw type View
         */
        @Override
        public void onClick(View vw) {

            switch (vw.getId()){
                                  //Right now start playing the song.
                case BTN_PLAY_ID:
                                  //Checking music playing list is not empty.
                    if (arrListPlaylistFromDB.size() >= 1) {
                        elControl.setiSongPlayingNumber(0);
                        elControl.playSong("BOTON");
                                  //Setting the play button text color to green.
                        btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));

                        txtvwPlayingSongName.setText(elControl.getCurrentPlayingSongName());
                        txtvwSongCurrentTime.setText("0");
                                  //Button pause is originally unable.
                        btnPause.setEnabled(true);
                        bBtnPauseState = true;
                                  //Managing the pause button color to let user
                                  //know if this button was activated.
                        setBtnPauseColorState();
                                  //Checking if the button is in the deactivated
                                  //state, if so change it.
                        if (bBtnLoopState == false) {
                            btnLoop.setEnabled(true);
                                  //Changing the loop button color to activated.
                            btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        }
                    }
                    break;
                                  //Playing next song right now.
                case BTN_PLAYNEXT_ID:
                                  //Checking music playing list is not empty.
                    if (arrListPlaylistFromDB.size() >= 1) {
                                  //Button pause is originally unable.
                        btnPause.setEnabled(true);
                        bBtnPauseState = true;
                                  //Managing the pause button color to let user
                                  //know if this button was activated.
                        setBtnPauseColorState();
                        elControl.playNextSong();
                                  //Setting the play button text color to green.
                        btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
                                  //Checking if the button is in the deactivated
                                  //state, if so change it.
                        if (bBtnLoopState == false) {
                            btnLoop.setEnabled(true);
                                  //Changing the loop button color to activated.
                            btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        }
                    }
                    break;
                                  //Playing previous song right now.
                case BTN_PLAYPREVIOUS_ID:
                                  //Checking music playing list is not empty.
                    if (arrListPlaylistFromDB.size() >= 1) {
                                  //Button pause is originally unable.
                        btnPause.setEnabled(true);
                                  //Setting boolean pause var to know it is on.
                        bBtnPauseState = true;
                                  //Managing the pause button color to let user
                                  //know if this button was activated.
                        setBtnPauseColorState();
                        elControl.playPreviousSong();
                                  //Setting the play button text color to green.
                        btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
                                  //Checking if the button is in the deactivated
                                  //state, if so change it.
                        if (bBtnLoopState == false) {
                            btnLoop.setEnabled(true);
                                  //Changing the loop button color to activated.
                            btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        }
                    }
                    break;
                                  //Pause song being played.
                case BTN_PAUSEPLAY_ID:
                                  //Checking music playing list is not empty.
                    if (arrListPlaylistFromDB.size() >= 1) {
                        elControl.pausePlayingSong();

                                  //Managing the pause button color to let user
                                  //know if this button was activated.
                        setBtnPauseColorState();
                    }
                    break;
                                  //Stop the song being played.
                case BTN_STOPPLAY_ID:
                                  //Checking music playing list is not empty.
                    if (arrListPlaylistFromDB.size() >= 1) {
                                  //Parameter zero means totally turn off the
                                  //equalizer.
                        elControl.stopPlaying(0);
                                  //Setting the play button text color to green.
                        btnPlay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLetras));
                                  //Disabling the pause button.
                        btnPause.setEnabled(false);
                                  //Setting the pause button to deactivated color.
                        btnPause.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBtnDesactivadoStyle));

                                  //Setting the loop button state to disabled.
                        btnLoop.setEnabled(false);
                        btnLoop.setText("LOOP");
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBtnDesactivadoStyle));
                                  //False means loop playing mode is OFF.
                        bBtnLoopState = false;

                    }

                    break;
                                  //This button set the player to loop playing
                                  //all the songs for ever or until the app is
                                  //stopped.
                case BTN_LOOP_ID:
                                  //When the loop button is OFF.
                    if (bBtnLoopState == false) {
                        elControl.setBtnLoopState(true);
                        bBtnLoopState = true;
                                  //Setting the loop button to activated.
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorVerde));
                    }
                                  //When the loop button is ON.
                    else {
                        elControl.setBtnLoopState(false);
                        bBtnLoopState = false;
                                  //Setting the loop button to deactivated.
                        btnLoop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLetras));

                                  //Turning OFF random song selection if it is ON.
                        if (bRandonPlay == true){
                                  //False means random song selection is OFF.
                            bRandonPlay = false;
                                  //Setting random mode OFF on ElControl class.
                            elControl.setbRandomPlay(bRandonPlay);
                            btnLoop.setText("LOOP");
                        }

                    }

                    break;
                                  //Start activity to manage play library list.
                case BTN_MUS_LIB_ID:
                    Intent intentoMusLib = new Intent(contexto, MusLib2.class);
                    startActivityForResult(intentoMusLib, RESULT_FROM_ACTIVITY);
                    break;
                                  //Activate sound equalizer.
                case BTN_SOUNDEQUALIZER_ID:
                    if(elControl.getMdPlayer() == null && !elControl.isMdPlayerPlaying()){
                                  //Asking user to start playing a song first.
                        Toast.makeText(getApplicationContext(), "Start playing a song first.",
                                                                          Toast.LENGTH_SHORT).show();
                    }
                    else {
                                  //Starting the sound equalizer activation
                                  //process. This is done in ElControl class.
                        elControl.setEqualizerState();
                                  //AL holding the equalizer preset names.
                        presetNamesList = new ArrayList<String>();
                                  //Getting all equalizer preset names from
                                  //ElControl.
                        presetNamesList.addAll(elControl.getpresetNamesList());
                                  //Getting and customizing the dialog window.
                        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
                                  //Inflating the layout file to customize UI.
                        View prompView = layoutInflater.inflate(R.layout.equali_dialog_layout, null);
                                  //Getting the AD UI button to control it.
                        final Button btnOK = prompView.findViewById(R.id.btnEquOKId);
                        final Button btnNegative = prompView.findViewById(R.id.btnEquCancelId);
                        final Button btnManual = prompView.findViewById(R.id.btnEquManualId);
                                  //Creating the AlertDialog builder.
                        alertDiaBuilder = new AlertDialog.Builder(mainActivity);
                                  //Setting which layout file to use for the AD.
                        alertDiaBuilder.setView(prompView);

                                  //Creating the set of radio buttons.
                        RadioGroup radioGroup = prompView.findViewById(R.id.radioGroupId);
                                  //Getting the number of radio buttons that the
                                  //equalizer needs.
                        int radioGroupSize = radioGroup.getChildCount();
                        for(int i = 0; i < radioGroupSize; i++) {
                            RadioButton radioBtnTmp = (RadioButton) radioGroup.getChildAt(i);
                                  //Set of RB that will be visible in the dialog
                                  //window.
                            if (i < presetNamesList.size()) {
                                radioBtnTmp.setText(presetNamesList.get(i).toString());
                            }
                            else {
                                  //Set of RB that will not be visible in dialog
                                  //window.
                                radioBtnTmp.setEnabled(false);
                                radioBtnTmp.setVisibility(View.GONE);
                            }
                        }

                                  //RadioGroup listener that is called when user
                                  //select a equalizer preset option.
                        radioGroup.setOnCheckedChangeListener(
                                new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton rdBtnTmp = group.findViewById(checkedId);
                                  //Getting preset number, setting equalizer to
                                  //that preset.
                                        presetSelectedNumber = (short) presetNamesList.indexOf(rdBtnTmp.getText().toString());
                                  //Synchronizing the value of presetSelectedNumber
                                  //with that variable with same name that lives in
                                  //ElControl class.
                                        elControl.setPresetSelectedNumber(presetSelectedNumber);
                                  //Activating the equalizer preset selected.
                                        elControl.equalizerUsePreset(presetSelectedNumber);
                                  //Setting the main window equalizer button text.
                                        btnSoundEqualizer.setText(R.string.btn_eq_is_on);
                                  //Enabling AlertDialog custom button and setting
                                  //the color text.
                                        btnOK.setEnabled(true);
                                        btnOK.setTextColor(ContextCompat.getColor(mainActivity, R.color.colorLetras));
                                    }
                                });

                                  //Dialog button listeners to handle click events
                                  //on these buttons.
                        alertDiaBuilder.setCancelable(false);
                                  //Creating the user interface, but showing it.
                        alertDialog = alertDiaBuilder.create();
                                  //Showing the AlertDialog UI.
                        alertDialog.show();

                                  //Checking if equalizer is ON and a preset has
                                  //has been selected.
                        if (presetSelectedNumber >= 0) {
                                  //Turning on the preset selected.
                            radioGroup.check(radioGroup.getChildAt(presetSelectedNumber).getId());
                        }

                                  //This set of buttons are not the buttons that
                                  //are part of the AlertDialog UI. These buttons
                                  //are part of the layout file used to customize
                                  //the AlertDialog UI.

                                  //Setting the OK button click listener.
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (presetSelectedNumber <= -1) {
                                  //Turning equalizer off. It is not used.
                                    elControl.releaseEqualizer(0);
                                }
                                  //Closing AD UI.
                                alertDialog.dismiss();
                            }
                        });

                                  //Setting the Cancel button click listener.
                        btnNegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                  //Turning equalizer off. It is not used.
                                elControl.releaseEqualizer(0);
                                  //Closing AD UI.
                                alertDialog.dismiss();
                                  //Setting MainActivity button text.
                                btnSoundEqualizer.setText(R.string.btn_eq_is_off);

                                presetSelectedNumber = -1;
                                  //Synchronizing the value of presetSelectedNumber
                                  //with that variable with same name that lives in
                                  //ElControl class.
                                elControl.setPresetSelectedNumber(presetSelectedNumber);
                            }
                        });

                                  //Setting the Manual button click listener.
                                  //This feature is not working on this version.
                        btnManual.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "This feature no implemented yet.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    break;
                                  //Activate radio feature.
                case BTN_RADIO_ID:
                    l("Starting radio intent");
                                  //Stopping mp3 playing and equalizer.
                    elControl.stopPlaying(0);
                    elControl.releaseEqualizer(0);
                                  //Starting the radio turn on activity.
                    Intent intento = new Intent(contexto, NKRadio.class);
                    intento.putExtra("NIKO", "Este es el NIKOLAZO");
                    startActivity(intento);
                    break;

            }   //End of switch().

            if (arrListPlaylistFromDB.size() <= 0) {
                Toast.makeText(getApplicationContext(), "List Song is empty.", Toast.LENGTH_SHORT).show();
            }
        }   //End of OnClick() function.

    }   //End of inner class UIBotonListener.




    /**
     * setBtnPauseColorState() function is used to change the button pause
     * color from yellow to red and from red to yellow. It is changed to color
     * red every time the user pauses the song being played. When the user play
     * a song by clicking the play, next, or back button the pause button color
     * is set to yellow.
     */
    public void setBtnPauseColorState() {
        if (bBtnPauseState == false) {
            btnPause.setTextColor(Color.RED);
            bBtnPauseState = true;
        }
        else {
            btnPause.setTextColor(Color.YELLOW);
            bBtnPauseState = false;
        }
    }   //End of setBtnPauseColorState() function




    /**
     * onActivityResult(int, int, Intent) callback function is used to manage
     * user updates on the play list data make on another user interface. It
     * synchronizes-shows new data to user and it asks the controlling class
     * to update and synchronize it data and functioning with the updated data.
     *
     * @param requestCode type int
     * @param resultCode type int
     * @param data type Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_FROM_ACTIVITY && resultCode == RESULT_OK) {
                                  //Getting info to know if user did any data
                                  //update.
            boolean myBool = data.getBooleanExtra(UPDATE_NEEDED, true);

                                  //Clearing ArrayList to get new updated data.
            if (arrListPlaylistFromDB != null) {
                arrListPlaylistFromDB.clear();
            }
                                  //Getting updated data from DB play list table.
            arrListPlaylistFromDB = elModelo.getArrayListFromPlayListTable();
                                  //if-making sure ArrayList is not null or empty.
            if (arrListPlaylistFromDB != null && arrListPlaylistFromDB.size() == 0) {
                                  //Synchronizing data functioning on controling
                                  //class.
                elControl.stopPlaying(1);
                elControl.getArrayListPlayList().clear();
                elControl.setiSongPlayingNumber(0);
                                  //Setting info about song that maybe playing now.
                txtvwPlayingSongName.setText(MainActivity.PLAYLIST_EMPTY);
                txtvwSongCurrentTime.setText("0");
                txtvwSongTotalTime.setText("0");
            }
                                  //Updating list adapter with updated data
                                  //from DB.
            muxListViewAdapter.getData().clear();
            muxListViewAdapter.getData().addAll(arrListPlaylistFromDB);
            muxListViewAdapter.createHashMap();
            muxListViewAdapter.notifyDataSetChanged();
                                  //Asking controlling class to update its play
                                  //list data from db
            elControl.setArrayListPlaylist(arrListPlaylistFromDB);
                                  //Adjusting song number when it is bigger than
                                  //ArrayList size.
            if (elControl.getiSongPlayingNumber() >= arrListPlaylistFromDB.size()) {
                elControl.setiSongPlayingNumber(arrListPlaylistFromDB.size() - 1);
            }

        }

    }   //End of onActivityResult() function.




    /**
     * onDestroy() function is called when the back button of the device is
     * clicked.  It stops the music if any is been playing, then destroy the
     * application.
     *
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
                                  //Stopping the music being playing by another
                                  //class.
        elControl.stopPlaying(0);
        elControl.releaseEqualizer(0);

    }   //End of onDestroy() function.



    /**
     * checkStorageAccessPermission() function is used to check if permission to
     * access storage has been granted already. It asks for that permission if
     * user has not granted that permission yet. It set a boolean variable to
     * let part of this app that storage access is Ok.
     */
    private void checkStorageAccessPermission(){

                                  //if to check if this SDK is Nougat version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                  //Checking if storage access has been granted.
            int permisoGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permisoGranted == PackageManager.PERMISSION_GRANTED) {
                                  //Permissions to access storage have been
                                  //granted already.
                bPermissionsState = true;
            }
            else {
                bPermissionsState = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                  //App does need to info user why storage access
                                  //is needed.
                    Toast.makeText(this, "Storage access is needed to search MP3 files.", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISION_ACCESS_CODE);
                }
                else {
                                  //App does not need to info user why storage
                                  //access is needed.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISION_ACCESS_CODE);
                }
            }
        }
        else {
                                  //Code section to handle storage access on
                                  //SDK less than N.
            l("Esta es una version de android mas vieja que NOUGAT");
        }

    }   //End of checkStorageAccessPermission() function.




    /**
     * onRequestPermissionsResult(int, String[], int[]) function is used to
     * manage the user decision of permission granting. bPermissionsState
     * variable is set tp true or false based on user decision. This variable
     * is used to allow this app to access main storage and SD Card storage.
     *
     * @param requestCode type int
     * @param permissions type String[]
     * @param grantResults type int[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
                                  //Permission granted case.
            case PERMISION_ACCESS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bPermissionsState = true;
                }
                break;
                                  //User denied permission to access storage.
            default:
                bPermissionsState = false;
                break;
        }

    }   //End of onRequestPermissionsResult() function.




    /**
     * onBackPressed() function is used when the user click the back button.
     *
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }   //End of onBackPressed() function.




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




    /**
     * showToast(String) function is used to show user that something has just
     * happened. So far it is used only by the ElControl class.
     *
     * @param str
     */
    public void showToast(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

    }   //End of showToast(String) function.




}   //End of MainActivity class.



/***********************END OF FILE MainActivity.java*************************/


/******************************************************/

