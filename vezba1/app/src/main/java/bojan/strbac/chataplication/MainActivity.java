package bojan.strbac.chataplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button log;
    private Button reg;

    boolean user_entered = false;
    boolean pass_entered = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.user_text);
        password = findViewById(R.id.pass_text);

        log = findViewById(R.id.log_button);
        log.setOnClickListener(this);
        reg = findViewById(R.id.reg_button);
        reg.setOnClickListener(this);

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
        if(view.getId() == R.id.reg_button) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if(view.getId() == R.id.log_button) {
            Intent intent2 = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent2);
        }
        //DODATI OVO KAD ZAVRSIM TRECI AKTIVITI else if(view.getId() == R.id.log_button)
    }
}
