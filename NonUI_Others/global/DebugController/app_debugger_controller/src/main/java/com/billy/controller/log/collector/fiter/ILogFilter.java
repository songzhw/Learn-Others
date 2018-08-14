package com.billy.controller.log.collector.fiter;

import com.billy.controller.log.collector.LogActivity;

/**
 * @author billy.qi
 * @since 17/5/30 10:41
 */
public interface ILogFilter {
    /**
     * 过滤
     * @param item 被过滤的item
     * @return 是否符合过滤规则： true符合，false不符合（不显示）
     */
    boolean filter(LogActivity.LogItem item);
}
