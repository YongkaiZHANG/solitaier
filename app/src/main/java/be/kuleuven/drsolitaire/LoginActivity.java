//@GN

package be.kuleuven.drsolitaire;

//import android.arch.persistence.room.Room;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;

import be.kuleuven.drsolitaire.classes.Person;
import be.kuleuven.drsolitaire.database.AppDatabase;
import be.kuleuven.drsolitaire.database.EntityMapper;
import be.kuleuven.drsolitaire.ui.GameSelector;

// @GN
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EntityMapper entityMapper = SharedData.getEntityMapper();
    private boolean isAutoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupContent();
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        SharedData.getEntityMapper().setRequestQueue(rq);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "moves").allowMainThreadQueries().build();
        SharedData.setAppDatabase(db);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences pref = getSharedPreferences(SharedData.PREFS_NAME,MODE_PRIVATE);
        String username2 = pref.getString(SharedData.PREF_USERNAME, null);
        String password2 = pref.getString(SharedData.PREF_PASSWORD, null);

        if (username2 != null && password2 != null) {
            SharedData.getEntityMapper().getpMapper().getPersonByUsernameAndPassword(new Person(username2,password2));
            username.setText(username2);
            password.setText(password2);
            new GetPerson().execute();
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void loginApp(View view) {
        SharedData.getEntityMapper().getpMapper().getPersonByUsernameAndPassword(new Person(username.getText().toString(),password.getText().toString())); //TODO bypassed login here
        new GetPerson().execute();
    }

    public void skipApp(View view) {
        Intent intent = new Intent(LoginActivity.this, GameSelector.class);
        startActivity(intent);

    }

    public void registerApp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }

    private void setupContent(){
        username = (EditText) findViewById(R.id.username);
        password  = (EditText) findViewById(R.id.password);
    }

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
            Log.d("DB",""+person);
            if (person != null) {
                if (password.getText().toString().equals(person.getPassword())) {
                    Intent intent = new Intent(LoginActivity.this, GameSelector.class);
                    SharedData.user = person;
                    getSharedPreferences(SharedData.PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(SharedData.PREF_USERNAME, person.getUsername())
                            .putString(SharedData.PREF_PASSWORD, person.getPassword())
                            .commit();
                    startActivity(intent);
                    finish();
                } else if (entityMapper.isErrorHappened()) {
                    entityMapper.errorHandled();
                    Toast.makeText(LoginActivity.this, "Geen verbinding met het internet. Verbind je toestel met het netwerk om je in te loggen.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(LoginActivity.this,"Je logingegevens zijn incorrect", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(LoginActivity.this,"Je logingegevens zijn incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}
