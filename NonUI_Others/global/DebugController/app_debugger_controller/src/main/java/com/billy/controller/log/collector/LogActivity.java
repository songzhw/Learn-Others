package com.billy.controller.log.collector;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.billy.controller.BaseActivity;
import com.billy.controller.R;
import com.billy.controller.core.ServerMessageProcessorManager;
import com.billy.controller.core.IServerMessageProcessor;
import com.billy.controller.core.ConnectionStatus;
import com.billy.controller.log.collector.fiter.LevelFilter;
import com.billy.controller.log.collector.fiter.StringFilter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogActivity extends BaseActivity implements IServerMessageProcessor {

    private LogAdapter adapter;
    List<Integer> idList = Arrays.asList(
            R.id.log_level_v
            , R.id.log_level_d
            , R.id.log_level_i
            , R.id.log_level_w
            , R.id.log_level_e);
    private LevelFilter levelFilter;
    private StringFilter stringFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.log_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new LogAdapter(this);
        levelFilter = new LevelFilter();
        stringFilter = new StringFilter();
        adapter.addLogFilter(levelFilter);
        adapter.addLogFilter(stringFilter);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ServerMessageProcessorManager.addProcessor(this);
        RadioGroup group = (RadioGroup) findViewById(R.id.log_level_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int logLevel = idList.indexOf(checkedId);
                levelFilter.setCurLogLevel(logLevel);
                adapter.refreshFilter();
            }
        });
        EditText editText = (EditText) findViewById(R.id.et_filter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) {
                String filter = s.toString().trim();
                stringFilter.setFilterStr(filter);
                adapter.refreshFilter();
            }
        });
        CheckBox cbSelectAll = (CheckBox) findViewById(R.id.log_select_all);
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.selectAll(isChecked);
            }
        });
    }

    @Override
    protected void onDestroy() {
        ServerMessageProcessorManager.removeProcessor(this);
        super.onDestroy();
    }

    @Override
    public void onStatus(ConnectionStatus status) {
    }

    @Override
    public void onMessage(String message) {
        if (message != null) {
            cache.offer(message);
            if (!readingCache.get()) {
                runOnUiThread(readFromCache);
            }
        }
    }

    @Override
    public String getDebugKey() {
        return ServerMessageProcessorManager.KEY_LOG;
    }

    LinkedBlockingQueue<String> cache = new LinkedBlockingQueue<>();

    AtomicBoolean readingCache = new AtomicBoolean(false);

    Runnable readFromCache = new Runnable() {
        @Override
        public void run() {
            if (readingCache.compareAndSet(false, true)) {
                List<LogItem> list = new LinkedList<>();
                String msg;
                while((msg = cache.poll()) != null) {
                    list.add(new LogItem(msg));
                }
                readingCache.set(false);
                adapter.addLogItems(list);
            }
        }
    };

    public class LogItem {
        static final int LOG_TYPE_INDEX = 32;
        static final String LEVEL = "DIWE";
        public String content;
        public int level;
        boolean selected;

        LogItem(String s) {
            if (s != null) {
                content = s;
                if (s.length() > LOG_TYPE_INDEX) {
                    char c = s.charAt(LOG_TYPE_INDEX - 1);
                    level = LEVEL.indexOf(c) + 1;// 0, 1, 2, 3, 4
                }
                if (level == 0 && s.length() > 0) {//有些设备上获取的日志会将Log等级之前的信息去除
                    char c = s.charAt(0);
                    level = LEVEL.indexOf(c) + 1;// 0, 1, 2, 3, 4
                }
            }
        }
    }

    @Override
    protected int getMenuResId() {
        return R.menu.menu_log;
    }

    @Override
    protected boolean onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.action_view:
                adapter.viewSelectItems();
                return true;
            case R.id.action_share:
                adapter.shareSelectedItems();
                return true;
            case R.id.action_clear:
                cache.clear();
                adapter.clear();
                return true;
        }
        return super.onOptionsItemSelected(itemId);
    }
}
