package bojan.strbac.chataplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Display;

/**
 * Created by Korisnik on 22.4.2018..
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_CONTACTS = "Contact";
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_USERNAME = "Userame";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";

    public static final String TABLE_NAME_MESSAGE = "Messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECEIVER_ID = "receiver_id";
    public static final String COLUMN_MESSAGE = "message";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT);" );

        db.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGE + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MESSAGE + " TEXT); " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertContact(Model model) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, model.getId());
        values.put(COLUMN_USERNAME, model.getUsername());
        values.put(COLUMN_FIRST_NAME, model.getFirst_name());
        values.put(COLUMN_LAST_NAME, model.getLast_name());

        db.insert(TABLE_NAME_CONTACTS, null, values);
        close();
    }

    public void insertMessage(ModelMessage model) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID, model.getMessage_id());
        values.put(COLUMN_SENDER_ID, model.getSender_id());
        values.put(COLUMN_RECEIVER_ID, model.getReceiver_id());
        values.put(COLUMN_MESSAGE, model.getMessage());

        db.insert(TABLE_NAME_MESSAGE, null, values);
        close();
    }

    public Model[] readContacts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, null, null);

        if(cursor.getCount() <= 0) {
            return null;
        }

        Model[] contacts = new Model[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    public Model readContact(String index) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_CONTACT_ID + "=?",
                new String[] {index}, null, null, null);
        cursor.moveToFirst();
        Model contact = createContact(cursor);

        close();
        return contact;
    }

    private Model createContact(Cursor cursor) {
        String contact_id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));

        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String first_name = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String last_name = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
        return new Model(contact_id, first_name, last_name, username);
    }

    public void deleteContact(String index) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CONTACTS, COLUMN_CONTACT_ID + "=?", new String[] {index});
        close();
    }

    public ModelMessage[] readMessages(String sender, String receiver) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, "(sender_id =? AND receiver_id =?) OR (sender_id =? AND receiver_id =?)", new String[] {sender,receiver,receiver,sender}, null, null, null, null);

        if(cursor.getCount() <= 0) {
            return null;
        }

        ModelMessage[] messages = new ModelMessage[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    public ModelMessage readMessage(String index) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, COLUMN_CONTACT_ID + "=?",
                new String[] {index}, null, null, null);
        cursor.moveToFirst();
        ModelMessage message = createMessage(cursor);

        close();
        return message;
    }

    private ModelMessage createMessage(Cursor cursor) {
        String message_id = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        String sender_id = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID));
        String receiver_id = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID));
        String message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
        return new ModelMessage(message_id, sender_id, receiver_id, message);
    }

    public void deleteMessage(String message_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MESSAGE, COLUMN_MESSAGE_ID + "=?", new String[] {message_id});
        close();
    }
}
