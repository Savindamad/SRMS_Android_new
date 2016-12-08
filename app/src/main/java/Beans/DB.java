package Beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by savinda on 12/5/16.
 */

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_rms.DB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_MENU = "menu";
    public static final String TABLE_USER = "user";
    public static final String ITEM_COLUMN_CODE = "code";
    public static final String ITEM_COLUMN_NAME = "name";
    public static final String ITEM_COLUMN_TYPE = "type";
    public static final String ITEM_COLUMN_DESCRIPTION = "description";
    public static final String ITEM_COLUMN_PRICE = "price";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_F_NAME = "f_name";
    public static final String USER_COLUMN_L_NAME = "l_name";
    public static final String USER_COLUMN_TYPE = "type";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_MENU + "(" +
                    ITEM_COLUMN_CODE + " TEXT, " +
                    ITEM_COLUMN_NAME + " TEXT, " +
                    ITEM_COLUMN_TYPE + " TEXT, " +
                    ITEM_COLUMN_DESCRIPTION + " TEXT, " +
                    ITEM_COLUMN_PRICE + " TEXT);"
            );
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USER + "(" +
                    USER_COLUMN_ID + " TEXT, " +
                    USER_COLUMN_F_NAME + " TEXT, " +
                    USER_COLUMN_L_NAME + " TEXT, " +
                    USER_COLUMN_TYPE + " TEXT);");
            System.out.println("table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }
    public void addMenuItem(ArrayList<MenuItems> menu, SQLiteDatabase db){
        db.execSQL("delete from "+ TABLE_MENU);
        for(int i=0; i<menu.size(); i++){
            MenuItems item = menu.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_COLUMN_CODE, item.getItemCode());
            contentValues.put(ITEM_COLUMN_NAME, item.getItemName());
            contentValues.put(ITEM_COLUMN_TYPE, item.getItemType());
            contentValues.put(ITEM_COLUMN_DESCRIPTION, item.getItemDescription());
            contentValues.put(ITEM_COLUMN_PRICE, item.getItemPrice());
            db.insert(TABLE_MENU, null, contentValues);
            System.out.println(i);
        }
    }
    public ArrayList<MenuItems> getMenu(SQLiteDatabase db){
        ArrayList<MenuItems> menu = new ArrayList<MenuItems>();

        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_MENU, null );
        res.moveToFirst();
        while (!res.isAfterLast()){
            String code = res.getString(res.getColumnIndex(ITEM_COLUMN_CODE));
            String name = res.getString(res.getColumnIndex(ITEM_COLUMN_NAME));
            String type = res.getString(res.getColumnIndex(ITEM_COLUMN_TYPE));
            String description = res.getString(res.getColumnIndex(ITEM_COLUMN_DESCRIPTION));
            String price = res.getString(res.getColumnIndex(ITEM_COLUMN_PRICE));

            MenuItems item = new MenuItems(code,name,type,description,price);
            menu.add(item);
            res.moveToNext();
        }
        return menu;
    }
    public void setUser(User user, SQLiteDatabase db){
        System.out.print("set user");
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, user.getUserID());
        contentValues.put(USER_COLUMN_F_NAME, user.getFirstName());
        contentValues.put(USER_COLUMN_L_NAME, user.getLastName());
        contentValues.put(USER_COLUMN_TYPE, user.getType());
        db.insert(TABLE_USER, null, contentValues);

    }
    public User getUser(SQLiteDatabase db){
        System.out.print("check availability");
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_USER, null );
        User user;
        if(res.moveToFirst()){
            String id = res.getString(res.getColumnIndex(USER_COLUMN_ID));
            String f_name = res.getString(res.getColumnIndex(USER_COLUMN_F_NAME));
            String l_name = res.getString(res.getColumnIndex(USER_COLUMN_L_NAME));
            String type = res.getString(res.getColumnIndex(USER_COLUMN_TYPE));
            user = new User(f_name,l_name,id,type);
            return user;
        }
        else{
            return null;
        }
    }
    public void logout(SQLiteDatabase db){
        db.execSQL("delete from "+ TABLE_USER);
        //todo
    }
    public boolean checkLogin(SQLiteDatabase db){
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_USER, null );
        if(res.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

}
