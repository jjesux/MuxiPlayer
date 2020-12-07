package com.jjesuxyz.muxiplayer.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jjesuxyz.muxiplayer.ListaCompletaFrgmTab;
import com.jjesuxyz.muxiplayer.PlayListFrgmTab;
import com.jjesuxyz.muxiplayer.R;
import com.jjesuxyz.muxiplayer.SingerListFragTab;




/**
 * This class and file was created by AS when a tabbed activity was created. Each
 * od these tab/fragment represent and action of this app. One fragment UI search
 * all the mp3 files in the device, another tab/fragment UI search al mp3 files
 * by searching all the artists in this device. Another tab/fragment UI get all
 * mp3 files that have been selected by the user to be played.
 * All these mp3 file paths have been inserted into a local app database.
 * All these selected mp3 files can be played by clicking in the play all button
 * or by clicking the device back button. This clicking action takes the app to
 * the main app UI.
 *
 *
 * A [FragmentPagerAdapter] that returns a fragment corresponding to one of the
 * sections/tabs/pages.
 *
 * Created on 12/5/2022 3:16 pm.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
                                  //Tabs names gotten from string file
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.play_list_name, R.string.full_list_name, R.string.singer_list_name};
    private final Context mContext;



    /**
     * Class constructor is used only to get the context app.
     *
     * @param context
     * @param fm
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }



    /**
     * getItem(int) function is used to return tha fragment corresponding to the
     * tab that has been selected.
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {

        Fragment frag;
        switch (position) {
                                  //Fragment to get from DB all mp3 file paths
                                  //the user has selected to be played.
            case 0:
                return frag = new PlayListFrgmTab();
                                  //Fragment to search mp3 files in the device.
                                  //All these file paths are inserted into local
                                  //database table and shown to the user.
            case 1:
                return frag = new ListaCompletaFrgmTab();
                                  //Fragment to search all mp3 files in the device,
                                  //but this search is based on the artist names.
                                  //All these mp3 file paths are inserted into
                                  // local database table and shown to the user.
            case 2:
                return frag = new SingerListFragTab();
        }
                                  //Just in case something goes wrong.
        return null;

    }   //End of getItem(int) function



    /**
     * getPageTitle(int) function is used to return the tab selected title name.
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }   //End of getPageTitle(int) function



    /**
     * getCount() function is used to return the total number of pages or
     * in this app fragments UI.
     * @return
     */
    @Override
    public int getCount() {
                                  // Show 3 total pages.
        return 3;
    }   //End of getCount() function


}   //End of SectionsPagerAdapter class



/*******************END OF FILE SectionsPagerAdapter.java**********************/
