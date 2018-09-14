package com.github.naz013.weekscroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

class WeeksAdapter extends RecyclerView.Adapter<WeeksAdapter.Holder> {

    private List<Day> days = new LinkedList<>();
    @Nullable
    private OnDateClickListener listener;
    @Nullable
    private Drawable todayBackground = null;
    @Nullable
    private Drawable dayBackground = null;
    @Nullable
    private Drawable selectedBackground = null;
    @ColorInt
    private int todayBackgroundColor = 0;
    @ColorInt
    private int dayBackgroundColor = 0;
    @ColorInt
    private int selectedBackgroundColor = 0;
    @ColorInt
    private int todayTextColor = Color.DKGRAY;
    @ColorInt
    private int dayTextColor = Color.DKGRAY;
    @ColorInt
    private int selectedTextColor = Color.DKGRAY;
    private int todayTextSize = 0;
    private int dayTextSize = 0;
    private int selectedTextSize = 0;
    @StyleRes
    private int todayTextStyle = 0;
    @StyleRes
    private int dayTextStyle = 0;
    @StyleRes
    private int selectedTextStyle = 0;

    void setDayBackground(@Nullable Drawable dayBackground) {
        this.dayBackground = dayBackground;
    }

    void setDayBackgroundColor(int dayBackgroundColor) {
        this.dayBackgroundColor = dayBackgroundColor;
    }

    void setDayTextColor(int dayTextColor) {
        this.dayTextColor = dayTextColor;
    }

    void setDayTextSize(int dayTextSize) {
        this.dayTextSize = dayTextSize;
    }

    void setDayTextStyle(int dayTextStyle) {
        this.dayTextStyle = dayTextStyle;
    }

    void setSelectedBackground(@Nullable Drawable selectedBackground) {
        this.selectedBackground = selectedBackground;
    }

    void setSelectedBackgroundColor(int selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    void setSelectedTextSize(int selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
    }

    void setSelectedTextStyle(int selectedTextStyle) {
        this.selectedTextStyle = selectedTextStyle;
    }

    void setTodayBackground(@Nullable Drawable todayBackground) {
        this.todayBackground = todayBackground;
    }

    void setTodayBackgroundColor(int todayBackgroundColor) {
        this.todayBackgroundColor = todayBackgroundColor;
    }

    void setTodayTextColor(int todayTextColor) {
        this.todayTextColor = todayTextColor;
    }

    void setTodayTextSize(int todayTextSize) {
        this.todayTextSize = todayTextSize;
    }

    void setTodayTextStyle(int todayTextStyle) {
        this.todayTextStyle = todayTextStyle;
    }

    void setListener(@Nullable OnDateClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    private OnDateClickListener getListener() {
        return listener;
    }

    void refresh(@NonNull Week start, @NonNull Week current, @NonNull Week end) {
        this.days.clear();
        this.days.addAll(start.getDays());
        this.days.addAll(current.getDays());
        this.days.addAll(end.getDays());
        notifyDataSetChanged();
    }

    void addToStart(@NonNull Week week) {
        this.days.addAll(0, week.getDays());
        notifyItemRangeInserted(0, 7);
        this.days = this.days.subList(0, 21);
        notifyItemRangeRemoved(21, 7);
        notifyItemRangeChanged(0, this.days.size());
    }

    void addToEnd(@NonNull Week week) {
        this.days.addAll(week.getDays());
        notifyItemRangeInserted(21, 7);
        this.days = this.days.subList(7, this.days.size());
        notifyItemRangeRemoved(0, 7);
        notifyItemRangeChanged(0, this.days.size());
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_day, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bind(days.get(i));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    private void handleClick(int position) {
        Day day = days.get(position);
        if (!day.isSelected()) {
            deselectPrev();
            day.setSelected(true);
            notifyItemChanged(position);
            if (getListener() != null) {
                getListener().onDateClicked(day.getDateTime());
            }
        }
    }

    private void deselectPrev() {
        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            if (day.isSelected()) {
                day.setSelected(false);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void setStyles(Context context, TypedArray a) {
        todayTextSize = a.getDimensionPixelSize(R.styleable.WeekScroller_todayTextSize, todayTextSize);
        dayTextSize = a.getDimensionPixelSize(R.styleable.WeekScroller_dayTextSize, dayTextSize);
        selectedTextSize = a.getDimensionPixelSize(R.styleable.WeekScroller_selectedTextSize, selectedTextSize);

        todayTextColor = a.getColor(R.styleable.WeekScroller_todayTextColor, todayTextColor);
        dayTextColor = a.getColor(R.styleable.WeekScroller_dayTextColor, dayTextColor);
        selectedTextColor = a.getColor(R.styleable.WeekScroller_selectedTextColor, selectedTextColor);

        todayBackgroundColor = a.getColor(R.styleable.WeekScroller_todayBackgroundColor, todayBackgroundColor);
        dayBackgroundColor = a.getColor(R.styleable.WeekScroller_dayBackgroundColor, dayBackgroundColor);
        selectedBackgroundColor = a.getColor(R.styleable.WeekScroller_selectedBackgroundColor, selectedBackgroundColor);

        todayTextStyle = a.getResourceId(R.styleable.WeekScroller_todayTextAppearance, todayTextStyle);
        dayTextStyle = a.getResourceId(R.styleable.WeekScroller_dayTextAppearance, dayTextStyle);
        selectedTextStyle = a.getResourceId(R.styleable.WeekScroller_selectedTextAppearance, selectedTextStyle);

        int todayBackgroundId = a.getResourceId(R.styleable.WeekScroller_todayBackground, 0);
        int dayBackgroundId = a.getResourceId(R.styleable.WeekScroller_dayBackground, 0);
        int selectedBackgroundId = a.getResourceId(R.styleable.WeekScroller_selectedBackground, 0);

        if (todayBackgroundId != 0) {
            todayBackground = ContextCompat.getDrawable(context, todayBackgroundId);
        }
        if (dayBackgroundId != 0) {
            dayBackground = ContextCompat.getDrawable(context, dayBackgroundId);
        }
        if (selectedBackgroundId != 0) {
            selectedBackground = ContextCompat.getDrawable(context, selectedBackgroundId);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        private SquareTextView tvDay;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick(getAdapterPosition());
                }
            });
        }

        void bind(Day day) {
            tvDay.setText("" + day.getDateTime().getDayOfMonth());
            if (day.isCurrent()) {
                if (todayBackground != null) {
                    tvDay.setBackgroundDrawable(todayBackground);
                } else if (todayBackgroundColor != 0) {
                    tvDay.setBackgroundColor(todayBackgroundColor);
                } else {
                    tvDay.setBackgroundDrawable(null);
                }
                if (todayTextStyle != 0) {
                    tvDay.setTextAppearance(tvDay.getContext(), todayTextStyle);
                } else {
                    tvDay.setTextColor(todayTextColor);
                    if (todayTextSize != 0) {
                        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, todayTextSize);
                    }
                }
            } else if (day.isSelected()) {
                if (selectedBackground != null) {
                    tvDay.setBackgroundDrawable(selectedBackground);
                } else if (selectedBackgroundColor != 0) {
                    tvDay.setBackgroundColor(selectedBackgroundColor);
                } else {
                    tvDay.setBackgroundDrawable(null);
                }
                if (selectedTextStyle != 0) {
                    tvDay.setTextAppearance(tvDay.getContext(), selectedTextStyle);
                } else {
                    tvDay.setTextColor(selectedTextColor);
                    if (selectedTextSize != 0) {
                        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectedTextSize);
                    }
                }
            } else {
                if (dayBackground != null) {
                    tvDay.setBackgroundDrawable(dayBackground);
                } else if (dayBackgroundColor != 0) {
                    tvDay.setBackgroundColor(dayBackgroundColor);
                } else {
                    tvDay.setBackgroundDrawable(null);
                }
                if (dayTextStyle != 0) {
                    tvDay.setTextAppearance(tvDay.getContext(), dayTextStyle);
                } else {
                    tvDay.setTextColor(dayTextColor);
                    if (dayTextSize != 0) {
                        tvDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, dayTextSize);
                    }
                }
            }
        }
    }

    interface OnDateClickListener {
        void onDateClicked(@NonNull DateTime dateTime);
    }
}
