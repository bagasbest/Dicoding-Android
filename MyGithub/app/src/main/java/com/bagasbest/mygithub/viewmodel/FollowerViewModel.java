package com.bagasbest.mygithub.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagasbest.mygithub.model.UserFollower;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowerViewModel extends ViewModel {

    private MutableLiveData<ArrayList<UserFollower>> listFollower = new MutableLiveData<>();


    public void setFollower(final Context context, String username) {

        final ArrayList <UserFollower> listUserFollower = new ArrayList<>();

        final String url = "https://api.github.com/users/"+ username +"/followers";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token 26a65ff9842fb2bda5a6473b1e52f873e9086339");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);

                try {

                    JSONArray items = new JSONArray(result);

                    for (int i=0; i<items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        UserFollower user = new UserFollower();
                        user.setId(item.getInt("id"));
                        user.setNama(item.getString("login"));
                        user.setImage(item.getString("avatar_url"));

                        listUserFollower.add(user);

                    }

                    listFollower.postValue(listUserFollower);

                }catch (Exception e) {
                    Log.d("Exception: ", e.getMessage());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure: ", error.getMessage());
            }
        });


    }

    public LiveData<ArrayList<UserFollower>> getUserFollowerList() {
        return listFollower;
    }
}
