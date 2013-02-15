package org.mayaa.android.examples.preference.radio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

public class PreferenceAccountFragment extends PreferenceFragment {

    private PreferenceScreen mRootPreferenceScreen;

    private String mSelectAccount;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootPreferenceScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        setPreferenceScreen(mRootPreferenceScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSelectAccount = getSelectAccount(getActivity());
        removeAllPreferenceScreen();
        buildAccountsPreferences();
    }

    private void removeAllPreferenceScreen() {
        mRootPreferenceScreen.removeAll();
    }

    private void buildAccountsPreferences() {
        for (final String account : Constants.ACCOUNTS) {
            final Preference preference = buildAccountPreferences(account);
            mRootPreferenceScreen.addPreference(preference);
        }
    }

    private Preference buildAccountPreferences(final String account) {
        final CheckBoxPreference p = new CheckBoxPreference(getActivity());

        p.setTitle(account);

        final boolean isChecked = TextUtils.equals(mSelectAccount, account);
        p.setChecked(isChecked);

        return p;
    }

    private String getSelectAccount(final Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_account_key), null);
    }

    private void setSelectAccount(final Context context, final String value) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_account_key), value);
        editor.commit();
    }
}
