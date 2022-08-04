package com.example.kalorisayac.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.kalorisayac.AppDatabase;
import com.example.kalorisayac.dao.FoodDao;
import com.example.kalorisayac.model.Food;

import java.util.List;

public class FoodRepository {
    private MutableLiveData<List<Food>> foodsByUidSearchResult = new MutableLiveData<>();
    private MutableLiveData<List<Food>> foodByUidAndDateSearchResult = new MutableLiveData<>();

    private FoodDao foodDao;

    public FoodRepository(Application application){
        this.foodDao = AppDatabase
                .getInstance(application.getApplicationContext())
                .foodDao();
    }

    public void insert(Food food){
        InsertAsyncQuery task = new InsertAsyncQuery(this.foodDao);
        task.execute(food);
    }
    public void delete(Food food){
        DeleteAsyncQuery task = new DeleteAsyncQuery(this.foodDao);
        task.execute(food);
    }
    public void loadFoodsByUid(String uid){
        FoodsByUidAsyncQuery task = new FoodsByUidAsyncQuery(this.foodDao);
        task.delegate = this;
        task.execute(uid);
    }

    public void loadFoodByUidAndDate(String uid, String date){
        FoodByUidAndDateAsyncQuery task = new FoodByUidAndDateAsyncQuery(this.foodDao);
        task.delegate = this;
        task.execute(uid, date);
    }

    public MutableLiveData<List<Food>> getFoodsByUidSearchResult() {
        return foodsByUidSearchResult;
    }


    public MutableLiveData<List<Food>> getFoodByUidAndDateSearchResult() {
        return foodByUidAndDateSearchResult;
    }

    private void foodsByUidSearchResultAsyncFinished(List<Food> result){
        foodsByUidSearchResult.setValue(result);
    }

    private void foodByUidAndDateSearchResultAsyncFinished(List<Food> result){
        foodByUidAndDateSearchResult.setValue(result);
    }

    private static class FoodsByUidAsyncQuery extends AsyncTask<String, Void, List<Food>>{
        private FoodDao foodDao;
        private FoodRepository delegate = null;
        public FoodsByUidAsyncQuery(FoodDao foodDao){
            this.foodDao = foodDao;
        }
        @Override
        protected List<Food> doInBackground(String... strings) {
            return foodDao.loadByUid(strings[0]);
        }
        @Override
        protected void onPostExecute(List<Food> result) {
            delegate.foodsByUidSearchResultAsyncFinished(result);
        }
    }

    private static class FoodByUidAndDateAsyncQuery extends AsyncTask<String, Void, List<Food>>{
        private FoodDao foodDao;
        private FoodRepository delegate = null;
        public FoodByUidAndDateAsyncQuery(FoodDao foodDao){
            this.foodDao = foodDao;
        }
        @Override
        protected List<Food> doInBackground(String... strings) {
            return foodDao.loadByUidAndDate(strings[0], strings[1]);
        }
        @Override
        protected void onPostExecute(List<Food> result) {
            delegate.foodByUidAndDateSearchResultAsyncFinished(result);
        }
    }

    private static class InsertAsyncQuery extends AsyncTask<Food, Void, Void>{
        private FoodDao foodDao;

        public InsertAsyncQuery(FoodDao dao){
            this.foodDao = dao;
        }
        @Override
        protected Void doInBackground(Food... foods) {
            foodDao.insert(foods[0]);
            return null;
        }
    }
    private static class DeleteAsyncQuery extends AsyncTask<Food, Void, Void>{
        private FoodDao foodDao;

        public DeleteAsyncQuery(FoodDao dao){
            this.foodDao = dao;
        }
        @Override
        protected Void doInBackground(Food... foods) {
            foodDao.delete(foods[0]);
            return null;
        }
    }
}
