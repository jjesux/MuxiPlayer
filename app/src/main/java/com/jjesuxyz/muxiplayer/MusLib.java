package com.jjesuxyz.muxiplayer;

import android.content.Intent;
import android.os.Bundle;
//import android.app.ActionBar;
//import android.app.ActionBar;

import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;




/**
 * MusLib class is used to create fragments. This activity will host those fragments. This
 * class will also create tabs that will be inserted and displayed in the action bar app. This
 * class will also synchronize swipe action with tabs and fragments. It also sends data to
 * those fragments. It also sends data back to the parent activity when the user is done
 * interacting with the fragments it hosts.
 *
 * Created by jjesu on 6/6/2018.
 */

public class MusLib extends   AppCompatActivity
                        implements ActionBar.TabListener,
                                    ViewPager.OnPageChangeListener,
                                    ListaCompletaFrgmTab.ListaCompletaFrgmInterfaceListener,
                                    PlayListFrgmTab.PlayListInterfaceListener,
                                    SingerListFragTab.SingerInterfaceListener{



                                        //ViewPage to achieve the swipe behavior of the fragments
    private ViewPager viewPager;
                                        //Swipe adapter for the view pager
    private SwipePageLibAdapter swipePageLibAdapter;
                                        //Set of variables to hold tabs references
    private ActionBar.Tab tab1;
    private final String PLAYLIST = "PLAY LIST";
    private ActionBar.Tab tab2;
    private final String ALL_MP3_FILES = "ALL MP3 FILES";
    private ActionBar.Tab tab3;
    private final String SINGER = "SINGERS";
                                        //ActionBar widget
    //private android.app.ActionBar actionBar;
    private androidx.appcompat.app.ActionBar actionBar;
                                        //Arrays holding MP3 file paths
    private ArrayList<String> arrayLIstFilePaths;




    /**
     * onCreate(Bundle) function is used to initialize ArrayList that will hold data from the
     * local database. It also defines the tabs that are going to be inserted into the action
     * bar. It defines these tabs before adding them into the ActionBar widget.
     *
     * @param savedInstanceState type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mus_lib_layout);

                                    //Instantiating ArrayList to hold database data
        arrayLIstFilePaths = new ArrayList<>();
        arrayLIstFilePaths.add("uno");
        arrayLIstFilePaths.add("dos");
        arrayLIstFilePaths.add("tres");
                                        //Getting the ActionBar widget to insert tabs
        actionBar = getSupportActionBar();//getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
                                        //Tab 1 to insert into ActionBar widget
        tab1 = actionBar.newTab();
        tab1.setText(PLAYLIST);
        tab1.setTabListener(this);
        actionBar.addTab(tab1);
                                        //Tab 2 to insert into ActionBar widget
        tab2 = actionBar.newTab();
        tab2.setText(ALL_MP3_FILES);
        tab2.setTabListener(this);
        actionBar.addTab(tab2);
                                        //Tab 3 to insert into ActionBar widget
        tab3 = actionBar.newTab();
        tab3.setText(SINGER);
        tab3.setTabListener(this);
        actionBar.addTab(tab3);
                                        //Instantiating the SwipePageAdapter
        swipePageLibAdapter = new SwipePageLibAdapter(getSupportFragmentManager());
                                        //Initializing ViewPager to hold fragments and swipe behavior
        viewPager = findViewById(R.id.pager);
                                        //Setting the SwipeAdapter
        viewPager.setAdapter(swipePageLibAdapter);
                                        //Setting the ViewPage swipe change listener
        viewPager.setOnPageChangeListener(this);

    }   //End of onCreate() function




    /**
     * onTabSelected(ActionBar.Tab, FragmentTransaction) function is used to change the fragment
     * to be display when the user select a different tab. It synchronize tabs and fragments when
     * the user selects a different fragment.
     *
     * @param tab type ActionBar.Tab
     * @param ft type FragmentTransaction
     */
    //////////////////////////////////@Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        if (viewPager != null) {
                                        //Changing fragment to be displayed
            viewPager.setCurrentItem(tab.getPosition());
        }

    }   //End of onTabSelected() function




    /**
     * onTabUnselected(ActionBar.Tab, android.app.FragmentTransaction function is not implemented
     * in this project yet.
     *
     * @param tab type ActionBar.Tab
     * @param ft type android.app.FragmentTransaction
     */
    ////////////////////////@Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }   //End of onTabUnselected() function




    /**
     * onTabReselected(ActionBar.Tab, FragmentTransition) function is not implemented in this
     * project yet.
     *
     * @param tab type ActionBar.Tab
     * @param ft type android.app.FragmentTransaction
     */
    ///////////////////////////////@Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
    }   //End of onTabReselected() function




    /**
     * onPageScrolled(int, float, int) function is not implemented in this project yet.
     *
     * @param position type int
     * @param positionOffset type float
     * @param positionOffsetPixels type int
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }   //End of onPageScrolled() function




    /**
     * onPageSelected(int) function is called when the user change/swap/swipe fragments to be
     * display when the user want to interact/see the next or previous fragment in the list of
     * fragments. It synchronize tabs and fragments displays with each swipe action done by the
     * user.
     *
     * @param position type int
     */
    @Override
    public void onPageSelected(int position) {
                                        //Log.d("NIKO", "onPageSelected(position: " + position);
        ///////////////////////////////////////ActionBar actionB = getActionBar();
        androidx.appcompat.app.ActionBar actionB = getSupportActionBar();
        if (actionB != null) {
                                        //Changing fragment to be displayed
            actionB.setSelectedNavigationItem(position);
        }

    }   //End of onPageSelected() function



    /**
     * onPageScrollStateChanged(int) function is NOT implemented in this project yet.
     *
     * @param state type int
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }   //End of onPageScrollStateChanged() function




    /**
     * comunicacionInterfaceFunction() function used to receive data/info from the fragments
     * that this activity created and hosted. This function is called by the fragments when
     * the user is done interacting with them and click a specific button.
     * It is also used to pass data to the activity that created this activity and to
     * destroy itself calling the finish function.
     *
     * @param blUpdate type boolean
     * @param resultCode type int
     */
    @Override
    public void comunicacionInterfaceFunction(boolean blUpdate, int resultCode) {
                                        //Data to be send to parent activity
        Intent intent = new Intent();
        intent.putExtra(MainActivity.UPDATE_NEEDED, blUpdate);
                                        //Sending dat/info to the parent activity
        setResult(MainActivity.RESULT_OK, intent);
                                        //Self destruction
        finish();

    }   //End of comunicacionInterfaceFunction() function




    /**
     * onBackPressed() function is used to pass user data update info to the MainActivity class.
     * It is called when the back key is pressed and the app is in one of the fragments life
     * period. Any data update done and completed by the user, it is done also in the database
     * unless the user did not complete the transaction such as deleting or inserting data into
     * the database before pressing the back key.
     */
    @Override
    public void onBackPressed(){
                                        //Creating data to pass to MainActivity when back key pressed
        Intent intent = new Intent();
                                        //true asks MainActivity ListView to update itself from DB
        intent.putExtra(MainActivity.UPDATE_NEEDED, true);
        setResult(MainActivity.RESULT_OK, intent);

        super.onBackPressed();

    }   //End of onBackPressed() function

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


    /**
     * SwipePageLibAdapter adapter class is used to create object of fragments that will be
     * display in this hosting class, and to pass data to them.  The member getItem function
     * will create and return those objects that will be synchronized with Tabs and
     * swipe behavior.
     */
    private class SwipePageLibAdapter extends FragmentPagerAdapter {


        /**
         * SwipePageLibAdapter(FragmentManager) constructor is used to pass FragmentManager
         * reference to its super class. It is required to instantiate an object of this class.
         *
         * @param fm type FragmentManager
         */
        public SwipePageLibAdapter(FragmentManager fm) {
            super(fm);

        }   //End of public class constructor




        /**
         * getItem(int) function is used instantiate and return objects of Fragments that will
         * be displayed in this histing class, and to pass data to them if they need that data.
         *
         * @param position type int
         * @return type Fragment
         */
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                                        //Data to be sent to fragments
                    Bundle playListArgs = new Bundle();
                    playListArgs.putStringArrayList("LISTA", arrayLIstFilePaths);
                                        //Creating fragments to be displayed
                    PlayListFrgmTab playListFrag = new PlayListFrgmTab();
                    playListFrag.setArguments(playListArgs);
                                        //Returning fragment to be displayed
                    return playListFrag;

                case 1:
                                        //Creating fragments to be displayed
                    ListaCompletaFrgmTab listaCompletaFrgmTab = new ListaCompletaFrgmTab();
                                        //Returning fragment to be displayed
                    return listaCompletaFrgmTab;
                case 2:
                                        //Creating fragments to be displayed
                    SingerListFragTab singerListFragTab = new SingerListFragTab();
                                        //Returning fragment to be displayed
                    return singerListFragTab;
                default:
                    return null;
            }

        }   //End of getItem() function




        /**
         * getCount() function is used to get the number of fragments that are going to be display
         * and inserted in the hosting activity.
         *
         * @return type int
         */
        @Override
        public int getCount() {
            return 3;

        }   //End of getCount() function


    }   //End of SwipePageLibAdapter private inner class




    /**
     * onDestroy() function is not implemented in this version of the app.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }   //End of onDestroy() funtion




    /**
     * The l(String) function is used only to debug this class. It uses the Log.d() function to pass
     * the information to the Android Monitor window.
     * This information contains the class name and some information about the error or data
     * about the debugging process.
     *
     * @param str type String
     */
    private void l(String str){
        Log.d("NIKO", this.getClass().getSimpleName() + " -> " + str);

    }   //End of l() function




}   //End of Class MusLib



/***************************************END OF FILE MusLib.java************************************/

