package idv.tomazwang.app.drawpokemon;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import idv.tomazwang.app.drawpokemon.colorpicker.ColorPickerDialog;
import idv.tomazwang.app.drawpokemon.view.DrawingView;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_SIZE = 4;


    private DrawingView mDrawingView;
    private Spinner mSpinnerPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private int[] mColors;
    private int mSelectedColor = Color.BLACK;
    private ColorPickerDialog mColorPickerDialog;
    private ImageView mColorPickBtn;

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

        if(color == cColor(R.color.paint_red)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_red));
        }else if(color == cColor(R.color.paint_orange)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_orange));
        }else if(color == cColor(R.color.paint_yellow)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_yellow));
        }else if(color == cColor(R.color.paint_green_light)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_green_l));
        }else if(color == cColor(R.color.paint_green)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_green));
        }else if(color == cColor(R.color.paint_blue)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_blue));
        }else if(color == cColor(R.color.paint_blue_light)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_blue_l));
        }else if(color == cColor(R.color.paint_purple)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_purple));
        }else if(color == cColor(R.color.paint_pink)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_pink));
        }else if(color == cColor(R.color.paint_brown)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_brown));
        }else if(color == cColor(R.color.paint_gray)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_gray));
        }else if(color == cColor(R.color.paint_black)){
            mColorPickBtn.setImageDrawable(cDrawable(R.drawable.painter_palette_black));
        }


    }


    private int cColor(int id){
        return ContextCompat.getColor(this, id);
    }

    private Drawable cDrawable(int id){
        return ContextCompat.getDrawable(this, id);
    }



}
