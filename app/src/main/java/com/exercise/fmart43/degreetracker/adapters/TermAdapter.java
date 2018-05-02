package com.exercise.fmart43.degreetracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.exercise.fmart43.degreetracker.R;
import com.exercise.fmart43.degreetracker.data.DegreeTrackerContract;
import com.exercise.fmart43.degreetracker.util.DegreeUtils;

import java.util.Date;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermAdapterViewHolder> {

    private Cursor mCursor;

    private ListItemClickListener mOnClickListener;

    public TermAdapter(Cursor cursor, ListItemClickListener listener){
        this.mCursor = cursor;
        this.mOnClickListener = listener;
    }

    @Override
    public TermAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListTerm = R.layout.term_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachParentImmediately = false;
        View view = inflater.inflate(layoutIdForListTerm, parent, shouldAttachParentImmediately);
        TermAdapterViewHolder termAdapterViewHolder = new TermAdapterViewHolder(view);

        return termAdapterViewHolder;

    }

    @Override
    public void onBindViewHolder(TermAdapterViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        int termId = mCursor.getInt(mCursor.getColumnIndex(DegreeTrackerContract.TermEntry._ID));
        String termTitle = mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_TITLE));
        String termStartDate = mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_START_DATE));
        String termEndDate = mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.TermEntry.COLUMN_END_DATE));
        Date startDate = DegreeTrackerContract.getDateFromDBStrValue(termStartDate);
        Date endDate = DegreeTrackerContract.getDateFromDBStrValue(termEndDate);

        holder.mTermId = termId;
        holder.mTermTitle.setText(termTitle);
        if(startDate != null && endDate != null)
            holder.mTermDateRange.setText(DegreeUtils.DATE_FORMAT_EDIT_TEXT.format(startDate) + " - " + DegreeUtils.DATE_FORMAT_EDIT_TEXT.format(endDate));
        holder.mTermStatus.setText(DegreeTrackerContract.TermEntry.getStatusByStartEndDate(startDate, endDate));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ListItemClickListener{
        void onListItemClick(int termId);

        void onListItemClickDelete(int termId);

        void onListItemClickEdit(int term);
    }


    class TermAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int mTermId;

        private TextView mTermTitle;

        private TextView mTermDateRange;

        private TextView mTermStatus;

        private ImageView mTermDelete;

        private ImageView mTermEdit;

        public TermAdapterViewHolder(View itemView) {
            super(itemView);

            mTermTitle = itemView.findViewById(R.id.tv_termlist_title);
            mTermDateRange = itemView.findViewById(R.id.tv_termlist_daterange);
            mTermStatus = itemView.findViewById(R.id.tv_termlist_status);
            mTermDelete = itemView.findViewById(R.id.iv_termList_delete);
            mTermEdit = itemView.findViewById(R.id.iv_termList_edit);

            itemView.setOnClickListener(this);
            mTermDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClickDelete(mTermId);
                }
            });

            mTermEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClickEdit(mTermId);
                }
            });
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(mTermId);
        }
    }
}
