package org.mayaa.android.examples;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Examples extends ListActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        String path = intent.getStringExtra("org.mayaa.android.examples.Path");

        if (path == null) {
            path = "";
        }

        setListAdapter(new SimpleAdapter(
            this,
            getData(path),
            android.R.layout.simple_list_item_1,
            new String[] { "title" },
            new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);
    }

    protected List<Map<String, Object>> getData(final String prefix) {
        final List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory("android.intent.category.MAYAA_SAMPLE_CODE");

        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list) {
            return myData;
        }

        String[] prefixPath;
        String prefixWithSlash = prefix;

        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }

        final int len = list.size();

        final Map<String, Boolean> entries = new HashMap<String, Boolean>();

        for (int i = 0; i < len; i++) {
            final ResolveInfo info = list.get(i);
            final CharSequence labelSeq = info.loadLabel(pm);
            final String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;

            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {

                final String[] labelPath = label.split("/");

                final String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(
                        myData,
                        nextLabel,
                        activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix
                            + "/"
                            + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);

        return myData;
    }

    private final static Comparator<Map<String, Object>> sDisplayNameComparator =
        new Comparator<Map<String, Object>>() {
            private final Collator collator = Collator.getInstance();

            @Override
            public int compare(final Map<String, Object> map1, final Map<String, Object> map2) {
                return collator.compare(map1.get("title"), map2.get("title"));
            }
        };

    protected Intent activityIntent(final String pkg, final String componentName) {
        final Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browseIntent(final String path) {
        final Intent result = new Intent();
        result.setClass(this, Examples.class);
        result.putExtra("org.mayaa.android.examples.Path", path);
        return result;
    }

    protected void addItem(final List<Map<String, Object>> data, final String name, final Intent intent) {
        final Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        final Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);

        final Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
