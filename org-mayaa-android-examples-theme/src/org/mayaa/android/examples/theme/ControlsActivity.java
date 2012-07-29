package org.mayaa.android.examples.theme;

import org.mayaa.android.examples.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * A gallery of basic controls: Button, EditText, RadioButton, Checkbox,
 * Spinner. This example uses the light theme.
 */
public class ControlsActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls);

        final Button disabledButton = (Button) findViewById(R.id.button_disabled);
        disabledButton.setEnabled(false);

        final Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        final ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
    }

    private static final String[] mStrings = {
        "Mercury",
        "Venus",
        "Earth",
        "Mars",
        "Jupiter",
        "Saturn",
        "Uranus",
        "Neptune" };
}
