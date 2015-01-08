package iit.edu.mramacha_androidassignment2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * @author Megha Ramachandra
 * 
 * This Activity when started shows progressdialog loading data (Thread Concept Included) and display the lists of IIT Main Campus Buildings in a List view.
 * When an building is selected from listview next activity(BuildingDetailsDisplay) is started which displays the details about the building selected.
 * Swipe listener is implemented which navigates thorugh all the activities prensent in the app.
 */

public class IITMainBuildings extends Activity {

	private static ListView mListView_BuildingsListView = null;
	private static final int SWIPEMINDISTANCE = 120;
	private static final int SWIPEMAXOFFPATH = 250;
    private static final int SWIPETHRESHOLDVELOCITY = 200;
	private GestureDetector mGestureScan;
	View.OnTouchListener mGestureListener;
	private static ProgressDialog mWaitMsgDialog;
	private static ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.iitbuildingslist);
		
		//Initializing Variables
		mListView_BuildingsListView = (ListView)findViewById(R.id.mylist);
		String[] buildings = getResources().getStringArray(R.array.Buildings);
		mGestureScan = new GestureDetector(new OnMyGestureDetector());		
		adapter  = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, buildings);;		
		
		//Start Loading.... message
		mWaitMsgDialog  = ProgressDialog.show(IITMainBuildings.this,"IIT Main Buildings", "Loading...", false,true);
		startThread();	
		
		//set listener to start activity which displays building details
		mListView_BuildingsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent detailsDisplay = new Intent(IITMainBuildings.this,BuildingDetailsDisplay.class);
				detailsDisplay.putExtra("selectedPosition", position);
				startActivity(detailsDisplay);
				
			}
		}); 
        
        //Swipe listener
        mGestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureScan.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };	
        mListView_BuildingsListView.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View view, MotionEvent e) {
		    	mGestureScan.onTouchEvent(e);
		        return false;
		    }
		});
		
	}
	
	//Starting Thread which loads Buildings List and dismiss the Loading Progress dialog
	private void startThread() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try{
					Thread.sleep(7000);				
					mWaitMsgDialogHandler.sendEmptyMessage(DetailsPage.MESSAGE_SUCCESS);				 	
				} catch (Exception e) {		
					mWaitMsgDialogHandler.sendEmptyMessage(DetailsPage.MESSAGE_NOT_SUCCESS);
				}
			}

		}.start();
		
	}
	
	//Handler to thread
	//loads list on success(0) and dismiss the Loading progressdialog.
	//if not success(0) doesn't loads list and dismisses the Loading progressdialog.
	private static Handler mWaitMsgDialogHandler = new Handler() {
	    public void handleMessage(Message msg) {
	        switch(msg.what)
	        	{        	
	        		case DetailsPage.MESSAGE_SUCCESS:
	        			//assigning the buildings names in a listview 
	        			mListView_BuildingsListView.setAdapter(adapter);
	        			mListView_BuildingsListView.setClickable(true);
	        			mListView_BuildingsListView.setSelectionAfterHeaderView();
	        			break;
					
					default:
						break;				 
	        	}
	        	mWaitMsgDialog .dismiss();
	        }
	    };

	//If swipe gesture is detected navigate to respective activities.
	  class OnMyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onFling(MotionEvent m1, MotionEvent m2, float vX, float vY) {
	            try {
	                if (Math.abs(m1.getY() - m2.getY()) > SWIPEMAXOFFPATH)
	                    return false;
	                // Right to Left swipe(go to building details activity)
	                if(m1.getX() - m2.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {
	                	Intent detailsDisplay = new Intent(IITMainBuildings.this,BuildingDetailsDisplay.class);
	    				detailsDisplay.putExtra("selectedPosition", 0);
	    				startActivity(detailsDisplay);
	                }//Left to Right(go to about display activity) 
	                else if (m2.getX() - m1.getX() > SWIPEMINDISTANCE && Math.abs(vX) > SWIPETHRESHOLDVELOCITY) {
	                	Intent aboutDisplay = new Intent(IITMainBuildings.this,About.class);	    				
	    				startActivity(aboutDisplay);
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

	    //Adding menu data.
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.optionsmenu, menu);
	        return true;
	    }
	    
	    //About activity started from menu.
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	        case R.id.menu_about:
	            Intent aboutIntent = new Intent(IITMainBuildings.this, About.class);
	            startActivity(aboutIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }	
}
