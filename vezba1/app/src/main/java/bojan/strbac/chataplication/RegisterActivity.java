package bojan.strbac.chataplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";
    private Button reg_button;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private DatePicker date;
    //private DbHelper db;
    //private int i;
    //private int contact_exists=1;

    boolean email_entered = false;
    boolean user_entered = false;
    boolean pass_entered = false;
    boolean first_name_entered = false;
    boolean last_name_entered = false;

    private HttpHelper http;
    private Handler handler;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String REGISTER_URL = BASE_URL + "/register";

    public static String MY_PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.reg_user_id);
        password = findViewById(R.id.reg_pass_id);
        first_name = findViewById(R.id.reg_first_name_id);
        last_name = findViewById(R.id.reg_last_name_id);
        email = findViewById(R.id.reg_email_id);
        date = findViewById(R.id.reg_date_id);

        // Creating data base for contacts
        //db = new DbHelper(this);

        reg_button = findViewById(R.id.reg_button_id);
        reg_button.setOnClickListener(this);

        //Setting max date on date picker to be today date!
        Date real_time = Calendar.getInstance().getTime();
        date.setMaxDate(real_time.getTime());

        http = new HttpHelper();
        handler = new Handler();

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
                    if(pass_entered == true && email_entered == true && first_name_entered == true && last_name_entered == true) {
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
                    if (user_entered == true && email_entered == true && first_name_entered == true && last_name_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else{
                    pass_entered = false;
                    reg_button.setEnabled(false);
                }
            }
        });

        first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = first_name.getText().toString();

                if(text.length() >= 0) {
                    first_name_entered = true;
                    if (user_entered == true && email_entered == true && pass_entered == true && last_name_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else{
                    first_name_entered = false;
                    reg_button.setEnabled(false);
                }
            }
        });

        last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = last_name.getText().toString();

                if(text.length() >= 0) {
                    last_name_entered = true;
                    if (user_entered == true && email_entered == true && pass_entered == true && first_name_entered == true) {
                        reg_button.setEnabled(true);
                    }
                }
                else{
                    last_name_entered = false;
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
                    if (user_entered == true && pass_entered == true && first_name_entered == true && last_name_entered == true) {
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        jsonObject.put("email", email.getText().toString());

                        final boolean response = http.registerUserOnServer(RegisterActivity.this, REGISTER_URL, jsonObject);
                        Log.d(TAG, "run: " + response);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response) {
                                    Toast.makeText(RegisterActivity.this, getText(R.string.successfully_registred_user), Toast.LENGTH_SHORT).show();
                                    Intent LoginActivity_intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(LoginActivity_intent);
                                }
                                else {
                                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String error_message = prefs.getString("register_error_message", null);
                                    Toast.makeText(RegisterActivity.this, error_message, Toast.LENGTH_SHORT).show();
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
    }
}
