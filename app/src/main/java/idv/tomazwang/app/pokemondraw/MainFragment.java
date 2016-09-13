package idv.tomazwang.app.pokemondraw;

import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import idv.tomazwang.app.pokemondraw.colorpicker.ColorPickerDialog;
import idv.tomazwang.app.pokemondraw.view.DrawingView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by TomazWang on 2016/9/13.
 */

public class MainFragment extends Fragment{

    private static final String TAG = MainFragment.class.getSimpleName();


    // -- widgets
    private DrawingView mDrawingView;
    private TextView mTxtPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private ImageView mPlayBtn;
    private ImageView mSettingBtn;
    private ImageView mInfoBtn;

    // color picker
    private ImageView mColorPickBtn;
    private ColorPickerDialog mColorPickerDialog;
    private int[] mColors;
    private int mSelectedColor = Color.BLACK;
    private static final int COLOR_PICKER_COLUMN_SIZE = 4;


    private int mGameTime = 45; //seconds.
    private static final String POKEMON_LIST_FILE = "pokedex.json";
    private static final String TAG_RESULT_DIALOG = "result_dialog";

    // -- dialog
    private ResultDialog mResultDialog;


    // -- time states
    public static final int TIMER_STOP = 0;
    public static final int TIMER_RUNNING = 1;
    public static final int TIMER_PAUSE = 2;
    @IntDef({TIMER_STOP, TIMER_RUNNING, TIMER_PAUSE})
    public @interface TimerState {

    }

    @TimerState
    private int mTimerState = TIMER_STOP;

    // -- game states
    private static final int GAME_RESET = 518;
    private static final int GAME_START = 126;
    private static final int GAME_PAUSE = 562;
    private static final int GAME_STOP = 578;

    @IntDef({GAME_RESET, GAME_START, GAME_PAUSE, GAME_STOP})
    public @interface GameFlag {
    }

    @GameFlag
    private int mGameFlag = GAME_STOP;


    private ArrayList<Pokemon> mPokemonList = new ArrayList<>();

    // --- this round
    private int mPokeIDThisRound;
    private String mPkmRawFileNameThisRound;
    private String mSaveFileNameThisRound;
    private Bitmap mUserDrawingBtimapThisRound;
    private Bitmap mResultBitmapThisRound;
    private File mResultFile;

    // -- flag
    private boolean isSaving = false;

    private long mRemainTime = 0;
    private CountDownTimer mTimer;


    private MainActivity mActivity;


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // get widgets
        mDrawingView = (DrawingView) view.findViewById(R.id.view_drawing_pannel);
        mTxtPokemonName = (TextView) view.findViewById(R.id.txt_pokemon_name);
        mPokemonPic = (ImageView) view.findViewById(R.id.iv_pokemon_pic);
        mTimerText = (TextView) view.findViewById(R.id.txt_timer);
        mColorPickBtn = (ImageView) view.findViewById(R.id.btn_color_picker);
        mPlayBtn = (ImageView) view.findViewById(R.id.btn_play);

        mSettingBtn = (ImageView) view.findViewById(R.id.btn_setting);
        mInfoBtn = (ImageView) view.findViewById(R.id.btn_info);


        mColorPickBtn.setVisibility(GONE);


        mActivity = (MainActivity)getActivity();

        setupColorPicker();


        mDrawingView.setDrawable(false);



        mTimerText.setText(String.valueOf(mGameTime));

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        readAllPokemon();

        mPlayBtn.setOnClickListener(v -> {

            MainActivity activity = (MainActivity)getActivity();


            int timeState = mTimerState;

            if (timeState == TIMER_STOP) {
                startGame();
            } else if (timeState == TIMER_PAUSE) {
                resumeGame();
            }

            hidePlayBtn();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGameFlag == GAME_START) {
            pauseGame();
        }
    }

    private void setupColorPicker() {

        mColors = getResources().getIntArray(R.array.paint_colors);

        mColorPickBtn.setOnClickListener(view -> showColorPicker());
        setPickerColor(mSelectedColor);

    }


    // ---- About Color Picker

    private void showColorPicker() {

        if (mColorPickerDialog != null) {
            mColorPickerDialog.show(getFragmentManager(), "color_picker");
            return;
        }

        mColorPickerDialog = ColorPickerDialog.newInstance(
                getResources().getString(R.string.default_color_picker_title),
                mColors,
                mSelectedColor,
                COLOR_PICKER_COLUMN_SIZE,
                Utils.isTablet(getActivity()) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL
        );

        mColorPickerDialog.setColorSelectedListener(this::colorSelected);
        mColorPickerDialog.show(getFragmentManager(), "color_picker");
    }


    private void closeColorPicker() {
        if (mColorPickerDialog != null) {
            mColorPickerDialog.dismiss();
        }
    }

    protected void setPickerColor(int color) {

        mDrawingView.setPaintColor(mSelectedColor);
        mColorPickBtn.setImageDrawable(cDrawable(colorToDrawable(color)));

    }

    private void colorSelected(int color) {
        mSelectedColor = color;
        setPickerColor(mSelectedColor);

    }


    private int cColor(int id) {
        return ContextCompat.getColor(getActivity(), id);
    }

    private Drawable cDrawable(int id) {
        return ContextCompat.getDrawable(getActivity(), id);
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

    // -- Play Btn

    public void showPlayBtn(){
        showPlayBtn(VISIBLE);
    }

    public void showPlayBtn(int visiblity) {
        mPlayBtn.setVisibility(visiblity);
        mSettingBtn.setVisibility(visiblity);
        mInfoBtn.setVisibility(visiblity);

        switch (visiblity){
            case GONE:
            case INVISIBLE:
                mColorPickBtn.setVisibility(VISIBLE);
                break;
            case VISIBLE:
                mColorPickBtn.setVisibility(GONE);
        }
    }

    private void hidePlayBtn() {
        showPlayBtn(GONE);
    }



    // -- About Game






    private void readAllPokemon() {
        try {


            AssetManager assetManager = getActivity().getAssets();
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

    public void resetGame() {
        mPokemonPic.setImageDrawable(cDrawable(R.drawable.pokeball));

        mDrawingView.cleanCanvas();
        mDrawingView.setDrawable(false);

        mTxtPokemonName.setText("???");

        mPokeIDThisRound = 0;
        mPkmRawFileNameThisRound = "";
        mSaveFileNameThisRound = null;

        mUserDrawingBtimapThisRound = null;
        mResultBitmapThisRound = null;

        closeColorPicker();

        mGameFlag = GAME_RESET;

        setPickerColor(cColor(R.color.paint_black));
    }


    public void startGame() {

        if (mGameFlag != GAME_RESET) {
            resetGame();
        }


        mPokeIDThisRound = (int) ((Math.random() * mPokemonList.size()) + 1);

        String pkmName = mPokemonList.get(mPokeIDThisRound).getName_zh();
        mTxtPokemonName.setText("#"+ mPokeIDThisRound + " " + pkmName);

        mPkmRawFileNameThisRound = mPokemonList.get(mPokeIDThisRound).getPic_name().split("\\.")[0];

        mSaveFileNameThisRound = getNewFileName(mPokeIDThisRound);

        Glide
                .with(this)
                .load("android.resource://idv.tomazwang.app.drawpokemon/raw/" + mPkmRawFileNameThisRound)
                .thumbnail(0.1f)
                .error(R.drawable.pokeball)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d(TAG, "onException: " + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mPokemonPic);

        mDrawingView.setDrawable(true);

        startTimer(mGameTime);
        mGameFlag = GAME_START;

    }


    private void pauseGame() {
        // TODO: show pause dialog

        pauseTimer();
        mGameFlag = GAME_PAUSE;

        mPlayBtn.setImageDrawable(cDrawable(R.drawable.btn_resume));

        showPlayBtn(View.VISIBLE);

        closeColorPicker();
    }

    public void resumeGame() {

        // TODO: check if pause dialog is showing.

        mDrawingView.setDrawable(true);

        resumeTimer();
        mGameFlag = GAME_START;
    }


    private void stopGame() {


        closeColorPicker();
        mDrawingView.drawLastLine();
        mDrawingView.setDrawable(false);

        mUserDrawingBtimapThisRound = getUserDrawingBitmap();


        mResultDialog = ResultDialog.newInstance(mSaveFileNameThisRound,this);
        mResultDialog.show(getFragmentManager(), TAG_RESULT_DIALOG);

        saveImage();
        stopTimer();

        mGameFlag = GAME_STOP;

    }


    public void playAgain() {
        resetGame();
        mPlayBtn.setImageDrawable(cDrawable(R.drawable.btn_play));

        showPlayBtn(View.VISIBLE);

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


    public Bitmap getUserDrawingBitmap() {

        if(mUserDrawingBtimapThisRound != null){
            return mUserDrawingBtimapThisRound;
        }

        if (mDrawingView != null) {

            mDrawingView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mDrawingView.getDrawingCache());
            mDrawingView.setDrawingCacheEnabled(false);
            mDrawingView.destroyDrawingCache();

            return bitmap;
        } else {
            return null;
        }
    }

    public String getCurrentPokemonRawFileName(){
        return mPkmRawFileNameThisRound;
    }


    private String getNewFileName(int pokemonID){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return "pd_"+ String.valueOf(pokemonID) + "_"+ timeStamp +".jpg";

    }


    private void saveImage() {
        Log.d(TAG, "saveImage: saving image");

        isSaving = true;

        Bitmap bitmapDrawing = getUserDrawingBitmap();

        String pkmRawFileName = getCurrentPokemonRawFileName();
        Log.d(TAG, "getResultImage: raw file name = "+pkmRawFileName);

        // get pokemon img bitmap from raw file.
        LoadImageFromRawTask task = new LoadImageFromRawTask(getActivity(),
                new Point(bitmapDrawing.getWidth()/4, bitmapDrawing.getHeight()/4), bitmaps -> {

            // refresh view after loading complete.
            Bitmap pkmBmp = bitmaps[0];
            Log.d(TAG, "getResultImage: get pkmBmp");
            mResultBitmapThisRound = Utils.combineImage(pkmBmp, bitmapDrawing);

            SaveImageTask saveImageTask = new SaveImageTask(getActivity(), mSaveFileNameThisRound, filePath -> onSaveComplete(filePath));
            saveImageTask.execute(mResultBitmapThisRound);

        });

        task.execute(pkmRawFileName);

    }

    private File onSaveComplete(File filePath) {

        if (filePath == null) {
            Log.w(TAG, "onSaveComplete: file dir error");
            return null;
        }

        Toast saveCompleteMsg = Toast.makeText(getActivity(), getString(R.string.saveComplete), Toast.LENGTH_SHORT);
        saveCompleteMsg.show();
        Log.d(TAG, "onSaveComplete: save image in " + filePath);


        MediaScannerConnection.scanFile(
                getActivity(),
                new String[]{filePath.getAbsolutePath()},
                null,
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        Log.d(TAG, "onMediaScannerConnected");
                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG, "onScanCompleted");
                    }
                }
        );

        isSaving = false;

        mResultFile = filePath;
        mResultDialog.notifyImageComplete();
        return filePath;
    }


    public File getResultFile() {
        return mResultFile;
    }

    public boolean isSaving() {
        return isSaving;
    }



}
