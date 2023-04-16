package be.kuleuven.drsolitaire;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;

import java.util.HashMap;

import be.kuleuven.drsolitaire.classes.Person;
import be.kuleuven.drsolitaire.database.EntityMapper;
import be.kuleuven.drsolitaire.interfaces.RegistrationFragmentListener;
import be.kuleuven.drsolitaire.ui.GameSelector;

import static be.kuleuven.drsolitaire.SharedData.PREF_KEY_MENU_BAR_POS_LANDSCAPE;
import static be.kuleuven.drsolitaire.SharedData.reinitializeData;

public class RegistrationActivity extends AppCompatActivity implements RegistrationFragmentListener {

    /**
     * The {@link androidx.core.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link androidx.core.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private HashMap<String, String> registrationInfo = new HashMap<>();
    private static EntityMapper entityMapper = SharedData.getEntityMapper();


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addStringHashmap(String key, String value) {
        registrationInfo.put(key, value);

    }

    @Override
    public String getStringHashmap(String key) {
        return registrationInfo.get(key);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return DemographyFragment.newInstance(position + 1);
                case 1: return ExperienceFragment.newInstance(position + 1);
                case 2: return UserPasswordFragment.newInstance(position + 1);
                default: return DemographyFragment.newInstance(position + 1);
            } }

        @Override
        public int getCount() {
            return 3;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class UserPasswordFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RegistrationFragmentListener mCallback;
        View rootView;
        TextView username;
        TextView password1;

        public UserPasswordFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static UserPasswordFragment newInstance(int sectionNumber) {
            UserPasswordFragment fragment = new UserPasswordFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_registration_userpassword, container, false);
            username = rootView.findViewById(R.id.username);
            password1 = rootView.findViewById(R.id.password1);
            TextView password2 = rootView.findViewById(R.id.password2);
            Button proceed = rootView.findViewById(R.id.proceedBtn);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (password1.getText().toString().equals(password2.getText().toString())) {
                        SharedData.getEntityMapper().getpMapper().getPersonByUsernameAndPassword(new Person(username.getText().toString(),password1.getText().toString()));
                        new GetPerson().execute();
                    }
                    else {
                        Snackbar.make(rootView, "Password doesn't match, please try again.", Snackbar.LENGTH_LONG).show();
                        }
                }
            });
            //TextView was defined here
            return rootView;
        }

        /*
        Start registerprocess

         */


        private class GetPerson extends AsyncTask<Void, Void, Person> {
            protected Person doInBackground(Void... voids) {
                Person person = new Person();
                while (!entityMapper.isDataReady() && !entityMapper.isErrorHappened()) {
                    if (isCancelled()) break;
                }
                if (entityMapper.isDataReady()) {
                    person = SharedData.getEntityMapper().person;
                    SharedData.getEntityMapper().dataGrabbed();
                }
                return person;
            }

            protected void onPostExecute(Person person) {
                if (person != null) {
                    if (entityMapper.isErrorHappened()) {
                        entityMapper.errorHandled();
                        Snackbar.make(rootView, "Geen verbinding met het internet. Verbind je toestel met het netwerk om je te registreren.", Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        Snackbar.make(rootView, "Deze gebruikersnaam bestaat al!", Snackbar.LENGTH_LONG).show();
                        }
                }
                else {
                    if (isInteger(mCallback.getStringHashmap("AGE"))) {
                        Snackbar.make(rootView, "Registratie gelukt. Veel plezier!", Snackbar.LENGTH_LONG).show();
                        person = new Person(username.getText().toString(),
                                password1.getText().toString(),
                                Integer.parseInt(mCallback.getStringHashmap("AGE")),
                                mCallback.getStringHashmap("GENDER"),
                                Integer.parseInt(mCallback.getStringHashmap("PATIENCEXP")), Integer.parseInt(mCallback.getStringHashmap("TABLETXP")));
                        entityMapper.getpMapper().createPerson(person);
                        new CreatePerson().execute();
                    }
                    else {
                        Snackbar.make(rootView, "De ingevoerde leeftijd is geen getal.", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
        public static boolean isInteger(String s) {
            try {
                Integer.parseInt(s);
            } catch(NumberFormatException e) {
                return false;
            } catch(NullPointerException e) {
                return false;
            }
            return true;
        }

        private class CreatePerson extends AsyncTask<Void, Void, Person> {
            protected Person doInBackground(Void... voids) {
                Person person = new Person();
                while (!entityMapper.isDataReady() && !entityMapper.isErrorHappened()) {
                    if (isCancelled()) break;
                }
                if (entityMapper.isDataReady()) {
                    person = SharedData.getEntityMapper().person;
                    SharedData.getEntityMapper().dataGrabbed();
                }
                return person;
            }

            protected void onPostExecute(Person person) {
                if (person!= null) {
                    SharedData.user = person;
                    Intent intent = new Intent(getActivity(), GameSelector.class);
                    getActivity().getSharedPreferences(SharedData.PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(SharedData.PREF_USERNAME, username.getText().toString())
                            .putString(SharedData.PREF_PASSWORD, password1.getText().toString())
                            .commit();
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    entityMapper.errorHandled();
                    Snackbar.make(rootView, "Geen verbinding met het internet. Verbind je toestel met het netwerk om je te registreren.", Snackbar.LENGTH_LONG).show();
                }
            }
        }

        /*
            End registerprocess

         */

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (RegistrationFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement RegistrationFragmentListener");
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DemographyFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RegistrationFragmentListener mCallback;


        public DemographyFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DemographyFragment newInstance(int sectionNumber) {
            DemographyFragment fragment = new DemographyFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration_demography, container, false);
            RadioGroup radioGroupGender = (RadioGroup) rootView.findViewById(R.id.radioGroupGender);
            RadioGroup radioGroupHand = (RadioGroup) rootView.findViewById(R.id.radioGroupHand);
            TextView age = (TextView) rootView.findViewById(R.id.age);
            Button button = rootView.findViewById(R.id.proceedBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedGender = radioGroupGender.getCheckedRadioButtonId();
                    int selectedHand = radioGroupHand.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton radioButtonGender = (RadioButton) rootView.findViewById(selectedGender);
                    mCallback.addStringHashmap("GENDER", radioButtonGender.getText().toString());
                    RadioButton radioButtonHand = (RadioButton) rootView.findViewById(selectedHand);
                    reinitializeData(getActivity().getApplicationContext());
                    SharedData.putSharedString(PREF_KEY_MENU_BAR_POS_LANDSCAPE , ((radioButtonHand.getText().toString().equals("Rechtshandig")) ? "left" : "right"));
                    mCallback.addStringHashmap("AGE", age.getText().toString());

                    mViewPager.setCurrentItem(1);
                }
                });
            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (RegistrationFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement RegistrationFragmentListener");
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ExperienceFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        RegistrationFragmentListener mCallback;


        public ExperienceFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ExperienceFragment newInstance(int sectionNumber) {
            ExperienceFragment fragment = new ExperienceFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration_experience, container, false);
            Button button = rootView.findViewById(R.id.registerBtn);
            IndicatorSeekBar indicatorTablet = (IndicatorSeekBar) rootView.findViewById(R.id.indicatorSeekBarTablet);
            IndicatorSeekBar indicatorPatience = (IndicatorSeekBar) rootView.findViewById(R.id.indicatorSeekBarPatience);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCallback.addStringHashmap("TABLETXP", Integer.toString(indicatorTablet.getProgress()));
                    mCallback.addStringHashmap("PATIENCEXP", Integer.toString(indicatorPatience.getProgress()));

                    mViewPager.setCurrentItem(2);
                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (RegistrationFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement RegistrationFragmentListener");
            }
        }
    }

}
