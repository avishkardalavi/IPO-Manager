package com.avishkar.ipomanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fund_transfers")
public class TransferEntity {
    @PrimaryKey(autoGenerate = true) public long id;
    public long amount;
    public String fromAccount, toAccount, ipoName, date;
    public boolean directReceived;
    public TransferEntity(long amount,String fromAccount,String toAccount,String ipoName,String date,boolean directReceived){
        this.amount=amount; this.fromAccount=fromAccount; this.toAccount=toAccount; this.ipoName=ipoName; this.date=date; this.directReceived=directReceived;
    }
}
