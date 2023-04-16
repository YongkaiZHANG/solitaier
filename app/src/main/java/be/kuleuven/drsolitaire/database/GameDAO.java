package be.kuleuven.drsolitaire.database;

//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import be.kuleuven.drsolitaire.classes.GamePlayed;

//@GN
@Dao
public interface GameDAO {
    @Query("SELECT * FROM gamePlayed")
    List<GamePlayed> getAll();

    @Query("SELECT * FROM gamePlayed WHERE id = :id LIMIT 1")
    GamePlayed getGameById(int id);

    @Insert
    void insertAll(GamePlayed... games);

    @Delete
    void delete(GamePlayed game);
}