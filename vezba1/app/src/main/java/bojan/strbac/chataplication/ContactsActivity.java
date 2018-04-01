package bojan.strbac.chataplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button log_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        CharacterAdapter adapter = new CharacterAdapter(this);
        adapter.AddCharacter(new Model("" + getResources().getString(R.string.contact1).toString().charAt(0), getResources().getString(R.string.contact1), getResources().getDrawable(R.drawable.ic_send_black)));
        adapter.AddCharacter(new Model("" + getResources().getString(R.string.contact2).toString().charAt(0), getResources().getString(R.string.contact2), getResources().getDrawable(R.drawable.ic_send_black)));
        adapter.AddCharacter(new Model("" + getResources().getString(R.string.contact3).toString().charAt(0), getResources().getString(R.string.contact3), getResources().getDrawable(R.drawable.ic_send_black)));
        adapter.AddCharacter(new Model("" + getResources().getString(R.string.contact4).toString().charAt(0), getResources().getString(R.string.contact4), getResources().getDrawable(R.drawable.ic_send_black)));
        adapter.AddCharacter(new Model("" + getResources().getString(R.string.contact5).toString().charAt(0), getResources().getString(R.string.contact5), getResources().getDrawable(R.drawable.ic_send_black)));

        log_out = findViewById(R.id.con_log_out_id);
        log_out.setOnClickListener(this);

        ListView list = (ListView) findViewById(R.id.lista);
        list.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.con_log_out_id) {
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
