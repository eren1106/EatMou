package com.example.eatmou.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;

import java.util.ArrayList;

public class CategoryFilterRecViewAdapter extends RecyclerView.Adapter<CategoryFilterRecViewAdapter.ViewHolder>{

    OnItemClickListener listener;

    private final ArrayList<String> category;

    private Context context;

    public CategoryFilterRecViewAdapter(Context context, ArrayList<String> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_filter_button, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryBtn.setText(category.get(position));
        holder.categoryBtn.setTextOn(category.get(position));
        holder.categoryBtn.setTextOff(category.get(position));

        // filter function

    }


    @Override
    public int getItemCount() {
        return category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ToggleButton categoryBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryBtn = itemView.findViewById(R.id.categoryBtn);

            categoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        String filter = categoryBtn.getText().toString();
                        listener.onItemClick(filter, categoryBtn.isChecked());
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(String filter, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
