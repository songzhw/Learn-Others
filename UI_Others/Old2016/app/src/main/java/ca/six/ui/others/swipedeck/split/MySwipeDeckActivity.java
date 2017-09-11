package ca.six.ui.others.swipedeck.split;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.swipedeck.SwipeDeckAdapter;

/**
 * Created by songzhw on 2016/1/11
 */
public class MySwipeDeckActivity extends Activity {
    private MySwipeDeck swipeDeck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_swipe_dec);

        List<String> data = new ArrayList<String>();
        data.add("001");data.add("002");data.add("003");data.add("004");
        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(data, this);
        adapter.setLayoutResId(R.layout.item_card_three);

        swipeDeck = (MySwipeDeck) findViewById(R.id.my_swipe_deck);
        swipeDeck.setAdapter(adapter);

        findViewById(R.id.my_swipe_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeDeck.setAdapter(adapter);
                swipeDeck.refresh();
//                swipeDeck.requestLayout(); // 只走onLayout(),不走onSizeChanged()
            }

        });

    }
}
