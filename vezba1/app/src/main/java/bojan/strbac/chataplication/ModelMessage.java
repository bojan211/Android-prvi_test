package bojan.strbac.chataplication;

/**
 * Created by Korisnik on 1.4.2018..
 */

public class ModelMessage {
    public String message;
    public boolean sending = false;

    public ModelMessage(String m_message, boolean m_type) {
        this.message = m_message;
        this.sending = m_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
