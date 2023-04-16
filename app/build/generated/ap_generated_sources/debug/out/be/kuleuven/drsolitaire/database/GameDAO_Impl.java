package be.kuleuven.drsolitaire.database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import be.kuleuven.drsolitaire.classes.GamePlayed;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class GameDAO_Impl implements GameDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GamePlayed> __insertionAdapterOfGamePlayed;

  private final EntityDeletionOrUpdateAdapter<GamePlayed> __deletionAdapterOfGamePlayed;

  public GameDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGamePlayed = new EntityInsertionAdapter<GamePlayed>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `GamePlayed` (`id`,`personID`,`gameTime`,`isSolved`,`gameseed`,`score`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GamePlayed value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getPersonID());
        stmt.bindLong(3, value.getGameTime());
        final int _tmp = value.isSolved() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        stmt.bindLong(5, value.getGameseed());
        stmt.bindLong(6, value.getScore());
      }
    };
    this.__deletionAdapterOfGamePlayed = new EntityDeletionOrUpdateAdapter<GamePlayed>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `GamePlayed` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, GamePlayed value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertAll(final GamePlayed... games) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGamePlayed.insert(games);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final GamePlayed game) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfGamePlayed.handle(game);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<GamePlayed> getAll() {
    final String _sql = "SELECT * FROM gamePlayed";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPersonID = CursorUtil.getColumnIndexOrThrow(_cursor, "personID");
      final int _cursorIndexOfGameTime = CursorUtil.getColumnIndexOrThrow(_cursor, "gameTime");
      final int _cursorIndexOfIsSolved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSolved");
      final int _cursorIndexOfGameseed = CursorUtil.getColumnIndexOrThrow(_cursor, "gameseed");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final List<GamePlayed> _result = new ArrayList<GamePlayed>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final GamePlayed _item;
        final int _tmpPersonID;
        _tmpPersonID = _cursor.getInt(_cursorIndexOfPersonID);
        final int _tmpGameTime;
        _tmpGameTime = _cursor.getInt(_cursorIndexOfGameTime);
        final boolean _tmpIsSolved;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSolved);
        _tmpIsSolved = _tmp != 0;
        final int _tmpGameseed;
        _tmpGameseed = _cursor.getInt(_cursorIndexOfGameseed);
        final long _tmpScore;
        _tmpScore = _cursor.getLong(_cursorIndexOfScore);
        _item = new GamePlayed(_tmpPersonID,_tmpGameTime,_tmpIsSolved,_tmpGameseed,_tmpScore);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public GamePlayed getGameById(final int id) {
    final String _sql = "SELECT * FROM gamePlayed WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPersonID = CursorUtil.getColumnIndexOrThrow(_cursor, "personID");
      final int _cursorIndexOfGameTime = CursorUtil.getColumnIndexOrThrow(_cursor, "gameTime");
      final int _cursorIndexOfIsSolved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSolved");
      final int _cursorIndexOfGameseed = CursorUtil.getColumnIndexOrThrow(_cursor, "gameseed");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final GamePlayed _result;
      if(_cursor.moveToFirst()) {
        final int _tmpPersonID;
        _tmpPersonID = _cursor.getInt(_cursorIndexOfPersonID);
        final int _tmpGameTime;
        _tmpGameTime = _cursor.getInt(_cursorIndexOfGameTime);
        final boolean _tmpIsSolved;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSolved);
        _tmpIsSolved = _tmp != 0;
        final int _tmpGameseed;
        _tmpGameseed = _cursor.getInt(_cursorIndexOfGameseed);
        final long _tmpScore;
        _tmpScore = _cursor.getLong(_cursorIndexOfScore);
        _result = new GamePlayed(_tmpPersonID,_tmpGameTime,_tmpIsSolved,_tmpGameseed,_tmpScore);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
