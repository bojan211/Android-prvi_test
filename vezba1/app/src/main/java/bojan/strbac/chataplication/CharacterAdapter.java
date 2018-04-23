package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Korisnik on 30.3.2018..
 */

public class CharacterAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Model> characters;

    public static final String MY_PREFS_NAME = "PrefsFile";
    
    public CharacterAdapter(Context context_par) {
        context = context_par;
        characters = new ArrayList<Model>();
    }

    public void removeContact(int position){
        characters.remove(position);
        notifyDataSetChanged();
    }

    public void addCharacters(Model[] contacts){
        characters.clear();
        if(contacts != null) {
            for(Model contact : contacts) {
                characters.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = characters.get(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {

        if(convert_view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert_view = inflater.inflate(R.layout.raw_item, null);
            //Set on click listener for button in list item !!!
            final ImageView next_button = (ImageButton) convert_view.findViewById(R.id.next_button_ID);
            final View bundle_convert_view = convert_view;
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    TextView text = bundle_convert_view.findViewById(R.id.item_name_ID);
                    String name = text.getText().toString();
                    bundle.putString("item_name_ID", name);

                    SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString("receiver_user_id", view.getTag().toString());
                    editor.apply();

                    Intent intent = new Intent(context,MessageActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            ViewHolder holder = new ViewHolder();
            holder.initial = (TextView) convert_view.findViewById(R.id.item_letter_ID);
            holder.full_name = (TextView) convert_view.findViewById(R.id.item_name_ID);
            holder.next_button = (ImageButton) convert_view.findViewById(R.id.next_button_ID);
            holder.next_button.setTag(position);
            holder.initial.setBackgroundColor(RandomColor());

            convert_view.setTag(holder);
        }

        Model model = (Model) getItem(position);
        ViewHolder holder = (ViewHolder) convert_view.getTag();
        holder.initial.setText(model.getFirst_name().substring(0,1).toUpperCase());
        String full_name = model.getFirst_name() + " " + model.getLast_name();
        holder.full_name.setText(full_name);
        //holder.next_button = (ImageView) convert_view.findViewById(R.id.next_button_ID);
        holder.next_button.setTag(model.getId());
        holder.initial.setBackgroundColor(RandomColor());

        return convert_view;
    }

    private class ViewHolder {
        public TextView initial = null;
        public TextView full_name = null;
        public ImageButton next_button = null;
    }

    public int RandomColor(){
        Random i = new Random();
        return Color.argb(255, i.nextInt(256), i.nextInt(256), i.nextInt(256));
    }
}
