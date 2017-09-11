package ca.six.ui.others.spring.popmenu;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ca.six.ui.others.R;

public class PopMenuDemoActivity extends AppCompatActivity {
    private PopMenu popMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popmenu_demo);
        Resources res = getResources();

        popMenu = new PopMenu.Builder().attachToActivity(this)
                .addMenuItem(new PopMenuItem("文字", res.getDrawable(R.drawable.tabbar_compose_idea)))
                .addMenuItem(new PopMenuItem("照片/视频", res.getDrawable(R.drawable.tabbar_compose_photo)))
                .addMenuItem(new PopMenuItem("头条文章", res.getDrawable(R.drawable.tabbar_compose_headlines)))
                .addMenuItem(new PopMenuItem("签到", res.getDrawable(R.drawable.tabbar_compose_lbs)))
                .addMenuItem(new PopMenuItem("点评", res.getDrawable(R.drawable.tabbar_compose_review)))
                .addMenuItem(new PopMenuItem("更多", res.getDrawable(R.drawable.tabbar_compose_more)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        Toast.makeText(PopMenuDemoActivity.this, "你点击了第" + position + "个位置", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();


        findViewById(R.id.ivPopMenuBottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popMenu.isShowing()) {
                    popMenu.show();
                }
            }
        });
    }


}
