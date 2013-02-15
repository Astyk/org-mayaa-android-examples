package org.mayaa.android.examples.preference.radio;

import java.util.List;

import android.preference.PreferenceActivity;

public class PreferenceMainActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(final List<Header> target) {
        loadHeadersFromResource(R.xml.preference, target);
    }
}
