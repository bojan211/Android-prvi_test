package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
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

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;
    private Button send;
    private EditText message;
    private ListView list;
    final MessageAdapter adapter = new MessageAdapter(this);


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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("item_name_ID");
        contact_name.setText(name);

        //MessageAdapter adapter = new MessageAdapter(this);
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message1).toString(),true));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message2).toString(),false));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message3).toString(),true));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message4).toString(),false));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message5).toString(),true));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message6).toString(),false));
        adapter.AddCharacter(new ModelMessage(getResources().getString(R.string.message7).toString(),true));

        //ListView list = (ListView) findViewById(R.id.message_list_ID);
        list.setAdapter(adapter);



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
    public void onClick(View view) {
        if(view.getId() == R.id.mes_log_out_id) {
            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.mes_send_id) {
            Context context = getApplicationContext();
            CharSequence toast_text = "Message is sent!";
            int duration = Toast.LENGTH_SHORT;
            String text = message.getText().toString();
            adapter.AddCharacter(new ModelMessage(text,true));
            Toast toast = Toast.makeText(context, toast_text, duration);
            toast.show();
            message.setText("");
        }

    }
}
