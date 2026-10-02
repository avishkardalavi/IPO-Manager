package com.avishkar.ipomanager.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ProfitDao_Impl implements ProfitDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ProfitEntity> __insertAdapterOfProfitEntity;

  public ProfitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfProfitEntity = new EntityInsertAdapter<ProfitEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `ipo_profits` (`id`,`ipoName`,`type`,`bank`,`person`,`date`,`amount`,`year`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ProfitEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.ipoName == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.ipoName);
        }
        if (entity.type == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.type);
        }
        if (entity.bank == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.bank);
        }
        if (entity.person == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.person);
        }
        if (entity.date == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.date);
        }
        statement.bindLong(7, entity.amount);
        statement.bindLong(8, entity.year);
      }
    };
  }

  @Override
  public void insert(final ProfitEntity profit) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfProfitEntity.insert(_connection, profit);
      return null;
    });
  }

  @Override
  public List<ProfitEntity> getAll() {
    final String _sql = "SELECT * FROM ipo_profits ORDER BY year DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfIpoName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ipoName");
        final int _columnIndexOfType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "type");
        final int _columnIndexOfBank = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "bank");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfYear = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "year");
        final List<ProfitEntity> _result = new ArrayList<ProfitEntity>();
        while (_stmt.step()) {
          final ProfitEntity _item;
          final String _tmpIpoName;
          if (_stmt.isNull(_columnIndexOfIpoName)) {
            _tmpIpoName = null;
          } else {
            _tmpIpoName = _stmt.getText(_columnIndexOfIpoName);
          }
          final String _tmpType;
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmpType = null;
          } else {
            _tmpType = _stmt.getText(_columnIndexOfType);
          }
          final String _tmpBank;
          if (_stmt.isNull(_columnIndexOfBank)) {
            _tmpBank = null;
          } else {
            _tmpBank = _stmt.getText(_columnIndexOfBank);
          }
          final String _tmpPerson;
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _tmpPerson = null;
          } else {
            _tmpPerson = _stmt.getText(_columnIndexOfPerson);
          }
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final long _tmpAmount;
          _tmpAmount = _stmt.getLong(_columnIndexOfAmount);
          final int _tmpYear;
          _tmpYear = (int) (_stmt.getLong(_columnIndexOfYear));
          _item = new ProfitEntity(_tmpIpoName,_tmpAmount,_tmpType,_tmpBank,_tmpPerson,_tmpDate,_tmpYear);
          _item.id = _stmt.getLong(_columnIndexOfId);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public int count() {
    final String _sql = "SELECT COUNT(*) FROM ipo_profits";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _result;
        if (_stmt.step()) {
          _result = (int) (_stmt.getLong(0));
        } else {
          _result = 0;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void updateProfit(final String oldName, final long oldAmount, final String oldType,
      final String oldBank, final String oldPerson, final String oldDate, final int oldYear,
      final String newName, final long newAmount, final String newType, final String newBank,
      final String newPerson, final String newDate, final int newYear) {
    final String _sql = "UPDATE ipo_profits\n"
            + "SET ipoName = ?,\n"
            + "    amount = ?,\n"
            + "    type = ?,\n"
            + "    bank = ?,\n"
            + "    person = ?,\n"
            + "    date = ?,\n"
            + "    year = ?\n"
            + "WHERE ipoName = ?\n"
            + "  AND amount = ?\n"
            + "  AND type = ?\n"
            + "  AND bank = ?\n"
            + "  AND person = ?\n"
            + "  AND date = ?\n"
            + "  AND year = ?\n";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (newName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newName);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, newAmount);
        _argIndex = 3;
        if (newType == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newType);
        }
        _argIndex = 4;
        if (newBank == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newBank);
        }
        _argIndex = 5;
        if (newPerson == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newPerson);
        }
        _argIndex = 6;
        if (newDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newDate);
        }
        _argIndex = 7;
        _stmt.bindLong(_argIndex, newYear);
        _argIndex = 8;
        if (oldName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldName);
        }
        _argIndex = 9;
        _stmt.bindLong(_argIndex, oldAmount);
        _argIndex = 10;
        if (oldType == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldType);
        }
        _argIndex = 11;
        if (oldBank == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldBank);
        }
        _argIndex = 12;
        if (oldPerson == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldPerson);
        }
        _argIndex = 13;
        if (oldDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldDate);
        }
        _argIndex = 14;
        _stmt.bindLong(_argIndex, oldYear);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteProfit(final String name, final long amount, final String type,
      final String bank, final String person, final String date, final int year) {
    final String _sql = "DELETE FROM ipo_profits\n"
            + "WHERE ipoName = ?\n"
            + "  AND amount = ?\n"
            + "  AND type = ?\n"
            + "  AND bank = ?\n"
            + "  AND person = ?\n"
            + "  AND date = ?\n"
            + "  AND year = ?\n";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, name);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, amount);
        _argIndex = 3;
        if (type == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, type);
        }
        _argIndex = 4;
        if (bank == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, bank);
        }
        _argIndex = 5;
        if (person == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, person);
        }
        _argIndex = 6;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, date);
        }
        _argIndex = 7;
        _stmt.bindLong(_argIndex, year);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
