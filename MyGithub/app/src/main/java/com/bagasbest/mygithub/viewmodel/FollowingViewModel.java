package com.bagasbest.mygithub.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagasbest.mygithub.model.UserFollowing;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowingViewModel extends ViewModel {

        private MutableLiveData<ArrayList<UserFollowing>> listFollowing = new MutableLiveData<>();


        public void setFollowing(final Context context, String username) {

            final ArrayList <UserFollowing> listUserFollowing = new ArrayList<>();

            final String url = "https://api.github.com/users/"+ username + "/following";

            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", "token 2136e1a95f0ac825da57c8ef353019cdf2482f5e");
            client.addHeader("User-Agent", "request");
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    String result = new String(responseBody);

                    try {

                        JSONArray items = new JSONArray(result);

                        for (int i=0; i<items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            UserFollowing user = new UserFollowing();
                            user.setId(item.getInt("id"));
                            user.setNama(item.getString("login"));
                            user.setImage(item.getString("avatar_url"));

                            listUserFollowing.add(user);

                        }

                        listFollowing.postValue(listUserFollowing);

                    }catch (Exception e) {
                        Log.d("Exception: ", e.getMessage());
                        Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure: ", error.getMessage());
                    Toast.makeText(context, "onFailure: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    public LiveData<ArrayList<UserFollowing>> getUserFollowingList() {
        return listFollowing;
    }

}
