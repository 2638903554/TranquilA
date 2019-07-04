package me.wcy.music.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.hwangjr.rxbus.RxBus;

import me.wcy.music.application.AppCache;
import me.wcy.music.constants.Keys;
import me.wcy.music.model.Clock;
import java.util.ArrayList;

import me.wcy.music.R;
import me.wcy.music.activity.SetAlarmActivity;
import me.wcy.music.adapter.ClockListAdapter;
import me.wcy.music.utils.binding.Bind;


public class ClockFragment extends BaseFragment {

    @Bind(R.id.clock_List)
    public ListView clockListView;
    private int hour;
    private int minute;
    Switch itemswitch;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clock, container, false);
//        ViewUtils.inject(this);
        clockListView = view.findViewById(R.id.clock_List);
        clockListView.setAdapter(new ClockListAdapter(getActivity(), loadClockList()));

        clockListView.setOnItemClickListener((parent, view1, position, id) -> {

            itemswitch= view1.findViewById(R.id.clock_statebutton);
            TextView clockringtime=view1.findViewById(R.id.clock_ringtime);
            Intent intent=new Intent(getActivity(),SetAlarmActivity.class);
            intent.putExtra("itemid",position+"th");
           // Toast.makeText(getActivity(), position+"i", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }


    public Clock addClock(int hour, int minute){        //闹钟界面添加闹钟
       Clock clock = new Clock();
        clock.setTime(hour, minute);
        return clock;
    }


    public ArrayList<Clock> loadClockList() {
        ArrayList<Clock> clockArrayList = new ArrayList<>();
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        clockArrayList.add(addClock(0,0));
        return clockArrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        clockListView.setAdapter(new ClockListAdapter(getActivity(), loadClockList()));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        int position = clockListView.getFirstVisiblePosition();
//        int offset = (clockListView.getChildAt(0) == null) ? 0 : clockListView.getChildAt(0).getTop();
//        outState.putInt(Keys.ALARM_POSITION, position);
//        outState.putInt(Keys.ALARM_OFFSET, offset);
    }

    public void onRestoreInstanceState(final Bundle savedInstanceState) {
//        clockListView.post(() -> {
//            int position = savedInstanceState.getInt(Keys.ALARM_POSITION);
//            int offset = savedInstanceState.getInt(Keys.ALARM_OFFSET);
//            clockListView.setSelectionFromTop(position, offset);
//        });
    }
}
