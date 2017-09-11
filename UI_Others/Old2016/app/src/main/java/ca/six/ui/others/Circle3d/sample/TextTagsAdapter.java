package ca.six.ui.others.Circle3d.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ca.six.ui.others.Circle3d.TagsAdapter;

/**
 * Created by moxun on 16/1/19.
 */
public class TextTagsAdapter extends TagsAdapter {

    private List<String> dataSet = new ArrayList<>();

    public TextTagsAdapter(@NonNull String... data) {
        dataSet.clear();
        Collections.addAll(dataSet,data);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
//        System.out.println("szw getView( "+position+" )"); // why three times [0-12] getView()?
        TextView tv = new TextView(context);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(100, 100);
        tv.setLayoutParams(lp);
        tv.setText("No." + position);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(12);
        return tv;
    }

    // 返回Tag数据
    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    // 针对每个Tag返回一个权重值，该值与ThemeColor和Tag初始大小有关
    @Override
    public int getPopularity(int position) {
        return Math.abs(new Random().nextInt(5) + 2);
    }

    // Tag主题色发生变化时会回调该方法
    @Override
    public void onThemeColorChanged(View view, int themeColor) {
        ((TextView)view).setTextColor(themeColor);
    }
}