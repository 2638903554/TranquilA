package me.wcy.music.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import me.wcy.music.R;

public class AlarmAlert extends Activity
{
    private MediaPlayer mediaPlayer;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this, R.raw.clockmusic2 );
        String ring=this.getIntent().getStringExtra("RING");
        switch(ring){
            case "1th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_1 );
                break;
            case "2th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_2 );
                break;
            case "3th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_3 );
                break;
            case "4th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_4 );
                break;
            case "5th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_5 );
                break;
            case "6th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_6 );
                break;
            case "7th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_7 );
                break;
            case "8th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_8 );
                break;
            case "9th":
                mediaPlayer = MediaPlayer.create(this, R.raw.music_9 );
                break;
            default:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_1 );
                break;

        }
        mediaPlayer.start();
        alertDialog =new AlertDialog.Builder(AlarmAlert.this)
                .setTitle("智慧闹钟")
                .setMessage("小主人，时间到了，快起来吧!!!")
                .setPositiveButton("关掉它",
                        (dialog, whichButton) -> {
                            mediaPlayer.stop();
                            finish();
                        })
                .show();


    }
}

