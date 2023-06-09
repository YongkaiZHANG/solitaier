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
import android.graphics.Bitmap;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import be.kuleuven.drsolitaire.R;

import static be.kuleuven.drsolitaire.SharedData.*;

/**
 * dialog for picking the card front drawable. It uses a custom layout, so I can dynamically update
 * the widget icon of the preference.
 */

public class DialogPreferenceCardDialog extends DialogPreference implements View.OnClickListener {

    private LinearLayout[] linearLayouts = new LinearLayout[NUMBER_OF_CARD_THEMES];
    private Context context;
    private ImageView image;

    public DialogPreferenceCardDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_settings_cards);
        setDialogIcon(null);
        this.context = context;
    }

    @Override
    protected void onBindDialogView(View view) {
        int row = getSharedBoolean(PREF_KEY_4_COLOR_MODE, DEFAULT_4_COLOR_MODE) ? 1 : 0;

        linearLayouts[0] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsBasic);
        linearLayouts[1] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsClassic);
        linearLayouts[2] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsAbstract);
        linearLayouts[3] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsSimple);
        linearLayouts[4] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsModern);
        linearLayouts[5] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsOxygenDark);
        linearLayouts[6] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsOxygenLight);
        linearLayouts[7] = (LinearLayout) view.findViewById(R.id.settingsLinearLayoutCardsPoker);

        for (int i = 0; i < NUMBER_OF_CARD_THEMES; i++) {
            linearLayouts[i].setOnClickListener(this);
            ImageView imageView = (ImageView) linearLayouts[i].getChildAt(0);
            imageView.setImageBitmap(bitmaps.getCardPreview(i, row));
        }

        super.onBindDialogView(view);
    }

    public void onClick(View v) {
        int choice;

        switch (v.getId()) {
            case R.id.settingsLinearLayoutCardsBasic:
            default:
                choice = 1;
                break;
            case R.id.settingsLinearLayoutCardsClassic:
                choice = 2;
                break;
            case R.id.settingsLinearLayoutCardsAbstract:
                choice = 3;
                break;
            case R.id.settingsLinearLayoutCardsSimple:
                choice = 4;
                break;
            case R.id.settingsLinearLayoutCardsModern:
                choice = 5;
                break;
            case R.id.settingsLinearLayoutCardsOxygenDark:
                choice = 6;
                break;
            case R.id.settingsLinearLayoutCardsOxygenLight:
                choice = 7;
                break;
            case R.id.settingsLinearLayoutCardsPoker:
                choice = 8;
                break;

        }

        putSharedInt(CARD_DRAWABLES, choice);
        updateSummary();
        getDialog().dismiss();
    }

    /*
     * Get the layout from the preference, so I can get the imageView from the widgetLayout
     */
    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);

        image = (ImageView) view.findViewById(R.id.preference_cards_imageView);
        updateSummary();

        return view;
    }

    /**
     * Gets the bitmap for the card theme preference icon and also set its summary
     */
    private void updateSummary() {
        String text;
        Bitmap cardPreview;
        int row = getSharedBoolean(PREF_KEY_4_COLOR_MODE, DEFAULT_4_COLOR_MODE) ? 1 : 0;
        int selectedTheme = getSharedInt(CARD_DRAWABLES, 2);

        switch (selectedTheme) {
            case 1:
                text = context.getString(R.string.settings_basic);
                break;
            case 2:
                default:
                text = context.getString(R.string.settings_classic);
                break;
            case 3:
                text = context.getString(R.string.settings_abstract);
                break;
            case 4:
                text = context.getString(R.string.settings_simple);
                break;
            case 5:
                text = context.getString(R.string.settings_modern);
                break;
            case 6:
                text = context.getString(R.string.settings_oxygen_dark);
                break;
            case 7:
                text = context.getString(R.string.settings_oxygen_light);
                break;
            case 8:
                text = context.getString(R.string.settings_poker);
                break;
        }

        cardPreview = bitmaps.getCardPreview2(selectedTheme - 1, row);

        image.setImageBitmap(cardPreview);
        setSummary(text);
    }
}
