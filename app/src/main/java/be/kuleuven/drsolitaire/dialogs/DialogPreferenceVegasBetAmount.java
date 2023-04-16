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

package be.kuleuven.drsolitaire.dialogs;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;


import be.kuleuven.drsolitaire.R;

import static be.kuleuven.drsolitaire.SharedData.DEFAULT_VEGAS_BET_AMOUNT;
import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_VEGAS_BET_AMOUNT;
import static be.kuleuven.drsolitaire.SharedData.getSharedInt;
import static be.kuleuven.drsolitaire.SharedData.putSharedInt;


/*
 * custom dialog to set the background music volume. it can be set from 0 (off) to 100%.
 */

public class DialogPreferenceVegasBetAmount extends DialogPreference implements SeekBar.OnSeekBarChangeListener{

    private SeekBar mSeekBar;
    private TextView mTextView;

    public DialogPreferenceVegasBetAmount(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_vegas_bet_amount);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        mTextView = (TextView) view.findViewById(R.id.textView);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);

        int amount = getSharedInt(PREF_KEY_VEGAS_BET_AMOUNT,DEFAULT_VEGAS_BET_AMOUNT);
        mSeekBar.setProgress(amount);
        setProgressText(amount);

        super.onBindDialogView(view);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        setProgressText(i);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            putSharedInt(PREF_KEY_VEGAS_BET_AMOUNT,mSeekBar.getProgress());
        }
    }

    private void setProgressText(int value){
        mTextView.setText(String.format(Locale.getDefault(),getContext().getString(R.string.settings_vegas_bet_amount_progress_text),value*10,value));
    }
}
