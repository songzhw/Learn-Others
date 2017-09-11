package ca.six.ui.others.m_intro_view.a_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.six.ui.others.R;
import ca.six.ui.others.m_intro_view.MaterialIntroView;
import ca.six.ui.others.m_intro_view.animation.MaterialIntroListener;
import ca.six.ui.others.m_intro_view.shape.Focus;
import ca.six.ui.others.m_intro_view.shape.FocusGravity;


/**
 * Created by mertsimsek on 31/01/16.
 */
public class GravityFragment extends Fragment implements MaterialIntroListener {

    private static final String INTRO_CARD1 = "intro_card_1";
    private static final String INTRO_CARD2 = "intro_card_2";
    private static final String INTRO_CARD3 = "intro_card_3";

    CardView cardView1;
    CardView cardView2;
    CardView cardView3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gravity, container, false);

        cardView1 = (CardView) view.findViewById(R.id.my_card);
        cardView2 = (CardView) view.findViewById(R.id.my_card2);
        cardView3 = (CardView) view.findViewById(R.id.my_card3);

        showIntro(cardView1, INTRO_CARD1, "This intro focuses on RIGHT", FocusGravity.RIGHT);

        return view;
    }

    @Override
    public void onUserClicked(String materialIntroViewId) {
        if(materialIntroViewId == INTRO_CARD1)
            showIntro(cardView2, INTRO_CARD2, "This intro focuses on CENTER.", FocusGravity.CENTER);
        if(materialIntroViewId == INTRO_CARD2)
            showIntro(cardView3, INTRO_CARD3, "This intro focuses on LEFT.", FocusGravity.LEFT);
    }

    public void showIntro(View view, String id, String text, FocusGravity focusGravity){
        new MaterialIntroView.Builder(getActivity())
                .enableDotAnimation(true)
                .setFocusGravity(focusGravity)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setListener(this)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }
}
