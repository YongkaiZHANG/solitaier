package be.kuleuven.drsolitaire.classes;

import android.content.Context;
import android.preference.PreferenceFragment;

import static be.kuleuven.drsolitaire.SharedData.reinitializeData;

/**
 * Custom PreferenceFragment, to override onAttach. If the app got killed within a
 * PreferenceFragment and restarted, the data has to be reinitialized
 */

public class CustomPreferenceFragment extends PreferenceFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        reinitializeData(context);
    }
}
