/*******************************************************************************
 * Copyright (c) 2012 Manning
 * See the file license.txt for copying permission.
 ******************************************************************************/
package ca.six.ui.others.sectionhead;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.TextView;

import ca.six.ui.others.R;

public class SectionHeadTestActivity extends ListActivity {

    private TextView topHeader;
    private int topVisiblePosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_head);
        topHeader = (TextView) findViewById(R.id.header);
        setListAdapter(new SectionAdapter(this, Countries.COUNTRIES));
        getListView().setOnScrollListener(
                new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // Empty.
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (firstVisibleItem != topVisiblePosition) {
                            topVisiblePosition = firstVisibleItem;
                            setTopHeader(firstVisibleItem);
                        }
                    }
                });
        setTopHeader(0);
    }

    private void setTopHeader(int pos) {
        final String text = Countries.COUNTRIES[pos].substring(0, 1);
        topHeader.setText(text);
    }
}
