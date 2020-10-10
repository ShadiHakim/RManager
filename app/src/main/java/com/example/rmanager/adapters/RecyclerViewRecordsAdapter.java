package com.example.rmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rmanager.R;
import com.example.rmanager.classes.Recording;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class RecyclerViewRecordsAdapter  extends RecyclerView.Adapter<RecyclerViewRecordsAdapter.ViewHolder> {

    private ArrayList<Recording> mRecordings;
    private ArrayList<Recording> mDRecordings;
    private LayoutInflater mInflater;
    private ItemClickListener mSaveClickListener;
    private ItemClickListener mClickListener;
    private ItemClickListener mLongClickListener;

    public RecyclerViewRecordsAdapter(Context context, ArrayList<Recording> data, ArrayList<Recording> Ddata) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecordings = data;
        this.mDRecordings = Ddata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_recorde_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Recording recording = mRecordings.get(position);
        holder.myCard.setChecked(recording.checkIfSaved(mDRecordings == null?new ArrayList<Recording>():mDRecordings));
        holder.myTextViewName.setText(recording.get_contactName());
        holder.myTextViewDate.setText(recording.get_date());
        holder.myImageViewSave.setImageResource(recording.is_saved()?R.mipmap.baseline_star:R.mipmap.outline_star);
    }

    @Override
    public int getItemCount() {
        return mRecordings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        MaterialCardView myCard;
        TextView myTextViewName;
        TextView myTextViewDate;
        ImageView myImageViewSave;

        ViewHolder(View itemView) {
            super(itemView);
            myCard = itemView.findViewById(R.id.card);
            myTextViewName = itemView.findViewById(R.id.textView_Name);
            myTextViewDate = itemView.findViewById(R.id.textView_Date);
            myImageViewSave = itemView.findViewById(R.id.imageView_Save);
            myImageViewSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSaveClickListener != null) mSaveClickListener.onSaveClick(view, getAdapterPosition());
                }
            });
            myCard.setOnClickListener(this);
            myCard.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null) mLongClickListener.onItemLongClick(view, getAdapterPosition());
            return true;
        }
    }

    public Recording getRecord(int id) {
        return mRecordings.get(id);
    }

    public void setSaveClickListener(ItemClickListener itemClickListener) {
        this.mSaveClickListener = itemClickListener;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setLongClickListener(ItemClickListener itemClickListener) {
        this.mLongClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onSaveClick(View view, int position);
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}