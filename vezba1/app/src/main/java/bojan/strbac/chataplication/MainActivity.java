package bojan.strbac.chataplication;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button log;
    private Button reg;

    private DbHelper db;

    public static final String MY_PREFS_NAME = "PrefsFile";

    boolean user_entered = false;
    boolean pass_entered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.main_user_text);
        password = findViewById(R.id.main_pass_text);

        log = findViewById(R.id.main_log_button);
        log.setOnClickListener(this);
        reg = findViewById(R.id.main_reg_button);
        reg.setOnClickListener(this);

        db = new DbHelper(this);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = username.getText().toString();

                if(text.length() > 0) {
                    user_entered = true;
                    if(pass_entered == true) {
                        log.setEnabled(true);
                    }
                }
                else {
                    user_entered = false;
                    log.setEnabled(false);
                }

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = password.getText().toString();

                if(text.length() >= 6) {
                    pass_entered = true;
                    if(user_entered == true) {
                        log.setEnabled(true);
                    }
                }
                else {
                    pass_entered = false;
                    log.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.main_reg_button) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.main_log_button) {
            Intent intent2 = new Intent(MainActivity.this, ContactsActivity.class);

            Model[] contacts = db.readContacts();

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

            int contact_found = 0;

            if(contacts != null) {
                for(int i = 0; i < contacts.length; i++) {
                    if((contacts[i].getUsername().compareTo(username.getText().toString())) == 0 ) {
                        editor.putString("loggedin_user", contacts[i].getId());
                        editor.apply();
                        contact_found = 1;
                    }
                }
            }

            if(contact_found == 1) {
                startActivity(intent2); // If contact is found, go to contact activity
            }
            else {
                Toast.makeText(this, getText(R.string.user_doesnt_exists), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
