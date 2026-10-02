package com.avishkar.ipomanager.data;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProfitDao _profitDao;

  private volatile TransferDao _transferDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "89df6cf7fd64eab150b07ef24cfd71ca", "2acba8b621f690bbcf026cf1ac147ac8") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `ipo_profits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ipoName` TEXT, `type` TEXT, `bank` TEXT, `person` TEXT, `date` TEXT, `amount` INTEGER NOT NULL, `year` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `fund_transfers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` INTEGER NOT NULL, `fromAccount` TEXT, `toAccount` TEXT, `ipoName` TEXT, `date` TEXT, `directReceived` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '89df6cf7fd64eab150b07ef24cfd71ca')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `ipo_profits`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `fund_transfers`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsIpoProfits = new HashMap<String, TableInfo.Column>(8);
        _columnsIpoProfits.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("ipoName", new TableInfo.Column("ipoName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("bank", new TableInfo.Column("bank", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("person", new TableInfo.Column("person", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIpoProfits.put("year", new TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysIpoProfits = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesIpoProfits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIpoProfits = new TableInfo("ipo_profits", _columnsIpoProfits, _foreignKeysIpoProfits, _indicesIpoProfits);
        final TableInfo _existingIpoProfits = TableInfo.read(connection, "ipo_profits");
        if (!_infoIpoProfits.equals(_existingIpoProfits)) {
          return new RoomOpenDelegate.ValidationResult(false, "ipo_profits(com.avishkar.ipomanager.data.ProfitEntity).\n"
                  + " Expected:\n" + _infoIpoProfits + "\n"
                  + " Found:\n" + _existingIpoProfits);
        }
        final Map<String, TableInfo.Column> _columnsFundTransfers = new HashMap<String, TableInfo.Column>(7);
        _columnsFundTransfers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("fromAccount", new TableInfo.Column("fromAccount", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("toAccount", new TableInfo.Column("toAccount", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("ipoName", new TableInfo.Column("ipoName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFundTransfers.put("directReceived", new TableInfo.Column("directReceived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysFundTransfers = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesFundTransfers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFundTransfers = new TableInfo("fund_transfers", _columnsFundTransfers, _foreignKeysFundTransfers, _indicesFundTransfers);
        final TableInfo _existingFundTransfers = TableInfo.read(connection, "fund_transfers");
        if (!_infoFundTransfers.equals(_existingFundTransfers)) {
          return new RoomOpenDelegate.ValidationResult(false, "fund_transfers(com.avishkar.ipomanager.data.TransferEntity).\n"
                  + " Expected:\n" + _infoFundTransfers + "\n"
                  + " Found:\n" + _existingFundTransfers);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ipo_profits", "fund_transfers");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "ipo_profits", "fund_transfers");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProfitDao.class, ProfitDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransferDao.class, TransferDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProfitDao profitDao() {
    if (_profitDao != null) {
      return _profitDao;
    } else {
      synchronized(this) {
        if(_profitDao == null) {
          _profitDao = new ProfitDao_Impl(this);
        }
        return _profitDao;
      }
    }
  }

  @Override
  public TransferDao transferDao() {
    if (_transferDao != null) {
      return _transferDao;
    } else {
      synchronized(this) {
        if(_transferDao == null) {
          _transferDao = new TransferDao_Impl(this);
        }
        return _transferDao;
      }
    }
  }
}
