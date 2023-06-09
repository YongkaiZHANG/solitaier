/* Copyright (C) 2016  Tobias Bielefeld
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;


import be.kuleuven.drsolitaire.R;
import be.kuleuven.drsolitaire.ui.GameManager;

import static be.kuleuven.drsolitaire.SharedData.DEFAULT_CURRENT_GAME;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_CURRENT_GAME;
import static be.kuleuven.drsolitaire.SharedData.currentGame;
import static be.kuleuven.drsolitaire.SharedData.gameLogic;
import static be.kuleuven.drsolitaire.SharedData.putSharedInt;
import static be.kuleuven.drsolitaire.SharedData.scores;
import static be.kuleuven.drsolitaire.SharedData.timer;

/**
 * dialog to handle new games or returning to main menu( in that case, cancel the current activity)
 */

public class DialogWon extends DialogFragment {

    private static String KEY_SCORE = "SCORE";
    private static String KEY_BONUS = "BONUS";
    private static String KEY_TOTAL = "TOTAL";

    private long score, bonus, total;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedState) {
        final GameManager gameManager = (GameManager) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_won, null);

        builder.setCustomTitle(view)
                .setItems(R.array.won_menu, new DialogInterface.OnClickListener() {
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
                                if (gameManager.hasLoaded) {
                                    timer.save();
                                    gameLogic.setWonAndReloaded();
                                    gameLogic.save();
                                }

                                putSharedInt(PREF_KEY_CURRENT_GAME, DEFAULT_CURRENT_GAME);          //otherwise the menu would load the current game again, because last played game will start
                                gameManager.finish();
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.game_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //just cancel
                    }
                });

        LinearLayout layoutScores = (LinearLayout) view.findViewById(R.id.dialog_won_layout_scores);

        //only show the calculation of the score if bonus is enabled
        if (currentGame.isBonusEnabled()) {
            layoutScores.setVisibility(View.VISIBLE);
            TextView text1 = (TextView) view.findViewById(R.id.dialog_won_text1);
            TextView text2 = (TextView) view.findViewById(R.id.dialog_won_text2);
            TextView text3 = (TextView) view.findViewById(R.id.dialog_won_text3);

            score = (savedState!=null && savedState.containsKey(KEY_SCORE)) ? savedState.getLong(KEY_SCORE) : scores.getPreBonus();
            bonus = (savedState!=null && savedState.containsKey(KEY_BONUS)) ? savedState.getLong(KEY_BONUS) : scores.getBonus();
            total = (savedState!=null && savedState.containsKey(KEY_TOTAL)) ? savedState.getLong(KEY_TOTAL) : scores.getScore();

            text1.setText(String.format(Locale.getDefault(),getContext().getString(R.string.dialog_win_score), score));
            text2.setText(String.format(Locale.getDefault(),getContext().getString(R.string.dialog_win_bonus), bonus));
            text3.setText(String.format(Locale.getDefault(),getContext().getString(R.string.dialog_win_total), total));
        } else {
            layoutScores.setVisibility(View.GONE);
        }



        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_SCORE,score);
        outState.putLong(KEY_BONUS,bonus);
        outState.putLong(KEY_TOTAL,total);
    }
}