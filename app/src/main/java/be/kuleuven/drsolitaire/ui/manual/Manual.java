/*
 * Copyright (C) 2016  Tobias Bielefeld
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you want to contact me, send me an e-mail at tobias.bielefeld@gmail.com
 */

package be.kuleuven.drsolitaire.ui.manual;

import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
import androidx.core.view.GravityCompat;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;


import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.classes.CustomAppCompatActivity;

import static be.kuleuven.drsolitaire.SharedData.GAME;

/**
 * Manual Activity: Uses some fragments to show the manual pages.
 * <p>
 * Phones use a navigation drawer, and tablets (devices with xlarge displays) uses an another layout
 * with a listView instead the drawer. Therefore i have to distinguish between drawer and listView
 * for the actions.
 * <p>
 * Also i disabled recreation on orientation change, so i don't have to deal with scrolling back to old
 * position, load old fragment and so on
 */

public class Manual extends CustomAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    DrawerLayout drawer;
    ListView listView;
    View lastSelectedView;
    int lastSelectedViewPosition;
    boolean fragmentLoaded;
    NavigationView navigationView;

    boolean gamePageShown = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.manual_listView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentLoaded = false;


        loadFragment(ManualStartPage.class);


        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_startpage);
        } else if (listView != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            listView.setOnItemClickListener(this);

            listView.post(new Runnable() {
                @Override
                public void run() {
                    checkMenuItem(0);
                }
            });
        }

        //if the manual is called from the in game menu, show the corresponding game rule page
        if (getIntent()!=null && getIntent().hasExtra(GAME)){
            try {
                Fragment fragment = ManualGames.class.newInstance();

                //Put args, so the correct game page can be shown
                Bundle args = new Bundle();
                //args.putString(GAME, getIntent().getStringExtra(GAME));
                // @GN: fixed the error when the manual was opened on Canfield in stead of Klondike
                args.putString(GAME, "Klondike");
                fragment.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //set fragment loaded to false, so back press will return to the current game
            fragmentLoaded = false;
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        //phones
        if (drawer != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
        }

        //if another manual page has been loaded, return to the start page
        if (fragmentLoaded) {

            //if a game manual page has been shown, return to the selector
            if (gamePageShown) {
                loadFragment(ManualGames.class);
                gamePageShown = false;
                return;
            }

            //check the first menu item on phones/tablets
            if (drawer != null) {
                navigationView.setCheckedItem(R.id.nav_startpage);
            } else {
                checkMenuItem(0);
            }

            //return to start page
            loadFragment(ManualStartPage.class);
            fragmentLoaded = false;

        }
        //else close manual
        else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this method can be loaded only on tablets, because smaller screens
        //have the drawer menu

        if (fragmentLoaded) {
            if (gamePageShown) {
                loadFragment(ManualGames.class);
                gamePageShown = false;
            } else {
                checkMenuItem(0);
                loadFragment(ManualStartPage.class);
                fragmentLoaded = false;
            }
        } else {
            finish();
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //only used on phones and screens smaller than xlarge

        if (item.getItemId() == R.id.nav_back_to_game) {
            finish();
            return true;
        }

        int id = item.getItemId();
        Class fragmentClass;

        switch (id) {
            case R.id.nav_startpage:
            default:
                fragmentClass = ManualStartPage.class;
                break;
            case R.id.nav_menu:
                fragmentClass = ManualMenu.class;
                break;
            case R.id.nav_user_interface:
                fragmentClass = ManualUserInterface.class;
                break;
            case R.id.nav_games:
                fragmentClass = ManualGames.class;
                break;
            case R.id.nav_feedback:
                fragmentClass = ManualFeedback.class;
                break;
        }

        loadFragment(fragmentClass);

        fragmentLoaded = id != R.id.nav_startpage;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //only used on xlarge screens, without the drawer

        checkMenuItem(position);

        if (position == 5) {
            finish();
            return;
        }

        Class fragmentClass;

        switch (position) {
            case 0:
            default:
                fragmentClass = ManualStartPage.class;
                break;
            case 1:
                fragmentClass = ManualMenu.class;
                break;
            case 2:
                fragmentClass = ManualUserInterface.class;
                break;
            case 3:
                fragmentClass = ManualGames.class;
                break;
            case 4:
                fragmentClass = ManualFeedback.class;
                break;
        }

        loadFragment(fragmentClass);
        fragmentLoaded = position != 0;
    }

    private void loadFragment(Class fragmentClass) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkMenuItem(int listPosition) {
        if (lastSelectedView != null)
            lastSelectedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        lastSelectedView = listView.getChildAt(listPosition);
        lastSelectedViewPosition = listPosition;
        lastSelectedView.setBackgroundColor(getResources().getColor(R.color.colorDrawerSelected));
    }

    public void setGamePageShown(boolean shown) {
        gamePageShown = shown;
    }
}
