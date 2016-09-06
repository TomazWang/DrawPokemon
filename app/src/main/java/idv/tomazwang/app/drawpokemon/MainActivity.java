package idv.tomazwang.app.drawpokemon;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import idv.tomazwang.app.drawpokemon.colorpicker.ColorPickerDialog;
import idv.tomazwang.app.drawpokemon.view.DrawingView;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_SIZE = 4;
    private static final int GAME_TIME = 30; //seconds.


    private DrawingView mDrawingView;
    private Spinner mSpinnerPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private int[] mColors;
    private int mSelectedColor = Color.BLACK;
    private ColorPickerDialog mColorPickerDialog;
    private ImageView mColorPickBtn;

    private static final int TIMER_STOP = 0;
    private static final int TIMER_RUNNING = 1;
    private static final int TIMER_PAUSE = 2;

    @IntDef({TIMER_STOP,TIMER_RUNNING,TIMER_PAUSE})
    public @interface TimerState{};

    @TimerState private int mTimerState = TIMER_STOP;

    private long mRemainTime = 0;
    private CountDownTimer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawingView = (DrawingView) findViewById(R.id.view_drawing_pannel);
        mSpinnerPokemonName = (Spinner) findViewById(R.id.spinner_pokemon_name);
        mPokemonPic = (ImageView) findViewById(R.id.iv_pokemon_pic);
        mTimerText = (TextView) findViewById(R.id.txt_timer);
        mColorPickBtn = (ImageView)findViewById(R.id.btn_color_picker);

        setupColorPicker();

    }


    @Override
    protected void onResume() {
        super.onResume();


        mPokemonPic.setOnClickListener(v -> {

            if(mTimerState == TIMER_STOP){
                startTimer(GAME_TIME);
            }else if(mTimerState == TIMER_PAUSE){
                resumeTimer();
            }

        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer();
    }



    private void startGame(){

        // TODO: show pokemon pic

        startTimer(GAME_TIME);


    }



    private void pauseGame(){
        // TODO: show pause dialog
    }


    private void stopGame(){
        // TODO: after time's up
    }

    private void startTimer(int sec) {

        if(mTimer != null){
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(sec*1000, 100){

            @Override
            public void onTick(long millisUntilFinished) {
                mTimerText.setText(""+millisUntilFinished/1000);
                mRemainTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                mTimerText.setText("0");

            }

        };

        mTimer.start();
        mTimerState = TIMER_RUNNING;
    }


    private void stopTimer(){
        if(mTimer != null){
            mTimer.cancel();
            mTimerText.setText("0");
            mTimerState = TIMER_STOP;
        }
    }


    private void pauseTimer(){
        if(mTimer != null){
            mTimer.cancel();
            mTimerState = TIMER_PAUSE;
        }
    }

    private void resumeTimer(){
        startTimer((int) (mRemainTime/1000));
    }




    private void showColorPicker() {

        if(mColorPickerDialog != null){
            mColorPickerDialog.show(getFragmentManager(),"color_picker");
            return;
        }

        mColorPickerDialog = ColorPickerDialog.newInstance(
                getResources().getString(R.string.default_color_picker_title),
                mColors,
                mSelectedColor,
                COLUMN_SIZE,
                Utils.isTablet(this) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL
        );

        mColorPickerDialog.setColorSelectedListener(this::colorSelected);
        mColorPickerDialog.show(getFragmentManager(), "color_picker");
    }

    private void colorSelected(int color) {

        mSelectedColor = color;
        setPickerColor(mSelectedColor);

    }

    private void setupColorPicker() {

        mColors = getResources().getIntArray(R.array.paint_colors);

        mColorPickBtn.setOnClickListener(view -> showColorPicker());
        setPickerColor(mSelectedColor);

    }


    protected void setPickerColor(int color) {

        mDrawingView.setPaintColor(mSelectedColor);
        mColorPickBtn.setImageDrawable(cDrawable(colorToDrawable(color)));

    }


    private int cColor(int id){
        return ContextCompat.getColor(this, id);
    }

    private Drawable cDrawable(int id){
        return ContextCompat.getDrawable(this, id);
    }

    private int colorToDrawable(int color){

        if(color == cColor(R.color.paint_red)){
            return R.drawable.painter_palette_red;
        }else if(color == cColor(R.color.paint_orange)){
           return R.drawable.painter_palette_orange;
        }else if(color == cColor(R.color.paint_yellow)){
            return R.drawable.painter_palette_yellow;
        }else if(color == cColor(R.color.paint_green_light)){
            return R.drawable.painter_palette_green_l;
        }else if(color == cColor(R.color.paint_green)){
            return R.drawable.painter_palette_green;
        }else if(color == cColor(R.color.paint_blue)){
            return R.drawable.painter_palette_blue;
        }else if(color == cColor(R.color.paint_blue_light)){
            return R.drawable.painter_palette_blue_l;
        }else if(color == cColor(R.color.paint_purple)){
            return R.drawable.painter_palette_purple;
        }else if(color == cColor(R.color.paint_pink)){
            return R.drawable.painter_palette_pink;
        }else if(color == cColor(R.color.paint_brown)){
            return R.drawable.painter_palette_brown;
        }else if(color == cColor(R.color.paint_gray)){
            return R.drawable.painter_palette_gray;
        }else if(color == cColor(R.color.paint_black)){
            return R.drawable.painter_palette_black;
        }

        return R.drawable.painter_palette_black;
    }

}
