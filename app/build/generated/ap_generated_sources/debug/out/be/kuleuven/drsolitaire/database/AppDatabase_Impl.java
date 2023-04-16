package be.kuleuven.drsolitaire.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MoveDAO _moveDAO;

  private volatile GameDAO _gameDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `GamePlayed` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `personID` INTEGER NOT NULL, `gameTime` INTEGER NOT NULL, `isSolved` INTEGER NOT NULL, `gameseed` INTEGER NOT NULL, `score` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Move` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `gameId` INTEGER NOT NULL, `type` TEXT, `timestamp` INTEGER NOT NULL, `time` INTEGER NOT NULL, `accuracy` REAL NOT NULL, `originStack` INTEGER NOT NULL, `destinationStack` INTEGER NOT NULL, `originCard` TEXT, `destinationCard` TEXT, `numberOfCardsMoved` INTEGER NOT NULL, `score` INTEGER NOT NULL, `xCoordinate` REAL NOT NULL, `yCoordinate` REAL NOT NULL, `betaError` INTEGER NOT NULL, `suitError` INTEGER NOT NULL, `rankError` INTEGER NOT NULL, `aceBetaError` INTEGER NOT NULL, `kingBetaError` INTEGER NOT NULL, `noAceOnSuitError` INTEGER NOT NULL, `noKingOnBuildStackError` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '262a871ff001e17501d667a7ee72bbab')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `GamePlayed`");
        _db.execSQL("DROP TABLE IF EXISTS `Move`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsGamePlayed = new HashMap<String, TableInfo.Column>(6);
        _columnsGamePlayed.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGamePlayed.put("personID", new TableInfo.Column("personID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGamePlayed.put("gameTime", new TableInfo.Column("gameTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGamePlayed.put("isSolved", new TableInfo.Column("isSolved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGamePlayed.put("gameseed", new TableInfo.Column("gameseed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGamePlayed.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGamePlayed = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGamePlayed = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGamePlayed = new TableInfo("GamePlayed", _columnsGamePlayed, _foreignKeysGamePlayed, _indicesGamePlayed);
        final TableInfo _existingGamePlayed = TableInfo.read(_db, "GamePlayed");
        if (! _infoGamePlayed.equals(_existingGamePlayed)) {
          return new RoomOpenHelper.ValidationResult(false, "GamePlayed(be.kuleuven.drsolitaire.classes.GamePlayed).\n"
                  + " Expected:\n" + _infoGamePlayed + "\n"
                  + " Found:\n" + _existingGamePlayed);
        }
        final HashMap<String, TableInfo.Column> _columnsMove = new HashMap<String, TableInfo.Column>(21);
        _columnsMove.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("gameId", new TableInfo.Column("gameId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("time", new TableInfo.Column("time", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("accuracy", new TableInfo.Column("accuracy", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("originStack", new TableInfo.Column("originStack", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("destinationStack", new TableInfo.Column("destinationStack", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("originCard", new TableInfo.Column("originCard", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("destinationCard", new TableInfo.Column("destinationCard", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("numberOfCardsMoved", new TableInfo.Column("numberOfCardsMoved", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("xCoordinate", new TableInfo.Column("xCoordinate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("yCoordinate", new TableInfo.Column("yCoordinate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("betaError", new TableInfo.Column("betaError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("suitError", new TableInfo.Column("suitError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("rankError", new TableInfo.Column("rankError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("aceBetaError", new TableInfo.Column("aceBetaError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("kingBetaError", new TableInfo.Column("kingBetaError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("noAceOnSuitError", new TableInfo.Column("noAceOnSuitError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMove.put("noKingOnBuildStackError", new TableInfo.Column("noKingOnBuildStackError", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMove = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMove = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMove = new TableInfo("Move", _columnsMove, _foreignKeysMove, _indicesMove);
        final TableInfo _existingMove = TableInfo.read(_db, "Move");
        if (! _infoMove.equals(_existingMove)) {
          return new RoomOpenHelper.ValidationResult(false, "Move(be.kuleuven.drsolitaire.classes.Move).\n"
                  + " Expected:\n" + _infoMove + "\n"
                  + " Found:\n" + _existingMove);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "262a871ff001e17501d667a7ee72bbab", "0907bc2d4b3b9041997063aebe95cd32");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "GamePlayed","Move");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `GamePlayed`");
      _db.execSQL("DELETE FROM `Move`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MoveDAO.class, MoveDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(GameDAO.class, GameDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public MoveDAO moveDAO() {
    if (_moveDAO != null) {
      return _moveDAO;
    } else {
      synchronized(this) {
        if(_moveDAO == null) {
          _moveDAO = new MoveDAO_Impl(this);
        }
        return _moveDAO;
      }
    }
  }

  @Override
  public GameDAO gameDAO() {
    if (_gameDAO != null) {
      return _gameDAO;
    } else {
      synchronized(this) {
        if(_gameDAO == null) {
          _gameDAO = new GameDAO_Impl(this);
        }
        return _gameDAO;
      }
    }
  }
}
