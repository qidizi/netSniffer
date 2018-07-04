package com.qidizi.netSniffer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class KcaHandler implements Runnable {
    public Handler handler;

    public static Map<String, Boolean> flag = new HashMap<String, Boolean>();
    public static Map<String, JsonObject> data = new HashMap<String, JsonObject>();

    String url;
    byte[] requestBytes;
    byte[] responseBytes;

    public KcaHandler(Handler h, String u, byte[] b1, byte[] b2) {
        handler = h;
        url = u;
        requestBytes = b1;
        responseBytes = b2;
    }

    public void run() {
        if (handler == null) return;
        String reqData = new String(requestBytes);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.replace("/kcsapi", ""));
        bundle.putString("request", reqData);
        bundle.putByteArray("data", responseBytes);
        Message msg = handler.obtainMessage();
        msg.setData(bundle);

        handler.sendMessage(msg);
        Log.e("KCA", "Data Processed: " + url);
    }
}
