package bojan.strbac.chataplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class HttpHelper {

    private static final int SUCCESS = 200;
    public static final String MY_PREFS_NAME = "PrefsFile";
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String GET_NOTIFICATION_URL = BASE_URL + "/getfromservice";

    private static final String TAG = "HttpHelper";

    public boolean registerUserOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();


        if(responseCode!=SUCCESS) {
            SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String responseMsg = urlConnection.getResponseMessage();
            String registerErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("register_error_message", registerErr);
            editor.apply();
        }

        urlConnection.disconnect();

        return (responseCode==SUCCESS);
    }

    public boolean logInUserOnServer(Context context, String urlString, JSONObject jsonObject) throws IOException{

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        String sessionId = urlConnection.getHeaderField("sessionid");

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode==SUCCESS) {
            editor.putString("sessionId", sessionId);
            editor.apply();
        } else {
            String responseMsg = urlConnection.getResponseMessage();
            String loginErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("login_error_message", loginErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getContactsFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        //urlConnection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getContactsErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getContactsErr", getContactsErr);
            editor.apply();
            return null;
        }

    }

    public boolean logOutUserFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String logoutErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("logoutErr", logoutErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean sendMessageToServer(Context context, String urlString, JSONObject jsonObject) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        urlConnection.setReadTimeout(1000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        /*needed when used POST or PUT methods*/
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode =  urlConnection.getResponseCode();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if(responseCode!=SUCCESS) {
            String responseMsg = urlConnection.getResponseMessage();
            String sendMsgErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("sendMsgErr", sendMsgErr);
            editor.apply();
        }

        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public JSONArray getMessagesFromServer(Context context, String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();


        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        /*header fields*/
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();

        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        String responseMsg = urlConnection.getResponseMessage();

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        urlConnection.disconnect();

        if (responseCode == SUCCESS){
            return new JSONArray(jsonString);
        } else {
            String getMessagesErr = Integer.toString(responseCode) + " : " + responseMsg;
            editor.putString("getMessagesErr", getMessagesErr);
            editor.apply();
            return null;
        }
    }

    public boolean httpDelete(Context context, String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);

        Log.d(TAG, "httpDelete: "+","  + jsonObject.toString());

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }


        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        /*write json object*/
        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();

        int responseCode = urlConnection.getResponseCode();

        Log.i("STATUS", String.valueOf(responseCode));
        Log.i("MSG" , urlConnection.getResponseMessage());
        urlConnection.disconnect();
        return (responseCode==SUCCESS);
    }

    public boolean getNotification(Context context) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(GET_NOTIFICATION_URL);
        urlConnection = (HttpURLConnection) url.openConnection();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String sessionId = prefs.getString("sessionId", null);


        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("sessionid", sessionId);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");

        try {
            urlConnection.connect();
        } catch (IOException e) {
            return false;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();

        Boolean response = Boolean.valueOf(sb.toString());

        urlConnection.disconnect();
        return (response);
    }

    public boolean checkServer() throws IOException {

        HttpURLConnection urlConnection;
        java.net.URL url = new URL(BASE_URL);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Connection", "close");
        urlConnection.setConnectTimeout(2000 /* milliseconds */ );

        try {
            urlConnection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
