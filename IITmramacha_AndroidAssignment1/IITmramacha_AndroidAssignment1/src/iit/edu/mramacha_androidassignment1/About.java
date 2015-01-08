package iit.edu.mramacha_androidassignment1;


import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;

/*
 * Displays the application name and application implementer name.
 * Swipe listener is implemented
 */
public class About extends Activity {
	
	private GestureDetector mGestureScan;
	View.OnTouchListener mGestureListener;
	private static final int SWIPEMINDISTANCE = 120;
	private static final int SWIPEMAXOFFPATH = 250;
    private static final int SWIPETHRESHOLDVELOCITY = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		
		mGestureScan = new GestureDetector(new OnMyGestureDetector());
		mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureScan.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
	}
	
	//If swipe gesture is detected navigate to respective activities.	
	class OnMyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent m1, MotionEvent m2, float vX, float vY) {
            try {
                if (Math.abs(m1.getY() - m2.getY()) > SWIPEMAXOFFPATH)
                    return false;
                // Right to Left swipe(go to main activity)
                if(m1.getX() - m2.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {
                	finish();
                } // Left to Right swipe(go to main activity)
                else if (m2.getX() - m1.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {
                	//Intent buildingDetails = new Intent(About.this, BuildingDetailsDisplay.class);               	
                	finish();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureScan.onTouchEvent(event))
	        return true;
	    else
	    	return false;
    }




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}