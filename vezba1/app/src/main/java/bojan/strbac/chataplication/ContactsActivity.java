package bojan.strbac.chataplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;
    private Button refresh;
    private ListView list;
    //private DbHelper db;
    private Model[] all_contacts;
    private CharacterAdapter adapter;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String user_id;

    private HttpHelper http;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        log_out = findViewById(R.id.con_log_out_id);
        log_out.setOnClickListener(this);
        refresh = findViewById(R.id.refresh_contact);
        refresh.setOnClickListener(this);

        adapter = new CharacterAdapter(this);
        list = findViewById(R.id.lista);
        list.setAdapter(adapter);
        //db = new DbHelper(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("loggedin_user", null);

        http = new HttpHelper();
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContactList();
    }

    /*public void deleteLoggedContact() {
        contacts = db.readContacts();
        adapter.addCharacters(contacts);

        if(contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getId().compareTo(user_id) == 0) {
                    adapter.removeContact(i);
                    break;
                }
            }
        }
    }*/

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.con_log_out_id) {
            new Thread(new Runnable() {
                public void run() {
                    try {

                        final boolean success = http.logOutUserFromServer(ContactsActivity.this, LOGOUT_URL);

                        handler.post(new Runnable(){
                            public void run() {
                                if (success) {
                                    startActivity(new Intent(ContactsActivity.this, MainActivity.class));
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String logout_error = prefs.getString("logoutErr", null);
                                    Toast.makeText(ContactsActivity.this, logout_error, Toast.LENGTH_SHORT).show();}
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        if(view.getId() == R.id.refresh_contact) {
            updateContactList();
        }
    }

    public void updateContactList() {

        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray contacts = http.getContactsFromServer(ContactsActivity.this, CONTACTS_URL);
                    handler.post(new Runnable(){
                        public void run() {
                            if (contacts != null) {

                                JSONObject json_contact;
                                all_contacts = new Model[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++) {
                                    try {
                                        json_contact = contacts.getJSONObject(i);
                                        all_contacts[i] = new Model(json_contact.getString("username"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                adapter.addCharacters(all_contacts);
                            }
                        }
                    });
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
}
