package com.example.latihan0001;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetail extends Activity {

	private String TAG = ContactDetail.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String url = "http://apilearning.totopeto.com/contacts/";
    private TextView tname, taddress, temail, tphone, tdob;
	private String name, address, email, phone, dob;
	
    ArrayList<HashMap<String, String>> contactList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);
		
		tname = (TextView) findViewById(R.id.tvname);
		taddress = (TextView) findViewById(R.id.tvaddress);
		temail = (TextView) findViewById(R.id.tvemail);
		tphone = (TextView) findViewById(R.id.tvphone);
		tdob = (TextView) findViewById(R.id.tvdob);
		
		new GetContacts().execute();
	}
	
	private class GetContacts extends AsyncTask<Void, Void, Void> {
   	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ContactDetail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	Intent intent = getIntent();
    		String id = intent.getStringExtra("id");
    		
            HttpHandler sh = new HttpHandler();
 
            // Making a request to URL and getting response
            String jsonStr = sh.makeServiceCall(url + id);
 
            Log.e(TAG, "Response from url: " + jsonStr);
 
            // Read JSON
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    
                    JSONObject c = jsonObj.getJSONObject("contact");
                    
                    name = c.getString("name");
                    address = c.getString("address");
                    email = c.getString("email");
                    phone = c.getString("phone");
                    dob = c.getString("dob");
                    
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            tname.setText(name);
    		taddress.setText(address);
    		temail.setText(email);
    		tphone.setText(phone);
    		tdob.setText(dob);
        }
 
    }
}