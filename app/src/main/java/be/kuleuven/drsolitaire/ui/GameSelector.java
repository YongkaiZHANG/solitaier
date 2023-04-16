package be.kuleuven.drsolitaire.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
//import android.support.design.widget.NavigationView;
import androidx.core.view.GravityCompat;
//import androidx.core.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import be.kuleuven.drsolitaire.LoginActivity;

import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.CustomAppCompatActivity;
import be.kuleuven.drsolitaire.ui.about.AboutActivity;
import be.kuleuven.drsolitaire.ui.manual.Manual;
import be.kuleuven.drsolitaire.ui.settings.Settings;

import static be.kuleuven.drsolitaire.SharedData.*;

public class GameSelector extends CustomAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    private TableLayout tableLayout;
    private NavigationView navigationView;
    private int menuColumns;
    private ArrayList<Integer> indexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tableLayout = (TableLayout) findViewById(R.id.tableLayoutGameChooser);

        if (!getSharedBoolean(getString(R.string.pref_key_start_menu), false)) {
            int savedGame = getSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME);

            if (savedGame != 0) {
                Intent intent = new Intent(getApplicationContext(), GameManager.class);
                intent.putExtra(GAME, savedGame);
                startActivityForResult(intent, 0);
            }
        } else {
            putSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer!=null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.item_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            case R.id.item_manual:
                startActivity(new Intent(getApplicationContext(), Manual.class));
                break;
            case R.id.item_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.item_close:
                SharedData.user = null;
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(PREF_USERNAME, null)
                        .putString(PREF_PASSWORD, null)
                        .commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * load the game list of the menu. First clear everything and then add each game, if they aren't
     * set to be hidden. Add the end, add some dummies, so the last row doesn't have less entries.
     */
    private void loadGameList() {
        // @GN
        ArrayList<Integer> gameOrder = lg.getOrderedGameList();
        Integer game = gameOrder.get(7);
        gameOrder.clear();
        gameOrder.add(game);

        putSharedIntList(PREF_KEY_MENU_GAMES, gameOrder);

        ArrayList<Integer> isShownList = lg.getMenuShownList();
        ArrayList<Integer> orderedList = lg.getOrderedGameList();
        TableRow row = new TableRow(this);
        int counter = 0;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            menuColumns = Integer.parseInt(getSharedString(MENU_COLUMNS_LANDSCAPE, DEFAULT_MENU_COLUMNS_LANDSCAPE));
        } else {
            menuColumns = Integer.parseInt(getSharedString(MENU_COLUMNS_PORTRAIT, DEFAULT_MENU_COLUMNS_PORTRAIT));
        }

        //clear the complete layout first
        tableLayout.removeAllViewsInLayout();
        indexes.clear();

        int padding = (int) (getResources().getDimension(R.dimen.game_selector_images_padding));
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        params.weight = 1;

        //add the game buttons
        for (int i = 0; i < 2; i++) {

            int index = orderedList.indexOf(i);

            if (isShownList.get(index) == 1) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                imageView.setAdjustViewBounds(true);
                imageView.setLongClickable(true);
                imageView.setPadding(padding,padding,padding,padding);

                if (counter % menuColumns == 0) {
                    row = new TableRow(this);
                    tableLayout.addView(row);
                }

                imageView.setImageBitmap(bitmaps.getMenu(7));
                imageView.setOnTouchListener(this);
                indexes.add(i);
                row.addView(imageView);
                counter++;
            }
        }

        //add some dummies to the last row, if necessary
        while (row.getChildCount() < menuColumns) {
            FrameLayout dummy = new FrameLayout(this);
            dummy.setLayoutParams(params);
            row.addView(dummy);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the player returns from a game to the main menu, save it.
        putSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadGameList();

        if (navigationView != null) {
            navigationView.setCheckedItem(R.id.item_close);
        }
    }

    /*
     * Used to make the "button press" animation on the game imageViews. Only start the game if the
     * touch point is still on the imageView and stop the animation when scrolling the scrollView
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //shrink button
            changeButtonSize(v, 0.9f);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //regain button size
            changeButtonSize(v, 1.0f);

            float X = event.getX(), Y = event.getY();

            if (X > 0 && X < v.getWidth() && Y > 0 && Y < v.getHeight()) {
                startGame(v);
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            //regain button size
            changeButtonSize(v, 1.0f);
        }

        return false;
    }

    /**
     * changes the button size, according to the second parameter.
     * Used to shrink/expand the menu buttons.
     *
     * @param view  The view to apply the changes
     * @param scale The scale to apply
     */
    private void changeButtonSize(View view, float scale) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        animX.setDuration(100);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        animY.setDuration(100);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);

        if (scale == 1.0) { //expand button with a little delay
            animSetXY.setStartDelay(getResources().getInteger(R.integer.expand_button_anim_delay_ms));
        }

        animSetXY.start();
    }

    /**
     * Starts the clicked game. This uses the total index position of the clicked view to get the
     * game.
     *
     * @param view The clicked view.
     */
    private void startGame(View view) {
        TableRow row = (TableRow) view.getParent();
        TableLayout table = (TableLayout) row.getParent();
        ArrayList<Integer> orderedList = lg.getOrderedGameList();
        int index = indexes.get(table.indexOfChild(row)*menuColumns + row.indexOfChild(view));
        index = orderedList.indexOf(index);

        //avoid loading two games at once when pressing two buttons at once
        if (getSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME) != 0) {
            return;
        }

        putSharedInt(PREF_KEY_CURRENT_GAME, index);
        Intent intent = new Intent(getApplicationContext(), GameManager.class);
        intent.putExtra(GAME, index);
        startActivityForResult(intent, 0);
    }

}
