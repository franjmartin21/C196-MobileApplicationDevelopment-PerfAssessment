package com.exercise.fmart43.degreetracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteAdapterViewHolder> {

    private Cursor mCursor;

    private ListItemClickListener mOnClickListener;

    public NoteAdapter(Cursor cursor, ListItemClickListener listener){
        this.mCursor = cursor;
        this.mOnClickListener = listener;
    }

    @Override
    public NoteAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListTerm = R.layout.note_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachParentImmediately = false;
        View view = inflater.inflate(layoutIdForListTerm, parent, shouldAttachParentImmediately);
        NoteAdapterViewHolder noteAdapterViewHolder = new NoteAdapterViewHolder(view);

        return noteAdapterViewHolder;

    }

    @Override
    public void onBindViewHolder(NoteAdapterViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        holder.mNoteId = mCursor.getInt(mCursor.getColumnIndex(DegreeTrackerContract.NoteEntry._ID));
        holder.mNoteText.setText(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.NoteEntry.COLUMN_TEXT)));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ListItemClickListener{
        void onNoteItemClick(int termId);
    }


    class NoteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int mNoteId;

        private TextView mNoteText;

        public NoteAdapterViewHolder(View itemView) {
            super(itemView);
            mNoteText = itemView.findViewById(R.id.tv_notelist_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onNoteItemClick(mNoteId);
        }
    }
}
