package com.firstapp.sastamusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView txtsname, txtsstop;
    Button play, previous, next;
    SeekBar seekBar;
    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        play = (Button) findViewById(R.id.play);
        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);
        txtsname = (TextView) findViewById(R.id.txtsn);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }catch (InterruptedException | IllegalStateException e){e.printStackTrace();}
                }
            }
        };

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos",0);
        txtsname.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).toString();
        txtsname.setText(sname);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.teal_200), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.teal_200),PorterDuff.Mode.SRC_IN);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.pause();
                }
                else{
                    play.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                play.setBackgroundResource(R.drawable.icon_pause);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                play.setBackgroundResource(R.drawable.icon_pause);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next.performClick();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

       // String endTime = createTime(mediaPlayer.getDuration());
        //txtsstop.setText(endTime);
    }

    public String createTime(int duration){

        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";
        if (sec<10){
            time += "0";
            time+=sec;


        }
        return  time;
    }
}