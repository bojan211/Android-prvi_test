package bojan.strbac.chataplication;

/**
 * Created by Korisnik on 1.4.2018..
 */

public class ModelMessage {
    //private String message_id;
    private String sender_id;
    //private String receiver_id;
    private String message;


    public ModelMessage( String m_sender_id,String m_message) { //, String m_receiver_id, String m_message_id
        //this.message_id = m_message_id;
        this.sender_id = m_sender_id;
        //this.receiver_id = m_receiver_id;
        this.message = m_message;
    }

    /*public String getMessage_id() {
        return message_id;
    }*/

    public String getSender_id() {
        return sender_id;
    }

    /*public String getReceiver_id() {
        return receiver_id;
    }*/

    public String getMessage() {
        return message;
    }
}
