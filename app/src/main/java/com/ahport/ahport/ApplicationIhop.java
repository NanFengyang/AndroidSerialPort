package com.ahport.ahport;


import android.app.Application;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

public class ApplicationIhop extends Application {

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;
    private static final String TAG = "ComApplicationIhop";

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        Log.i(TAG, "start serial. dev:" + ComUtil.devName + ", baudrate:" + ComUtil.baudrate);
        if (mSerialPort == null) {
            //
            try {
                mSerialPort = new SerialPort(new File(ComUtil.devName), ComUtil.baudrate, 0);
            } catch (Exception e) {
                //e.printStackTrace();
                Log.i(TAG, "start serial fail!. mserialport:" + mSerialPort + ", dev:" + ComUtil.devName);
            }
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            //
            Log.i(TAG, "closeSerialPort");
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
