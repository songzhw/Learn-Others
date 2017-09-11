package ca.six.ui.others.epoxy.model;

import android.support.annotation.VisibleForTesting;

import com.airbnb.epoxy.EpoxyModel;

import ca.six.ui.others.R;
import ca.six.ui.others.epoxy.view.HeaderView;

public class HeaderModel extends EpoxyModel<HeaderView> {

    private int title, caption;

    public HeaderModel(int title, int caption) {
        this.title = title;
        this.caption = caption;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_headview;
    }

    @Override
    public void bind(HeaderView view) {
        super.bind(view);
        view.setTitle(title);
        view.setCaption(caption);
    }
}