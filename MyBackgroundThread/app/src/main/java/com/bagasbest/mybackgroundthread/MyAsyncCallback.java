package com.bagasbest.mybackgroundthread;

public interface MyAsyncCallback {
    void onPreExecute();
    void onPostExecute(String text);
}
