package android_serialport_api.sample.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android_serialport_api.SerialPort;
import android_serialport_api.sample.R;
import android_serialport_api.sample.SwitchMainActivity;

/**
 * Created by Luigi on 16/07/2015.
 */
public class PowerFragment extends Fragment {

    private final String TAG = "PowerFragment";

    private TextView voltageValue;
    private TextView currentValue;
    private TextView frequencyValue;
    private TextView powerFactorValue;
    private TextView powerValue;

    private boolean isVisible = false;
    private boolean progressVisible = false;

    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private boolean running = true;

    Handler handler = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_power_consumption, container, false);

        voltageValue = (TextView) rootView.findViewById(R.id.voltageValue);
        currentValue = (TextView) rootView.findViewById(R.id.currentValue);
        frequencyValue = (TextView) rootView.findViewById(R.id.frequencyValue);
        powerFactorValue = (TextView) rootView.findViewById(R.id.powerfactorValue);
        powerValue = (TextView) rootView.findViewById(R.id.powerValue);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setPowerDataLayout(((float[])msg.obj)[0],
                        ((float[])msg.obj)[1],
                        ((float[])msg.obj)[2],
                        ((float[])msg.obj)[3],
                        ((float[])msg.obj)[4]);
            }

        };

        new PingTask().execute();

        return rootView;
    }

    private void setPowerDataLayout(float v, float c, float f, float pf, float pw) {
        voltageValue.setText(String.format("%.1f", v) + " V");
        currentValue.setText(String.format("%.1f", c) + " A");
        frequencyValue.setText(String.format("%.1f", f) + " Hz");
        powerFactorValue.setText(String.format("%.1f", pf) + " ");
        powerValue.setText(String.format("%.1f", pw) + " W");
    }




    private class PingTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        protected String doInBackground(String... urls) {
            Log.i(TAG, "doInBackground");


            while(running){
                getCiabattaData();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");
        }
    }

    private byte STX              = 0x02;
    private byte CMD_CIABATTADATA = 0x07;

    private void getCiabattaData(){
        // accesso a serial port
        mOutputStream = ((SwitchMainActivity)getActivity()).getSerialOutputStream();
        String version = "";

        byte[] data = {STX,
                CMD_CIABATTADATA,
                (byte)0x00};

        byte crc = calculateCRC(new byte[]{STX, CMD_CIABATTADATA, (byte) 0x00});

        new RequestTask().execute(data[0], data[1], data[2], crc);
    }

    private byte calculateCRC (byte[] byteArray) {
        byte crc = 1;
        for (int i=0; i < byteArray.length; i++){
            crc += byteArray[i];
        }
        return crc;
    }


    private class RequestTask extends AsyncTask<Byte, Void, String> {

        @Override
        protected String doInBackground(Byte... params) {
            int size;
            String received = "";
            try {
                for (int i=0; i<params.length; i++) {
                    mOutputStream.write(params[i]);
                }

//				byte[] buffer = new byte[64];
//				if (mInputStream == null) return "Error";
//				size = mInputStream.read(buffer);
//				if (size > 0) {
//					received = new String(buffer, 0, size);
//
//					Log.i(TAG, "String received: " + received );
//				}
            } catch (IOException e) {
                e.printStackTrace();
                return "Error";
            }

            return received; //TODO: return message parsed
        }

        @Override
        protected void onPostExecute(String result) {
            //TODO: result messaggio ritornato
            // riparsare result
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    float v=0, f=0, c=0, pf=0, pw=0;

    public float[] parseData(String in){
        Log.v("", "parseData " +  in.substring(9, 12)  + " String size: " + in.substring(9, 12).length());

        v  = getFloatFromBytes(in.substring(9, 13));
        f  = getFloatFromBytes(in.substring(13, 17));
        c  = getFloatFromBytes(in.substring(18, 22));
        pf  =getFloatFromBytes(in.substring(22, 26));
        pw  =getFloatFromBytes(in.substring(26, 30));

        Log.w("VALORIII", v + " - " + f + " - " + c + " - " + pw + " - " + pf);

        if(handler != null){
            Message mm = handler.obtainMessage();
            float[] ff = {0,0,0,0,0};
            ff[0] = v;
            ff[1] = f;
            ff[2] = c;
            ff[3] = pf;
            ff[4] = pw;
            mm.obj = ff;
            handler.sendMessage(mm);
        }

        return null;
    }

    private float getFloatFromBytes(String s){
        if(s.length() != 4)
            return -1;

        String ss = "";
        byte[] bb = {0,0,0,0};

        for(int i=0; i<3; i++)
            bb[i] = (byte)s.charAt(i);

        float ff = ByteBuffer.wrap(bb).order(ByteOrder.BIG_ENDIAN).getFloat();

        Log.i("getFloatFromBytes", "NUMERO FLOAT: " + ff + " :0:3: " + bb[0] + " "+ bb[1] + " "+ bb[2] + " "+ bb[3] + " ");

        return ff;
    }
}
