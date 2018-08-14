package com.billy.controller.log.collector;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.controller.JsonViewActivity;
import com.billy.controller.R;
import com.billy.controller.log.collector.LogActivity.LogItem;
import com.billy.controller.log.collector.fiter.ILogFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author billy.qi
 * @since 17/5/25 20:42
 */
class LogAdapter extends Adapter {
    private final Context context;
    private List<Integer> logLevelColors;

    private List<LogItem> allData;
    private List<LogItem> data;

    LogAdapter(Context context) {
        this.context = context;
        this.allData = new ArrayList<>();
        this.data = new ArrayList<>();
        Resources resources = context.getResources();
        //日志的颜色
        logLevelColors = Arrays.asList(
                resources.getColor(R.color.log_level_v)
                , resources.getColor(R.color.log_level_d)
                , resources.getColor(R.color.log_level_i)
                , resources.getColor(R.color.log_level_w)
                , resources.getColor(R.color.log_level_e)
        );
    }

    private ArrayList<ILogFilter> logFilters = new ArrayList<>();

    void addLogFilter(ILogFilter filter) {
        logFilters.add(filter);
    }

    void selectAll(boolean selected) {
        for (LogItem item : data) {
            item.selected = selected;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogItem content = null;
        if (position >= 0 && position < data.size()) {
            content = data.get(position);
        }
        ((Holder)holder).bind(content);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    //过滤条件改变后，刷新数据
    void refreshFilter() {
        this.data = filter(allData);
        this.notifyDataSetChanged();
    }

    //新增日志信息
    void addLogItems(List<LogItem> list) {
        allData.addAll(list);
        List<LogItem> items = filter(list);
        if (!items.isEmpty()) {
            int oldSize = data.size();
            data.addAll(items);
            notifyItemRangeInserted(oldSize, list.size());
        }
    }

    private List<LogItem> filter(List<LogItem> list) {
        List<LogItem> result = new ArrayList<>();
        List<ILogFilter> filters = this.logFilters;
        itemLbl:
        for (LogItem item : list) {
            for (ILogFilter filter : filters) {
                if (!filter.filter(item)) {
                    continue itemLbl;
                }
            }
            result.add(item);
        }
        return result;
    }

    void shareSelectedItems() {
        StringBuilder sb = new StringBuilder();
        for (LogItem item : data) {
            if (item.selected) {
                sb.append(item.content).append("\n");
            }
        }
        String text = sb.toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(context, "select none", Toast.LENGTH_SHORT).show();
        } else {
            shareText(sb.toString());
        }
    }

    void clear() {
        allData.clear();
        data.clear();
        notifyDataSetChanged();
    }

    void viewSelectItems() {
        StringBuilder sb = new StringBuilder();
        for (LogItem item : data) {
            if (item.selected) {
                sb.append(item.content)
                        .append("\n")
                        .append("----------------------------")
                        .append("\n")
                ;
            }
        }
        String text = sb.toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(context, "select none", Toast.LENGTH_SHORT).show();
        } else {
            viewText(sb.toString());
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        int curType = -1;
        int curMaxLine;
        private static final int DEF_MAX_LINE = 3;
        private static final int MAX_MAX_LINE = 100;
        private LogItem item;

        Holder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.log_content);
            setMaxLine(DEF_MAX_LINE);
            this.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String text = textView.getText().toString().trim();
                    viewText(text);
                    return false;
                }
            });
            this.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMaxLine(curMaxLine == DEF_MAX_LINE ? MAX_MAX_LINE : DEF_MAX_LINE);
                }
            });
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (item != null) {
                        item.selected = isChecked;
                    }
                }
            });
        }

        void bind(LogItem item) {
            this.item = item;
            if (item != null) {
                textView.setText(item.content);
                if (item.level != curType) {
                    curType = item.level;
                    int color = logLevelColors.get(curType);
                    textView.setTextColor(color);
                    setMaxLine(DEF_MAX_LINE);
                }
                checkBox.setChecked(item.selected);
            } else {
                textView.setText("");
                checkBox.setChecked(false);
            }
        }
        void setMaxLine(int maxLine) {
            if (this.curMaxLine != maxLine) {
                this.curMaxLine = maxLine;
                textView.setMaxLines(maxLine);
            }
        }
    }

    private void viewText(String text) {
        Intent intent = new Intent(context, JsonViewActivity.class);
        intent.putExtra(JsonViewActivity.EXTRA_CONTENT, text);
        context.startActivity(intent);
    }

    private void shareText(String text) {
        if (context == null) {
            return;
        }
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(context, R.string.copy_to_clipboard_success, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }
}
