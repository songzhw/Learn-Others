package ca.six.ui.others.m_intro_view.a_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.six.ui.others.R;
import ca.six.ui.others.m_intro_view.MaterialIntroView;
import ca.six.ui.others.m_intro_view.shape.Focus;
import ca.six.ui.others.m_intro_view.shape.FocusGravity;
import ca.six.ui.others.m_intro_view.util.PreferencesManager;


/**
 * Created by mertsimsek on 31/01/16.
 */
public class IntroMainFragment extends Fragment implements View.OnClickListener{

    private static final String INTRO_CARD = "material_intro";

    private CardView cardView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_main, container, false);
        cardView = (CardView) view.findViewById(R.id.my_card);
        button = (Button) view.findViewById(R.id.button_reset_all);
        button.setOnClickListener(this);

        //Show intro
        showIntro(cardView, INTRO_CARD, "This is card! Hello There. You can set this text!");

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.button_reset_all)
            new PreferencesManager(getActivity().getApplicationContext()).resetAll();
    }

    private void showIntro(View view, String usageId, String text){
        new MaterialIntroView.Builder(getActivity())
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(usageId) //THIS SHOULD BE UNIQUE ID
                .show();
    }
}
