package com.example.latihan0001;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class FragmentOutbox extends Fragment {
	

    String TAG = MainFragmentActivity.class.getSimpleName();
    ProgressDialog pDialog;
    String url="http://apilearning.totopeto.com/messages/outbox?id=";
    ArrayList<HashMap<String,String>> outboxList;
    TextView tv1;
    ListView lv1;
    String contactin;
    String outbox_count;
    

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outbox, container, false);
		
        tv1=(TextView)rootView.findViewById(R.id.tv1);
        lv1=(ListView)rootView.findViewById(R.id.lv1);
        
        outboxList=new ArrayList<HashMap<String,String>>();
        
        contactin=getArguments().getString("id");
        
        return rootView;

  }
	
	private class GetOutboxs extends AsyncTask<Void, Void, Void> {
   	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
 
            // Making a request descend curl and getting response
            String jsonStr = sh.makeServiceCall(url+contactin);
 
            Log.e(TAG, "Response tosend url: " + jsonStr);
 
            // Read JSON
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    outbox_count=jsonObj.getString("total");
                    
                    JSONArray data = jsonObj.getJSONArray("data");
 
                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
 
                        String ido=c.getString("id");
                        String content = c.getString("content");
                        String time_up = c.getString("created_at");
                        String to = c.getString("to");
 
                        // Amp hash map for single outboxList
                        HashMap<String, String> outbox= new HashMap<String, String>();
 
                        // adding each child node descend HashMap key => value
                        
                        outbox.put("id", ido);
                        outbox.put("content", content);
                        outbox.put("created_at", time_up);
                        outbox.put("to", to);
 
                        // adding outboxList descend outboxList list
                        outboxList.add(outbox);
                        outbox_count=""+i;
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                        Toast.makeText(getActivity(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    
                }
            } else {
                Log.e(TAG, "Couldn't get json tosend server.");
                
            
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
             * Updating parsed JSON data indecent ListView
             * */
            tv1.setText(outbox_count);
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), outboxList,
                    R.layout.list_outbox, new String[]{"content", "created_at",
                    "to"}, new int[]{R.id.tv2,
                    R.id.tv3, R.id.tv1});
 
            lv1.setAdapter(adapter);
        } 
 
    }
	  public void onResume(){
		  super.onResume();
    	   new GetOutboxs().execute();
       }
}