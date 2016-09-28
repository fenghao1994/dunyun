package net.dunyun.framework.android.mainapp.gate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author chenzp
 * @date 2016/6/29
 * @Copyright:重庆平软科技有限公司
 */
public class GateConnect extends Thread {

    private String host;
    private int port;
    private Socket socket;
    private OutputStream out;
    private PrintWriter mPrintWriterClient;

    private boolean isRunning = false;
    private InputStream reader;
    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void destory() {
        isRunning = false;
        try {
            if (mPrintWriterClient != null) {
                mPrintWriterClient.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            try {
                System.out.println("-----------" + host + "," + port);
                socket = new Socket(host, port);
                out = socket.getOutputStream();
                mPrintWriterClient = new PrintWriter(
                        out, true);
                socket.setKeepAlive(false);
                // Log.i(Conf.TAG, "连接成功");

            } catch (IOException ex) {
                ex.printStackTrace();
                handler.sendEmptyMessage(1);
            }
            if (socket != null && socket.isConnected()) {
                isRunning = true;
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }

            int length = 0;
            byte[] bt = null;

            while (isRunning) {

                try {
                    reader = socket.getInputStream();
                    length = reader.available();

                    if (length != 0) {
                        bt = new byte[length];
                        reader.read(bt, 0, length);

                        if (bt != null && bt.length > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("data", bt);
                            Message msg = new Message();
                            msg.what = 2;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        try {
            if (out != null) {
                out.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
