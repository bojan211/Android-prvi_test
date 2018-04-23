package bojan.strbac.chataplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Korisnik on 1.4.2018..
 */

public class MessageAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ModelMessage> messages;

    private static final String MY_PREFS_NAME = "PrefsFile";

    public MessageAdapter(Context context_par) {
        context = context_par;
        messages = new ArrayList<ModelMessage>();
    }

    public void addMessages(ModelMessage[] m_messages) {
        messages.clear();
        if (messages != null) {
            for (ModelMessage message : messages) {
                messages.add(message);
            }
        }
        notifyDataSetChanged();
    }

    public void addMessage(ModelMessage model){
        messages.add(model);
        notifyDataSetChanged();
    }

    public void removeMessage(ModelMessage model){
        messages.remove(model);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = messages.get(position);
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
            convert_view = inflater.inflate(R.layout.raw_item_message, null);

            ViewHolder holder = new ViewHolder();
            holder.message = (TextView) convert_view.findViewById(R.id.item_message_ID);

            convert_view.setTag(holder);
        }

        ModelMessage model = (ModelMessage) getItem(position);
        ViewHolder holder = (ViewHolder) convert_view.getTag();
        holder.message.setText(model.getMessage());

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        String sender_userid = prefs.getString("loggedin_userId", null);

        if(model.getSender_id().compareTo(sender_userid) == 0) {
            holder.message.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        }
        else {
            holder.message.setBackgroundColor(Color.parseColor("#b7b3b3"));
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        }


        return convert_view;
    }

    private class ViewHolder {
        public TextView message = null;
    }
}
