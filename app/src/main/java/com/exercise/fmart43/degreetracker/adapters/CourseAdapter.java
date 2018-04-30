package com.exercise.fmart43.degreetracker.adapters;

import android.content.Context;
import android.database.Cursor;
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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseAdapterViewHolder> {

    public enum TypeEnum{
        COURSE_LIST,
        COURSE_TERM
    }

    private Cursor mCursor;

    private TypeEnum mType;

    private ListItemClickListener mOnClickListener;

    public CourseAdapter(Cursor cursor, ListItemClickListener listener, TypeEnum type){
        this.mCursor = cursor;
        this.mType = type;
        this.mOnClickListener = listener;
    }

    @Override
    public CourseAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListTerm = R.layout.course_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachParentImmediately = false;
        View view = inflater.inflate(layoutIdForListTerm, parent, shouldAttachParentImmediately);
        CourseAdapterViewHolder termAdapterViewHolder = new CourseAdapterViewHolder(view);

        return termAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(CourseAdapterViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)) return;

        holder.mCourseId = mCursor.getInt(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry._ID));
        holder.mCourseTitle.setText(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_TITLE)));
        Date startDate = DegreeTrackerContract.getDateFromDBStrValue(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_START_DATE)));
        Date endDate = DegreeTrackerContract.getDateFromDBStrValue(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_END_DATE)));
        holder.mCourseDateRange.setText(DegreeUtils.getStringFromDate(startDate) + "-" + DegreeUtils.getStringFromDate(endDate));
        if(mType == TypeEnum.COURSE_TERM) {
            holder.mCourseTerm.setVisibility(View.GONE);
            holder.mCourseStatus.setText(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry.COLUMN_STATUS)));
        } else{
            holder.mCourseStatus.setVisibility(View.GONE);
            holder.mCourseTerm.setText(mCursor.getString(mCursor.getColumnIndex(DegreeTrackerContract.CourseEntry.LABEL_TERM_TITLE)));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public interface ListItemClickListener{
        void onListItemClick(int termId);
    }


    class CourseAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int mCourseId;

        private TextView mCourseTitle;

        private TextView mCourseStatus;

        private TextView mCourseDateRange;

        private TextView mCourseTerm;


        public CourseAdapterViewHolder(View itemView) {
            super(itemView);
            mCourseTitle = itemView.findViewById(R.id.tv_courselist_title);
            mCourseStatus = itemView.findViewById(R.id.tv_courselist_status);
            mCourseDateRange = itemView.findViewById(R.id.tv_courselist_daterange);
            mCourseTerm = itemView.findViewById(R.id.tv_courselist_term);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(mCourseId);
        }
    }
}
