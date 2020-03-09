package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String API_KEY = "Y2U2NzJjNzA0Njc5NGEyNDk2NTc4ZmFhNTdjMDcxYzQ";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("Bitcoin", " "+adapterView.getItemAtPosition(position));
                Log.d("Bitcoin", " Position is : "+position);
                String finalURL = BASE_URL + adapterView.getItemAtPosition(position);
                Log.d("Bitcoin", " Final URL: "+ finalURL);
                letsDoSomeNetworking(finalURL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing selected");

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {
        Log.d("Bitcoin","letsDoSomeNetorking() called.");

        RequestParams params = new RequestParams();
        params.put("x-ba-key", API_KEY);
        Log.d("Bitcoin","Async begins.");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-ba-key",API_KEY);
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("BitCoin", "JSON: " + response.toString());

                try {
                    String price = response.getString("last");
                    mPriceTextView.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
//                Log.d("Bitcoin", "Fail response: " + errorResponse);
//                Log.e("ERROR", throwable.toString());
//            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.e("ERROR from letsDoSomeNetworking: ", throwable.toString());
            }
        });


    }


}
