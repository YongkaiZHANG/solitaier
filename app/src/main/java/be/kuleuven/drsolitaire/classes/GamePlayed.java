package be.kuleuven.drsolitaire.classes;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.Ignore;
//import android.arch.persistence.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representation of Games Played by Persons.
 */

// @NG
@Entity
public class GamePlayed {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "personID")
    private int personID;
    @ColumnInfo(name = "gameTime")
    private int gameTime;
    @ColumnInfo(name = "isSolved")
    private boolean isSolved;
    @ColumnInfo(name = "gameseed")
    private int gameseed;
    @ColumnInfo(name = "score")
    private long score;

    @Ignore
    public GamePlayed() {
    }

    @Ignore
    public GamePlayed(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.personID = obj.getInt("personID");
            this.gameTime = obj.getInt("gameTime");
            this.isSolved = (obj.getInt("isSolved") == 1); //@kg
            this.gameseed = obj.getInt("gameseed");
            this.score = obj.getLong("score");
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            e.printStackTrace();
        }
    }


    public GamePlayed(int personID, int gameTime, boolean isSolved, int gameseed, long score) {
        this.personID = personID;
        this.gameTime = gameTime;
        this.isSolved = isSolved;
        this.gameseed = gameseed;
        this.score = score;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }



    public int getGameseed() {
        return gameseed;
    }

    public void setGameseed(int gameseed) {
        this.gameseed = gameseed;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

}

