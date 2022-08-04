package com.example.kalorisayac.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.kalorisayac.model.Food;
import com.example.kalorisayac.repository.FoodRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private FoodRepository foodRepository;
    private MutableLiveData<List<Food>> foodsByUidSearchResult;
    private MutableLiveData<List<Food>> foodByUidAndDateSearchResult;
    public MainViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
        foodsByUidSearchResult = foodRepository.getFoodsByUidSearchResult();
        foodByUidAndDateSearchResult = foodRepository.getFoodByUidAndDateSearchResult();
    }

    public MutableLiveData<List<Food>> getFoodsByUidSearchResult() {
        return foodsByUidSearchResult;
    }

    public MutableLiveData<List<Food>> getFoodByUidAndDateSearchResult() {
        return foodByUidAndDateSearchResult;
    }

    public void insertFood(Food food){
        foodRepository.insert(food);
    }

    public void delete(Food food){
        foodRepository.delete(food);
    }

    public void loadFoodsByUid(String uid){
        foodRepository.loadFoodsByUid(uid);
    }

    public void loadFoodByUidAndDate(String uid, String date){
        foodRepository.loadFoodByUidAndDate(uid, date);
    }
}
