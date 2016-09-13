package idv.tomazwang.app.pokemondraw;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by TomazWang on 2016/9/14.
 */

public class SettingFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);

        Preference creditSetting = (Preference) getPreferenceScreen().findPreference(getString(R.string.setting_key_show_credit));
        creditSetting.setOnPreferenceClickListener(
                preference -> {

                    // TODO: show dialog content credits.

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_credit, null, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog creditDialog = builder
                            .setView(view)
                            .setTitle(getString(R.string.setting_label_show_credit))
                            .create();
                    creditDialog.show();

                    return true;
                }
        );


    }

}
