package ca.six.ui.others.epoxy;

import com.airbnb.epoxy.EpoxyAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.epoxy.model.HeaderModel;
import ca.six.ui.others.epoxy.model.MyTextModel;
import ca.six.ui.others.epoxy.model.SingleModel;


public class EpoxyDemoAdapter extends EpoxyAdapter {

    public EpoxyDemoAdapter() {
        super();
        enableDiffing();

        List list = new ArrayList();

        HeaderModel headerModel = new HeaderModel(R.string.app_name, R.string.app_caption);
        list.add(headerModel);

        SingleModel singleModel = new SingleModel();
        singleModel.setBackground(R.drawable.forest1);
        list.add(singleModel);

        for(int i = 0 ; i < 4; i++){
            MyTextModel textModel = new MyTextModel();
            textModel.setLeftText("left "+(i+1));
            textModel.setRightText("right "+(i+1));
            list.add(textModel);
        }

        Collections.shuffle(list);
        addModels(list);
    }
}
