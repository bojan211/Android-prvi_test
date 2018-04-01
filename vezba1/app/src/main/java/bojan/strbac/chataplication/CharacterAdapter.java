package bojan.strbac.chataplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    
    public CharacterAdapter(Context context_par) {
        context = context_par;
        characters = new ArrayList<Model>();
    }
    
    public void AddCharacter(Model model){
        characters.add(model);
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
            final ImageView next_button = (ImageView) convert_view.findViewById(R.id.next_button_ID);
            final View bundle_convert_view = convert_view;
            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    TextView text = bundle_convert_view.findViewById(R.id.item_name_ID);
                    String name = text.getText().toString();
                    bundle.putString("item_name_ID", name);

                    Intent intent = new Intent(context,MessageActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            ViewHolder holder = new ViewHolder();
            holder.initial = (TextView) convert_view.findViewById(R.id.item_letter_ID);
            holder.full_name = (TextView) convert_view.findViewById(R.id.item_name_ID);
            holder.next_button = (ImageView) convert_view.findViewById(R.id.next_button_ID);
            holder.initial.setBackgroundColor(RandomColor());

            convert_view.setTag(holder);
        }

        Model model = (Model) getItem(position);
        ViewHolder holder = (ViewHolder) convert_view.getTag();
        holder.initial.setText(model.initial);
        holder.full_name.setText(model.contact_name);
        holder.next_button.setImageDrawable(model.send_button);
        holder.initial.setBackgroundColor(RandomColor());

        return convert_view;
    }

    private class ViewHolder {
        public TextView initial = null;
        public TextView full_name = null;
        public ImageView next_button = null;
    }

    public int RandomColor(){
        Random i = new Random();
        return Color.argb(255, i.nextInt(256), i.nextInt(256), i.nextInt(256));
    }
}
