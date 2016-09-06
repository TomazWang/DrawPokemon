package idv.tomazwang.app.drawpokemon;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import idv.tomazwang.app.drawpokemon.colorpicker.ColorPickerDialog;
import idv.tomazwang.app.drawpokemon.view.DrawingView;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_SIZE = 4;
    private static final int GAME_TIME = 30; //seconds.
    private static final String POKEMON_LIST_FILE = "pokedex.json";
    private static final String TAG_RESULT_DIALOG = "result_dialog";
    private static final String TAG = MainActivity.class.getSimpleName();


    private DrawingView mDrawingView;
    private Spinner mSpinnerPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private ColorPickerDialog mColorPickerDialog;
    private ImageView mColorPickBtn;
    private Button mPlayBtn;

    private int[] mColors;
    private int mSelectedColor = Color.BLACK;

    private static final int TIMER_STOP = 0;
    private static final int TIMER_RUNNING = 1;
    private static final int TIMER_PAUSE = 2;
    private ArrayList<Pokemon> mPokemonList = new ArrayList<>();


    @IntDef({TIMER_STOP, TIMER_RUNNING, TIMER_PAUSE})
    public @interface TimerState {
    }

    @TimerState
    private int mTimerState = TIMER_STOP;


    private static final int GAME_RESET = 518;
    private static final int GAME_START = 126;
    private static final int GAME_PAUSE = 562;
    private static final int GAME_STOP = 578;

    @IntDef({GAME_RESET, GAME_START, GAME_PAUSE, GAME_STOP})
    public @interface GameFlag{}

    @GameFlag
    private int mGameFlag = GAME_STOP;

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
        mColorPickBtn = (ImageView) findViewById(R.id.btn_color_picker);
        mPlayBtn = (Button)findViewById(R.id.btn_play);

        setupColorPicker();
    }


    @Override
    protected void onResume() {
        super.onResume();

        readAllPokemon();

        mPlayBtn.setOnClickListener(v -> {

            if (mTimerState == TIMER_STOP) {
                startGame();
            } else if (mTimerState == TIMER_PAUSE) {
                resumeGame();
            }

            mPlayBtn.setVisibility(View.GONE);

        });

    }

    private void readAllPokemon() {
        try {


            AssetManager assetManager = this.getAssets();
            InputStream is = assetManager.open(POKEMON_LIST_FILE);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            String bufferString = new String(buffer, "UTF-8");

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Pokemon>>() {
            }.getType();

            ArrayList<Pokemon> pokemonList = gson.fromJson(bufferString, listType);

            mPokemonList.addAll(pokemonList);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    public void resetGame(){
        mPokemonPic.setImageDrawable(cDrawable(R.drawable.pokeball));

        mDrawingView.cleanCanvas();
        mDrawingView.setDrawable(false);

        if(mColorPickerDialog != null) {
            mColorPickerDialog.dismiss();
        }

        mGameFlag = GAME_RESET;
    }

    private void startGame() {

        if(mGameFlag != GAME_RESET){
            resetGame();
        }


        int pokeID = (int) ((Math.random() * mPokemonList.size()) + 1);

        String rawFileName = mPokemonList.get(pokeID).getPic_name().split("\\.")[0];

        Glide
                .with(this)
                .load("android.resource://idv.tomazwang.app.drawpokemon/raw/" + rawFileName)
                .thumbnail(0.1f)
                .error(R.drawable.pokeball)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onException: "+e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mPokemonPic);

        mDrawingView.setDrawable(true);

        startTimer(GAME_TIME);
        mGameFlag = GAME_START;

    }


    private void pauseGame() {
        // TODO: show pause dialog

        pauseTimer();
        mGameFlag = GAME_PAUSE;

        mPlayBtn.setVisibility(View.VISIBLE);
        mPlayBtn.setText(getResources().getString(R.string.resume));

        if(mColorPickerDialog != null) {
            mColorPickerDialog.dismiss();
        }
    }

    private void resumeGame(){

        // TODO: check if pause dialog is showing.

        mDrawingView.setDrawable(true);

        resumeTimer();
        mGameFlag = GAME_START;
    }


    private void stopGame() {

        mDrawingView.drawLastLine();

        ResultDialog.newInstance().show(getFragmentManager(),TAG_RESULT_DIALOG);

        stopTimer();
        mDrawingView.setDrawable(false);

        mGameFlag = GAME_STOP;

    }


    public void playAgain () {
        resetGame();
        mPlayBtn.setVisibility(View.VISIBLE);
        mPlayBtn.setText(getString(R.string.paly));
    }


    private void startTimer(int sec) {

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(sec * 1000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimerText.setText("" + millisUntilFinished / 1000);
                mRemainTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                stopGame();
            }

        };

        mTimer.start();
        mTimerState = TIMER_RUNNING;
    }


    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimerText.setText("0");
        mTimerState = TIMER_STOP;
    }


    private void pauseTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimerState = TIMER_PAUSE;
        }
    }

    private void resumeTimer() {
        startTimer((int) (mRemainTime / 1000));
    }


    public Bitmap getResultBitmap() {

        if (mDrawingView != null) {
            return mDrawingView.getBitmap();
        } else {
            return null;
        }
    }


    private void showColorPicker() {

        if (mColorPickerDialog != null) {
            mColorPickerDialog.show(getFragmentManager(), "color_picker");
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


    private int cColor(int id) {
        return ContextCompat.getColor(this, id);
    }

    private Drawable cDrawable(int id) {
        return ContextCompat.getDrawable(this, id);
    }

    private int colorToDrawable(int color) {

        if (color == cColor(R.color.paint_red)) {
            return R.drawable.painter_palette_red;
        } else if (color == cColor(R.color.paint_orange)) {
            return R.drawable.painter_palette_orange;
        } else if (color == cColor(R.color.paint_yellow)) {
            return R.drawable.painter_palette_yellow;
        } else if (color == cColor(R.color.paint_green_light)) {
            return R.drawable.painter_palette_green_l;
        } else if (color == cColor(R.color.paint_green)) {
            return R.drawable.painter_palette_green;
        } else if (color == cColor(R.color.paint_blue)) {
            return R.drawable.painter_palette_blue;
        } else if (color == cColor(R.color.paint_blue_light)) {
            return R.drawable.painter_palette_blue_l;
        } else if (color == cColor(R.color.paint_purple)) {
            return R.drawable.painter_palette_purple;
        } else if (color == cColor(R.color.paint_pink)) {
            return R.drawable.painter_palette_pink;
        } else if (color == cColor(R.color.paint_brown)) {
            return R.drawable.painter_palette_brown;
        } else if (color == cColor(R.color.paint_gray)) {
            return R.drawable.painter_palette_gray;
        } else if (color == cColor(R.color.paint_black)) {
            return R.drawable.painter_palette_black;
        }

        return R.drawable.painter_palette_black;
    }

}
