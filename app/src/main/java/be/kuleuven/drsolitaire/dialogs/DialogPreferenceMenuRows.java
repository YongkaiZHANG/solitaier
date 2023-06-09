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
import android.widget.Spinner;


import be.kuleuven.drsolitaire.R;

import static be.kuleuven.drsolitaire.SharedData.*;

/**
 * dialog for changing the rows shown in the menu. It uses different values for portrait and landscape
 */

public class DialogPreferenceMenuRows extends DialogPreference {

    Spinner spinnerPortrait, spinnerLandscape;

    public DialogPreferenceMenuRows(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_settings_menu_columns);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        spinnerPortrait = (Spinner) view.findViewById(R.id.dialogSettingsMenuColumnsPortrait);
        spinnerLandscape = (Spinner) view.findViewById(R.id.dialogSettingsMenuColumnsLandscape);

        //minus 1 because the values are 1 to 10, indexes are from 0 to 9
        spinnerPortrait.setSelection(Integer.parseInt(
                getSharedString(MENU_COLUMNS_PORTRAIT, DEFAULT_MENU_COLUMNS_PORTRAIT)) - 1);
        spinnerLandscape.setSelection(Integer.parseInt(
                getSharedString(MENU_COLUMNS_LANDSCAPE, DEFAULT_MENU_COLUMNS_LANDSCAPE)) - 1);

        super.onBindDialogView(view);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            putSharedString(MENU_COLUMNS_PORTRAIT, spinnerPortrait.getSelectedItem().toString());
            putSharedString(MENU_COLUMNS_LANDSCAPE, spinnerLandscape.getSelectedItem().toString());
        }
    }
}
