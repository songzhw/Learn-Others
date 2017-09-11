package ca.six.ui.others.epoxy.model;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;

import ca.six.ui.others.R;


public class MyTextViewHolder extends EpoxyHolder {
    TextView tvLeft;
    TextView tvRight;

    @Override
    protected void bindView(View itemView) {
        tvLeft = (TextView) itemView.findViewById(R.id.tvLeft);
        tvRight = (TextView) itemView.findViewById(R.id.tvRight);
    }
}
