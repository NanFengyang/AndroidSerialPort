package com.ahport.ahport;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComUtil {
    static public final String devName = "/dev/ttyMT1";
    static public final int baudrate = 115200;//
    private static final String TAG = "ComUtil";

    static public void sleepTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static public void initPTT(Context context) {
        SerialPort mSerialPort = null;
        OutputStream mOutputStream = null;
        InputStream mInputStream = null;
        Log.i(TAG, "initPTT begin");

        try {
            mSerialPort = new SerialPort(new File(ComUtil.devName), ComUtil.baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            Log.i(TAG, "serial port:" + mSerialPort + ", output:" + mOutputStream);

            String cmd = "";
            cmd = "AT+DMOCONNECT\r\n";
            mOutputStream.write(cmd.getBytes());
            sleepTime(300);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mSerialPort.close();
        mSerialPort = null;
        Log.i(TAG, "initPTT end");
    }
}
