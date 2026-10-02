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
public final class TransferDao_Impl implements TransferDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TransferEntity> __insertAdapterOfTransferEntity;

  public TransferDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTransferEntity = new EntityInsertAdapter<TransferEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `fund_transfers` (`id`,`amount`,`fromAccount`,`toAccount`,`ipoName`,`date`,`directReceived`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TransferEntity entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.amount);
        if (entity.fromAccount == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.fromAccount);
        }
        if (entity.toAccount == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.toAccount);
        }
        if (entity.ipoName == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.ipoName);
        }
        if (entity.date == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.date);
        }
        final int _tmp = entity.directReceived ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
  }

  @Override
  public void insert(final TransferEntity transfer) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTransferEntity.insert(_connection, transfer);
      return null;
    });
  }

  @Override
  public List<TransferEntity> getAll() {
    final String _sql = "SELECT * FROM fund_transfers ORDER BY rowid DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfFromAccount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "fromAccount");
        final int _columnIndexOfToAccount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "toAccount");
        final int _columnIndexOfIpoName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ipoName");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDirectReceived = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "directReceived");
        final List<TransferEntity> _result = new ArrayList<TransferEntity>();
        while (_stmt.step()) {
          final TransferEntity _item;
          final long _tmpAmount;
          _tmpAmount = _stmt.getLong(_columnIndexOfAmount);
          final String _tmpFromAccount;
          if (_stmt.isNull(_columnIndexOfFromAccount)) {
            _tmpFromAccount = null;
          } else {
            _tmpFromAccount = _stmt.getText(_columnIndexOfFromAccount);
          }
          final String _tmpToAccount;
          if (_stmt.isNull(_columnIndexOfToAccount)) {
            _tmpToAccount = null;
          } else {
            _tmpToAccount = _stmt.getText(_columnIndexOfToAccount);
          }
          final String _tmpIpoName;
          if (_stmt.isNull(_columnIndexOfIpoName)) {
            _tmpIpoName = null;
          } else {
            _tmpIpoName = _stmt.getText(_columnIndexOfIpoName);
          }
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final boolean _tmpDirectReceived;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfDirectReceived));
          _tmpDirectReceived = _tmp != 0;
          _item = new TransferEntity(_tmpAmount,_tmpFromAccount,_tmpToAccount,_tmpIpoName,_tmpDate,_tmpDirectReceived);
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
    final String _sql = "SELECT COUNT(*) FROM fund_transfers";
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
  public void updateTransfer(final long oldAmount, final String oldFrom, final String oldTo,
      final String oldIpo, final String oldDate, final boolean oldDirect, final long newAmount,
      final String newFrom, final String newTo, final String newIpo, final String newDate,
      final boolean newDirect) {
    final String _sql = "UPDATE fund_transfers\n"
            + "SET amount = ?,\n"
            + "    fromAccount = ?,\n"
            + "    toAccount = ?,\n"
            + "    ipoName = ?,\n"
            + "    date = ?,\n"
            + "    directReceived = ?\n"
            + "WHERE amount = ?\n"
            + "  AND fromAccount = ?\n"
            + "  AND toAccount = ?\n"
            + "  AND ipoName = ?\n"
            + "  AND date = ?\n"
            + "  AND directReceived = ?\n";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newAmount);
        _argIndex = 2;
        if (newFrom == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newFrom);
        }
        _argIndex = 3;
        if (newTo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newTo);
        }
        _argIndex = 4;
        if (newIpo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newIpo);
        }
        _argIndex = 5;
        if (newDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, newDate);
        }
        _argIndex = 6;
        final int _tmp = newDirect ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, oldAmount);
        _argIndex = 8;
        if (oldFrom == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldFrom);
        }
        _argIndex = 9;
        if (oldTo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldTo);
        }
        _argIndex = 10;
        if (oldIpo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldIpo);
        }
        _argIndex = 11;
        if (oldDate == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, oldDate);
        }
        _argIndex = 12;
        final int _tmp_1 = oldDirect ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp_1);
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteTransfer(final long amount, final String from, final String to,
      final String ipo, final String date, final boolean direct) {
    final String _sql = "DELETE FROM fund_transfers\n"
            + "WHERE amount = ?\n"
            + "  AND fromAccount = ?\n"
            + "  AND toAccount = ?\n"
            + "  AND ipoName = ?\n"
            + "  AND date = ?\n"
            + "  AND directReceived = ?\n";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, amount);
        _argIndex = 2;
        if (from == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, from);
        }
        _argIndex = 3;
        if (to == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, to);
        }
        _argIndex = 4;
        if (ipo == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, ipo);
        }
        _argIndex = 5;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, date);
        }
        _argIndex = 6;
        final int _tmp = direct ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
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
