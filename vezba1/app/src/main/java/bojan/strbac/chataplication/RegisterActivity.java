package bojan.strbac.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button reg_button;
    private EditText email;
    private EditText username;
    private EditText password;
    private DatePicker date;

    boolean email_entered = false;
    boolean user_entered = false;
    boolean pass_entered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.reg_user_id);
        password = findViewById(R.id.reg_pass_id);
        email = findViewById(R.id.reg_email_id);
        date = findViewById(R.id.reg_date_id);


        reg_button = findViewById(R.id.reg_button_id);
        reg_button.setOnClickListener(this);

        //Settin max date on date picker to be today date!
        Date real_time = Calendar.getInstance().getTime();
        date.setMaxDate(real_time.getTime());


        //Enabling register button
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
                    if(pass_entered == true && email_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else {
                    user_entered = false;
                    reg_button.setEnabled(false);
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
                    if (user_entered == true && email_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else{
                    pass_entered = false;
                    reg_button.setEnabled(false);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = email.getText().toString();

                if(Patterns.EMAIL_ADDRESS.matcher(text).matches() && text.length() > 0) {
                    email_entered = true;
                    if (user_entered == true && pass_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else {
                    email_entered = false;
                    reg_button.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.reg_button_id) {
            Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
            startActivity(intent);
        }
    }
}
