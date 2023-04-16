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

import be.kuleuven.drsolitaire.classes.Request;

//@GN
@Dao
public interface RequestDAO {
    @Query("SELECT * FROM request")
    List<Request> getAll();

    @Query("SELECT * FROM request WHERE number = :number LIMIT 1")
    Request getRequestByNumber(int number);

    @Insert
    void insertAll(Request... requests);

    @Delete
    void delete(Request request);
}