package com.jjesuxyz.muxiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.jjesuxyz.muxiplayer.ui.main.SectionsPagerAdapter;


/**
 * MusLib class is used to create fragments. This activity will host those
 * fragments. This class will also create tabs that will be inserted and
 * displayed in the action bar app. This class will also synchronize swipe
 * action with tabs and fragments. It also sends data to those fragments.
 * It also sends data back to the parent activity when the user is done
 * interacting with the fragments it hosts.
 *
 * Created on 12/4/2020 11:11 pm.
 */
public class MusLib2 extends AppCompatActivity
                         implements ViewPager.OnPageChangeListener,
                                    ListaCompletaFrgmTab.ListaCompletaFrgmInterfaceListener,
                                    PlayListFrgmTab.PlayListInterfaceListener,
                                    SingerListFragTab.SingerInterfaceListener {

    /**
     * onCreate(Bundle) function is used to initialize ArrayList that will hold
     * data from the local database. It also defines the tabs that are going to
     * be inserted into the action bar. It defines these tabs before adding them
     * into the ActionBar widget.
     *
     * @param savedInstanceState type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mus_lib2_layout);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }   //End of onCreate() function



    /**
     * onPageScrolled(int, float, int) function is not implemented in this
     * project yet.
     *
     * @param position type int
     * @param positionOffset type float
     * @param positionOffsetPixels type int
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**********************************************************************/
    }   //End of onPageScrolled() function



    /**
     * onPageSelected(int) function is called when the user change/swap/swipe
     * fragments to be display when the user want to interact/see the next or
     * previous fragment in the list of fragments. It synchronize tabs and
     * fragments displays with each swipe action done by the user.
     *
     * @param position type int
     */
    @Override
    public void onPageSelected(int position) {
        //Log.d("NIKO", "onPageSelected(position: " + position);
        androidx.appcompat.app.ActionBar actionB = getSupportActionBar();
        if (actionB != null) {
                                  //Changing fragment to be displayed
            actionB.setSelectedNavigationItem(position);
        }

    }   //End of onPageSelected() function



    /**
     * onPageScrollStateChanged(int) function is NOT implemented in this project
     * yet.
     *
     * @param state type int
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        /**********************************************************************/
    }   //End of onPageScrollStateChanged() function



    /**
     * comunicacionInterfaceFunction() function used to receive data/info from
     * the fragments that this activity created and hosted. This function is
     * called by the fragments when the user is done interacting with them and
     * click a specific button. It is also used to pass data to the activity that
     * created this activity and to destroy itself calling the finish function.
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
     * onBackPressed() function is used to pass user data update info to the
     * MainActivity class.  It is called when the back key is pressed and the
     * app is in one of the fragments life period. Any data update done and
     * completed by the user, it is done also in the database unless the user
     * did not complete the transaction such as deleting or inserting data into
     * the database before pressing the back key.
     */
    @Override
    public void onBackPressed(){
                                  //Creating data to pass to MainActivity when
                                  //back key pressed
        Intent intent = new Intent();
                                  //true asks MainActivity ListView to update
                                  //itself from DB
        intent.putExtra(MainActivity.UPDATE_NEEDED, true);
        setResult(MainActivity.RESULT_OK, intent);

        super.onBackPressed();

    }   //End of onBackPressed() function



    /**
     * onDestroy() function is not implemented in this version of the app.
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }   //End of onDestroy() funtion



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

    }   //End of l() function


}   //End of Class MusLib



/**************************END OF FILE MusLib.java*****************************/
