package com.bagasbest.myquote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListQuotesActivity extends AppCompatActivity {

    private ListView listView;
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quotes);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("List of Quotes");
        }

        listView = findViewById(R.id.listQuotes);

        progressDialog = new ProgressDialog(this);

        getQuotes();
    }

    private void getQuotes() {
        progressDialog();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://programming-quotes-api.herokuapp.com/quotes/page/1";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();

                ArrayList<String> listQuote = new ArrayList<>();

                String result = new String(responseBody);
                Log.d(TAG, result);

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String quote = jsonObject.getString("en");
                        String author = jsonObject.getString("author");
                        listQuote.add("\n"+quote +"\n — " + author + "\n");
                    }

                    ArrayAdapter adapter = new ArrayAdapter<>(ListQuotesActivity.this, android.R.layout.simple_list_item_1, listQuote);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    Toast.makeText(ListQuotesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();

                String errorMsg;
                switch (statusCode) {
                    case 401 :
                        errorMsg = statusCode + " : Bad Request";
                        break;

                    case 403 :
                        errorMsg = statusCode + " : Forbidden";
                        break;

                    case 404 :
                        errorMsg = statusCode + " : Not Found";
                        break;

                    default:
                        errorMsg = statusCode + " : " + error.getMessage();
                        break;


                }
                Toast.makeText(ListQuotesActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
