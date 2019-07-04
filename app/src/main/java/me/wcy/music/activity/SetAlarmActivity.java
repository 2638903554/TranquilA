package me.wcy.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.util.Calendar;

import me.wcy.music.R;
import me.wcy.music.model.Clock;
public class SetAlarmActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private int hour;
    private int minute;
    public  TextView cancel;
    public  TextView sure;
    public  TextView day;
    public  TextView ring;
    public  TextView ringname;
    public  TextView intervaltext;
    public CheckBox Mon;
    public CheckBox Tue;
    public CheckBox Wed;
    public CheckBox Thu;
    public CheckBox Fri;
    public CheckBox Sat;
    public CheckBox Sun;
    public TimePicker timePicker;
    public String clockJson;
    public RadioGroup radioGroup;
    public RadioButton radioButton0;
    public RadioButton radioButton1;
    public RadioButton radioButton2;
    public RadioButton radioButton3;
    //当前clock对象
    public Clock clocknow;
    public Calendar c;
    public int intervaltime=0;
    public int ringnumber;
    public boolean[] workday={false,false,false,false,false,false,false};
    public String[]rings={"默认铃声","电话铃声","新闻联播","雪之梦","皮卡丘","叮铃铃","SJLT","八音盒","月宝宝","Kalimbell"};
    public String str;
    public boolean switchstate;
    public String str_interval;
    String clockid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        timePicker=(TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        init();//实例化按钮
        //获取闹钟
        if(getclock(clockid)==null){
            clocknow=new Clock();
            str=rings[0];
            ringnumber=1;
            switchstate=true;
        }else{
            clocknow=getclock(clockid);
            hour=clocknow.getHour();
            minute=clocknow.getMinute();
            str=clocknow.getRingname();
            ringnumber=clocknow.getRing();
            switchstate=clocknow.getSwitchstate();
        }
        clockDisplay(clocknow);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                SetAlarmActivity.this.hour=hourOfDay;
                SetAlarmActivity.this.minute=minute;
                clocknow.setTime(SetAlarmActivity.this.hour,SetAlarmActivity.this.minute);
            }
        });
    }
    public void init(){
        clockid=getIntent().getStringExtra("itemid");
        c=Calendar.getInstance();
        cancel= (TextView) findViewById(R.id.cancel_text);
        cancel.setOnClickListener(this);
        sure=(TextView) findViewById(R.id.sure_text);
        sure.setOnClickListener(this);
        day=(TextView) findViewById(R.id.day_text);
        ring=(TextView) findViewById(R.id.ring_text);
        ring.setOnClickListener(this);
        ringname=(TextView) findViewById(R.id.ringname_text);
        ringname.setOnClickListener(this);
        Mon=(CheckBox) findViewById(R.id.Mon);
        Mon.setOnClickListener(this);
        Tue=(CheckBox) findViewById(R.id.Tue);
        Tue.setOnClickListener(this);
        Wed=(CheckBox) findViewById(R.id.Wed);
        Wed.setOnClickListener(this);
        Thu=(CheckBox) findViewById(R.id.Thu);
        Thu.setOnClickListener(this);
        Fri=(CheckBox) findViewById(R.id.Fri);
        Fri.setOnClickListener(this);
        Sat=(CheckBox) findViewById(R.id.Sat);
        Sat.setOnClickListener(this);
        Sun=(CheckBox) findViewById(R.id.Sun);
        Sun.setOnClickListener(this);
        intervaltext=(TextView)  findViewById(R.id.intervaltext);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioButton0=radioGroup.findViewById(R.id.radioButton0);
        radioButton1=radioGroup.findViewById(R.id.radioButton1);
        radioButton2=radioGroup.findViewById(R.id.radioButton2);
        radioButton3=radioGroup.findViewById(R.id.radioButton3);

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {// Q: 参数 group 暂时还没搞清什么用途
        switch (checkedId) {
            case R.id.radioButton0:
                intervaltime=0;
                str_interval="只响一次";
                intervaltext.setText(str_interval);
                break;
            case R.id.radioButton1:
                intervaltime=5;
                str_interval="每五分钟响一次";
                intervaltext.setText(str_interval);
                break;
            case R.id.radioButton2:
                intervaltime=10;
                str_interval="每十分钟响一次";
                intervaltext.setText(str_interval);
                break;
            case R.id.radioButton3:
                intervaltime=15;
                str_interval="每十五分钟响一次";
                intervaltext.setText(str_interval);
                break;
        }

    }

    @Override
    public void onClick( View view){
        switch (view.getId()){
            case R.id.Mon: case R.id.Tue:case R.id.Wed:case R.id.Thu:case R.id.Fri:case R.id.Sat:case R.id.Sun:
                getday();
                break;
            case R.id.cancel_text:
                finish();
                break;
            case R.id.sure_text:
                saveclock(clockid);
                //getclock(clockid);
                finish();
                break;
            case R.id.ring_text:
                startActivity(new Intent(SetAlarmActivity.this, RingActivity.class));
                break;
            case R.id.ringname_text:
                startActivity(new Intent(SetAlarmActivity.this, RingActivity.class));
                break;
        }

    }



    public void getday(){ //展示周一到周日的字符串，并且记录周一到周日的选中状态
        String r="";
        if(Mon.isChecked()) {
            r=r+"周一、" ;
        }
        if(Tue.isChecked()){
            r=r+"周二、" ;
        }
        if(Wed.isChecked()) {
            r=r+"周三、" ;
        }
        if(Thu.isChecked()) {
            r=r+"周四、" ;
        }
        if(Fri.isChecked()) {
            r=r+"周五、" ;
        }
        if(Sat.isChecked()) {
            r=r+"周六、" ;
        }
        if(Sun.isChecked()) {
            r=r+"周日、" ;
        }
        clocknow.setWorkday(Mon.isChecked(),Tue.isChecked(),Wed.isChecked(),Thu.isChecked(),Fri.isChecked(),Sat.isChecked(),Sun.isChecked());
        day.setText(r);
    }
    private void saveclock(String i){   //保存闹钟当前设置内容
        String key="KEY_CLOCK_"+i;
        clocknow.setTime(hour,minute);
        clocknow.setRingname(str);
        clocknow.setRing(ringnumber);
        clocknow.setWorkday(workday);
        clocknow.setSwitchstate(switchstate);
        clocknow.setIntervaltime(intervaltime);
        clocknow.setIntervaltext(str_interval);
        clocknow.setWorkday(Mon.isChecked(),Tue.isChecked(),Wed.isChecked(),Thu.isChecked(),Fri.isChecked(),Sat.isChecked(),Sun.isChecked());
        SharedPreferences sp = getSharedPreferences("SP_CLOCK",MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出，否则就创建一个此key的sp对象
        Gson gson=new Gson();
        String jsonStr=gson.toJson(clocknow);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(key, jsonStr) ;
        edit.commit();
    }
    private Clock getclock(String i){  //获取json内容，并转换为class对象

        Clock clock=new Clock();
        String key="KEY_CLOCK_"+i;
        SharedPreferences sp = getSharedPreferences("SP_CLOCK",MODE_PRIVATE);                     //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        clockJson = sp.getString(key,"");                        //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值

        if(clockJson!="")  //防空判断
        {
            Gson gson = new Gson();
            clock = gson.fromJson(clockJson, Clock.class);
        }

        return clock;
    }
    private void clockDisplay(Clock clockitem){  //展示闹钟的设置内容

        timePicker.setCurrentHour(clockitem.getHour());
        timePicker.setCurrentMinute(clockitem.getMinute());
        ringname.setText(clockitem.getRingname());
        //intervaltext.setText(clockitem.getIntervaltext());
        switch (clockitem.getIntervaltime())
        {
            case 0:
                radioButton0.setChecked(true);
                break;
            case 5:
                radioButton1.setChecked(true);
                break;
            case 10:
                radioButton2.setChecked(true);
                break;
            case 15:
                radioButton3.setChecked(true);
                break;
            default:
                break;
        }
        boolean workday[]=clockitem.getWorkday();
        Mon.setChecked(workday[0]);
        Tue.setChecked(workday[1]);
        Wed.setChecked(workday[2]);
        Thu.setChecked(workday[3]);
        Fri.setChecked(workday[4]);
        Sat.setChecked(workday[5]);
        Sun.setChecked(workday[6]);
        getday();

    }
    public void onRestart() {
        super.onRestart();
        SharedPreferences sp = getSharedPreferences("SP_RING",MODE_PRIVATE);                     //创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String ringvalue = sp.getString("NUM","");
        ringnumber=Integer.valueOf(ringvalue);
        str=rings[ringnumber];
        ringname.setText(str);

    }





}

