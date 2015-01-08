package iit.edu.mramacha_androidassignment2;


import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * This Activity displays building information of the respective building selected
 * Swipe listener is implemented which displays both activities.
 *
 */
public class BuildingDetailsDisplay extends Activity {
	
	private GestureDetector mGestureScan;
	View.OnTouchListener mGestureListener;
	private static final int SWIPEMINDISTANCE = 120;
	private static final int SWIPEMAXOFFPATH = 250;
    private static final int SWIPETHRESHOLDVELOCITY = 200;
    private Button mButton_BackButton = null;
    private TextView mTextView_BuildingTitle = null;
    private ImageView mImageView_BuildingImage = null;
    private TextView mTextView_BuildingCode = null;
    private TextView mTextView_BuildingAddress = null;
    private TextView mTextView_BuildingDescription = null;
    private ScrollView mScrollView_BuldingInfo = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.buildingdetails);
		
		int buildingSelected = getIntent().getExtras().getInt("selectedPosition");
		mTextView_BuildingTitle = (TextView)findViewById(R.id.TextViewBuildingTitle);
		mImageView_BuildingImage = (ImageView)findViewById(R.id.ImageViewBuilding);
		mTextView_BuildingCode = (TextView)findViewById(R.id.TextViewBuldingCode);
		mTextView_BuildingAddress = (TextView)findViewById(R.id.TextViewBuldingAddress);
		mTextView_BuildingDescription = (TextView)findViewById(R.id.TextViewBuldingDescription);
		mButton_BackButton = (Button)findViewById(R.id.ButtonBack);
		mScrollView_BuldingInfo = (ScrollView)findViewById(R.id.ScrollViewBuldingDescription);		
		//Getting resoursID into an array
		TypedArray buildingsImages = getResources().obtainTypedArray(R.array.builgingImgs);
		
		// Setting the respective image using resource Id
		mImageView_BuildingImage.setImageResource(buildingsImages.getResourceId(buildingSelected, -1));
			
		//Setting respective building data using resource ID and index value.
		mImageView_BuildingImage.setImageResource(buildingsImages.getResourceId(buildingSelected, -1));
	    mTextView_BuildingTitle.setText(DetailsPage.header.get(buildingSelected));
		mTextView_BuildingCode.setText(mTextView_BuildingCode.getText() + DetailsPage.buildingCodes.get(buildingSelected));
		mTextView_BuildingAddress.setText(mTextView_BuildingAddress.getText() + DetailsPage.address.get(buildingSelected));
		mTextView_BuildingDescription.setText(DetailsPage.description.get(buildingSelected));
		
		//back button listener to navigate from current activity to previous activity.
		mButton_BackButton.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
				
		 //Swipe listener
		mGestureScan = new GestureDetector(new OnMyGestureDetector());
		mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureScan.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };		
		
		//gesture detector on scroll view
		 mScrollView_BuldingInfo.setOnTouchListener(new OnTouchListener() {
			    public boolean onTouch(View view, MotionEvent e) {
			    	mGestureScan.onTouchEvent(e);
			        return false;
			    }
			});
		
	}
	
	//If swipe gesture is detected navigate to respective activities.
	 class OnMyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onFling(MotionEvent m1, MotionEvent m2, float vX, float vY) {
	            try {
	                if (Math.abs(m1.getY() - m2.getY()) > SWIPEMAXOFFPATH)
	                    return false;
	                // right to left swipe(go to about activity)
	                if(m1.getX() - m2.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {
	                	Intent aboutDisplay = new Intent(BuildingDetailsDisplay.this, About.class);
	                	startActivity(aboutDisplay);
	                	finish();
	                }//left to right (go to main activity)
	                else if (m2.getX() - m1.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {	                	
	                	finish();
	                }
	            } catch (Exception e) {
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

	
	
}