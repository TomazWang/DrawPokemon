package idv.tomazwang.app.drawpokemon.colorpicker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import idv.tomazwang.app.drawpokemon.R;

/**
 * Created by TomazWang on 2016/9/5.
 */

public class ColorPickerDialog extends DialogFragment implements OnColorSelectedListener {


    public static final int SIZE_LARGE = 0;
    public static final int SIZE_SMALL = 1;


    @IntDef({SIZE_LARGE,SIZE_SMALL})
    public @interface SIZE{

    }


    private static final String KEY_TITLE = "KEY_TITLE";
    private static final String KEY_COLUMNS = "KEY_COLUMNS";
    private static final String KEY_SIZE = "KEY_SIZE";
    private static final String KEY_COLORS = "KEY_COLORS";
    private static final String KEY_SELECTED_COLOR = "KEY_SELECTED_COLOR";

    protected String mTitle = "Color Picker";
    protected int[] mColors = null;
    protected int mSelectedColor;
    protected int mColumns;
    protected int mSize;

    private OnColorSelectedListener mListener;

    private ColorPickerPalette mPalette;
    private ProgressBar mProgress;


    public ColorPickerDialog() {
        super();
        // Empty constructor required for dialog fragments.
    }


    public static ColorPickerDialog newInstance(String title, int[] colors, int selectedColor, int colums, int size){

        ColorPickerDialog ret = new ColorPickerDialog();
        ret.initialize(title, colors, selectedColor, colums, size);
        return ret;
    }

    private void initialize(String title, int[] colors, int selectedColor, int colums, int size) {
        setArguments(title, colums, size);
        setColors(colors, selectedColor);


    }

    private void setArguments(String title, int colums, int size) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_COLUMNS, colums);
        bundle.putInt(KEY_SIZE, size);
        setArguments(bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(KEY_TITLE);
            mColumns = getArguments().getInt(KEY_COLUMNS);
            mSize = getArguments().getInt(KEY_SIZE);
        }

        if (savedInstanceState != null) {
            mColors = savedInstanceState.getIntArray(KEY_COLORS);
            mSelectedColor = (Integer) savedInstanceState.getSerializable(KEY_SELECTED_COLOR);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.color_picker_dialog, null);
        mProgress = (ProgressBar) view.findViewById(android.R.id.progress);
        mPalette = (ColorPickerPalette) view.findViewById(R.id.color_picker);
        mPalette.init(mSize, mColumns, this);

        if (mColors != null) {
            showPaletteView();
        }

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(mTitle)
                .setView(view)
                .create();

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return alertDialog;
    }


    @Override
    public void onColorSelected(int color) {
        if (mListener != null) {
            mListener.onColorSelected(color);
        }

        if (getTargetFragment() instanceof OnColorSelectedListener) {
            final OnColorSelectedListener listener =
                    (OnColorSelectedListener) getTargetFragment();
            listener.onColorSelected(color);
        }

        if (color != mSelectedColor) {
            mSelectedColor = color;
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mPalette.drawPalette(mColors, mSelectedColor);
        }

        dismiss();
    }


    public void showPaletteView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.GONE);
            refreshPalette();
            mPalette.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBarView() {
        if (mProgress != null && mPalette != null) {
            mProgress.setVisibility(View.VISIBLE);
            mPalette.setVisibility(View.GONE);
        }
    }


    public void setColors(int[] colors, int selectedColor){
        setColors(colors);
        setSeletedColor(selectedColor);
    }

    private void setSeletedColor(int selectedColor) {
        if(mSelectedColor != selectedColor){
            mSelectedColor = selectedColor;
        }
    }


    public void setColors(int[] colors){
        if(mColors != colors){
            mColors = colors;
            refreshPalette();
        }
    }

    public void setColorSelectedListener(OnColorSelectedListener mListener) {
        this.mListener = mListener;
    }

    private void refreshPalette() {
        if(mPalette != null && mColors != null){
            mPalette.drawPalette(mColors, mSelectedColor);
        }
    }

    public int[] getColors(){ return mColors;}

    public int getSelectedColor(){ return mSelectedColor;}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_COLORS, mColors);
        outState.putInt(KEY_SELECTED_COLOR, mSelectedColor);
    }
}
