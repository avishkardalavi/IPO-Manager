package com.avishkar.ipomanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ipo_profits")
public class ProfitEntity {
    @PrimaryKey(autoGenerate = true) public long id;
    public String ipoName, type, bank, person, date;
    public long amount;
    public int year;
    public ProfitEntity(String ipoName,long amount,String type,String bank,String person,String date,int year){
        this.ipoName=ipoName; this.amount=amount; this.type=type; this.bank=bank; this.person=person; this.date=date; this.year=year;
    }
}
