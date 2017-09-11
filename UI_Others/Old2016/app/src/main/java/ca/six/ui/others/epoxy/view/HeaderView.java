package ca.six.ui.others.epoxy.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.six.ui.others.R;

public class HeaderView extends LinearLayout {
    TextView title, caption;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.view_header, this);

        title = (TextView) findViewById(R.id.title_text);
        caption = (TextView) findViewById(R.id.caption_text);
    }

    public void setTitle(@StringRes int title) {
        this.title.setText(title);
    }

    public void setCaption(@StringRes int caption) {
        this.caption.setText(caption);
    }
}