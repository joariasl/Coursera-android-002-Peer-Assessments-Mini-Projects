/* Copyright 2014 Jorge Arias
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cl.jorgearias.modernartui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends Activity {
	
	private static final String URL_MORE_INFORMATION = "http://www.moma.org/";
	
	private SeekBar seekBar1;
	private List<View> views = new ArrayList<View>();
	private List<Integer> defaultColors = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get View objects
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        
        // Gets all Views created
        views.add(findViewById(R.id.view1_1));
        views.add(findViewById(R.id.view1_2));
        views.add(findViewById(R.id.view2_1));
        views.add(findViewById(R.id.view2_2));
        views.add(findViewById(R.id.view2_3));
        
        // Complete Array with defaults Colors
        loadDefaultColors();
        
        // Capture event
        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
				return;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				return;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changeColors(progress);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_more_information) {
        	// Start a new DialogFragment with new instance local method
        	AboutDialogFragment.newInstance().show(getFragmentManager(), "Confirm");
            return true;
        }
        if (id == R.id.action_about) {
        	startAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // Launch MoreInformation URL (moma.org)
    private void startMoreInformation(){
    	Intent intent_more_information = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_MORE_INFORMATION));
    	startActivity(intent_more_information);
    }
    
    // Added method to show about information in Activity
    private void startAbout(){
    	Intent intent_activity_about = new Intent(getApplicationContext(), AboutActivity.class);
    	startActivity(intent_activity_about);
    }
    
    private void loadDefaultColors() {
    	defaultColors.clear();
		for(View view : views){
			// Get the color, new int, non referential
			defaultColors.add(((ColorDrawable) view.getBackground()).getColor());
		}
	}
    
    private void changeColors(int sum){
    	for(View view : views){
    		int color = defaultColors.get(views.indexOf(view));
    		// Change color if is not GRAY or WHITE
    		if(
    				Color.red(color) < Color.red(Color.GRAY)
    				|| Color.green(color) < Color.green(Color.GRAY)
    				|| Color.blue(color) < Color.blue(Color.GRAY)
			){
    			view.setBackgroundColor(Color.rgb(
    					(Color.red(color)+sum <= 255)?Color.red(color)+sum:255,//Red
    					(Color.green(color)+sum <= 255)?Color.green(color)+sum:255,//Grey
    					Color.blue(color)//Blue static
					)
				);
    		}
    	}
    }
    
    // Create DialogFragment for about confirm
    public static class AboutDialogFragment extends DialogFragment{
    	
    	public static DialogFragment newInstance(){
    		return new AboutDialogFragment();
    	}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
				.setMessage("Inspired by the works of artist such as Piet Mondrian and Ben Nicholson.\n\nClick below to laern more!")
				.setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Call Activity method to launch URL Intent
						((MainActivity) getActivity()).startMoreInformation();
					}
				})
				.setNegativeButton("Not Now", null)
				.create();
		}
    }
}
