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

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private List<Food> searchResults = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public SearchResultsAdapter(Context context){
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setSearchResults(List<Food> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.result_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food result = searchResults.get(position);
        holder.myTextView.setText(result.getName() +" " + result.getCalories());
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvFoodInfo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(itemClickListener != null)
                itemClickListener.onItemClick(v, getAdapterPosition());

        }


    }
    public Food getItem(int id){
        return searchResults.get(id);
    }

    public void setOnClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
