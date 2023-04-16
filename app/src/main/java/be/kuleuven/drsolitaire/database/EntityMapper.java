package be.kuleuven.drsolitaire.database;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.GamePlayed;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.MoveType;
import be.kuleuven.drsolitaire.classes.Person;
import be.kuleuven.drsolitaire.classes.Request;
import be.kuleuven.drsolitaire.games.Game;
import be.kuleuven.drsolitaire.helper.GameLogic;

import static be.kuleuven.drsolitaire.SharedData.currentGame;

/**
 * Contains standard methods for mappers
 */

// @GN
public enum EntityMapper {
    UNIQUEMAPPER;

    /** Singleton design applied; use only one mapper for each entity. */
    private final PersonMapper pMapper = PersonMapper.UNIQUEMAPPER;
    private final GameMapper gMapper = GameMapper.UNIQUEMAPPER;
    private final MoveMapper mMapper = MoveMapper.UNIQUEMAPPER;

    private void initiateSingletons() {
        this.pMapper.setEntityMapper(this);
        this.gMapper.setEntityMapper(this);
        this.mMapper.setEntityMapper(this);
    }
    private RequestQueue requestQueue;
    public void setRequestQueue(RequestQueue rq) { this.requestQueue = rq; }
    public RequestQueue getRequestQueue() {return requestQueue;}

    /** Singleton design applied; return the single mappers to other mappers that need them. */
    public PersonMapper getpMapper() { return pMapper; }
    public GameMapper getgMapper() {return gMapper;}
    public MoveMapper getmMapper() {return mMapper;}

    /**
     * Prepare one of each entity, and one list for each of those entities.
     * Each time an entity or a list of them is requested from the DB, start
     * asynchronously requesting and parsing them, and fill the aforementioned
     * empty entities or lists with the parsed ones. As soon as they are filled,
     * set the isDataReady field "true" to indicate the entities can be grabbed.
     */
    public Person person;
    public List<Person> persons;
    public GamePlayed game;
    public List<GamePlayed> games;
    public Move move;
    public List<Move> moves;
    private boolean dataReady = false;
    private boolean errorHappened = false;
    public boolean isErrorHappened() {return this.errorHappened;}
    public void errorHappened() {this.errorHappened = true;}
    public void errorHandled() {this.errorHappened = false;}
    public boolean isDataReady() {return this.dataReady; }
    public void dataReady() {
        Log.d("DB","DataReady!");
        this.dataReady = true;}

    public void dataGrabbed() {
        Log.d("DB","DataGrabbed!");
        this.dataReady = false;
        person = null;
        persons = new LinkedList<>();
        game = null;
        games = new LinkedList<>();
        move = null;
        moves = new LinkedList<>();
    }

    private int storedGames = 0;
    public int getStoredGames() {return storedGames;}
    public void newStoredGame() {this.storedGames++;}
    public void handledStoredGame() {this.storedGames--;}


    public void queryPersonLogin(final Object obj, String url) {
        Person person = (Person) obj;
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, url
                , response -> {
            Log.d("DB", "Response: "+response);
            JSONArray JSONresponse = null;
            try {
                JSONresponse = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapToEntity(JSONresponse, obj);
            }, error -> {
                errorHappened();
                if (error instanceof NetworkError || error instanceof TimeoutError) {
                    Log.d("DB","No connection available so you are unable to log in... ");
                }
                else Log.d("DB","Some error in queryGame occurs! "+ error.getMessage() + " "+error.getLocalizedMessage());})
        {
            @Override
            protected Map<String,String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("user",person.getUsername());
                params.put("pass",person.getPassword());
                return params;
            }
        };
        requestQueue.add(sr);
    }
    public void queryRegisterPerson(final Object obj, String url) {
        Person person = (Person) obj;
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, url
                , response -> {
            Log.d("DB", response);
            JSONArray JSONresponse = null;
            try {
                JSONresponse = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapToEntity(JSONresponse, obj);
            dataReady();
        }, error -> {
            errorHappened();
            if (error instanceof NetworkError || error instanceof TimeoutError) {
                Log.d("DB","No connection available so you are unable to log in... ");
            }
            else Log.d("DB","Some error in queryGame occurs! "+ error.getMessage() + " "+error.getLocalizedMessage());})
        {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user",person.getUsername());
                params.put("pass",person.getPassword());
                params.put("age",String.valueOf(person.getAge()));
                params.put("gender", String.valueOf((person.getGender())));
                params.put("playLvl", String.valueOf(person.getLevel()));
                params.put("touchLvl", String.valueOf(person.getTabletLevel()));
                return params;
            }
        };
        requestQueue.add(sr);
    }

    public void mapToEntities(JSONArray json, Object obj) {
        for (int i=0;i<json.length();i++) {
            try {if (obj instanceof Person) {
                persons.add(new Person(json.getJSONObject(i)));
            }
            else if (obj instanceof GamePlayed) {
                games.add(new GamePlayed(json.getJSONObject(i)));
            }
            else if (obj instanceof Move) {
                moves.add(new Move(json.getJSONObject(i)));
            }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Object mapToEntity(JSONArray json, Object obj) {
        try {if (obj instanceof Person) {
            person = new Person(json.getJSONObject(0));
        }
        else if (obj instanceof GamePlayed) {
            game = new GamePlayed(json.getJSONObject(0));
            return game;
        }
        else if (obj instanceof Move) {
            move = new Move(json.getJSONObject(0));
            return move;
        }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        dataReady();
        return null;
    }




    EntityMapper() {
        initiateSingletons();
        dataGrabbed();
    }
}