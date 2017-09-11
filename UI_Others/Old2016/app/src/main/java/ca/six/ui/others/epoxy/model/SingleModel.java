package ca.six.ui.others.epoxy.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.airbnb.epoxy.SimpleEpoxyModel;

import ca.six.ui.others.R;


public class SingleModel extends SimpleEpoxyModel {
    @DrawableRes
    int background;

    public SingleModel() {
        super(R.layout.view_single);
    }

    @Override
    public void bind(View view) {
        super.bind(view);
        view.setBackgroundResource(background);
    }


    // ============== Mandatory Part ==============

    public void setBackground(int background) {
        this.background = background;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SingleModel that = (SingleModel) o;

        return background == that.background;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + background;
        return result;
    }

    @Override
    public String toString() {
        return "SingleModel{" +
                "background=" + background +
                '}' + super.toString();
    }
}
