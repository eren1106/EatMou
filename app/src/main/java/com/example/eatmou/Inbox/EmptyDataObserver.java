package com.example.eatmou.Inbox;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyDataObserver extends RecyclerView.AdapterDataObserver {
    private View emptyView;
    private RecyclerView recyclerView;

    public EmptyDataObserver(View emptyView, RecyclerView recyclerView) {
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;
        checkIfEmpty();
    }

    private void checkIfEmpty(){
        if(emptyView != null && recyclerView.getAdapter() != null){
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount()==0;
            emptyView.setVisibility(emptyViewVisible?View.VISIBLE : View.INVISIBLE);
            recyclerView.setVisibility(emptyViewVisible?View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkIfEmpty();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        checkIfEmpty();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        checkIfEmpty();
    }
}
