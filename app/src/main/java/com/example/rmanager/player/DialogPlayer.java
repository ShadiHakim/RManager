package com.example.rmanager.player;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rmanager.R;
import com.example.rmanager.classes.Recording;

public class DialogPlayer extends Dialog {

    M4aPlayer player;

    private Activity activity;
    private Recording recording;
    private TextView textView_name;
    private TextView textView_date;
    private TextView textViewB;
    private TextView textViewE;
    private Button button_play_pause;
    private SeekBar seekBar;

    public DialogPlayer(Activity activity, Recording recording) {
        super(activity);
        this.activity = activity;
        this.recording = recording;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.player_dialog);
        textView_name = (TextView) findViewById(R.id.textView_Name);
        textView_date = (TextView) findViewById(R.id.textView_Date);
        textViewB = (TextView) findViewById(R.id.textViewB);
        textViewE = (TextView) findViewById(R.id.textViewE);
        button_play_pause = (Button) findViewById(R.id.button_Play_Pause);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        textView_name.setText(recording.get_contactName());
        textView_date.setText(recording.get_date());

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        player = new M4aPlayer(getContext(),recording.get_path(), textViewB, textViewE, button_play_pause, seekBar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pausePlaying();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

}
