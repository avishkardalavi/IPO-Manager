package com.avishkar.ipomanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfitDao {

    @Insert
    void insert(ProfitEntity profit);

    @Query("SELECT * FROM ipo_profits ORDER BY year DESC")
    List<ProfitEntity> getAll();

    @Query("SELECT COUNT(*) FROM ipo_profits")
    int count();

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    @Query("""
            UPDATE ipo_profits
            SET ipoName = :newName,
                amount = :newAmount,
                type = :newType,
                bank = :newBank,
                person = :newPerson,
                date = :newDate,
                year = :newYear
            WHERE ipoName = :oldName
              AND amount = :oldAmount
              AND type = :oldType
              AND bank = :oldBank
              AND person = :oldPerson
              AND date = :oldDate
              AND year = :oldYear
            """)
    void updateProfit(
            String oldName,
            long oldAmount,
            String oldType,
            String oldBank,
            String oldPerson,
            String oldDate,
            int oldYear,

            String newName,
            long newAmount,
            String newType,
            String newBank,
            String newPerson,
            String newDate,
            int newYear
    );

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    @Query("""
            DELETE FROM ipo_profits
            WHERE ipoName = :name
              AND amount = :amount
              AND type = :type
              AND bank = :bank
              AND person = :person
              AND date = :date
              AND year = :year
            """)
    void deleteProfit(
            String name,
            long amount,
            String type,
            String bank,
            String person,
            String date,
            int year
    );
}