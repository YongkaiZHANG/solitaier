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

package be.kuleuven.drsolitaire.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
//import android.support.v7.app.ActionBar;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.classes.Card;
import be.kuleuven.drsolitaire.classes.CustomPreferenceFragment;
import be.kuleuven.drsolitaire.handler.HandlerStopBackgroundMusic;
import be.kuleuven.drsolitaire.helper.Sounds;

import static be.kuleuven.drsolitaire.SharedData.CARD_BACKGROUND;
import static be.kuleuven.drsolitaire.SharedData.CARD_BACKGROUND_COLOR;
import static be.kuleuven.drsolitaire.SharedData.CARD_DRAWABLES;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_BACKGROUND_VOLUME;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_MENU_BAR_POSITION_LANDSCAPE;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_MENU_BAR_POSITION_PORTRAIT;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_MENU_COLUMNS_LANDSCAPE;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_MENU_COLUMNS_PORTRAIT;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_ORIENTATION;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_VEGAS_BET_AMOUNT;
import static be.kuleuven.drsolitaire.SharedData.MENU_COLUMNS_LANDSCAPE;
import static be.kuleuven.drsolitaire.SharedData.MENU_COLUMNS_PORTRAIT;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_4_COLOR_MODE;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_BACKGROUND_MUSIC;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_BACKGROUND_VOLUME;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_LANGUAGE;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_LEFT_HANDED_MODE;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_MOVEMENT_SPEED;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_ORIENTATION;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_SOUND_ENABLED;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_VEGAS_BET_AMOUNT;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_WIN_SOUND;
import static be.kuleuven.drsolitaire.SharedData.activityCounter;
import static be.kuleuven.drsolitaire.SharedData.animate;
import static be.kuleuven.drsolitaire.SharedData.backgroundSound;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.getSharedBoolean;
import static be.kuleuven.drsolitaire.SharedData.getSharedInt;
import static be.kuleuven.drsolitaire.SharedData.getSharedString;
import static be.kuleuven.drsolitaire.SharedData.reinitializeData;
import static be.kuleuven.drsolitaire.SharedData.savedSharedData;
import static be.kuleuven.drsolitaire.SharedData.sharedStringEqualsDefault;

/**
 * Settings activity created with the "Create settings activity" tool from Android Studio.
 */

public class Settings extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Toast toast;
    private Preference preferenceMenuBarPosition;
    private Preference preferenceMenuColumns;
    private Preference preferenceBackgroundVolume;
    private Preference preferenceVegasBetAmount;
    private Sounds settingsSounds;

    HandlerStopBackgroundMusic handlerStopBackgroundMusic = new HandlerStopBackgroundMusic();

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ViewGroup) getListView().getParent()).setPadding(0, 0, 0, 0);                             //remove huge padding in landscape

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        reinitializeData(getApplicationContext());

        settingsSounds = new Sounds(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //only item is the back arrow
        finish();
        return true;
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    @Override
    public void onResume() {
        super.onResume();

        savedSharedData.registerOnSharedPreferenceChangeListener(this);
        showOrHideStatusBar();
        setOrientation();

        activityCounter++;
        backgroundSound.doInBackground(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        savedSharedData.unregisterOnSharedPreferenceChangeListener(this);

        activityCounter--;
        handlerStopBackgroundMusic.sendEmptyMessageDelayed(0, 100);
    }

    /*
     * Update settings when the shared preferences get new values. It uses a lot of if/else instead
     * of switch/case because only this way i can use getString() to get the xml values, otherwise
     * I would need to write the strings manually in the cases.
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(CARD_DRAWABLES)) {
            Card.updateCardDrawableChoice();

        } else if (key.equals(CARD_BACKGROUND) || key.equals(CARD_BACKGROUND_COLOR)) {
            Card.updateCardBackgroundChoice();

        } else if (key.equals(getString(R.string.pref_key_hide_status_bar))) {
            showOrHideStatusBar();

        } else if (key.equals(getString(R.string.pref_key_orientation))) {
            setOrientation();

        } else if (key.equals(PREF_KEY_LEFT_HANDED_MODE)) {
            if (gameLogic != null) {
                gameLogic.mirrorStacks();
            }

        } else if (key.equals(MENU_COLUMNS_PORTRAIT) || key.equals(MENU_COLUMNS_LANDSCAPE)) {
            updatePreferenceMenuColumnsSummary();

        } else if (key.equals(PREF_KEY_LANGUAGE)) {
            setLocale();

        } else if (key.equals(getString(R.string.pref_key_menu_bar_position_landscape)) || key.equals(getString(R.string.pref_key_menu_bar_position_portrait))) {
            updatePreferenceMenuBarPositionSummary();
            if (gameLogic != null) {
                gameLogic.updateMenuBar();
            }

        } else if (key.equals(PREF_KEY_4_COLOR_MODE)) {
            Card.updateCardDrawableChoice();

        } else if (key.equals(PREF_KEY_MOVEMENT_SPEED)) {
            if (animate != null) {
                animate.updateMovementSpeed();
            }

        } else if (key.equals(PREF_KEY_WIN_SOUND)) {
            settingsSounds.playWinSound();

        } else if (key.equals(PREF_KEY_BACKGROUND_MUSIC) || key.equals(PREF_KEY_SOUND_ENABLED)) {
            backgroundSound.doInBackground(this);

        } else if (key.equals(PREF_KEY_BACKGROUND_VOLUME)){
            updatePreferenceBackgroundVolumeSummary();
            backgroundSound.doInBackground(this);

        }
    }

    /**
     * Tests if a loaded fragment is valid
     *
     * @param fragmentName The name of the fragment to test
     * @return True if it's valid, false otherwise
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || CustomizationPreferenceFragment.class.getName().equals(fragmentName)
                || OtherPreferenceFragment.class.getName().equals(fragmentName)
                || MenuPreferenceFragment.class.getName().equals(fragmentName)
                || AdditionalMovementsPreferenceFragment.class.getName().equals(fragmentName)
                || SoundPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * Applies the user setting of the screen orientation.
     */
    private void setOrientation() {
        switch (getSharedString(PREF_KEY_ORIENTATION, DEFAULT_ORIENTATION)) {
            case "1": //follow system settings
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                break;
            case "2": //portrait
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case "3": //landscape
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case "4": //landscape upside down
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
        }
    }

    /**
     * Applies the user setting of the status bar.
     */
    private void showOrHideStatusBar() {
        if (getSharedBoolean(getString(R.string.pref_key_hide_status_bar), false))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Shows the given text as a toast. New texts override the old one.
     *
     * @param text The text to show
     */
    private void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else
            toast.setText(text);

        toast.show();
    }

    /**
     * Restarts the app to apply the new locale settings
     */
    private void setLocale() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updatePreferenceMenuColumnsSummary() {
        int portraitValue = Integer.parseInt(getSharedString(MENU_COLUMNS_PORTRAIT, DEFAULT_MENU_COLUMNS_PORTRAIT));
        int landscapeValue = Integer.parseInt(getSharedString(MENU_COLUMNS_LANDSCAPE, DEFAULT_MENU_COLUMNS_LANDSCAPE));

        String text = String.format(Locale.getDefault(), "%s: %d\n%s: %d",
                getString(R.string.portrait), portraitValue, getString(R.string.landscape), landscapeValue);

        preferenceMenuColumns.setSummary(text);
    }

    private void updatePreferenceMenuBarPositionSummary() {
        String portrait, landscape;
        if (sharedStringEqualsDefault(getString(R.string.pref_key_menu_bar_position_portrait), DEFAULT_MENU_BAR_POSITION_PORTRAIT)) {
            portrait = getString(R.string.settings_menu_bar_position_bottom);
        } else {
            portrait = getString(R.string.settings_menu_bar_position_top);
        }

        if (sharedStringEqualsDefault(getString(R.string.pref_key_menu_bar_position_landscape), DEFAULT_MENU_BAR_POSITION_LANDSCAPE)) {
            landscape = getString(R.string.settings_menu_bar_position_right);
        } else {
            landscape = getString(R.string.settings_menu_bar_position_left);
        }

        String text = String.format(Locale.getDefault(), "%s: %s\n%s: %s",
                getString(R.string.portrait), portrait, getString(R.string.landscape), landscape);

        preferenceMenuBarPosition.setSummary(text);
    }

    private void updatePreferenceBackgroundVolumeSummary(){
        int volume = getSharedInt(PREF_KEY_BACKGROUND_VOLUME,DEFAULT_BACKGROUND_VOLUME);

        preferenceBackgroundVolume.setSummary(String.format(Locale.getDefault(),"%s %%",volume));
    }

    private void updatePreferenceVegasBetAmountSummary(){
        int amount = getSharedInt(PREF_KEY_VEGAS_BET_AMOUNT,DEFAULT_VEGAS_BET_AMOUNT);

        preferenceVegasBetAmount.setSummary(String.format(Locale.getDefault(),getString(R.string.settings_vegas_bet_amount_summary),amount*10,amount));
    }

    public static class CustomizationPreferenceFragment extends CustomPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_customize);
            setHasOptionsMenu(true);

            Settings settings = (Settings) getActivity();

            settings.preferenceMenuBarPosition = findPreference(getString(R.string.pref_key_menu_bar_position));

            settings.updatePreferenceMenuBarPositionSummary();
        }
    }

    public static class OtherPreferenceFragment extends CustomPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_other);
            setHasOptionsMenu(true);
        }
    }

    public static class SoundPreferenceFragment extends CustomPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sounds);
            setHasOptionsMenu(true);


            Settings settings = (Settings) getActivity();

            settings.preferenceBackgroundVolume = findPreference(getString(R.string.pref_key_background_volume));

            settings.updatePreferenceBackgroundVolumeSummary();
        }
    }



    public static class MenuPreferenceFragment extends CustomPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_menu);
            setHasOptionsMenu(true);

            Settings settings = (Settings) getActivity();

            settings.preferenceMenuColumns = findPreference(getString(R.string.pref_key_menu_columns));
            settings.updatePreferenceMenuColumnsSummary();
        }
    }

    public static class AdditionalMovementsPreferenceFragment extends CustomPreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_movement_methods);
            setHasOptionsMenu(true);
        }
    }
}
