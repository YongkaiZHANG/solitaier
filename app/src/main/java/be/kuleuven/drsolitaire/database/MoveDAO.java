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

import be.kuleuven.drsolitaire.classes.Move;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

//@GN
@Dao
public interface MoveDAO {
    @Query("SELECT * FROM move")
    List<Move> getAll();

    @Query("SELECT * FROM move WHERE gameId LIKE :id")
    List<Move> loadAllById(int id);
    //this is just for test
    @Query("select gameId from Move where Id=531415")
    List<Integer> getIdOfmove();
    //
    @Insert
    void insertAll(Move... moves);

    @Delete
    void delete(Move move);

}
