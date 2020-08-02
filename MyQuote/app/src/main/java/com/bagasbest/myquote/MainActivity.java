package com.bagasbest.myquote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private TextView tvQuote, tvAuthor;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);

        Button btnAllQuote = findViewById(R.id.btnQuotes);
        btnAllQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListQuotesActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        getRandomQuote();
    }

    private void getRandomQuote() {
        //implementasikan LoopJ untuk mengambil date dari Web API seperti berikut
        progressDialog();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://programming-quotes-api.herokuapp.com/quotes/random";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //jika koneksi berhasil
                progressDialog.dismiss();

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    String quote = responseObject.getString("en");
                    String author = responseObject.getString("author");

                    tvQuote.setText(quote);
                    tvAuthor.setText(author);

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //jika koneksi gagal
                progressDialog.dismiss();

                String errorMsg;
                switch (statusCode) {
                    case 401 :
                        errorMsg = statusCode + " : Bad Requests";
                        break;

                    case 403 :
                        errorMsg = statusCode + " : Forbidden";
                        break;

                    case 404 :
                        errorMsg = statusCode + " : Not Found";
                        break;

                    default:
                        errorMsg = statusCode + " : " +error.getMessage();
                }
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
