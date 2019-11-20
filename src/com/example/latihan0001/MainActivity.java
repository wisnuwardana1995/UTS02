package com.example.latihan0001;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	 private String TAG = MainActivity.class.getSimpleName();
	    private ProgressDialog pDialog;
	    private ListView lv;
	    private Button btntambah;
	 
	    // URL to get contacts JSON
	    private static String url = "http://apilearning.totopeto.com/contacts";
	 
	    ArrayList<HashMap<String, String>> contactList;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	        contactList = new ArrayList<HashMap<String, String>>();
	        
	        lv = (ListView) findViewById(R.id.list);
	        btntambah=(Button)findViewById(R.id.bttambah);
	        
	        btntambah.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					 //TODO Auto-generated method stub
					Intent intentTambahContact=new Intent(MainActivity.this,TambahKontak.class);
					startActivity(intentTambahContact);
				}
			});
	 
	        
	        
	        	
	        lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					//Toast.makeText(MainActivity.this, "Tested!", Toast.LENGTH_SHORT).show();
					HashMap<String, String> hm = contactList.get(arg2);
					
					//Intent intentContactDetails = new Intent(MainActivity.this, ContactDetails.class);
					Intent intentInboxOutbox=new Intent(MainActivity.this,MainFragmentActivity.class);
					//intentContactDetails.putExtra("id", hm.get("id"));
					intentInboxOutbox.putExtra("id", hm.get("id"));
					intentInboxOutbox.putExtra("name", hm.get("name"));
					//startActivity(intentContactDetails);			
					startActivity(intentInboxOutbox);
				}
			});
	        
	    }


	    private class GetContacts extends AsyncTask<Void, Void, Void> {
	    	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            // Showing progress dialog
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setMessage("Please wait...");
	            pDialog.setCancelable(false);
	            pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	            HttpHandler sh = new HttpHandler();

	            // Making a request to url and getting response
	            String jsonStr = sh.makeServiceCall(url);
	 
	            Log.e(TAG, "Response from url: " + jsonStr);
	 
	            // Read JSON
	            if (jsonStr != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	 
	                    // Getting JSON Array node
	                    JSONArray contacts = jsonObj.getJSONArray("contacts");
	 
	                    // looping through All Contacts
	                    for (int i = 0; i < contacts.length(); i++) {
	                        JSONObject c = contacts.getJSONObject(i);
	                        
	                        String id = c.getString("id");
	                        String name = c.getString("name");
	                        String address = c.getString("address");
	                        String email = c.getString("email");
	                        String phone = c.getString("phone");
	                        String dob = c.getString("dob");
	                        String created_at = c.getString("created_at");
	                        String updated_at = c.getString("updated_at");
	 
	                        // tmp hash map for single contact
	                        HashMap<String, String> contact = new HashMap<String, String>();
	 
	                        // adding each child node to HashMap key => value
	                        contact.put("id", id);
	                        contact.put("name", name);
	                        contact.put("address", address);
	                        contact.put("email", email);
	                        contact.put("phone", phone);
	                        contact.put("dob", dob);
	                        contact.put("created_at", created_at);
	                        contact.put("updated_at", updated_at);
	 
	                        // adding contact to contact list
	                        contactList.add(contact);
	                    }
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
	            /**
	             * Updating parsed JSON data into ListView
	             * */
	            ListAdapter adapter = new SimpleAdapter(
	                    MainActivity.this, contactList,
	                    R.layout.list_item, new String[]{"name", "email",
	                    "phone"}, new int[]{R.id.name,
	                    R.id.email, R.id.phone});
	 
	            lv.setAdapter(adapter);
	        }
	 
	    }
	    
	    public void onResume(){
	 	   super.onResume();
	 	   new GetContacts().execute();
	    }
	}