package com.avishkar.ipomanager.data;
import android.content.Context;
import androidx.room.*;
@Database(entities={ProfitEntity.class,TransferEntity.class},version=1,exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfitDao profitDao();
    public abstract TransferDao transferDao();
    private static volatile AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context c){
        if(INSTANCE==null) synchronized(AppDatabase.class){
            if(INSTANCE==null) INSTANCE=Room.databaseBuilder(c.getApplicationContext(),AppDatabase.class,"ipo_manager.db").build();
        }
        return INSTANCE;
    }
}
