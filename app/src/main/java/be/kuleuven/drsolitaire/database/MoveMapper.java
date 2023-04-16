package be.kuleuven.drsolitaire.database;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.Move;

/**
 * Singleton to map Move entities to-and-from the database.
 */

public enum MoveMapper {
    UNIQUEMAPPER;

    private EntityMapper eMapper;

    /**
     * Use only one entity mapper for all mappers
     *
     * @param entityMapper Applying the singleton design
     */
    public void setEntityMapper(EntityMapper entityMapper) {
        this.eMapper = entityMapper;
    }

    /**
     * Store a move in the DB
     *
     * @param moves The Move object that needs to be stored
     */
    public void createMoves(List<Move> moves) {
        Log.d("DB_MOVE","---------------- CREATE MOVES SEQUENCE ----------------");
        Log.d("DB_MOVE","NOW INSERTING "+moves.size()+" MOVES");
        String insertMove = "https://iiw.kuleuven.be/onderzoek/drSolitaire/insertMove.php";
        queryMoves(moves, insertMove);
    }

    private void queryMoves(final List<Move> moves, String url) {
        Log.d("DB_MOVE", url);
        StringRequest sr = new StringRequest(com.android.volley.Request.Method.POST, url, response -> {
            Log.d("DB_MOVE", "Response: " + response.toString());
            JSONArray JSONResponse = null;
            try {
                JSONResponse = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            eMapper.mapToEntities(JSONResponse, new Move());
            if (eMapper.getStoredGames() == 0)
                Log.d("DB_MOVE", "---------------- END SEQUENCES ------------------------");
        }, error -> {
            eMapper.errorHappened();
            if (error instanceof NetworkError || error instanceof TimeoutError) {
                Log.d("DB_MOVE","No connection available...  so I pushed these moves locally!");
                for (Move m : moves) {
                    m.setGameid(eMapper.getStoredGames());
                    SharedData.getAppDatabase().moveDAO().insertAll(m);
                }
                Log.d("DB_MOVE", "Stored "+ moves.size() + " moves");
            }
            else {Log.d("DB","Some error in queryGame occurs! "+ error.getMessage() + " "+error.getLocalizedMessage());}
            Log.d("DB_MOVE", "---------------- END SEQUENCES ------------------------");
        })
        {
            @Override
            protected Map<String,String> getParams() { //DIT MAPPED ALS IN EEN JSON ARRAY EN VOEGT HET TOE
                Map<String,String> params = new HashMap<>();
                JSONArray array = new JSONArray();
                for(Move m : moves) { JSONObject obj = new JSONObject();
                    try {
                        obj.put("type",m.getType().toString()); //TODO INFO ZIT HIER: alles wordt juist doorgestuurd
                        obj.put("timestamp",m.getTimestamp());
                        obj.put("time",m.getTime());
                        obj.put("gameid",m.getGameid());
                        obj.put("originstack", m.getOriginstack());
                        obj.put("destinationstack", m.getDestinationstack());
                        obj.put("origincard", m.getOrigincard());
                        obj.put("destinationcard", m.getDestinationcard());
                        obj.put("accuracy", m.getAccuracy());
                        obj.put("numberOfCardsMoved", m.getNumberOfCardsMoved());
                        obj.put("rankError", m.isRankError());
                        obj.put("suitError", m.isSuitError());
                        obj.put("score", m.getScore());
                        obj.put("xCoordinate", m.getXCoordinate());
                        obj.put("yCoordinate", m.getYCoordinate());
                        obj.put("betaError", m.isBetaError());
                        obj.put("aceBetaError", m.isAceBetaError());
                        obj.put("kingBetaError", m.isKingBetaError());
                        obj.put("noAceOnSuitError", m.isNoAceOnSuitError());
                        obj.put("noKingOnBuildStackError", m.isNoKingOnBuildStackError());
                        array.put(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONERROR", e.getMessage());
                    }
                }
                params.put("moves",array.toString());
                return params;
            }
        };
        eMapper.getRequestQueue().add(sr);
    }
}