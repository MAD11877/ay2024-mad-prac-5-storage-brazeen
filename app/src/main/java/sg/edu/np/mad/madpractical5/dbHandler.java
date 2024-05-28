package sg.edu.np.mad.madpractical5;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Random;

import android.database.sqlite.SQLiteException;
import android.util.Log;

public class dbHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myusers.db";
    private static final String USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_FOLLOWED = "followed";

    public dbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        Log.i("Database Operations", "Creating a Table.");
        try {
            String CREATE_USERS_TABLE = "CREATE TABLE " + USERS + "(" + COLUMN_NAME + " TEXT," + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FOLLOWED + " TEXT" +")";
            db.execSQL(CREATE_USERS_TABLE);

//          Generate 1 Default User
            for (int i = 1; i<21; i++){
                int name = new Random().nextInt(9999999);
                int description = new Random().nextInt(9999999);
                boolean followed = new Random().nextBoolean();
                ContentValues values = new ContentValues();
//          Don't need this. ID is auto create and incremented by SQLite
//          values.put(COLUMN_ID, id);
                values.put(COLUMN_NAME, "Name" + String.valueOf(name));
                values.put(COLUMN_DESCRIPTION, "Description" + String.valueOf(description));
                values.put(COLUMN_FOLLOWED, String.valueOf(followed));
                db.insert(USERS, null, values);
            }

            Log.i("Database Operations", "Table created successfully.");
//          db.close();
        } catch (SQLiteException e) {
            Log.i("Database Operations", "Error creating table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + USERS);
        onCreate(db);
    }

    //  Get 1 user record based on user name.
    @SuppressLint("Range")
    public User getUser(String username) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        String query = "SELECT * FROM " + USERS + " WHERE " + COLUMN_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            user.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setFollowed(cursor.getInt(cursor.getColumnIndex(COLUMN_FOLLOWED)) == 1);
        }
        cursor.close();
        //db.close();
        return user;
    }


    //  Get 1 user record based on user unique id.
    public User getrUser(int user_id) {
        SQLiteDatabase db = getReadableDatabase();
        User user = new User("johnny", "johndoe", 3, false);
        Cursor cursor = db.query(USERS, new String[] { COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_ID, COLUMN_FOLLOWED}, COLUMN_ID + "=?",
                new String[] { String.valueOf(user_id) }, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            int id = cursor.getInt((int)cursor.getColumnIndex("id"));
            String name = cursor.getString((int)cursor.getColumnIndex("name"));
            String description = cursor.getString((int)cursor.getColumnIndex("description"));
            boolean followed = Boolean.parseBoolean(cursor.getString((int)cursor.getColumnIndex("followed")));
            user.setId(id);
            user.setName(name);
            user.setDescription(description);
            user.setFollowed(followed);
            cursor.close();
        }
//      db.close();
        return user;
    }

    //  READ all user records
    public ArrayList<User> getUsers() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM " + USERS;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt((int)cursor.getColumnIndex("id"));
            String name = cursor.getString((int)cursor.getColumnIndex("name"));
            String description = cursor.getString((int)cursor.getColumnIndex("description"));
            boolean followed = Boolean.parseBoolean(cursor.getString((int)cursor.getColumnIndex("followed")));
            User user = new User(name, description, id, followed);
            userList.add(user);
        }
        cursor.close();
//      db.close();
        return userList;
    }

    //  UPDATE user record
    public void updateUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLLOWED,  user.getFollowed());
        String clause = "id=?";
        String[] args = {String.valueOf(user.getId())};
        db.update(USERS, values, clause, args);
//      db.close();
    }

    @Override
    public void close() {
        Log.i("Database Operations", "Database is closed.");
       // super.close();
    }
}
