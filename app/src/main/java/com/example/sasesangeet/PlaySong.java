package com.example.sasesangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
TextView textView;
SeekBar seekbar;
ImageView play,next,previous;
MediaPlayer mediaPlayer;
String textcontent;
ArrayList<File> songs;
int position;
Thread updateSeek;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        play =findViewById(R.id.play);
        next =findViewById(R.id.next);
        previous =findViewById(R.id.previous);
        textView =findViewById(R.id.textView);
        seekbar =findViewById(R.id.seekBar);
        Intent in =getIntent();
        Bundle bundle =in.getExtras();
        songs =(ArrayList)bundle.getParcelableArrayList("songList");
        textcontent =in.getStringExtra("c");
        textView.setText(textcontent);
        textView.setSelected(true);
        position =in.getIntExtra("position", 0);
        Uri uri =Uri.parse(songs.get(position).toString());
        mediaPlayer =MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek =new Thread(){
            @Override
            public void run() {
               int currentPosition =0;
               try{
                   while(currentPosition<mediaPlayer.getDuration()){
                       currentPosition =mediaPlayer.getCurrentPosition();
                       seekbar.setProgress(currentPosition);
                       sleep(800);
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }

            }
        };updateSeek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position =position -1;
                }
                else{
                    position = songs.size()-1 ;

                }
                play.setImageResource(R.drawable.pause);
                Uri uri =Uri.parse(songs.get(position).toString());
                mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekbar.setMax(mediaPlayer.getDuration());
                textcontent =songs.get(position).getName().toString();
                textView.setText(textcontent);
            }
        });
     next.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             mediaPlayer.stop();
             mediaPlayer.release();
             if(position!=songs.size()-1){
                 position =position +1;
             }
             else{
                 position =0;
             }
             play.setImageResource(R.drawable.pause);
             Uri uri =Uri.parse(songs.get(position).toString());
             mediaPlayer =MediaPlayer.create(getApplicationContext(),uri);
             mediaPlayer.start();
             seekbar.setMax(mediaPlayer.getDuration());
             textcontent =songs.get(position).getName().toString();
             textView.setText(textcontent);
         }
     });

    }
}