package com.bagasbest.myviewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagasbest.myviewmodel.model.WeatherItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<WeatherItems>> listWeathers = new MutableLiveData<>();

    void setWeather (String cities) {
        //request API
        final ArrayList <WeatherItems> listItems = new ArrayList<>();

        String apiKey = "b16627615e4febf5dc5013dfc7cf9d8b";
        String url = "https://api.openweathermap.org/data/2.5/group?id=" + cities + "&units=metric&appid=" + apiKey;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //parsing JSON


                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("list");

                    for (int i=0; i<list.length(); i++) {
                        JSONObject weather = list.getJSONObject(i);
                        WeatherItems weatherItems = new WeatherItems();
                        weatherItems.setId(weather.getInt("id"));
                        weatherItems.setName(weather.getString("name"));
                        weatherItems.setCurrentWeather(weather.getJSONArray("weather").getJSONObject(0).getString("main"));
                        weatherItems.setDescription(weather.getJSONArray("weather").getJSONObject(0).getString("description"));

                        double tempInCelcius = weather.getJSONObject("main").getDouble("temp");
                        double tempInKelvin = tempInCelcius + 273;

                        weatherItems.setTemperature(new DecimalFormat("##.##").format(tempInKelvin));
                        listItems.add(weatherItems);
                    }
                    listWeathers.postValue(listItems);

                } catch (JSONException e) {
                    Log.d("Exceltion : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFilure : ", error.getMessage());

            }
        });
    }

    LiveData<ArrayList<WeatherItems>> getWeathers() {
        return listWeathers;
    }
}
