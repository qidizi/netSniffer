package com.qidizi.netSniffer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.httpclient.ChunkedInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class KcaUtils {
    public static String getStringFromException(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString().replaceAll("\n", " / ").replaceAll("\t", "");
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.ENGLISH, format, args);
    }

    public static JsonElement parseJson(String v) {
        return new JsonParser().parse(v);
    }

    public static String joinStr(List<String> list, String delim) {
        String resultStr = "";
        if (list.size() > 0) {
            int i;
            for (i = 0; i < list.size() - 1; i++) {
                resultStr = resultStr.concat(list.get(i));
                resultStr = resultStr.concat(delim);
            }
            resultStr = resultStr.concat(list.get(i));
        }
        return resultStr;
    }

    public static String getStringPreferences(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static Boolean getBooleanPreferences(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("pref", Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    // 값 저장하기
    public static void setPreferences(Context ctx, String key, Object value) {
        SharedPreferences pref = ctx.getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    public static byte[] gzipcompress(String value) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutStream = new GZIPOutputStream(
                new BufferedOutputStream(byteArrayOutputStream));
        gzipOutStream.write(value.getBytes());
        gzipOutStream.finish();
        gzipOutStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] gzipdecompress(byte[] contentBytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ByteStreams.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    private static int bytetoint(byte[] arr) {
        int csize = 0;
        for (int i = 0; i < arr.length; i++) {
            csize = csize << 4;
            if (arr[i] >= 0x30 && arr[i] <= 0x39) {
                csize += arr[i] - 0x30; // (0x30 = '0')
            } else if (arr[i] >= 0x61 && arr[i] <= 0x66) {
                csize += arr[i] - 0x61 + 0x0a; // (0x61 = 'a')
            } else if (arr[i] >= 0x41 && arr[i] <= 0x46) {
                csize += arr[i] - 0x41 + 0x0a; // (0x41 = 'A')
            }
        }
        return csize;
    }

    public static byte[] unchunkdata(byte[] contentBytes) throws IOException {
        byte[] unchunkedData = null;
        byte[] buffer = new byte[1024];
        ByteArrayInputStream bis = new ByteArrayInputStream(contentBytes);
        ChunkedInputStream cis = new ChunkedInputStream(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int read = -1;
        while ((read = cis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        unchunkedData = bos.toByteArray();
        bos.close();

        return unchunkedData;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(KcaUtils.format("%02x ", b & 0xff));
        return sb.toString();
    }
}
