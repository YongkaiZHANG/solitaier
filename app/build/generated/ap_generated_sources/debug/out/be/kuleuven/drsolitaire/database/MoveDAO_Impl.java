package be.kuleuven.drsolitaire.database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import be.kuleuven.drsolitaire.classes.Move;
import be.kuleuven.drsolitaire.classes.MoveType;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MoveDAO_Impl implements MoveDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Move> __insertionAdapterOfMove;

  private final EntityDeletionOrUpdateAdapter<Move> __deletionAdapterOfMove;

  public MoveDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMove = new EntityInsertionAdapter<Move>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Move` (`id`,`gameId`,`type`,`timestamp`,`time`,`accuracy`,`originStack`,`destinationStack`,`originCard`,`destinationCard`,`numberOfCardsMoved`,`score`,`xCoordinate`,`yCoordinate`,`betaError`,`suitError`,`rankError`,`aceBetaError`,`kingBetaError`,`noAceOnSuitError`,`noKingOnBuildStackError`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Move value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getGameid());
        final String _tmp = MoveType.getMoveTypeString(value.getType());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        stmt.bindLong(4, value.getTimestamp());
        stmt.bindLong(5, value.getTime());
        stmt.bindDouble(6, value.getAccuracy());
        stmt.bindLong(7, value.getOriginstack());
        stmt.bindLong(8, value.getDestinationstack());
        if (value.getOrigincard() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getOrigincard());
        }
        if (value.getDestinationcard() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getDestinationcard());
        }
        stmt.bindLong(11, value.getNumberOfCardsMoved());
        stmt.bindLong(12, value.getScore());
        stmt.bindDouble(13, value.getXCoordinate());
        stmt.bindDouble(14, value.getYCoordinate());
        final int _tmp_1 = value.isBetaError() ? 1 : 0;
        stmt.bindLong(15, _tmp_1);
        final int _tmp_2 = value.isSuitError() ? 1 : 0;
        stmt.bindLong(16, _tmp_2);
        final int _tmp_3 = value.isRankError() ? 1 : 0;
        stmt.bindLong(17, _tmp_3);
        final int _tmp_4 = value.isAceBetaError() ? 1 : 0;
        stmt.bindLong(18, _tmp_4);
        final int _tmp_5 = value.isKingBetaError() ? 1 : 0;
        stmt.bindLong(19, _tmp_5);
        final int _tmp_6 = value.isNoAceOnSuitError() ? 1 : 0;
        stmt.bindLong(20, _tmp_6);
        final int _tmp_7 = value.isNoKingOnBuildStackError() ? 1 : 0;
        stmt.bindLong(21, _tmp_7);
      }
    };
    this.__deletionAdapterOfMove = new EntityDeletionOrUpdateAdapter<Move>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Move` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Move value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertAll(final Move... moves) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMove.insert(moves);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Move move) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMove.handle(move);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Move> getAll() {
    final String _sql = "SELECT * FROM move";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGameid = CursorUtil.getColumnIndexOrThrow(_cursor, "gameId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfOriginstack = CursorUtil.getColumnIndexOrThrow(_cursor, "originStack");
      final int _cursorIndexOfDestinationstack = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationStack");
      final int _cursorIndexOfOrigincard = CursorUtil.getColumnIndexOrThrow(_cursor, "originCard");
      final int _cursorIndexOfDestinationcard = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCard");
      final int _cursorIndexOfNumberOfCardsMoved = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfCardsMoved");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final int _cursorIndexOfXCoordinate = CursorUtil.getColumnIndexOrThrow(_cursor, "xCoordinate");
      final int _cursorIndexOfYCoordinate = CursorUtil.getColumnIndexOrThrow(_cursor, "yCoordinate");
      final int _cursorIndexOfBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "betaError");
      final int _cursorIndexOfSuitError = CursorUtil.getColumnIndexOrThrow(_cursor, "suitError");
      final int _cursorIndexOfRankError = CursorUtil.getColumnIndexOrThrow(_cursor, "rankError");
      final int _cursorIndexOfAceBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "aceBetaError");
      final int _cursorIndexOfKingBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "kingBetaError");
      final int _cursorIndexOfNoAceOnSuitError = CursorUtil.getColumnIndexOrThrow(_cursor, "noAceOnSuitError");
      final int _cursorIndexOfNoKingOnBuildStackError = CursorUtil.getColumnIndexOrThrow(_cursor, "noKingOnBuildStackError");
      final List<Move> _result = new ArrayList<Move>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Move _item;
        final MoveType _tmpType;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfType);
        }
        _tmpType = MoveType.getMoveType(_tmp);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final long _tmpTime;
        _tmpTime = _cursor.getLong(_cursorIndexOfTime);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        final int _tmpOriginstack;
        _tmpOriginstack = _cursor.getInt(_cursorIndexOfOriginstack);
        final int _tmpDestinationstack;
        _tmpDestinationstack = _cursor.getInt(_cursorIndexOfDestinationstack);
        final String _tmpOrigincard;
        if (_cursor.isNull(_cursorIndexOfOrigincard)) {
          _tmpOrigincard = null;
        } else {
          _tmpOrigincard = _cursor.getString(_cursorIndexOfOrigincard);
        }
        final String _tmpDestinationcard;
        if (_cursor.isNull(_cursorIndexOfDestinationcard)) {
          _tmpDestinationcard = null;
        } else {
          _tmpDestinationcard = _cursor.getString(_cursorIndexOfDestinationcard);
        }
        final int _tmpNumberOfCardsMoved;
        _tmpNumberOfCardsMoved = _cursor.getInt(_cursorIndexOfNumberOfCardsMoved);
        final long _tmpScore;
        _tmpScore = _cursor.getLong(_cursorIndexOfScore);
        final float _tmpXCoordinate;
        _tmpXCoordinate = _cursor.getFloat(_cursorIndexOfXCoordinate);
        final float _tmpYCoordinate;
        _tmpYCoordinate = _cursor.getFloat(_cursorIndexOfYCoordinate);
        final boolean _tmpBetaError;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfBetaError);
        _tmpBetaError = _tmp_1 != 0;
        final boolean _tmpSuitError;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSuitError);
        _tmpSuitError = _tmp_2 != 0;
        final boolean _tmpRankError;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfRankError);
        _tmpRankError = _tmp_3 != 0;
        final boolean _tmpAceBetaError;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfAceBetaError);
        _tmpAceBetaError = _tmp_4 != 0;
        final boolean _tmpKingBetaError;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfKingBetaError);
        _tmpKingBetaError = _tmp_5 != 0;
        final boolean _tmpNoAceOnSuitError;
        final int _tmp_6;
        _tmp_6 = _cursor.getInt(_cursorIndexOfNoAceOnSuitError);
        _tmpNoAceOnSuitError = _tmp_6 != 0;
        final boolean _tmpNoKingOnBuildStackError;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfNoKingOnBuildStackError);
        _tmpNoKingOnBuildStackError = _tmp_7 != 0;
        _item = new Move(_tmpType,_tmpTimestamp,_tmpTime,_tmpOriginstack,_tmpDestinationstack,_tmpOrigincard,_tmpDestinationcard,_tmpAccuracy,_tmpNumberOfCardsMoved,_tmpSuitError,_tmpRankError,_tmpScore,_tmpXCoordinate,_tmpYCoordinate,_tmpBetaError,_tmpAceBetaError,_tmpKingBetaError,_tmpNoAceOnSuitError,_tmpNoKingOnBuildStackError);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpGameid;
        _tmpGameid = _cursor.getInt(_cursorIndexOfGameid);
        _item.setGameid(_tmpGameid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Move> loadAllById(final int id) {
    final String _sql = "SELECT * FROM move WHERE gameId LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfGameid = CursorUtil.getColumnIndexOrThrow(_cursor, "gameId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final int _cursorIndexOfAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "accuracy");
      final int _cursorIndexOfOriginstack = CursorUtil.getColumnIndexOrThrow(_cursor, "originStack");
      final int _cursorIndexOfDestinationstack = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationStack");
      final int _cursorIndexOfOrigincard = CursorUtil.getColumnIndexOrThrow(_cursor, "originCard");
      final int _cursorIndexOfDestinationcard = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationCard");
      final int _cursorIndexOfNumberOfCardsMoved = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfCardsMoved");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final int _cursorIndexOfXCoordinate = CursorUtil.getColumnIndexOrThrow(_cursor, "xCoordinate");
      final int _cursorIndexOfYCoordinate = CursorUtil.getColumnIndexOrThrow(_cursor, "yCoordinate");
      final int _cursorIndexOfBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "betaError");
      final int _cursorIndexOfSuitError = CursorUtil.getColumnIndexOrThrow(_cursor, "suitError");
      final int _cursorIndexOfRankError = CursorUtil.getColumnIndexOrThrow(_cursor, "rankError");
      final int _cursorIndexOfAceBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "aceBetaError");
      final int _cursorIndexOfKingBetaError = CursorUtil.getColumnIndexOrThrow(_cursor, "kingBetaError");
      final int _cursorIndexOfNoAceOnSuitError = CursorUtil.getColumnIndexOrThrow(_cursor, "noAceOnSuitError");
      final int _cursorIndexOfNoKingOnBuildStackError = CursorUtil.getColumnIndexOrThrow(_cursor, "noKingOnBuildStackError");
      final List<Move> _result = new ArrayList<Move>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Move _item;
        final MoveType _tmpType;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfType);
        }
        _tmpType = MoveType.getMoveType(_tmp);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final long _tmpTime;
        _tmpTime = _cursor.getLong(_cursorIndexOfTime);
        final double _tmpAccuracy;
        _tmpAccuracy = _cursor.getDouble(_cursorIndexOfAccuracy);
        final int _tmpOriginstack;
        _tmpOriginstack = _cursor.getInt(_cursorIndexOfOriginstack);
        final int _tmpDestinationstack;
        _tmpDestinationstack = _cursor.getInt(_cursorIndexOfDestinationstack);
        final String _tmpOrigincard;
        if (_cursor.isNull(_cursorIndexOfOrigincard)) {
          _tmpOrigincard = null;
        } else {
          _tmpOrigincard = _cursor.getString(_cursorIndexOfOrigincard);
        }
        final String _tmpDestinationcard;
        if (_cursor.isNull(_cursorIndexOfDestinationcard)) {
          _tmpDestinationcard = null;
        } else {
          _tmpDestinationcard = _cursor.getString(_cursorIndexOfDestinationcard);
        }
        final int _tmpNumberOfCardsMoved;
        _tmpNumberOfCardsMoved = _cursor.getInt(_cursorIndexOfNumberOfCardsMoved);
        final long _tmpScore;
        _tmpScore = _cursor.getLong(_cursorIndexOfScore);
        final float _tmpXCoordinate;
        _tmpXCoordinate = _cursor.getFloat(_cursorIndexOfXCoordinate);
        final float _tmpYCoordinate;
        _tmpYCoordinate = _cursor.getFloat(_cursorIndexOfYCoordinate);
        final boolean _tmpBetaError;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfBetaError);
        _tmpBetaError = _tmp_1 != 0;
        final boolean _tmpSuitError;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSuitError);
        _tmpSuitError = _tmp_2 != 0;
        final boolean _tmpRankError;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfRankError);
        _tmpRankError = _tmp_3 != 0;
        final boolean _tmpAceBetaError;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfAceBetaError);
        _tmpAceBetaError = _tmp_4 != 0;
        final boolean _tmpKingBetaError;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfKingBetaError);
        _tmpKingBetaError = _tmp_5 != 0;
        final boolean _tmpNoAceOnSuitError;
        final int _tmp_6;
        _tmp_6 = _cursor.getInt(_cursorIndexOfNoAceOnSuitError);
        _tmpNoAceOnSuitError = _tmp_6 != 0;
        final boolean _tmpNoKingOnBuildStackError;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfNoKingOnBuildStackError);
        _tmpNoKingOnBuildStackError = _tmp_7 != 0;
        _item = new Move(_tmpType,_tmpTimestamp,_tmpTime,_tmpOriginstack,_tmpDestinationstack,_tmpOrigincard,_tmpDestinationcard,_tmpAccuracy,_tmpNumberOfCardsMoved,_tmpSuitError,_tmpRankError,_tmpScore,_tmpXCoordinate,_tmpYCoordinate,_tmpBetaError,_tmpAceBetaError,_tmpKingBetaError,_tmpNoAceOnSuitError,_tmpNoKingOnBuildStackError);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpGameid;
        _tmpGameid = _cursor.getInt(_cursorIndexOfGameid);
        _item.setGameid(_tmpGameid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Integer> getIdOfmove() {
    final String _sql = "select gameId from Move where Id=531415";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Integer _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getInt(0);
        }
        _result.add(_item);
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
