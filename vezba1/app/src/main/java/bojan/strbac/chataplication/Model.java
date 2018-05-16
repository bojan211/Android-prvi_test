package bojan.strbac.chataplication;

import android.graphics.drawable.Drawable;

/**
 * Created by Korisnik on 30.3.2018..
 */

public class Model {
    //private String id;
    //private String first_name;
    //private String last_name;
    private String username;

    public Model( String m_username) {//String m_id, String m_first, String m_last,
        //this.id = m_id;
        //this.first_name = m_first;
        //this.last_name = m_last;
        this.username = m_username;
    }

    /*public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }*/

    public String getUsername() {
        return username;
    }
}
