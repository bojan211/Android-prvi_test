package bojan.strbac.chataplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;
    private ListView list;
    private DbHelper db;
    private Model[] contacts;
    private CharacterAdapter adapter;

    private static final String MY_PREFS_NAME = "PrefsFile";
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        log_out = findViewById(R.id.con_log_out_id);
        log_out.setOnClickListener(this);

        adapter = new CharacterAdapter(this);
        list = findViewById(R.id.lista);
        list.setAdapter(adapter);
        db = new DbHelper(this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user_id = prefs.getString("loggedin_user", null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        deleteLoggedContact();
    }

    public void deleteLoggedContact() {
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
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.con_log_out_id) {
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
