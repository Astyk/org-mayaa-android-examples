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
        mRootPreferenceScreen = getPreferenceManager().createPreferenceScreen(getActivity());
        setPreferenceScreen(mRootPreferenceScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSelectAccount();
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
        final CheckBoxPreference p = new CheckBoxPreference(getActivity(), null, R.attr.accountCheckBoxPreferenceStyle);

        p.setTitle(account);

        final boolean isChecked = TextUtils.equals(mSelectAccount, account);
        p.setChecked(isChecked);

        p.setOnPreferenceChangeListener(mOnPreferenceChangeListener);

        return p;
    }

    private void loadSelectAccount() {
        mSelectAccount = getSelectAccount(getActivity());
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

    private final Preference.OnPreferenceChangeListener mOnPreferenceChangeListener =
        new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                // true -> false ignore
                if (Boolean.FALSE.equals(newValue)) {
                    return false;
                }

                // save account
                final String account = String.valueOf(preference.getTitle());
                setSelectAccount(getActivity(), account);

                // refresh accounts preference screen
                loadSelectAccount();
                removeAllPreferenceScreen();
                buildAccountsPreferences();

                return true;
            }
        };
}
