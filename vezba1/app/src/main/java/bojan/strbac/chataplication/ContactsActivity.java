package bojan.strbac.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;
    private TextView contact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        log_out = findViewById(R.id.con_log_out_id);
        log_out.setOnClickListener(this);

        contact = findViewById(R.id.con_text_id);
        contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.con_log_out_id) {
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.con_text_id) {
            Intent intent2 = new Intent(ContactsActivity.this, MessageActivity.class);
            startActivity(intent2);
        }
    }
}
