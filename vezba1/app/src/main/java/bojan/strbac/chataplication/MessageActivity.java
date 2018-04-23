package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button log_out;
    private Button send;
    private EditText message;
    private ListView list;
    final MessageAdapter adapter = new MessageAdapter(this);
    private DbHelper db;
    private ModelMessage[] messages;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String receiver_user_id;
    private String sender_user_id;
    private String receiver_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        log_out = findViewById(R.id.mes_log_out_id);
        log_out.setOnClickListener(this);
        send = findViewById(R.id.mes_send_id);
        send.setOnClickListener(this);
        message = findViewById(R.id.mes_message_id);
        list = findViewById(R.id.message_list_ID);
        TextView contact_name = findViewById(R.id.contact_name_ID);

        //Setting contact name with bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("item_name_ID");
        contact_name.setText(name);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sender_user_id = prefs.getString("loggedin_user", null);
        receiver_user_id = prefs.getString("receiver_user_id", null);

        db = new DbHelper(this);

        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int position_for_delete = position;

                final ModelMessage message = (ModelMessage) adapter.getItem(position_for_delete);

                if (messages != null) {
                    for (int i = 0; i < messages.length; i++) {
                        if (messages[i].getMessage_id().compareTo(message.getMessage_id()) == 0) {
                            db.deleteMessage(message.getMessage_id());
                            break;
                        }
                    }
                }

                updateMessagesList();
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
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.mes_send_id) {
            String message_for_sending = message.getText().toString();
            ModelMessage m_message = new ModelMessage(null, sender_user_id, receiver_user_id, message_for_sending);
            db.insertMessage(m_message);
            updateMessagesList();
            Toast.makeText(getApplicationContext(), R.string.message_send, Toast.LENGTH_LONG).show();
            message.getText().clear();
        }

    }

    public void updateMessagesList() {
        messages = db.readMessages(sender_user_id, receiver_user_id);
        adapter.addMessages(messages);
    }
}
