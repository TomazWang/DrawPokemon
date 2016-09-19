package idv.tomazwang.app.pokemondraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by TomazWang on 2016/9/19.
 */

public class SetTimerDialog extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    public static final int DEFAULT_TIME = 30;
    private static final int DEFAULT_MAX_TIME = 120;
    private static final int MIN = 15;
    private TextView mTxtSec;
    private SeekBar mSeekBar;
    private int mCurrent_time = DEFAULT_TIME;

    public SetTimerDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    public SetTimerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.dialog_set_timer);
    }

    public SetTimerDialog(Context context) {
        super(context);
    }

    public SetTimerDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onBindDialogView(View view) {

        super.onBindDialogView(view);

        SharedPreferences sp = getSharedPreferences();
        mCurrent_time = sp.getInt(getContext().getResources().getString(R.string.setting_key_set_time), DEFAULT_TIME);

        mTxtSec = (TextView)view.findViewById(R.id.txt_sec);
        mSeekBar = (SeekBar)view.findViewById(R.id.seekBar_timer);

        syncText();
        mSeekBar.setMax(0);
        mSeekBar.setMax(DEFAULT_MAX_TIME);
        mSeekBar.setProgress(mCurrent_time);

        mSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getContext().getResources().getString(R.string.setting_key_set_time), mCurrent_time);
            editor.commit();
        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        int sec = progress - progress%5;

        if(sec < MIN){
            sec = MIN;
        }

        mCurrent_time = sec;
        syncText();

    }

    private void syncText() {
        mTxtSec.setText(String.valueOf(mCurrent_time));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        // set minimum
        if(seekBar.getProgress() < MIN){
            seekBar.setProgress(MIN);
        }

    }





}
