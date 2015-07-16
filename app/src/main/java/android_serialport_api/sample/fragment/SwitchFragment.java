package android_serialport_api.sample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import android_serialport_api.sample.R;
import android_serialport_api.sample.utils.RelaysManager;
import android_serialport_api.sample.views.CircolareConIndicatore;
import android_serialport_api.sample.views.CircolareConIndicatore.OnSwitchEventListener;

public class SwitchFragment extends Fragment {
	
	private final String TAG = "SwitchFragment";
	
	int switchSize = 140;

	private boolean isVisible;

    private RelaysManager mRelaysManager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_switch, container, false);
        
        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.linear);

        mRelaysManager = new RelaysManager();

		final CircolareConIndicatore lamp;
		lamp = new CircolareConIndicatore(getActivity().getBaseContext(), CircolareConIndicatore.LAMP);			
		lamp.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		lamp.setSwitchEventListener(new OnSwitchEventListener() {
			public void onEvent() {
				if (isVisible) {
					mRelaysManager.switchRelay(RelaysManager.RELAY1, lamp.isInterruttoreAcceso() ? RelaysManager.ON
							: RelaysManager.OFF);// 1 = ON; 0 = OFF);
				}
			}
		});
		linear.addView(lamp);
		
		final CircolareConIndicatore abajure;
		abajure = new CircolareConIndicatore(getActivity().getBaseContext(), CircolareConIndicatore.ABAJURE);		
		abajure.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		abajure.setSwitchEventListener(new OnSwitchEventListener() {
			public void onEvent() {
				Log.i(TAG, "Event fine circolo");
				if (isVisible) {

					mRelaysManager.switchRelay(RelaysManager.RELAY2, lamp.isInterruttoreAcceso() ? RelaysManager.ON
							: RelaysManager.OFF);

				}
			}
		});
		linear.addView(abajure);

		final CircolareConIndicatore bulb;
		bulb = new CircolareConIndicatore(getActivity().getBaseContext(), CircolareConIndicatore.BULB);
		bulb.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		bulb.setSwitchEventListener(new OnSwitchEventListener() {
			public void onEvent() {
				Log.i(TAG, "Event fine circolo");
				if (isVisible) {

					mRelaysManager.switchRelay(RelaysManager.RELAY3, lamp.isInterruttoreAcceso() ? RelaysManager.ON
							: RelaysManager.OFF);

				}
			}
		});
		linear.addView(bulb);

        return rootView;
    }
    
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	isVisible = true;
	    } else {
	    	isVisible = false;
	    }
	}  
}
