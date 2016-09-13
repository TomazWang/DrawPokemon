package idv.tomazwang.app.pokemondraw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static final String MF_TAG = "Main_Fragment";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ST_TAG = "Setting_Fragment";
    private MainFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment = MainFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, mFragment, MF_TAG)
                .addToBackStack(null)
                .commit();


    }


    public void startSetting(){

        SettingFragment settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.container_main, settingFragment, ST_TAG).commit();

    }




}
