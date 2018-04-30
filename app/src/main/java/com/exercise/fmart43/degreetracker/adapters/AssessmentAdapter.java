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

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentAdapterViewHolder> {

    private Cursor mCursor;

    private ListItemClickListener mOnClickListener;

    public AssessmentAdapter(Cursor cursor, ListItemClickListener listener){
        this.mCursor = cursor;
        this.mOnClickListener = listener;
    }

    @Override
    public AssessmentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListTerm = R.layout.assessment_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachParentImmediately = false;
        View view = inflater.inflate(layoutIdForListTerm, parent, shouldAttachParentImmediately);
        AssessmentAdapterViewHolder termAdapterViewHolder = new AssessmentAdapterViewHolder(view);

        return termAdapterViewHolder;

    }

    @Override
    public void onBindViewHolder(AssessmentAdapterViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        holder.mAssessmentId = mCursor.getInt(mCursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry._ID));
        holder.mAssessmentTitle.setText(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_TITLE)));
        DegreeTrackerContract.AssessmentEntry.TypeAssessment typeAssessment = DegreeTrackerContract.AssessmentEntry.TypeAssessment.valueOf(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.AssessmentEntry.COLUMN_TYPE)));
        holder.mAssessmentType.setText(typeAssessment.getPrefix());
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ListItemClickListener{
        void onListItemClick(int termId);
    }

    class AssessmentAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int mAssessmentId;

        private TextView mAssessmentTitle;

        private TextView mAssessmentType;

        public AssessmentAdapterViewHolder(View itemView) {
            super(itemView);
            mAssessmentTitle = itemView.findViewById(R.id.tv_assessmentlist_title);
            mAssessmentType = itemView.findViewById(R.id.tv_assessmentlist_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(mAssessmentId);
        }
    }
}
