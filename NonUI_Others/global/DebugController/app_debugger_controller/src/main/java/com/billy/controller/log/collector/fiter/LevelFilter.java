package com.billy.controller.log.collector.fiter;

import com.billy.controller.log.collector.LogActivity;

/**
 * @author billy.qi
 * @since 17/5/30 10:43
 */
public class LevelFilter implements ILogFilter {

    private int curLogLevel = -1;

    @Override
    public boolean filter(LogActivity.LogItem item) {
        return item != null && item.level >= curLogLevel;
    }

    public int getCurLogLevel() {
        return curLogLevel;
    }

    public void setCurLogLevel(int curLogLevel) {
        this.curLogLevel = curLogLevel;
    }
}
