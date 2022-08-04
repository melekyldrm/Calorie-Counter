package com.example.kalorisayac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kalorisayac.R;
import com.example.kalorisayac.model.Food;

import java.util.ArrayList;
import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private List<Food> records = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public RecordsAdapter(Context context){
        this.layoutInflater = LayoutInflater.from(context);
    }

    public Food getItem(int id){return records.get(id);}
    public void setRecords(List<Food> records) {
        this.records = records;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.record_row, parent, false);
        return new RecordsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food result = records.get(position);
        holder.myTextView.setText(result.getName() +" " + result.getCalories() + " " + result.getDate());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView myTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.recordRowTextView);
        }
    }
}
