/* Copyright (C) 2016  Tobias Bielefeld
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

package be.kuleuven.drsolitaire.dialogs;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
import android.util.Log;
import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;

import be.kuleuven.drsolitaire.LoginActivity;
import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.ui.GameManager;
import be.kuleuven.drsolitaire.ui.manual.Manual;

import static android.content.Context.MODE_PRIVATE;
import static be.kuleuven.drsolitaire.SharedData.DEFAULT_CURRENT_GAME;
import static be.kuleuven.drsolitaire.SharedData.GAME;
import static be.kuleuven.drsolitaire.SharedData.PREFS_NAME;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_CURRENT_GAME;
import static be.kuleuven.drsolitaire.SharedData.PREF_PASSWORD;
import static be.kuleuven.drsolitaire.SharedData.PREF_USERNAME;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.lg;
import static be.kuleuven.drsolitaire.SharedData.putSharedInt;
import static be.kuleuven.drsolitaire.SharedData.timer;

/**
 * dialog to handle new games or returning to main menu( in that case, cancel the current activity)
 */

public class DialogRestart extends DialogFragment {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final GameManager gameManager = (GameManager) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Klondike Solitaire") // hard coded name of game, as the only game is Klondike Solitaire, can be changes if hard coded is not good enough
                .setItems(R.array.restart_menu, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // "which" argument contains index of selected item
                        switch (which) {
                            case 0:
                                gameLogic.newGame();
                                break;
                            case 1:
                                gameLogic.redeal();
                                break;
                            case 2:
                                Intent intent = new Intent(gameManager, Manual.class);
                                intent.putExtra(GAME,lg.getSharedPrefName());
                                startActivity(intent);
                                break;
                            case 3:
                                if (gameManager.hasLoaded) {
                                    timer.save();
                                    gameLogic.setWonAndReloaded();
                                    gameLogic.save();

                                    SharedData.user = null;
                                    gameManager.getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                            .edit()
                                            .putString(PREF_USERNAME, null)
                                            .putString(PREF_PASSWORD, null)
                                            .commit();
                                    startActivity(new Intent(gameManager.getApplicationContext(), LoginActivity.class));
                                    gameManager.finish();
                                }

                                putSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME);          //otherwise the menu would load the current game again, because last played game will start
                                gameManager.finish();

                                LoginActivity.deleteCache(getContext());
                                ((ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.game_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //just cancel
                    }
                });

        return builder.create();
    }

}