package idv.tomazwang.app.pokemondraw;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import static android.content.ContentValues.TAG;


/**
 * Created by TomazWang on 2016/9/14.
 */

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference creditSetting = getPreferenceScreen().findPreference(getString(R.string.setting_key_show_credit));
        creditSetting.setOnPreferenceClickListener(
                preference -> {

                    CharSequence credit = getText(R.string.credits);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog creditDialog = builder
                            .setTitle(getString(R.string.setting_label_show_credit))
                            .setMessage(credit)
                            .create();

                    creditDialog.show();

                    return true;
                });

        Preference timeSetting = getPreferenceScreen().findPreference(getString(R.string.setting_key_set_time));
        SharedPreferences sp = timeSetting.getSharedPreferences();
        String timeString = String.valueOf(sp.getInt(getString(R.string.setting_key_set_time),SetTimerDialog.DEFAULT_TIME));
        String timeSummary = String.format(getString(R.string.format_time_summary), timeString);
        timeSetting.setSummary(timeSummary);



    }



    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: something changed "+key);

        Preference pref = findPreference(key);

        if(pref instanceof SetTimerDialog){
            String time = String.valueOf(sharedPreferences.getInt(key,SetTimerDialog.DEFAULT_TIME));
            String summary = String.format(getString(R.string.format_time_summary), time);

            Log.d(TAG, "onSharedPreferenceChanged: summary = "+summary);

            pref.setSummary(summary);
        }
    }
}


