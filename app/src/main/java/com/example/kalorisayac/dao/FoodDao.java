package com.example.kalorisayac.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kalorisayac.model.Food;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert(entity = Food.class, onConflict = REPLACE)
    void insert(Food food);

    @Delete(entity = Food.class)
    void delete(Food food);

    @Query("SELECT * FROM foods WHERE user_id = :uid AND date = :date")
    List<Food> loadByUidAndDate(String uid, String date);

    @Query("SELECT * FROM foods WHERE user_id = :uid")
    List<Food> loadByUid(String uid);

}
