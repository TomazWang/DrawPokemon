package idv.tomazwang.app.drawpokemon;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import idv.tomazwang.app.drawpokemon.view.DrawingView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPainColorList;
    private DrawingView mDrawingView;
    private Spinner mSpinnerPokemonName;
    private ImageView mPokemonPic;
    private TextView mTimerText;
    private static ArrayList<Integer> sColorList;

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
    }

    private void setupColorPicker() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPainColorList.setLayoutManager(layoutManager);

        TypedArray colorArray = getResources().obtainTypedArray(R.array.paint_colors);

        for(int i = 0; i<colorArray.length(); i++){
            sColorList.add(colorArray.getColor(i,0));
        }

        ColorPickerListAdapter adapter = new ColorPickerListAdapter(sColorList);



    }



}
