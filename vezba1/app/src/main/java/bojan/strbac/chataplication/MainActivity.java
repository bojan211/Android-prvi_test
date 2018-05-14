package bojan.strbac.chataplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button log;
    private Button reg;

    //private DbHelper db;

    public static final String MY_PREFS_NAME = "PrefsFile";

    private Context context;
    private HttpHelper http;
    private Handler handler;

    boolean user_entered = false;
    boolean pass_entered = false;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";

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

        //db = new DbHelper(this);
        context = this;
        http = new HttpHelper();
        handler = new Handler();

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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());

                        final boolean response = http.logInUserOnServer(context, LOGIN_URL, jsonObject);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response) {
                                    SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putString("loggedin_username", username.getText().toString());
                                    editor.apply();

                                    Intent intent2 = new Intent(MainActivity.this, ContactsActivity.class);
                                    startActivity(intent2);
                                }
                                else {
                                    SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                    String error_message = prefs.getString("login_error_message", null);
                                    Toast.makeText(MainActivity.this, error_message, Toast.LENGTH_SHORT).show();
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
