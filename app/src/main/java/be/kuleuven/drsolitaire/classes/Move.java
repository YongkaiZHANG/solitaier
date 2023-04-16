package be.kuleuven.drsolitaire.classes;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.Ignore;
//import android.arch.persistence.room.PrimaryKey;
//import android.arch.persistence.room.TypeConverters;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karst on 15-Jan-18.
 * Containerclass for a move.
 */

// @GN
@Entity
public class Move {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "gameId")
    private int gameid;
    @ColumnInfo(name = "type")
    @TypeConverters(be.kuleuven.drsolitaire.classes.MoveType.class)
    private MoveType type;
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "time")
    private long time;
    @ColumnInfo(name = "accuracy")
    private double accuracy;
    @ColumnInfo(name = "originStack")
    private int originstack;
    @ColumnInfo(name = "destinationStack")
    private int destinationstack;
    @ColumnInfo(name = "originCard")
    private String origincard;
    @ColumnInfo(name = "destinationCard")
    private String destinationcard;
    @ColumnInfo(name = "numberOfCardsMoved")
    private int numberOfCardsMoved;
    @ColumnInfo(name = "score")
    private long score;
    @ColumnInfo(name = "xCoordinate")
    private float xCoordinate;
    @ColumnInfo(name = "yCoordinate")
    private float yCoordinate;
    @ColumnInfo(name = "betaError")
    private boolean betaError;
    @ColumnInfo(name = "suitError")
    private boolean suitError;
    @ColumnInfo(name = "rankError")
    private boolean rankError;
    @ColumnInfo(name = "aceBetaError")
    private boolean aceBetaError;
    @ColumnInfo(name = "kingBetaError")
    private boolean kingBetaError;
    @ColumnInfo(name = "noAceOnSuitError")
    private boolean noAceOnSuitError;
    @ColumnInfo(name = "noKingOnBuildStackError")
    private boolean noKingOnBuildStackError;


    @Ignore
    public Move(){

    }

    public Move(MoveType type, long timestamp,long time, int originstack, int destinationstack, String origincard, String destinationcard, double accuracy, int numberOfCardsMoved, boolean suitError, boolean rankError, long score, float xCoordinate, float yCoordinate, boolean betaError, boolean aceBetaError, boolean kingBetaError, boolean noAceOnSuitError, boolean noKingOnBuildStackError)
    {
        this.type = type;
        this.timestamp = timestamp;
        this.time = time;
        this.gameid = 0;
        this.originstack = originstack;
        this.destinationstack = destinationstack;
        this.origincard = origincard;
        this.destinationcard = destinationcard;
        this.accuracy = accuracy;
        this.numberOfCardsMoved = numberOfCardsMoved;
        this.rankError = rankError;
        this.suitError = suitError;
        this.score = score;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.betaError = betaError;
        this.aceBetaError = aceBetaError;
        this.kingBetaError = kingBetaError;
        this.noAceOnSuitError = noAceOnSuitError;
        this.noKingOnBuildStackError = noKingOnBuildStackError;
    }


    @Ignore
    public Move(JSONObject obj) {
        try {
            this.id = 0;
            this.type = MoveType.getMoveType(obj.getString("type"));
            this.timestamp = obj.getLong("timestamp");
            this.time = obj.getLong("time");
            this.gameid = obj.getInt("gameid");
            this.originstack = obj.getInt("originstack");
            this.destinationstack = obj.getInt("destinationstack");
            this.origincard = obj.getString("origincard");
            this.destinationcard = obj.getString("destinationcard");
            this.accuracy = obj.getDouble("accuracy");
            this.numberOfCardsMoved = obj.getInt("numberOfCardsMoved");
            this.rankError = obj.getBoolean("rankError");
            this.suitError = obj.getBoolean("suitError");
            this.score = obj.getLong("score");
            this.xCoordinate = Float.parseFloat(obj.getString("xCoordinate")); //TODO rekening houden dat dit misschien niet goed doorloopt
            this.yCoordinate = Float.parseFloat(obj.getString("yCoordinate"));
            this.betaError = obj.getBoolean("betaError");
            this.aceBetaError = obj.getBoolean("aceBetaError");
            this.kingBetaError = obj.getBoolean("kingBetaError");
            this.noAceOnSuitError = obj.getBoolean("noAceOnSuitError");
            this.noKingOnBuildStackError = obj.getBoolean("noKingOnBuildStackError");
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MoveType getType() {
        return type;
    }


    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public void setType(MoveType type) {
        this.type = type;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public int getOriginstack() {
        return originstack;
    }

    public void setOriginstack(int originstack) {
        this.originstack = originstack;
    }

    public int getDestinationstack() {
        return destinationstack;
    }

    public void setDestinationstack(int destinationstack) {
        this.destinationstack = destinationstack;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getOrigincard() {
        return origincard;
    }

    public void setOrigincard(String origincard) {
        this.origincard = origincard;
    }

    public String getDestinationcard() {
        return destinationcard;
    }

    public void setDestinationcard(String destinationcard) {
        this.destinationcard = destinationcard;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNumberOfCardsMoved() {
        return numberOfCardsMoved;
    }

    public void setNumberOfCardsMoved(int numberOfCardsMoved) {
        this.numberOfCardsMoved = numberOfCardsMoved;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }


    public boolean isBetaError() {
        return betaError;
    }

    public void setBetaError(boolean betaError) {
        this.betaError = betaError;
    }

    public boolean isSuitError() {
        return suitError;
    }

    public void setSuitError(boolean suitError) {
        this.suitError = suitError;
    }

    public boolean isRankError() {
        return rankError;
    }

    public void setRankError(boolean rankError) {
        this.rankError = rankError;
    }

    public boolean isAceBetaError() {
        return aceBetaError;
    }

    public void setAceBetaError(boolean aceBetaError) {
        this.aceBetaError = aceBetaError;
    }

    public boolean isKingBetaError() {
        return kingBetaError;
    }

    public void setKingBetaError(boolean kingBetaError) {
        this.kingBetaError = kingBetaError;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isNoAceOnSuitError() {
        return noAceOnSuitError;
    }

    public void setNoAceOnSuitError(boolean noAceOnSuitError) {
        this.noAceOnSuitError = noAceOnSuitError;
    }

    public boolean isNoKingOnBuildStackError() {
        return noKingOnBuildStackError;
    }

    public void setNoKingOnBuildStackError(boolean noKingOnBuildStackError) {
        this.noKingOnBuildStackError = noKingOnBuildStackError;
    }
}
