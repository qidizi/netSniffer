package com.qidizi.netSniffer;

import android.net.Uri;

public class KcaConstants {
    public final static String KC_PACKAGE_NAME = "com.dmm.dmmlabo.kancolle";
    public final static String DMMLOGIN_PACKAGE_NAME = "com.dmm.app.store";

    public static final String PREF_VPN_ENABLED = "enabled";
    public static final String VPN_STOP_REASON = "vpn_stop_from_main";
    public static final int PACKETSTORE_VERSION = 1;

    public static final String AUTHORITY = "com.qidizi.netSniffer.contentprovider";
    public static final String PATH  = "/request";

    public static final String BROADCAST_ACTION = "com.qidizi.netSniffer.broadcast";
    public static final String WIDGET_TOGGLE_ACTION = "com.qidizi.netSniffer.widget.ACTION_TOGGLE";
    public static final String WIDGET_SET_ACTION = "com.qidizi.netSniffer.widget.ACTION_SET";

    public static final Uri CONTENT_URI = Uri.parse("content://".concat(AUTHORITY).concat(PATH));
}
