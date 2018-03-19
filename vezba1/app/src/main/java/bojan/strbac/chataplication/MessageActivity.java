package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;
    private Button send;
    private EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        log_out = findViewById(R.id.mes_log_out_id);
        log_out.setOnClickListener(this);
        send = findViewById(R.id.mes_send_id);

        message = findViewById(R.id.mes_message_id);

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
            CharSequence text = "Message is sent!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            message.setText("");
        }

    }
}
