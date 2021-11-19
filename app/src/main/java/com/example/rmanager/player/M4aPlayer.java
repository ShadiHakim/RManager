package com.example.rmanager.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.io.IOException;

public class M4aPlayer extends Handler{

    private Context context;
    private MediaPlayer player;
    private String source;
    private int duration;
    private int Interval = 100;

    private TextView textViewB;
    private TextView textViewE;
    private Button play_pause;
    private SeekBar seekBar;

    public M4aPlayer(Context context, String source, TextView textViewB, TextView textViewE, Button play_pause, SeekBar seekBar) {
        this.context = context;
        this.source = source;
        this.textViewB = textViewB;
        this.textViewE = textViewE;
        this.play_pause = play_pause;
        this.seekBar = seekBar;

        setupPlayer();
    }

    public void setupPlayer() {
        if (player != null){
            stopPlaying();
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(source);
            player.prepare();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.seekTo(0);
                    play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));

                }
            });

            duration = player.getDuration();
            seekBar.setMax(this.duration);
        } catch (IOException e) {
            Log.e("ChatAdapter_startPlay", "prepare() failed");
        }

        this.play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPausePlaying();
            }
        });

        this.seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            player.seekTo(progress);
                            seekBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (player.isPlaying()){
                            player.pause();
                            play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        player.seekTo(player.getCurrentPosition());
                        player.start();
                        play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
                    }
                }
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (player != null) {
                    try {
                        Message msg = new Message();
                        msg.what = player.getCurrentPosition();
                        sendMessage(msg);
                        Thread.sleep(Interval);
                    } catch (InterruptedException e) { }
                }
            }
        }).start();

        playPausePlaying();
    }

    public void playPausePlaying(){
        if (player.isPlaying()){
            player.pause();
            play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
        }
        else {
            player.seekTo(player.getCurrentPosition());
            player.start();
            play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_pause));
        }
    }

    public void pausePlaying(){
        if (player.isPlaying()){
            player.pause();
            play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
        }
    }

    public void stopPlaying() {
        player.stop();
        player.release();
        player = null;
        play_pause.setBackground(ContextCompat.getDrawable(context, android.R.drawable.ic_media_play));
    }

    @Override
    public void handleMessage(Message msg) {
        int currentPosition = msg.what;

        seekBar.setProgress(currentPosition);

        // Update Labels.
        String elapsedTime = createTimeLabel(currentPosition);
        textViewB.setText(elapsedTime);

        String remainingTime = createTimeLabel(duration-currentPosition);
        textViewE.setText("- " + remainingTime);
    }

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }
}
