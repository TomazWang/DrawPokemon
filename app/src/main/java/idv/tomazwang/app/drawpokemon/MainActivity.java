package idv.tomazwang.app.drawpokemon;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import idv.tomazwang.app.drawpokemon.colorpicker.ColorPickerDialog;
import idv.tomazwang.app.drawpokemon.view.DrawingView;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_SIZE = 4;


    private RecyclerView mPainColorList;
    private DrawingView mDrawingView;
    private Spinner mSpinnerPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private int[] mColors;
    private int mSelectedColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPainColorList = (RecyclerView)findViewById(R.id.list_paint_color);
        mDrawingView = (DrawingView)findViewById(R.id.view_drawing_pannel);
        mSpinnerPokemonName = (Spinner)findViewById(R.id.spinner_pokemon_name);
        mPokemonPic = (ImageView)findViewById(R.id.iv_pokemon_pic);
        mTimerText = (TextView)findViewById(R.id.txt_timer);

        setupColorPicker();



    }


    @Override
    protected void onResume() {
        super.onResume();

        Button btn_pick_color = (Button)findViewById(R.id.btn_color_picker);
        btn_pick_color.setOnClickListener(view -> showColorPicker());


    }

    private void showColorPicker() {

        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(
                getResources().getString(R.string.default_color_picker_title),
                mColors,
                mSelectedColor,
                COLUMN_SIZE,
                isTablet(this)? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL
        );

        colorPickerDialog.setColorSelectedListener(this::colorSelected);
        colorPickerDialog.show(getFragmentManager(), "color_picker");
    }

    private void colorSelected(int color) {

        mSelectedColor = color;
        mDrawingView.setPaintColor(mSelectedColor);

    }

    private void setupColorPicker() {

        mColors = getResources().getIntArray(R.array.paint_colors);

    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


}
