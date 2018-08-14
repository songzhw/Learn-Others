package com.billy.controller.log.collector.fiter;

import android.text.TextUtils;

import com.billy.controller.log.collector.LogActivity;

/**
 * @author billy.qi
 * @since 17/5/30 10:47
 */
public class StringFilter implements ILogFilter {
    private String filterStrUpper;

    @Override
    public boolean filter(LogActivity.LogItem item) {
        if (TextUtils.isEmpty(filterStrUpper)) {
            return true;
        }
        if (item == null || item.content == null) {
            return false;
        }
        return item.content.toUpperCase().contains(filterStrUpper);
    }

    public String getFilterStr() {
        return filterStrUpper;
    }

    public void setFilterStr(String filterStr) {
        if (filterStr != null) {
            this.filterStrUpper = filterStr.toUpperCase();
        } else {
            this.filterStrUpper = null;
        }
    }
}
