package android_serialport_api.sample.fragment;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import android_serialport_api.sample.R;
import android_serialport_api.sample.SwitchMainActivity;
import android_serialport_api.sample.meteoparser.DatiMeteo;

public class CitofonoFragment extends Fragment {
	
	private final String TAG = "CitofonoFragment";
	
	private ImageView meteoImg;
	private ImageView weatherStateImg;
	private TextView weatherStateTxt;
	private TextView gradesTxt;
	private VideoView mVideoView;
	
	private DatiMeteo datiMeteo;
	
	private boolean isVisible = false;
	private boolean progressVisible = false;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_citofono, container, false);

		mVideoView = (VideoView) rootView.findViewById(R.id.videoView);
		String path = "android.resource://" + getActivity().getApplicationContext().getPackageName() + "/" + R.raw.weather_windy_day;
		mVideoView.setVideoURI(Uri.parse(path));
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});
		mVideoView.start();

		return rootView;
	}
	

	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	isVisible = true;	
	    	
	    	if (progressVisible) {
	    		SwitchMainActivity.showProgressDialog();
	    	}
	    	
//	    	TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 
//	    															-100f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															100f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															0f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															0f);
//	    	animation.setDuration(3000);
//	    	animation.setRepeatCount(-1);
//	    	animation.setRepeatMode(Animation.REVERSE);
//	    	animation.setInterpolator(new LinearInterpolator());	    	
//	        meteoImg.startAnimation(animation);
//
	    } else { 
	    	isVisible = false;
	    	if (!progressVisible) {
	    		SwitchMainActivity.hideProgressDialog();
	    	}
	    }
	}
	
}
