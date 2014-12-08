package com.example.lorafabiandemoclient;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;


/*
 * Example from:
 * 	http://android-er.blogspot.fr/2012/07/example-of-using-preferencefragment.html
 * 
 * */
public class SetPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	  
		// Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
        
	 }
 
	
	public static class PrefsFragment extends PreferenceFragment {

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.layout.preferences);
	    }
	}

}