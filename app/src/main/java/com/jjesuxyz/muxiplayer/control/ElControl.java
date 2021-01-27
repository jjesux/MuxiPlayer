package com.jjesuxyz.muxiplayer.control;

import android.app.AlertDialog;
import android.database.Cursor;
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

import androidx.core.content.ContextCompat;

import com.jjesuxyz.muxiplayer.MainActivity;
import com.jjesuxyz.muxiplayer.R;
import com.jjesuxyz.muxiplayer.modelo.DBData.DBAccess;
import com.jjesuxyz.muxiplayer.modelo.DBData.DBAccessHelper;

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
                                  //Number of equalizer presets
    private short presetSelectedNumber = -1;
                                  //Names of equalizer presets
    private ArrayList<String> presetNamesList = null;
                                  //Song time information
    private int iSongCurrentTime = 0;
    private int iSongTotalTime = 0;
                                  //Vars to manage pause and loop states
    private boolean btnLoopState = false;
    private boolean bIsSongPaused = false;
                                  //Index of song being played
    private int iSongPlayingNumber = 0;
                                  //Variable used to show a UI with equalizer btns
    private AlertDialog alertDialog = null;





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
    private int getMdPlayerAudioSessionId(){
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
    public boolean isMdPlayerPlaying(){
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
    public int getSongTotalTime(){
                                  //Variable set when song starts playing
        return iSongTotalTime;

    }   //End of getSongTotalTime() function



    /**
     * The stopPlaying() function is used to stop playing the audio file being
     * played. It also nullify the objects being used to play the audio file;
     * media player, equalizer and the SeekBar synchronizing object.
     *
     */
    public void stopPlaying(){
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
    public void pausePlayingSong(){
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
    public void playNextSong(){
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
    public void playPreviousSong(){
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
    public void playSongFromList(int position){
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
    public void playSong(String songNamePar){
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
    public void setBtnLoopState(boolean btnLoopState){
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
            }
        }

    }   //End of function



    /**
     * setPresetSelectedNumber(short) function is used to set the value of the
     * variable holding the equalizer preset number that is been used at the
     * time this function is called.
     * In this project it is used to synchronize the value of this variable with
     * the variable with same name that lives in the MainActivity class. This
     * synchronizing the equalizer UI with the control.
     *
     * @param presetSelectedNumberPar
     */
    public void setPresetSelectedNumber(short presetSelectedNumberPar) {
        presetSelectedNumber = presetSelectedNumberPar;
    }



    /**
     * getPresetSelectedNumber() function is used to get or pass the value of
     * the presetSelectedNumber variable to the part of the code that calls this
     * function. At this moment it is not been used.
     *
     * @return short
     */
    public short getPresetSelectedNumber() {
        return presetSelectedNumber;
    }



    /**
     * getpresetNamesList() function is used to return o pass the list of the
     * equalizer preset names. The ArrayList of names is initialized in another
     * part of the program. It may also return null.
     *
     * @return ArrayList
     */
    public ArrayList getpresetNamesList() {
        return presetNamesList;
    }   //End of getpresetNamesList() function



    /**
     * equalizerUsePreset(short) function is used to set the preset that the
     * equalizer should activated. The preset name is passed to this function
     * as a short number.
     *
     * @param presetNumber
     */
    public void equalizerUsePreset(short presetNumber) {
        equalizer.usePreset(presetNumber);

    }   //End of equalizerUsePreset() function



    /**
     * The releaseEqualizer() function is used to deactivated and destroy the
     * equalizer object as well, and set it to null, so it can be recycled.
     * This function is used, as well, selectively to keep the equalizer preset
     * selected by the user and to set the main window equalizer button ON or
     * OFF.
     *
     * @param totalRelease type int
     */
    public void releaseEqualizer(int totalRelease){
                                  //Release and disabling equalizer
        if(equalizer != null) {
            equalizer.setEnabled(false);
            equalizer.release();
            equalizer = null;
        }

                                  //Nulling the ArrayList of preset names
        presetNamesList = null;
                                  //Code execute only when the dialog equalizer
                                  //presets cancel button is clicked. It turns
                                  //equalizer OFF
        if(totalRelease == 0) {
            presetSelectedNumber = -1;
            contextoMA.setBtnSoundEqualizerState(contextoMA.getResources().getString(R.string.btn_eq_is_off));
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
            String song_end = contextoMA.getResources().getString(R.string.ma_song_end) + " ";
            String secs = " " + contextoMA.getResources().getString(R.string.ma_secs);
            contextoMA.setTxtvwSongCurrentTime(song_end + contador + secs);
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
            String song_end = contextoMA.getResources().getString(R.string.ma_song_end) + " ";
            String secs = " " + contextoMA.getResources().getString(R.string.ma_secs);
            contextoMA.setTxtvwSongCurrentTime(song_end + contador + secs);
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
