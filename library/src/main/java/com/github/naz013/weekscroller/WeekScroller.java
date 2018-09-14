package com.github.naz013.weekscroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("LogNotTimber")
public final class WeekScroller extends RecyclerView {

    static final String TAG = "WeekScroller";

    @NonNull
    private final WeeksAdapter adapter = new WeeksAdapter();
    private int mStartDay = DateTimeConstants.SUNDAY;
    private boolean mIsTodayMarked = true;
    @NonNull
    private DateTime mLastSelected = toSample(DateTime.now());
    @NonNull
    private DateTime mCurrentDate = toSample(DateTime.now());
    @NonNull
    private Week mCurrentWeek = new Week();
    @NonNull
    private Week mPrevWeek = new Week();
    @NonNull
    private Week mNextWeek = new Week();
    @Nullable
    private DateSelectListener dateSelectListener;
    @Nullable
    private DateRangeChangeListener dateRangeChangeListener;

    public WeekScroller(@NonNull Context context) {
        this(context, null);
    }

    public WeekScroller(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekScroller(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    public void setDayBackground(@Nullable Drawable dayBackground) {
        this.adapter.setDayBackground(dayBackground);
    }

    public void setDayBackgroundColor(int dayBackgroundColor) {
        this.adapter.setDayBackgroundColor(dayBackgroundColor);
    }

    public void setDayTextColor(int dayTextColor) {
        this.adapter.setDayTextColor(dayTextColor);
    }

    public void setDayTextSize(int unit, int dayTextSize) {
        this.adapter.setDayTextSize(TypedValue.complexToDimensionPixelSize(dayTextSize, getResources().getDisplayMetrics()));
    }

    public void setDayTextStyle(int dayTextStyle) {
        this.adapter.setDayTextStyle(dayTextStyle);
    }

    public void setSelectedBackground(@Nullable Drawable selectedBackground) {
        this.adapter.setSelectedBackground(selectedBackground);
    }

    public void setSelectedBackgroundColor(int selectedBackgroundColor) {
        this.adapter.setSelectedBackgroundColor(selectedBackgroundColor);
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.adapter.setSelectedTextColor(selectedTextColor);
    }

    public void setSelectedTextSize(int unit, int selectedTextSize) {
        this.adapter.setSelectedTextSize(TypedValue.complexToDimensionPixelSize(selectedTextSize, getResources().getDisplayMetrics()));
    }

    public void setSelectedTextStyle(int selectedTextStyle) {
        this.adapter.setSelectedTextStyle(selectedTextStyle);
    }

    public void setTodayBackground(@Nullable Drawable todayBackground) {
        this.adapter.setTodayBackground(todayBackground);
    }

    public void setTodayBackgroundColor(int todayBackgroundColor) {
        this.adapter.setTodayBackgroundColor(todayBackgroundColor);
    }

    public void setTodayTextColor(int todayTextColor) {
        this.adapter.setTodayTextColor(todayTextColor);
    }

    public void setTodayTextSize(int unit, int todayTextSize) {
        this.adapter.setTodayTextSize(TypedValue.complexToDimensionPixelSize(todayTextSize, getResources().getDisplayMetrics()));
    }

    public void setTodayTextStyle(int todayTextStyle) {
        this.adapter.setTodayTextStyle(todayTextStyle);
    }

    public void setDateRangeChangeListener(@Nullable DateRangeChangeListener dateRangeChangeListener) {
        this.dateRangeChangeListener = dateRangeChangeListener;
    }

    public void setCurrentDate(@NonNull DateTime dateTime) {
        this.mCurrentDate = dateTime;
        initScroller(mCurrentWeek.getStart());
    }

    public void setTodayMarkEnabled(boolean markEnabled) {
        this.mIsTodayMarked = markEnabled;
        invalidate();
    }

    public boolean isTodayMarkEnabled() {
        return mIsTodayMarked;
    }

    @Nullable
    public DateRangeChangeListener getDateRangeChangeListener() {
        return dateRangeChangeListener;
    }

    public void setDateSelectListener(@Nullable DateSelectListener dateSelectListener) {
        this.dateSelectListener = dateSelectListener;
    }

    @Nullable
    public DateSelectListener getDateSelectListener() {
        return dateSelectListener;
    }

    public int getStartDay() {
        return mStartDay;
    }

    public void setStartDay(int startDay) throws WeekScrollerException {
        if (startDay < DateTimeConstants.MONDAY || startDay > DateTimeConstants.SUNDAY) {
            throw new WeekScrollerException("Only allowed values from 1 to 7 (included).");
        }
        this.mStartDay = startDay;
        invalidate();
    }

    public void setSelectedDate(@Nullable DateTime dateTime) {
        if (dateTime == null) {
            initScroller(mCurrentDate);
        } else {
            initScroller(dateTime);
        }
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        super.setLayoutManager(new GridLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).setSpanCount(7));
        super.setAdapter(adapter);
        this.adapter.setListener(new WeeksAdapter.OnDateClickListener() {
            @Override
            public void onDateClicked(@NonNull DateTime dateTime) {
                mLastSelected = dateTime;
                if (getDateSelectListener() != null) {
                    getDateSelectListener().onDateSelected(dateTime);
                }
            }
        });

        if (attrs != null) {
            prepareAttributes(context, attrs);
        }

        SnapToBlock snapToBlock = new SnapToBlock(1);
        snapToBlock.attachToRecyclerView(this);
        snapToBlock.setSnapBlockCallback(new SnapToBlock.SnapBlockCallback() {
            @Override
            public void onBlockSnap(int snapPosition) {
            }

            @Override
            public void onBlockSnapped(int snapPosition) {
                Log.d(TAG, "onBlockSnapped: " + snapPosition);
                if (snapPosition == 0) {
                    moveBackward();
                } else if (snapPosition == 14) {
                    moveForward();
                }
            }
        });

        this.mCurrentDate = toSample(DateTime.now());
        initScroller(this.mCurrentDate);
    }

    private void prepareAttributes(@NonNull Context context, @NonNull AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.WeekScroller, 0, 0);
        try {
            setTodayMarkEnabled(a.getBoolean(R.styleable.WeekScroller_todayMarkEnabled, isTodayMarkEnabled()));
            setStartDay(a.getInt(R.styleable.WeekScroller_startDayOfWeek, getStartDay()));
            adapter.setStyles(context, a);
            a.recycle();
        } catch (Exception e) {
            Log.d(TAG, "prepareAttributes: " + e.getMessage());
        }
    }

    private void initScroller(@NonNull DateTime dateTime) {
        this.mLastSelected = toSample(DateTime.now());
        this.mCurrentWeek = weekForDate(dateTime);
        this.mPrevWeek = weekBefore(this.mCurrentWeek);
        this.mNextWeek = weekAfter(this.mCurrentWeek);
        this.adapter.refresh(this.mPrevWeek, this.mCurrentWeek, this.mNextWeek);
        this.scrollToPosition(7);
    }

    private void moveBackward() {
        Log.d(TAG, "moveBackward: ");
        this.mNextWeek = mCurrentWeek;
        this.mCurrentWeek = mPrevWeek;
        this.mPrevWeek = weekBefore(this.mCurrentWeek);
        this.adapter.addToStart(this.mPrevWeek);

        if (getDateRangeChangeListener() != null) {
            getDateRangeChangeListener().onRangeChanged(mCurrentWeek.getStart(), mCurrentWeek.getEnd());
        }
    }

    private void moveForward() {
        Log.d(TAG, "moveForward: ");
        this.mPrevWeek = mCurrentWeek;
        this.mCurrentWeek = mNextWeek;
        this.mNextWeek = weekAfter(this.mCurrentWeek);
        this.adapter.addToEnd(this.mNextWeek);

        if (getDateRangeChangeListener() != null) {
            getDateRangeChangeListener().onRangeChanged(mCurrentWeek.getStart(), mCurrentWeek.getEnd());
        }
    }

    @NonNull
    private Week weekForDate(@NonNull DateTime dateTime) {
        DateTime current = toSample(dateTime);
        DateTime start = findStartDayOfWeek(current);
        DateTime end = start.plusDays(6);
        return new Week().setStart(start).setEnd(end).setDays(create(start, end, current));
    }

    @NonNull
    private List<Day> create(@NonNull DateTime start, @NonNull DateTime end, @Nullable DateTime current) {
        List<Day> days = new LinkedList<>();
        DateTime pointer = new DateTime(start);
        while (pointer.isBefore(end)) {
            addDay(current, days, pointer);
            pointer = pointer.plusDays(1);
        }
        addDay(current, days, end);
        Log.d(TAG, "create: " + days.size());
        return days;
    }

    private void addDay(@Nullable DateTime current, List<Day> days, DateTime pointer) {
        Day day = new Day().setDateTime(pointer);
        if (current != null && isSameDay(pointer, current)) {
            day.setSelected(true);
        } else if (isSameDay(pointer, mLastSelected)) {
            day.setSelected(true);
        }
        if (mIsTodayMarked && isSameDay(pointer, mCurrentDate)) {
            day.setCurrent(true);
        }
        days.add(day);
    }

    private boolean isSameDay(@NonNull DateTime dt1, @NonNull DateTime dt2) {
        return dt1.getYear() == dt2.getYear()
                && dt1.getMonthOfYear() == dt2.getMonthOfYear()
                && dt1.getDayOfMonth() == dt2.getDayOfMonth();
    }

    @NonNull
    private Week weekBefore(Week week) {
        DateTime end = week.getStart().minusDays(1);
        DateTime start = end.minusDays(6);
        return new Week().setStart(start).setEnd(end).setDays(create(start, end, week.findSelected()));
    }

    @NonNull
    private DateTime toSample(@NonNull DateTime dateTime) {
        return new DateTime()
                .withDate(dateTime.getYear(),
                        dateTime.getMonthOfYear(),
                        dateTime.getDayOfMonth())
                .withTime(13, 0, 0, 0);
    }

    @NonNull
    private Week weekAfter(Week week) {
        DateTime start = week.getEnd().plusDays(1);
        DateTime end = start.plusDays(6);
        return new Week().setStart(start).setEnd(end).setDays(create(start, end, week.findSelected()));
    }

    @NonNull
    private DateTime findStartDayOfWeek(DateTime dateTime) {
        for (int i = 0; i < 7; i++) {
            if (dateTime.getDayOfWeek() == mStartDay) {
                return dateTime;
            }
            dateTime = dateTime.minusDays(1);
        }
        return dateTime;
    }

    @Override
    public final void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof WeeksAdapter) {
            super.setAdapter(adapter);
        }
    }

    @Override
    public final void setLayoutManager(@Nullable LayoutManager layout) {

    }

    class GridLinearLayoutManager extends LinearLayoutManager {

        private Context mContext;
        private int spanCount;

        GridLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            this.mContext = context;
        }

        @Override
        public void measureChild(@NonNull View child, int widthUsed, int heightUsed) {
            RecyclerView.LayoutParams lpTmp = (RecyclerView.LayoutParams) child.getLayoutParams();
            final RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(measureScreenWidth(mContext) / spanCount, lpTmp.height);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(measureScreenWidth(mContext) / spanCount, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
            child.measure(widthSpec, heightSpec);
        }

        @Override
        public void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
            RecyclerView.LayoutParams lpTmp = (RecyclerView.LayoutParams) child.getLayoutParams();
            final RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(measureScreenWidth(mContext) / spanCount, lpTmp.height);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(measureScreenWidth(mContext) / spanCount, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
            child.measure(widthSpec, heightSpec);
        }

        private int measureScreenWidth(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);

            return point.x;
        }

        public int getSpanCount() {
            return spanCount;
        }

        public GridLinearLayoutManager setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }
    }

    public interface DateSelectListener {
        void onDateSelected(@NonNull DateTime dateTime);
    }

    public interface DateRangeChangeListener {
        void onRangeChanged(@NonNull DateTime start, @NonNull DateTime end);
    }
}
