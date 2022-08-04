package com.example.kalorisayac;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.kalorisayac.dao.FoodDao;
import com.example.kalorisayac.model.Food;

@Database(entities = {Food.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_database.db";

    public abstract FoodDao foodDao();

    private static AppDatabase appDatabase;

    private static AppDatabase init(final Context context){
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME
        ).fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized AppDatabase getInstance(Context context){
        if(appDatabase == null ) appDatabase = init(context);
        return appDatabase;
    }
}
