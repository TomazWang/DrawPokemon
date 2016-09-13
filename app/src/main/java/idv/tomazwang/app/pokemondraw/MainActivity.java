package idv.tomazwang.app.pokemondraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static final String MF_TAG = "Main_Fragment";
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment = MainFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container_main, mFragment, MF_TAG).commit();


    }


    public void startSetting(){

    }




}
