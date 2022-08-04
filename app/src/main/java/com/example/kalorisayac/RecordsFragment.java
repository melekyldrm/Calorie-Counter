package com.example.kalorisayac;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kalorisayac.adapter.RecordsAdapter;
import com.example.kalorisayac.model.Food;
import com.example.kalorisayac.viewmodel.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class RecordsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private MainViewModel viewModel;
    private RecordsAdapter adapter;

    private ImageButton previousMonthButton,nextMonthButton;
    private TextView monthTextView;
    private RecyclerView recordsRV;


    public static RecordsFragment newInstance(String param1, String param2) {
        RecordsFragment fragment = new RecordsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        init(view);

        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(getActivity().getApplication())
                .create(MainViewModel.class);
        observerSetup();

        recordsRV = view.findViewById(R.id.recordsRV);
        recordsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecordsAdapter(getActivity());
        recordsRV.setAdapter(adapter);
        refreshData();

        return view;
    }

    private void refreshData() {
        viewModel.loadFoodsByUid(FirebaseAuth.getInstance().getUid());
    }

    private void observerSetup() {
        viewModel.getFoodsByUidSearchResult().observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                adapter.setRecords(foods);
            }
        });
    }

    private void init(View v){
        previousMonthButton = v.findViewById(R.id.previousMonthButton);
        nextMonthButton = v.findViewById(R.id.nextMonthButton);
        monthTextView = v.findViewById(R.id.monthTextView);
        recordsRV = v.findViewById(R.id.recordsRV);
    }
}