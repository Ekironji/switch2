package android_serialport_api.sample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.sample.fragment.CitofonoFragment;
import android_serialport_api.sample.fragment.MediaPlayerFragment;
import android_serialport_api.sample.fragment.MeteoFragment;
import android_serialport_api.sample.fragment.PowerFragment;
import android_serialport_api.sample.fragment.SwitchFragment;


public class SwitchMainActivity extends SerialPortActivity {

    final String TAG = "SwitchMainActivity";

	static ProgressDialog dialog;
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    private PowerFragment mPowerFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		dialog = new ProgressDialog(this);
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(2);

        try {
            mOutputStream.write((byte)0x02);
            mOutputStream.write((byte)0x00);
            mOutputStream.write((byte)0x00);
            mOutputStream.write((byte)0x03);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    String msg = "";

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        String recieved = new String(buffer, 0, size);
        Log.v(TAG, "DATA: " + recieved + "  SIZE: " + recieved.length());
        msg += recieved;
        Log.v(TAG, "&&&&&&&&& DATA: " + msg + "  SIZE: " + msg.length());


        if(msg.charAt(0) != 0x02)
            msg = "";
        else{
            if(msg.length() == 30){
                if(mPowerFragment != null){
                    mPowerFragment.parseData(msg);
                    msg = "";
                }
            }
            else if(msg.length() > 30)
                msg = "";
            else{
                ;
            }

        }


    }

    public OutputStream getSerialOutputStream() {
        return mOutputStream;
    }

    @Override
	public void onResume() {
		super.onResume(); 
	}
 
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
    protected void onDestroy() {
//        GionjiUtils.closeSerialPort();
//        mSerialPort = null;
        super.onDestroy();
    }
	
	public static void showProgressDialog(){
		dialog.setMessage("Attendere il caricamento dei dati...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();		
	}
	
	public static void hideProgressDialog(){
		dialog.dismiss();		
	}
	
	 /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	Fragment returnFragment = null;
        
        	switch (position) {
            case 0:
                mPowerFragment = new PowerFragment();
                returnFragment = mPowerFragment;
                break;
        	case 1:
        		returnFragment = new MeteoFragment();
        		break;
        	case 2:
        		returnFragment = new SwitchFragment();
        		break;
        	case 3:
        		returnFragment = new MediaPlayerFragment();
        		break;
            case 4:
                returnFragment = new CitofonoFragment();
                break;
    		default:
    			break;
        	
        	}        	
            return returnFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    	
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


}
