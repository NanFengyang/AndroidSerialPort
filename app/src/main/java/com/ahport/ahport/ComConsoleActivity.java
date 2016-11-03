package com.ahport.ahport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class ComConsoleActivity extends Activity {

    protected ApplicationIhop mApplication;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;
    private ReadThread mReadThread;
    private static final String TAG = "ComConsoleActivity";


    private class ReadThread extends Thread {
        @Override
        public void run() {
            Log.i(TAG, "thread start reading from serial...");
            super.run();
            while (!isInterrupted()) {
                try {
                    if (mInputStream != null) {
                        int size;
                        size = mInputStream.read();
                        byte[] buffer = new byte[size];
                        mInputStream.read(buffer, 0, size);
                        if (size > 0) {
                            Log.i(TAG, "size:" + size);
                            onDataReceived(buffer, size);
                        }
                    } else {
                        Log.i(TAG, "mInputStream:null");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "IOException:" + e.toString());
                }
            }
            Log.i(TAG, "return from Com ReadThread");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "ComConsoleActivity onCreate");
        super.onCreate(savedInstanceState);
        mApplication = (ApplicationIhop) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            if (mSerialPort != null) {
                Log.i(TAG, "get serial port succeed!");
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
            }
            /* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            Log.i(TAG, "onCreate error");
        } catch (IOException e) {
            Log.i(TAG, "onCreate error_unknown");
        } catch (InvalidParameterException e) {
            Log.i(TAG, "onCreate error_configuration");
        }
    }

    protected abstract void onDataReceived(final byte[] buffer, final int size);

    @Override
    protected void onDestroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        mApplication.closeSerialPort();
        mSerialPort = null;
        super.onDestroy();
    }
}
