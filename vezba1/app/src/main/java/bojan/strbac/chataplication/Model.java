package bojan.strbac.chataplication;

import android.graphics.drawable.Drawable;

/**
 * Created by Korisnik on 30.3.2018..
 */

public class Model {
    public String initial;
    public String contact_name;
    public Drawable send_button;

    public Model(String m_initial, String m_contact_name, Drawable m_send_button) {
        this.initial = m_initial;
        this.contact_name = m_contact_name;
        this.send_button = m_send_button;
    }

    public String getInitial() {
        return initial;
    }

    public String getContact_name() {
        return contact_name;
    }

    public Drawable getSend_button() {
        return send_button;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setSend_button(Drawable send_button) {
        this.send_button = send_button;
    }
}
