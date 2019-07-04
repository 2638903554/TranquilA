package me.wcy.music.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.wcy.music.R;
import me.wcy.music.receiver.CallAlarm;
import me.wcy.music.model.Clock;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class ClockListAdapter extends BaseAdapter {
    private Context context;
    private boolean switchstate;
    private Clock clock;
    private LayoutInflater layoutInflater;
    private ArrayList<Clock> clockArrayList;
    private View.OnClickListener listener;

    public ClockListAdapter(Context context,ArrayList<Clock> clockArrayList){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
        this.clockArrayList=clockArrayList;

    }


    @Override
    public int getCount() {
        return clockArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return clockArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        //显示item中时间的两个组件
        public TextView clock_ringtime;
        public Switch clock_switchbutton;
    }

    public void closeclock(int position){
        Intent intent = new Intent(context, CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,position, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am;
        am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        am.cancel(sender);
        Toast.makeText(context,"闹钟取消", Toast.LENGTH_SHORT).show();
    }
    public void open(int hour,int minute,int position,int interval,int ring,boolean[] week){
        Calendar c=Calendar.getInstance();
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                if (week[6]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            case Calendar.MONDAY:
                if (week[0]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;

            case Calendar.TUESDAY:
                if (week[1]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            case Calendar.WEDNESDAY:
                if (week[2]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            case Calendar.THURSDAY:
                if (week[3]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            case Calendar.FRIDAY:
                if (week[4]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            case Calendar.SATURDAY:
                if (week[5]){
                    openclock(hour,minute,position,interval,ring);
                }
                break;
            default:
                break;

        }

    }


    //设置闹钟时间
    public void openclock(int hour,int minute,int position,int interval,int ring){
        //Log.d("clock","hour  "+hour+"minite"+minute+"interval"+interval+"position"+position);

        Calendar c = Calendar.getInstance();
        //long time=System.currentTimeMillis();
        long times=interval*60 *1000;
        long sethourInMillis = hour * 3600 * 1000;
        long setmninuteInMillis = minute* 60 * 1000;
        long nowMinuteInMillis = c.get(Calendar.MINUTE) * 1000 * 60;
        long nowHoursInMillis = c.get(Calendar.HOUR_OF_DAY) * 1000 * 3600;
        long nowSecondInMills=c.get(Calendar.SECOND)*1000;
        long TimeInMills=sethourInMillis+setmninuteInMillis-nowMinuteInMillis-nowHoursInMillis-nowSecondInMills+5000;
        //闹钟和现在的时间间隔毫秒
        if(TimeInMills<0){
            TimeInMills+=24*3600*1000;
        }
        Intent intent = new Intent(context, CallAlarm.class);
        String str=ring+"th";
       // Log.d("clocktime",TimeInMills+ "interval"+times);
        intent.putExtra("RING",str);
        PendingIntent sender = PendingIntent.getBroadcast(context,position, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if(interval==0){
            Log.d("clocktime","am.set successful");
            am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+TimeInMills,sender);
        }
        else {
            Log.d("systm","am.setrepeat successful0");
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+TimeInMills,1000+times,sender);
        }

        Toast.makeText(context,"响铃时间  "+ hour+" 时"+minute+" 分", Toast.LENGTH_SHORT).show();
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder=null;
        if(convertView == null){
            //填写ListView的图标和标题等控件的来源，来自于layout;clocklist_item这个布局文件
            //把控件所在的布局文件加载到当前类中
            convertView = layoutInflater.inflate(R.layout.clocklist_item,null);
            //生成一个ViewHolder的对象
            holder = new ViewHolder();
            //获取控件对象
            holder.clock_ringtime=convertView.findViewById(R.id.clock_ringtime);
            holder.clock_switchbutton=convertView.findViewById(R.id.clock_statebutton);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Clock clock= clockArrayList.get(position);       //clock列表中的clock
        final String pos=position+"";
        final Clock clockget=getclock(pos+"");           //位于此处的clock从文件中获取得到的clock对象
       // Log.d("test",getclock(pos+"").getSwitchstate()+"");
        holder.clock_ringtime.setText(clockget.getTime());
        holder.clock_switchbutton.setChecked(clockget.getSwitchstate());
        holder.clock_switchbutton.setOnClickListener(listener);
        holder.clock_switchbutton.setTag(position);

        //按钮开关设置
        holder.clock_switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    saveclock(pos,clockget,true);
//                    Log.d("clock","hour  "+clockget.getHour()+"minite"+clockget.getMinute()+"interval"+clockget.getIntervaltime()+"position"+position
//                    +"ring");
                    open(clockget.getHour(),clockget.getMinute(),position,clockget.getIntervaltime(),clockget.getRing(),clockget.getWorkday());
                }else{
                    saveclock(pos,clockget,false);
                    closeclock(position);
                }
            }
        });
        return convertView;
    }

    private Clock getclock(String i){  //获取json内容，并转换为class对象
        Clock clock=new Clock();
        String key="KEY_CLOCK_"+i+"th";
        SharedPreferences sp = context.getSharedPreferences("SP_CLOCK", MODE_PRIVATE);
        String clockJson = sp.getString(key,""); //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if(clockJson!="")  //防空判断
        {
            Gson gson = new Gson();
            //Toast.makeText(context,clockJson,Toast.LENGTH_LONG).show();
            clock = gson.fromJson(clockJson, Clock.class); //将json字符串转换成 people对象
        }

        return clock;
    }
    private void saveclock(String i,Clock clock,boolean switchstate){   //保存闹钟当前设置内容
        Clock clockitem=new Clock();
        clockitem=clock;
        String key="KEY_CLOCK_"+i+"th";
        clockitem.setSwitchstate(switchstate);
        SharedPreferences sp = context.getSharedPreferences("SP_CLOCK",MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出，否则就创建一个此key的sp对象
        Gson gson=new Gson();
        String jsonStr=gson.toJson(clockitem);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(key, jsonStr) ;
        edit.commit();
      //  Toast.makeText(context,edit.commit()+"",Toast.LENGTH_LONG).show();
    }


}


