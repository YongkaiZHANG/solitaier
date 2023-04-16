package be.kuleuven.drsolitaire.database;

//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import be.kuleuven.drsolitaire.classes.GamePlayed;
import be.kuleuven.drsolitaire.classes.Move;

// @GN
@Database(entities = {GamePlayed.class, Move.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "www_drsolitaire")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


    public abstract MoveDAO moveDAO();
    public abstract GameDAO gameDAO();
}
