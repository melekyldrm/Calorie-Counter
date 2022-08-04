package com.example.kalorisayac;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kalorisayac.adapter.SearchResultsAdapter;
import com.example.kalorisayac.model.Food;
import com.example.kalorisayac.util.DateHandler;
import com.example.kalorisayac.viewmodel.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CounterFragment extends Fragment implements SearchResultsAdapter.ItemClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private SearchResultsAdapter adapter;
    private MainViewModel viewModel;
    private static Food food;

    EditText searchEditText;
    ProgressDialog progressDialog;
    TextView searchResultTV, dateTextView,totalCaloriesTextView;
    Button searchButton;
    ImageButton previousDayButton, nextDayButton;
    DateHandler dateHandler;
    double totalCalorie = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public static CounterFragment newInstance(String param1, String param2) {
        CounterFragment fragment = new CounterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultTV = view.findViewById(R.id.searchResultTV);
        searchButton = view.findViewById(R.id.searchButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        totalCaloriesTextView = view.findViewById(R.id.totalCaloriesTextView);
        previousDayButton = view.findViewById(R.id.previousDayButton);
        nextDayButton = view.findViewById(R.id.nextDayButton);
        dateHandler = new DateHandler();

        dateTextView.setText(dateHandler.getChoosedDate());

        previousDayButton.setOnClickListener(v -> {
            dateTextView.setText(dateHandler.previousDay());
            refreshData();
        });
        nextDayButton.setOnClickListener(v -> {
            dateTextView.setText(dateHandler.nextDay());
            refreshData();
        });

        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(MainViewModel.class);
        observerSetup();

        RecyclerView recyclerView = view.findViewById(R.id.searchResultsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new SearchResultsAdapter(getActivity());
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        refreshData();

        searchButton.setOnClickListener(v -> {
            String searchQuery = searchEditText.getText().toString();
            if(!searchQuery.isEmpty()){
                ApiCall apiCall = new ApiCall();
                apiCall.execute(searchQuery);
            }else{
                Toast.makeText(getActivity(), "Bir Yiyecek İsmi Girin", Toast.LENGTH_LONG).show();
            }
        });

        searchResultTV.setOnClickListener(v -> {
            String text = searchResultTV.getText().toString();
            if(text == null || text.isEmpty()){
                Toast.makeText(getActivity(), "Bir Yiyecek Arayın", Toast.LENGTH_LONG).show();
            }else{
                Food f = food;
                viewModel.insertFood(f);
                refreshData();
            }
        });
        return view;
    }
@RequiresApi(api = Build.VERSION_CODES.O)
private void refreshData(){
    viewModel.loadFoodByUidAndDate(FirebaseAuth.getInstance().getUid(), dateHandler.getChoosedDate());
}
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(View view, int position) {
        viewModel.delete(adapter.getItem(position));
        refreshData();
    }

    public class ApiCall extends AsyncTask<String, Integer, String>{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            JSONObject itemsObj = null;
            String name = null;
            Double calories = null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.calorieninjas.com/v1/nutrition?query=" + strings[0])
                        .addHeader("X-Api-Key", "oA1GBwK2mAF/rlXiuaJLoA==PXtcDG8fN8IHmetq")
                        .build();

                Call call =  client.newCall(request);
                Response response = call.execute();
                String jsonData = response.body().string();
                System.out.println(jsonData);
                JSONObject obj = null;
                obj = new JSONObject(jsonData);
                JSONArray items = obj.getJSONArray("items");
                if(!items.isNull(0)){
                    itemsObj = items.getJSONObject(0);
                    name = itemsObj.getString("name");
                    calories = (Double)itemsObj.get("calories");
                }else{

                    return "";
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            food = new Food();
            food.setUserId(FirebaseAuth.getInstance().getUid());
            food.setDate(dateHandler.getChoosedDate());
            food.setName(name);
            food.setCalories(calories);
            return name.toUpperCase() +"  ("+ calories.toString()+" kalori)";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Yiyecek Aranıyor...");
            progressDialog.setMessage("Lütfen Bekleyin");
            progressDialog.create();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();

            searchResultTV.setText(s);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void observerSetup(){
        viewModel.getFoodByUidAndDateSearchResult().observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                totalCalorie = 0;
                for (Food f : foods) totalCalorie += f.getCalories();
                setTotalCalorieText();
                adapter.setSearchResults(foods);
            }
        });
    }

    private void setTotalCalorieText(){
        totalCaloriesTextView.setText(String.valueOf(totalCalorie));
    }
}