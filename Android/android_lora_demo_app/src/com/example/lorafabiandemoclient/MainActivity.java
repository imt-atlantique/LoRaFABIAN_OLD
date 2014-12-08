package com.example.lorafabiandemoclient;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;





import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;





public class MainActivity extends Activity {
	
	// http://192.108.119.152:8080/proxy/coap://localhost:PORT/node_0001/led/on
	private static String httpServerIPByDefault = "192.108.119.152:8080";
	private String httpServerAddress = "http://" + httpServerIPByDefault + "/proxy/coap://localhost:PORT/";
	
	private TextView textViewPreferencesHTTPClientURL;
	
	
	private static String led_on_URI  = "node_0001/led/on";
	private static String led_off_URI = "node_0001/led/off";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Load Preferences
		textViewPreferencesHTTPClientURL = (TextView)findViewById(R.id.preferenceHTTPClientURL);
		loadPreferences();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStop() {
		//Stop Services
		boolean runningServices = false;
		if(runningServices){
			// stop services;
		}
		super.onStop();
	}
	
	//########################################################################################
	//######## Load Preferences
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {

	  /*
	   * Because it's onlt ONE option in the menu.
	   * In order to make it simple, We always start SetPreferenceActivity
	   * without checking.
	   */
	  
	  Intent intent = new Intent();
	        intent.setClass(MainActivity.this, SetPreferenceActivity.class);
	        startActivityForResult(intent, 0); 
	  
	        return true;
	 }
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // TODO Auto-generated method stub
	  //super.onActivityResult(requestCode, resultCode, data);
	  
	  /*
	   * To make it simple, always re-load Preference setting.
	   */
	  
		loadPreferences();
	 }
	
	private void loadPreferences(){
		
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String preference_http_server_ip = mySharedPreferences.getString("edit_text_preference_http_server_ip", httpServerIPByDefault);
		
		httpServerAddress = "http://" + preference_http_server_ip + "/proxy/coap://localhost:PORT/";
		textViewPreferencesHTTPClientURL.setText(httpServerAddress);
		
	}
	
	
	
	//########################################################################################
	//######## RELAY SWITCH
		
	public void onRelaySwitchButtonToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
		//boolean on = true;
	
	    
	    
	    if (on) { // Enable
	    	
	    	new doHTTPGET().execute(httpServerAddress + led_on_URI);
	    	
	    	
	    	//showDialogUnauthorized();
	    	
	    } else  { // Disable
	    	
	    	new doHTTPGET().execute(httpServerAddress + led_off_URI);
	    	
	    }
	}
	
	private class doHTTPGET extends android.os.AsyncTask<String, String, String> {
	    
		
	 	protected String doInBackground(String... args) {
	 		String uri 	   = args[0];
	 		

	 		HttpClient httpClient = new DefaultHttpClient();  
	 		String url = uri;
	 		Log.d("DEBU", "url: " + url); // Debug
	 		
	 		
	 		// HTTP conn params:
	 		// http://stackoverflow.com/questions/9925113/httpclient-stuck-without-any-exception
	 		// By default, HttpClient does not timeout (which causes more problem than it helps).
	 		
	 		int connectionTimeoutMillis = 1000, socketTimeoutMillis = 1000;
	 		HttpParams httpParams = httpClient.getParams();
	 		HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeoutMillis);
	 		HttpConnectionParams.setSoTimeout(httpParams, socketTimeoutMillis);
	 		
	 		HttpGet httpGet = new HttpGet(url);
	 		
	 		try {
	 		    HttpResponse response = httpClient.execute(httpGet); // By defaults does not timeout, thats why wee neded to put.
	 		    
	 		    /*StatusLine statusLine = response.getStatusLine();
	 		     * 
	 		    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
	 		        HttpEntity entity = response.getEntity();
	 		        ByteArrayOutputStream out = new ByteArrayOutputStream();
	 		        entity.writeTo(out);
	 		        out.close();
	 		        String responseStr = out.toString();
	 		        // do something with response 
	 		    } else {
	 		        // handle bad response
	 		    }*/
	 		} catch (ClientProtocolException e) {
	 		    // handle exception
	 		} catch (IOException e) {
	 		    // handle exception
	 		}
	
			return null;
	 	}
		 
	 	 protected void onProgressUpdate(String... progress) {
	 		 MainActivity.this.showDialogUnauthorized();
	 	 }
	 	/*protected void onPostExecute(String result) { if(result != null){;}}*/
	 	 
	}
	
	
	protected void showDialogUnauthorized(){
		// http://stackoverflow.com/questions/2115758/how-to-display-alert-dialog-in-android
		new android.app.AlertDialog.Builder(MainActivity.this)
	    	.setTitle("HTTP Response")
	    	.setMessage("The Resource is inexistent")
	    	.setNeutralButton("OK", null)
	    	.show();
	}
}
