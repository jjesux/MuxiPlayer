package com.jjesuxyz.muxiplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;



import com.jjesuxyz.muxiplayer.DBData.DBAccess;
import com.jjesuxyz.muxiplayer.DBData.DBAccessHelper;

import java.io.IOException;
import java.util.ArrayList;





/**
 * ElControl class is used to manage the process to play a list of songs, MP3
 * files, and to manage the setting and use and synchronization of the sound
 * equalizer. This class contains a link to the MainActivity class to modify
 * and synchronize info, shown to the user, with the song being played by this
 * class.
 *
 * Created on 12/5/2020 12:43 pm.
 */

public class ElControl implements AudioManager.OnAudioFocusChangeListener{

                                  //Global variable for debugging
    public final String TAG = "NICKY";

                                  //Pointer to the main class of this project
    private MainActivity contextoMA;
                                  //ArrayLIst to hold the list of songs to be
                                  //played
    private ArrayList<String> arrayListPlayList;
    private MediaPlayer mdPlayer;
                                  //Inner class to synchronize SeekBar with
                                  //song being played
    private AsyThrMngSeekBar asyThrMngSeekBar = null;
                                  //Set of variables used with the equalizer
    private Equalizer equalizer;
    private int numberBands;
    private short numberPresets;
    private short presetSelectedNumber = -1;
    private ArrayList<String> presetNamesList;
                                  //Song time information
    private int iSongCurrentTime = 0;
    private int iSongTotalTime = 0;
                                  //Vars to manage pause and loop states
    private boolean btnLoopState = false;
    private boolean bIsSongPaused = false;
                                  //Index sof song being played
    private int iSongPlayingNumber = 0;





    /**
     * Constructor of ElControl class. This is the only constructor that this
     * class uses. The parameter it receives is a reference to the main class of
     * this project. It instantiates an object of the class that get the list of
     * songs in the device.
     *
     * @param contexto type MainActivity
     */
    public ElControl(MainActivity contexto){
        this.contextoMA = contexto;
        //initDB();
        arrayListPlayList = new ArrayList<>();

    }   //End of class constructor



    /**
     * setiSongPlayingNumber(int) function is used to set the variable holding
     * the number/index of the song being played. This number is the index in
     * the ArrayList holding the list of mp3 files to be played
     *
     * @param iSongPlayingNumber type int
     */
    public void setiSongPlayingNumber(int iSongPlayingNumber){
        this.iSongPlayingNumber = iSongPlayingNumber;

    }   //End of setiSongPlayingNumber() function



    /**
     * getiSongPlayingNumber() function is used to returns the ArrayList index
     * of the song being played. It is mostly used in the MainActivity class
     * ListView.
     *
     * @return type int
     */
    public int getiSongPlayingNumber(){
        return iSongPlayingNumber;

    }   //End of getiSongPlayingNumber() function




    /**************************************************************************
     * GETTING LIST OF AUDIO FILES -MP3- CODE SECTION                         *
     **************************************************************************/

    /**
     * setArrListFiles() function is used to set the whole list of audio files,
     * mp3's, that saved in the device sdcard. It calls a function defined in
     * another class that hold all the code to search the device sdcard. This
     * class is called ElModelo.
     *
     */
    public void setArrayListPlaylist(){

        DBAccess dbAccess =  new DBAccess(contextoMA);
        Cursor cursor = dbAccess.getAllMP3FilePathsFromDBPlaylistTable();

        int index = cursor.getColumnIndex(DBAccessHelper.TABLE_PLAY_LIST);
        while (cursor.moveToNext()) {
            String str = cursor.getString(0);
                                        //l("PLay list file name: " + str);
            arrayListPlayList.add(str);
        }
        dbAccess.close();

    }   //End of setArrListFiles() function



    /**
     * The setArrayListPlaylist(ArrayList) function is used to initialized the
     * local ArrayList that holds all the mp3 file paths that are in the main
     * ListView of the app graphical interface to interact with users. These
     * files maybe played by the MediaPlayer.
     *
     * @param arrayListPlaylistParam type ArrayList
     */
    public void setArrayListPlaylist(ArrayList<String> arrayListPlaylistParam){
        if (arrayListPlayList != null) {
            arrayListPlayList.clear();
            arrayListPlayList = arrayListPlaylistParam;
        }

    }   //End of setArrListFiles() function




    /**
     * The getArrListFiles() function is used by other function in this class
     * or in other classes to get the list of audio files, mp3's, that are
     * saved in the device sdcard.
     *
     * @return type ArrayList<String>
     */
    public ArrayList<String> getArrayListPlayList(){
        if(arrayListPlayList != null) {
            return arrayListPlayList;
        }
        else{
            return null;
        }

    }   //End of getArrListFiles() function




    /**
     * getCurrentPlayingSongName() function is used only to return the name of
     * the song being played by the MediaPlayer object.
     *
     * @return type String
     */
    public String getCurrentPlayingSongName(){
        //l("control->songplayingnumber:  " + iSongPlayingNumber);
        if (arrayListPlayList.size() >= 1) {
            //l("control->songplayingnumber:  " + iSongPlayingNumber);
            String currentPlayingSongName = arrayListPlayList.get(iSongPlayingNumber);
            return currentPlayingSongName;
        }
        else {
            return "";
        }

    }   //End of getCurrentPlayingSongName() function



    /**************************************************************************
     * END OF GETTING LIST OF AUDIO FILES -MP3- CODE SECTION                  *
     **************************************************************************/





    /**************************************************************************
     * PLAYING SONGS LOGISTIC CODE SECTION                                    *
     *************************************************************************/




    /**
     * The getMdPlayer() function is used just to return a reference to the
     * media player object.
     *
     * @return type MediaPlayer
     */
    public MediaPlayer getMdPlayer(){
        return mdPlayer;

    }   //End of getMdPlayer() function




    /**
     * The getMdPlayerAudioSessionId() function is used to return the audio
     * session id of the media player playing an audio file.  If media player
     * is not playing any audio file, the function returns -1.
     *
     * @return type int
     */
    public int getMdPlayerAudioSessionId(){
        if(mdPlayer != null) {
            return mdPlayer.getAudioSessionId();
        }
        else {                    //if mdPlayer == null
            return -1;
        }

    }   //End of getMdPlayerAudioSessionId() function




    /**
     * The isMdPlayerPlaying() function is used to inquire if the media player
     * is playing any audio file. It returns true if it is, false otherwise.
     *
     * @return type boolean
     */
    boolean isMdPlayerPlaying(){
                                  //True song is being played
        if(mdPlayer != null) {
            return mdPlayer.isPlaying();
        }
        else {
            return false;
        }

    }   //End of isMediaPlayerPlaying



    /**
     * The getSongTotalTime() function is used to get the total time that the
     * audio file being played last. This time is got it from the audio file
     * in another function.
     *
     * @return type int
     */
    int getSongTotalTime(){
                                  //Variable set when song starts playing
        return iSongTotalTime;

    }   //End of getSongTotalTime() function




    /**
     * The stopPlaying() function is used to stop playing the audio file being
     * played. It also nullify the objects being used to play the audio file;
     * media player, equalizer and the SeekBar synchronizing object.
     *
     */
    void stopPlaying(){
        bIsSongPaused = false;
                                  //SeekBar object
        if(asyThrMngSeekBar != null){
            asyThrMngSeekBar.cancel(true);
        }
                                  //MediaPLayer object
        if(mdPlayer != null) {
            mdPlayer.stop();
            mdPlayer.release();
            mdPlayer = null;
        }
                                  //Equalizer object
        releaseEqualizer(1);

    }   //End of stopPlaying() function




    /**
     * The pausePlayingSong() function is used to pause the audio file being
     * played. It also restarts playing the song if user click the paused button
     * again.
     *
     */
    void pausePlayingSong(){
        if(mdPlayer != null){
                                  //Pausing song
            if(mdPlayer.isPlaying()){
                bIsSongPaused = true;
                mdPlayer.pause();
            }
            else{
                                  //Restarting song
                bIsSongPaused = false;
                mdPlayer.start();
            }
        }

    }   //End of pausePlayingSong() function



    /**
     * The playNextSong() function is used to play the next audio file in the
     * list of mp3 files that are in the device sdcard. It checks when the
     * list get to the end of the list of files.
     *
     */
    void playNextSong(){
                                  //Making sure song number is not beyond ArrayList range
        if((iSongPlayingNumber + 1) < arrayListPlayList.size()){
            iSongPlayingNumber = iSongPlayingNumber + 1;
        }
        else{
            iSongPlayingNumber = 0;
        }
        String songName = arrayListPlayList.get(iSongPlayingNumber);
                                  //Actually playing the audio file
        playMySong(songName);

    }   //End of playNextSong() function



    /**
     * The playPreviousSong() function is used to play the previous audio file
     * in the list of mp3 files that are in the device sdcard. It Checks when
     * the list get to the beginning of the list of mp3 files that are in the
     * device sdcard.
     *
     */
    void playPreviousSong(){
                                  //Making sure song number is not negative
        if((iSongPlayingNumber - 1) >= 0){
            iSongPlayingNumber = iSongPlayingNumber - 1;
        }
        else{
            iSongPlayingNumber = arrayListPlayList.size() - 1;
        }
        String songName = arrayListPlayList.get(iSongPlayingNumber);
                                  //Actually playing the audio file
        playMySong(songName);

    }   //End of playPreviousSong() function



    /**
     * playSongFromList() function is used to play an audio file when user click
     * an mp3 file from the list of mp3 files that are in the device sdcard.
     *
     * @param position type int
     */
    void playSongFromList(int position){
                                  //Making sure song number is within ArrayList size range
        if(position >= 0 && position <= arrayListPlayList.size()){
            iSongPlayingNumber = position;
        }
         String songName = arrayListPlayList.get(iSongPlayingNumber);
                                  //Actually playing the the audio file
        playMySong(songName);

    }   //End of playSongFromList() function



    /**
     * The playSong() function is used to play an audio file. This function is
     * called only from the class that creates the main window, when the play
     * button is clicked. This function calls another function to actually play
     * the mp3 file. The parameter is not used in this function.
     *
     * @param songNamePar type String
     */
    void playSong(String songNamePar){
                                  //Making sure song number is within ArrayList size range
        if(iSongPlayingNumber >= 0 && iSongPlayingNumber < arrayListPlayList.size()){
            String songName = arrayListPlayList.get(iSongPlayingNumber);
                                  //Call function to Actually playing the song
            playMySong(songName);
        }

    }   //End of playSong() function



    /**
     * The playMySong() function is used to actually play an audio file, mp3 in
     * this case.  It uses the MediaPlayer library to decode the file and produce
     * the sound.  It creates a new MediaPlayer object and sets everything that
     * need to be initialized.  It also gets the information that other features
     * of the application need to let know users about the progress of the song
     * being played. These features are like TextView holding information about
     * the song times, The SeekBar progress and synchronization, the equalizer
     * settings. It also implements the function that is called when the audio
     * file ends.
     *
     * @param songName type String
     */
    private void playMySong(String songName){
                                  //Making sure MediaPLayer variable is null
        stopPlaying();
                                  //MP object created
        mdPlayer = new MediaPlayer();
                                  //Listen when song ends playing
        mdPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                                  //Making sure that song being played is stopped
                stopPlaying();
                iSongCurrentTime = 0;
                iSongTotalTime = 0;
                                  //Checking the end of the list of audio files
                if (btnLoopState) {
                    if ((iSongPlayingNumber + 1) < arrayListPlayList.size()) {
                        iSongPlayingNumber = iSongPlayingNumber + 1;
                    } else {
                        iSongPlayingNumber = 0;
                    }
                                  //Playing next if looping is on
                    playMySong(arrayListPlayList.get(iSongPlayingNumber));
                }
                else {
                                  //No more playing
                    mdPlayer = null;
                }
            }
        });

                                  //Setting MP to actually play song
        try{
            bIsSongPaused = false;
            mdPlayer.setDataSource(songName);
            mdPlayer.prepare();
            mdPlayer.start();
                                  //Getting and setting song info for users on SeekBar
            iSongCurrentTime = mdPlayer.getCurrentPosition();
            iSongTotalTime = mdPlayer.getDuration() / 1000;
            contextoMA.setSeekBarMax(iSongTotalTime);
            contextoMA.setTxtvwSongTotalTime(String.valueOf(iSongTotalTime));
            contextoMA.setTxtvwPlayingSongName(songName);
                                  //Synchronizing SeekBar
            asyThrMngSeekBar = null;
            asyThrMngSeekBar = new AsyThrMngSeekBar();
            asyThrMngSeekBar.execute();
                                  //Scrolling ListView display to show song name being played
            contextoMA.getMyListView().smoothScrollToPositionFromTop(iSongPlayingNumber, 0, 1000);
                                  //Activating the equalizer if button is clicked
            if(presetSelectedNumber >= 0){
                                  //no modify preset y btnEqualizer en MainAct
                releaseEqualizer(1);
                equalizer = new Equalizer(0, mdPlayer.getAudioSessionId());
                equalizer.setEnabled(true);
                equalizer.usePreset(presetSelectedNumber);
            }
        }
                                  //Catching error
        catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e){
            e.printStackTrace();
        }

    }   //End of playMySong() function



    /**
     * The setBtnLoopState() function is used to set/change the state of the loop
     * button in the main window. This state is just on/true and off/false.
     *
     * @param btnLoopState type boolean
     */
    void setBtnLoopState(boolean btnLoopState){
        this.btnLoopState = btnLoopState;

    }   //End of setBtnLoopState() function



    /**************************************************************************
     * END OF PLAYING SONGS LOGISTIC CODE SECTION                             *
     **************************************************************************/





    /**************************************************************************
     * EQUALIZER CODE SECTION                                                 *
     **************************************************************************/



    /**
     * The setEqualizerState() function is used just call another function named
     * setSoundEqualizerServiceON(). This is because in another older version of
     * the app was used to do some more tasks. In this new version those tasks
     * were removed or moved to another place of the application.
     *
     */
    public void setEqualizerState(){
        setSoundEqualizerServiceON();

    }   //End of function



    /**
     * The setSoundEqualizerServiceON() function is used to do all the equalizer
     * activation process. This process start with creating new object, enable it,
     * get the list of the build in presets, and calling another function to show
     * the dialog window that will show the preset and options to the user.
     *
     */
    private void setSoundEqualizerServiceON(){
        if(mdPlayer == null && !isMdPlayerPlaying()){
                                  //Ask user to start playing music before starting
                                  //equalizer
            dbgFunc("Start playing song first.");
        }
        else{                     //AVISO: checar si la session audio id es zero or
                                  //menor que zero
                                  //creating new equalizer and getting information
            if(arrayListPlayList.size() > 0 && arrayListPlayList.get(0).equals(MainActivity.PLAYLIST_EMPTY)){
                dbgFunc("Playlist is empty. Select songs first.");
            }
            else {
                equalizer = new Equalizer(0, getMdPlayerAudioSessionId());
                equalizer.setEnabled(true);
                numberBands = equalizer.getNumberOfBands();
                numberPresets = equalizer.getNumberOfPresets();
                                  //Getting list of the build in equalizer
                                  //presets
                presetNamesList = new ArrayList<String>();
                for (short i = 0; i < numberPresets; i++) {
                    presetNamesList.add(equalizer.getPresetName(i));
                }
                                  //Calling a function to show user the preset
                                  //option
                showSoundPresetsDialog();
            }
        }

    }   //End of function



    /**
     * The showSoundPresetsDialog() function is used to show the user the build
     * in equalizer list of presets in a dialog window. This function also
     * completes the equalizer activation process, gets user preset, and sets the
     * equalizer to that preset. This function also sets the RadioGroup listener
     * as well as the OK, Cancel, and Manual button listeners to handle user
     * decision events.
     *
     */
    private void showSoundPresetsDialog(){
                                  //Getting and customizing the dialog window
        LayoutInflater layoutInflater = LayoutInflater.from(contextoMA);
        View promptView = layoutInflater.inflate(R.layout.equali_dialog_layout, null);

        AlertDialog.Builder alertDiaBuil = new AlertDialog.Builder(contextoMA);
        alertDiaBuil.setView(promptView);
                                  //Getting the list of Radio Buttons that are
                                  //in the dialog window with the equalizer
                                  //preset options
        ArrayList<RadioButton> radioButtonsArr = new ArrayList<RadioButton>();
        RadioGroup radioGroup = promptView.findViewById(R.id.radioGroupId);
        int radioGroupSize = radioGroup.getChildCount();
        for(int i = 0; i < radioGroupSize; i++){
            RadioButton radioBtnTmp = (RadioButton) radioGroup.getChildAt(i);
                                  //Set of RB that will be visible in the dialog
                                  //window
            if(i < presetNamesList.size()) {
                radioBtnTmp.setText(presetNamesList.get(i).toString());
            }
            else{
                                  //Set of RB that will not be visible in dialog
                                  //window
                radioBtnTmp.setEnabled(false);
                radioBtnTmp.setVisibility(View.GONE);

            }
        }
                                  //RadioGroup listener that is called when user
                                  //select a equalizer preset option
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rdBtnTmp = group.findViewById(checkedId);
                dbgFunc(rdBtnTmp.getText().toString());
                                  //Getting preset number, setting equalizer to
                                  //that preset
                presetSelectedNumber = (short)presetNamesList.indexOf(rdBtnTmp.getText().toString());
                equalizer.usePreset((short)presetNamesList.indexOf(rdBtnTmp.getText().toString()));
                                  //Setting the main window equalizer button ON
                contextoMA.setBtnSoundEqualizerState("ON");
            }
        });
                                  //Dialog button listeners to handle click events
                                  //on these buttons.
        alertDiaBuil.setCancelable(false)
                                  //OK Btn used mainly to remove dialog window.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbgFunc("Btn OK");
                                  //If user do not select any option equalizer
                                  //remains OFF
                        if(presetSelectedNumber < 0){
                            releaseEqualizer(0);
                        }
                    }
                })
                                  //Cancel btn used to release equalizer object
                                  //and activation process.
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbgFunc("Cancelando: Doing nothing");
                                  //setting equalizer object to null, preset
                                  //number to -1
                                  //main window equalizer button to OFF
                        releaseEqualizer(0);
                    }
                })
                                  //Right now it is not implemented
                .setNeutralButton("Manual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbgFunc("Manual Equalization");
                    }
                });               //.show(); con this show no se cannot be
                                  //customize the dialog buttons creating
                                  //the equalizer preset dialog window

        AlertDialog alertDialog = alertDiaBuil.create();
                                  //customizeDialogButtons(alertDialog); si se pone
                                  //here no function, return a button null
        alertDialog.show();
                                  //Setting dialog buttons background to blue
        customizeDialogButtons(alertDialog);

    }   // End of function showSoundPresetsDialog()



    /**
     * The customizeDialogButtons(AlertDialog aD), is used to customize the
     * equalizer preset dialog buttons. Cancel, Manual and OK buttons. So far
     * it just sets the buttons background color to blue.
     *
     * @param aD type AlertDialog
     */
    private void customizeDialogButtons(AlertDialog aD){
        Button btnCancelTmp = aD.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnCancelTmp.setBackgroundColor(Color.BLACK);
        btnCancelTmp.setTextColor(Color.BLUE);
        btnCancelTmp.setTextSize(18.0f);
        Button btnPositivelTmp = aD.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositivelTmp.setBackgroundColor(Color.BLACK);
        btnPositivelTmp.setTextColor(Color.BLUE);
        btnPositivelTmp.setTextSize(18.0f);
        Button btnNeutralTmp = aD.getButton(AlertDialog.BUTTON_NEUTRAL);
        btnNeutralTmp.setBackgroundColor(Color.BLACK);
        btnNeutralTmp.setTextColor(Color.BLUE);
        btnNeutralTmp.setTextSize(18.0f);

    }   //End of function customizeDialogButtons()




    /**
     * The releaseEqualizer() function is used to deactivated and destroy the
     * equalizer object as well, and set it to null, so it can be recycled.
     * This function is used, as well, selectively to keep the equalizer preset
     * selected by the user and to set the main window equalizer button ON or
     * OFF.
     *
     * @param totalRelease type int
     */
    void releaseEqualizer(int totalRelease){
                                  //Release and disabling equalizer
        if(equalizer != null) {
            equalizer.setEnabled(false);
            equalizer.release();
            equalizer = null;
        }
                                  //Code execute only when the dialog equalizer
                                  //presets cancel button is clicked. It turns
                                  //equalizer OFF
        if(totalRelease == 0) {
            presetSelectedNumber = -1;
            contextoMA.setBtnSoundEqualizerState("OFF");
            dbgFunc("Equalizer is OFF");
        }

    }   //End of releaseEqualizer() function



    /**************************************************************************
     * END OF EQUALIZER CODE SECTION                                          *
     **************************************************************************/





    /**
     * The onAudioFocusChange() this function is not implemented in this version
     * of the project.
     *
     * @param focusChange type int
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
    }   //End of onAudioFocusChange() function



    /**************************************************************************
     * SeeKBar SYNCHRONIZATION CODE SECTION                                   *
     **************************************************************************/

    /**
     * The Class AsyThrMngSeekBar extends AsyncTask. This is an inner class.
     * This class is used to handle and synchronize the SeekBar widget
     * representing the time and progress of the song being played.
     */
    private class AsyThrMngSeekBar extends AsyncTask<Void, Integer, String> {
        Integer[] counter = {0};


        /**
         * doInBackground() function is used to actually get the progress of the
         * song being played. This information is got it in this background task.
         *
         * @param params type Void
         * @return type String
         */
        @Override
        protected String doInBackground(Void... params) {
                                  //Loop to return the progress-time of the song
                                  //being played. It is used in the SeekBar
                                  //animation
            while(mdPlayer.isPlaying() || bIsSongPaused){
                if(isCancelled()){
                    break;
                }
                                  //Sleeping this background task 1 second if the
                                  //pause button is clicked until clicked againg
                if(bIsSongPaused){
                    SystemClock.sleep(1000);
                    continue;     //Staying in this paused state
                }
                                  //counting the seconds the song has been played
                counter[0] = mdPlayer.getCurrentPosition() / 1000;
                                  //Publishing results for whoever may need them
                publishProgress(counter[0]);
                                  //Sleeping one second this task
                SystemClock.sleep(1000);
                if(mdPlayer == null){
                    break;
                }
            }
                                  //The final result, when the audio file ends
            return (counter[0]++).toString();

        }   //End of doInBackground() function



        /**
         * The onProgressUpdate() function is actually changing the song being
         * played information, so the can see the progress on a TextView and in
         * a SeekBar widgets
         *
         * @param contador type Integer
         */
        @Override
        protected void onProgressUpdate(Integer... contador){
            contextoMA.setTxtvwSongCurrentTime(contador[0].toString());
            contextoMA.setSeekBarProgress(contador[0]);

        }   //End of function



        /**
         * No implemented yet
         */
        @Override
        protected void onPreExecute(){}



        /**
         * The onPostExecute() function is used to publish the final information
         * of the song being played, when the song ends.
         *
         * @param contador type String
         */
        @Override
        protected void onPostExecute(String contador){
            contextoMA.setTxtvwSongCurrentTime("Song Ended at: " + contador + " secs");
            contextoMA.setSeekBarProgress(0);

        }   //End of function



        /**
         * The onCancelled function is used to publish information of the song
         * being played if the song being played is stopped before it ends.
         *
         * @param contador type String
         */
        @Override
        protected void onCancelled(String contador){
            contextoMA.setTxtvwSongCurrentTime("Song Ended at: " + contador + " secs");
            contextoMA.setSeekBarProgress(0);

        }

    }   //End of the class AsyThrMngSeekBar


    /**************************************************************************
     * END OF SEEK BAR SYNCHRONIZATION CODE SECTION                           *
     **************************************************************************/




    /**
     * The dbgFunc(String str) is used to do some debug tasks. These tasks may be
     * checking string values or number values.
     *
     * @param str type String
     */
    private void dbgFunc(String str){
        Toast.makeText(contextoMA, str, Toast.LENGTH_SHORT).show();

    }   //End of function



    /**
     * The l(String) function is used only to debug this class. It uses the
     * Log.d() function to pass the information to the Android Monitor window.
     * This information contains the class name and some information about the
     * error or data about the debugging process.
     *
     * @param str type String
     */
    private void l(String str){
        Log.d(TAG, this.getClass().getSimpleName() + " -> " + str);
    }

}   //End of Class ElControl



/*************************END OF FILE ElControl.java***************************/
