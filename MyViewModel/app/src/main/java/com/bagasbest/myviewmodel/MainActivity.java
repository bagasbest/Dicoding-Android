package com.bagasbest.myviewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bagasbest.myviewmodel.adapter.WeatherAdapter;
import com.bagasbest.myviewmodel.model.WeatherItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private EditText etCity;
    private Button btnCari;
    private ProgressBar progressBar;

    private WeatherAdapter weatherAdapter;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //casting view
        etCity = findViewById(R.id.etCity);
        btnCari = findViewById(R.id.btnCity);
        progressBar = findViewById(R.id.progressBar);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter();
        weatherAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(weatherAdapter);

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCity.getText().toString().trim();

                if(TextUtils.isEmpty(city)) return;

                showLoading(true);

                mainViewModel.setWeather(city);
            }
        });

        mainViewModel.getWeathers().observe(this, new Observer<ArrayList<WeatherItems>>() {
            @Override
            public void onChanged(ArrayList<WeatherItems> weatherItems) {
                if(weatherItems != null) {
                    weatherAdapter.setData(weatherItems);
                    showLoading(false);
                }
            }
        });

    }

    private void setWeather(String city) {
        final ArrayList<WeatherItems> listWeather = new ArrayList<>();

        String apiKey = "b16627615e4febf5dc5013dfc7cf9d8b";
        String url = "https://api.openweathermap.org/data/2.5/group?id=" + city + "&units=metric&appid=" + apiKey;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //parsing JSON
                String result = new String(responseBody);

                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("list");

                    for(int i = 0; i<list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        WeatherItems weatherItems = new WeatherItems();
                        weatherItems.setId(weather.getInt("id"));
                        weatherItems.setName(weather.getString("name"));
                        weatherItems.setCurrentWeather(weather.getJSONArray("weather").getJSONObject(0).getString("main"));
                        weatherItems.setDescription(weather.getJSONArray("weather").getJSONObject(0).getString("description"));

                        double tempInCelcius = weather.getJSONObject("main").getDouble("temp");
                        double tempInKelvin = tempInCelcius + 273;

                        weatherItems.setTemperature(new DecimalFormat("##.##").format(tempInKelvin));
                        listWeather.add(weatherItems);
                    }
                    //set data ke adapter
                    weatherAdapter.setData(listWeather);
                    showLoading(false);

                } catch (JSONException e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());

            }
        });

    }

    private void showLoading (Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
