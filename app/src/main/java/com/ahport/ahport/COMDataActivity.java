package com.ahport.ahport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class COMDataActivity extends ComConsoleActivity {
    private static final String TAG = "ComDataActivity";
    private EditText mSendText;
    private TextView mRcvText;
    private Button mBtnSendTest;
    private Button mBtnSendAlarm;
    private Button mBtnSendPic;
    private ImageView mImageViewPicShow;
    private final int MSG_REFRESH = 0;
    private int index = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.console);
        mRcvText = (TextView) findViewById(R.id.EditTextReceived);
        mSendText = (EditText) findViewById(R.id.EditTextSend);
        mBtnSendTest = (Button) findViewById(R.id.ButtonSendTest);
        mBtnSendAlarm = (Button) findViewById(R.id.ButtonSendAlarm);
        mBtnSendPic = (Button) findViewById(R.id.ButtonSendPic);
        mImageViewPicShow = (ImageView) findViewById(R.id.imageview);
        mBtnSendTest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick -> comSendTst()");
                if (null == mOutputStream) {
                    Toast.makeText(COMDataActivity.this, "串口设备为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSendText.setText("{ Android-to-PC-count:" + index++ + "}");
                sendTst2(mSendText.getText().toString());
            }
        });

        mBtnSendAlarm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(COMDataActivity.this, "face", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("ihop.intent.ALARM_NO_FACE");
                sendBroadcast(intent);

            }
        });

        mBtnSendPic.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick -> send()");
                final File file = new File("/sdcard/1.jpg");
                if (!file.exists()) {
                    Log.i(TAG, "File:文件不存在." + file.getAbsolutePath());
                    return;
                }
                Log.i(TAG, "onClick -> send()-fileName:" + file.getAbsolutePath());
                if (null == mOutputStream) {
                    Toast.makeText(COMDataActivity.this, "串口设备为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send16HexPic(file.getAbsolutePath());
                    }
                }).start();
            }
        });
    }


    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String data = new String(buffer);
                Log.i(TAG, "content:" + data);
                mRcvText.append(data + "\n");
            }
        });
    }


    /**
     * 发送十六进制图片
     *
     * @param filename
     */
    private void send16HexPic(String filename) {
        StringBuffer sb = new StringBuffer();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = fis.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            // 得到图片的字节数组
            byte[] result = bos.toByteArray();
            String string = byte2HexStr(result);
            Log.i(TAG, "result:" + string);
            mOutputStream.write(string.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "FileNotFoundException:" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "IOException:" + e.toString());
        }
    }

    /**
     * 实现字节数组向十六进制的转换方法一
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


    /**
     * 发送文本
     */
    private void sendTst2(String cmd) {
        try {
            Log.i(TAG, "sending data to serial:" + cmd);
            mOutputStream.write(cmd.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
