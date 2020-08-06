package com.bagasbest.mygithub.viewmodel;



import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagasbest.mygithub.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();
    private static final String TAG = MainViewModel.class.getSimpleName();


    public void setUserList(String username) {

        //request API
        final ArrayList<User> userArrayList = new ArrayList<>();

        final String url = "https://api.github.com/search/users?q=" + username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token c9cab51a42a36bdde81b50ab26a7cf4bea1b1342");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);
                Log.d(TAG, result);

                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray items = responseObject.getJSONArray("items");

                    for (int i=0; i<items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        User user = new User();
                        user.setName(item.getString("login"));
                        user.setId(item.getInt("id"));
                        user.setOrganization(item.getString("organizations_url"));
                        user.setImage(item.getString("avatar_url"));

                        userArrayList.add(user);
                    }

                    listUser.postValue(userArrayList);

                } catch (JSONException e) {
                    //Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Exception: ", e.getMessage());
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(context, "Exception: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", error.getMessage());
            }
        });
    }

    public LiveData<ArrayList<User>> getUserList() {
        return listUser;
    }

}
