package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MessageActivity";
    
    private Button log_out;
    private Button refresh;
    private Button send;
    private EditText message;
    private ListView list;
    final MessageAdapter adapter = new MessageAdapter(this);
    //private DbHelper db;
    private ModelMessage[] all_messages;

    Crypto m_crypto;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String receiver_user_id;
    private String sender_user_id;
    private String receiver_username;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";


    private HttpHelper http;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        log_out = findViewById(R.id.mes_log_out_id);
        log_out.setOnClickListener(this);
        send = findViewById(R.id.mes_send_id);
        send.setOnClickListener(this);
        refresh = findViewById(R.id.refresh_messages);
        refresh.setOnClickListener(this);
        message = findViewById(R.id.mes_message_id);
        list = findViewById(R.id.message_list_ID);
        TextView contact_name = findViewById(R.id.contact_name_ID);

        //Setting contact name with bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("item_name_ID");
        contact_name.setText(name);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sender_user_id = prefs.getString("loggedin_username", null);
        receiver_user_id = prefs.getString("receiver_user_id", null);

        //db = new DbHelper(this);
        http = new HttpHelper();
        handler = new Handler();

        m_crypto = new Crypto();

        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new Thread(new Runnable() {
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        ModelMessage model  = (ModelMessage) adapter.getItem(position);
                        String message_for_del = model.getMessage();

                        try {
                            jsonObject.put("sender", sender_user_id);
                            jsonObject.put("receiver", receiver_user_id);
                            jsonObject.put("data", message_for_del);

                            Log.d(TAG, "run: " + jsonObject.toString());

                            final boolean success = http.httpDelete(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable(){
                                public void run() {
                                    if (success) {
                                        Toast.makeText(MessageActivity.this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                        message.getText().clear();
                                        updateMessagesList();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                        String sendMsgErr = prefs.getString("sendMsgErr", null);
                                        Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return true;
            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = message.getText().toString();

                if(text.length() > 0) {
                    send.setEnabled(true);
                }
                else {
                    send.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMessagesList();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.mes_log_out_id) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        final boolean response = http.logOutUserFromServer(MessageActivity.this, LOGOUT_URL);
                        handler.post(new Runnable(){
                            public void run() {
                                if (response) {
                                    startActivity(new Intent(MessageActivity.this, MainActivity.class));
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String logoutErr = prefs.getString("logoutErr", null);
                                    Toast.makeText(MessageActivity.this, logoutErr, Toast.LENGTH_SHORT).show();
                                }
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
        if(view.getId() == R.id.mes_send_id) {
            new Thread(new Runnable() {
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("receiver", receiver_user_id);
                        String my_message = message.getText().toString();
                        String crypted_message = m_crypto.crypt(my_message);
                        jsonObject.put("data", crypted_message);
                        final boolean success = http.sendMessageToServer(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                        handler.post(new Runnable(){
                            public void run() {
                                if (success) {
                                    Toast.makeText(MessageActivity.this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                    message.getText().clear();
                                    updateMessagesList();
                                } else {
                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String sendMsgErr = prefs.getString("sendMsgErr", null);
                                    Toast.makeText(MessageActivity.this, sendMsgErr, Toast.LENGTH_SHORT).show();
                                }
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

        if(view.getId() == R.id.refresh_messages) {
            updateMessagesList();
        }

    }

    public void updateMessagesList() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    final JSONArray messages = http.getMessagesFromServer(MessageActivity.this, GET_MESSAGE_URL+receiver_user_id);

                    handler.post(new Runnable(){
                        public void run() {
                            if (messages != null) {

                                JSONObject json_message;
                                all_messages = new ModelMessage[messages.length()];

                                for (int i = 0; i < messages.length(); i++) {
                                    try {
                                        json_message = messages.getJSONObject(i);
                                        //all_messages[i] = new ModelMessage(json_message.getString("sender"),json_message.getString("data"));
                                        String message = json_message.getString("data");
                                        String decrypted_message = m_crypto.crypt(message);
                                        all_messages[i] = new ModelMessage(json_message.getString("sender"), decrypted_message);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                adapter.addMessages(all_messages);
                            } else {
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                String getMessagesErr = prefs.getString("getMessagesErr", null);
                                Toast.makeText(MessageActivity.this, getMessagesErr, Toast.LENGTH_SHORT).show();
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
