package com.avishkar.ipomanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransferDao {

    @Insert
    void insert(TransferEntity transfer);

    @Query("SELECT * FROM fund_transfers ORDER BY rowid DESC")
    List<TransferEntity> getAll();

    @Query("SELECT COUNT(*) FROM fund_transfers")
    int count();

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Query("""
            UPDATE fund_transfers
            SET amount = :newAmount,
                fromAccount = :newFrom,
                toAccount = :newTo,
                ipoName = :newIpo,
                date = :newDate,
                directReceived = :newDirect
            WHERE amount = :oldAmount
              AND fromAccount = :oldFrom
              AND toAccount = :oldTo
              AND ipoName = :oldIpo
              AND date = :oldDate
              AND directReceived = :oldDirect
            """)
    void updateTransfer(
            long oldAmount,
            String oldFrom,
            String oldTo,
            String oldIpo,
            String oldDate,
            boolean oldDirect,

            long newAmount,
            String newFrom,
            String newTo,
            String newIpo,
            String newDate,
            boolean newDirect
    );

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Query("""
            DELETE FROM fund_transfers
            WHERE amount = :amount
              AND fromAccount = :from
              AND toAccount = :to
              AND ipoName = :ipo
              AND date = :date
              AND directReceived = :direct
            """)
    void deleteTransfer(
            long amount,
            String from,
            String to,
            String ipo,
            String date,
            boolean direct
    );
}