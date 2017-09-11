/*******************************************************************************
 * Copyright (c) 2012 Manning
 * See the file license.txt for copying permission.
 ******************************************************************************/
package ca.six.ui.others.sectionhead;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ca.six.ui.others.R;

public class SectionAdapter extends ArrayAdapter<String> {

    private Activity activity;

    public SectionAdapter(Activity activity, String[] objects) {
        super(activity, R.layout.item_one_tv, R.id.label, objects);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_one_tv,
                    parent, false);
        }
        TextView header = (TextView) view.findViewById(R.id.header);
        String label = getItem(position);
        if (position == 0
                || getItem(position - 1).charAt(0) != label.charAt(0)) {
            header.setVisibility(View.VISIBLE);
            header.setText(label.substring(0, 1));
        } else {
            header.setVisibility(View.GONE);
        }
        return super.getView(position, view, parent);
    }
}
