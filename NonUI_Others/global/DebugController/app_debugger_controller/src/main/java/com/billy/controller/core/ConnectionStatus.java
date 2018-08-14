package com.billy.controller.core;

import com.billy.controller.R;

/**
 * @author billy.qi
 * @since 17/5/25 11:47
 */
public enum ConnectionStatus {
    STARTING(R.string.log_starting, R.drawable.btn_stop)
    , WAITING_CLIENT(R.string.log_status_waiting, R.drawable.btn_stop)
    , RUNNING(R.string.log_status_on, R.drawable.btn_stop)
    , STOPPING(R.string.log_status_stopping, R.drawable.btn_stop)
    , STOPPED(R.string.log_status_off, R.drawable.btn_start);


    public int resId;
    public int iconResId;
    ConnectionStatus(int resId, int icon) {
        this.resId = resId;
        this.iconResId = icon;
    }

}
