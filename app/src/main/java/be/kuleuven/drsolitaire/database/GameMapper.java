package be.kuleuven.drsolitaire.database;

//import android.support.annotation.NonNull;
import android.util.Log;
import androidx.annotation.NonNull;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.drsolitaire.SharedData;
import be.kuleuven.drsolitaire.classes.GamePlayed;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.Request;

/**
 * Singleton to map Person entities to-and-from the database.
 * @author Koen Pelsmaekers
 */

// @GN
public enum GameMapper {
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
     * Store a game in the database
     *
     * @param game The Person object that needs to be stored
     */
    public void createGame(GamePlayed game, List<Move> moves) {
        Log.d("DB_GAME","---------------- CREATE GAME SEQUENCE ----------------");
        queryGame(game, moves, getURL());
    }

    @NonNull
    private String getURL() {
        return "https://iiw.kuleuven.be/onderzoek/drSolitaire/insertGame.php";
    }

    private void queryGame(GamePlayed obj, List<Move> moves, String url) {
        List<Move> newList = new ArrayList<Move>(moves);
        Log.d("DB_GAME",url);
        StringRequest json = new StringRequest(com.android.volley.Request.Method.POST,url, response -> {
            Log.d("DB_GAME", "GameResponse: "+response);
            JSONArray JSONResponse = null;
            try {
                JSONResponse = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GamePlayed dbGame = (GamePlayed) eMapper.mapToEntity(JSONResponse, obj);
            for (Move m: newList) m.setGameid(dbGame.getId());
            eMapper.getmMapper().createMoves(newList); //TODO INFO Hierna worden moves gefinalised: want we hebben id.

            // HANDLE OLD GAMES
            if (eMapper.getStoredGames() != 0) handleOldGames(eMapper.getStoredGames());
        }
                , error -> {
            eMapper.errorHappened();
            if (error instanceof NetworkError || error instanceof TimeoutError) {
                Log.d("DB_GAME","No connection available... so I pushed this game locally!");
                eMapper.newStoredGame(); //increase number of stored games
                Log.d("DB_GAME", "Stored Games = "+eMapper.getStoredGames());
                //Request request = new Request();
                //request.setUrl(url);
                //request.setNumber(eMapper.getStoredGames());
                obj.setId(eMapper.getStoredGames());
                SharedData.getAppDatabase().gameDAO().insertAll(obj);
                for (Move m: newList) m.setGameid(eMapper.getStoredGames());
                eMapper.getmMapper().createMoves(newList);
            }
            else Log.d("DB_GAME","Some error in queryGame occurs! "+ error.getMessage() + " "+error.getLocalizedMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("personID", String.valueOf(obj.getPersonID()));
                params.put("gameTime", String.valueOf(obj.getGameTime()));
                params.put("isSolved", String.valueOf((obj.isSolved() ? 1 : 0)));
                params.put("gameseed", String.valueOf(obj.getGameseed()));
                params.put("score", String.valueOf(obj.getScore()));
                return params;
            }
        };
        eMapper.getRequestQueue().add(json);
    }

    private void handleOldGames(int storedGames) {
        List<Move> oldMoves = SharedData.getAppDatabase().moveDAO().loadAllById(storedGames);
        if (!oldMoves.isEmpty()) {
            Log.d("DB_MOVE","Old Moves are: "+oldMoves.toString());
            for (Move m : oldMoves) SharedData.getAppDatabase().moveDAO().delete(m);
        }
        else Log.d("DB_MOVE", "Error in handleOldGames: oldMoves is empty!");

        GamePlayed game = SharedData.getAppDatabase().gameDAO().getGameById(storedGames);
        if (game != null) {
            SharedData.getAppDatabase().gameDAO().delete(game);
            queryGame(game, oldMoves, getURL());
            eMapper.handledStoredGame();
            Log.d("DB_GAME", "Games to store = "+eMapper.getStoredGames());

        }
        else Log.d("DB_GAME", "Error in handleOldGames: Request = null!");

    }


}