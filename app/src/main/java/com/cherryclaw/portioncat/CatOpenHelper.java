package com.cherryclaw.portioncat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * CatOpenHelper extends SQLiteOpenHelper and is used to create the cat database
 * when the app is first created.  It also handles all methods that access the database.
 *
 * Cats are added here, deleted, and queried.  The database versions are also maintained here.
 *
 * @author      Jocelynne Gonzales
 * @see         SQLiteOpenHelper
 * @see         SQLiteDatabase
 */


public class CatOpenHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "catDB.db";
    private static final String TABLE_CATS = "cat";



    CatOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * On app creation, cat schema is made with attributes including primary key _id, which is
     * required to autoincrement for Cat individual IDs.  This allows support for
     * multiple cats with the same attributes, including their name.
     * <p>
     * Schema is CAT(_id, name, weight, birthday, sex, catImage, foodType).  If more attributes
     * are needed in the future, separate tables will be created using _id as the primary key.
     *
     * @param  db  SQLite database used to execute statements.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CAT_TABLE_CREATE =
                "CREATE TABLE CAT("+
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        "name TEXT,"+
                        "weight decimal(6,2),"+
                        "birthday TEXT,"+
                        "sex TEXT,"+
                        "catImage BLOB,"+
                        "foodType INTEGER"+ ");";

        db.execSQL(CAT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CAT");
        onCreate(db);
    }

    /**
     * Adds cat object to database by accessing its variables.
     *
     * @param  cat  Cat object
     */
    public void addCat(Cat cat) {
        //Opens writable database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", cat.getName());
        values.put("weight", cat.getWeight());
        values.put("birthday", cat.getBirthday());
        values.put("foodType", cat.getFoodType());
        values.put("sex", cat.getSex());

        //Image is added as byte array
        values.put("catImage", cat.getCatImage());

        //Inserts cat table to database
        db.insert(TABLE_CATS, null, values);
        db.close();
        Log.d(null,"Cat" + cat.getName() + " added");
    }



    /**
     * Queries cat entry in the database using its name, sets values using a cursor, then returns
     * the cat as an object with all values.
     *
     * @param  catName  string of cat name, e.g. "Charlie"
     * @return cat object
     */
        public Cat findCat(String catName) {
            String query = "Select * FROM " + "CAT" + " WHERE " + "name" + " =  \"" + catName + "\"";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            //Creates cat object to set
            Cat cat = new Cat();

                if (cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    cat.setID(          cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                    cat.setName(        cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    cat.setWeight(      cursor.getFloat(cursor.getColumnIndexOrThrow("weight")));
                    cat.setBirthday(    cursor.getString(cursor.getColumnIndexOrThrow("birthday")));
                    cat.setSex(         cursor.getString(cursor.getColumnIndexOrThrow("sex")));
                    cat.setCatImage(    cursor.getBlob(cursor.getColumnIndexOrThrow("catImage")));
                    cat.setFoodType(    cursor.getString(cursor.getColumnIndexOrThrow("foodType")));
                    cursor.close();
                } else {
                cat = null;
            }
            db.close();
            return cat;
        }

    /**
     * Queries cat entry in the database using its name, then looks at the ID number and deletes
     * the cat selected for deletion.
     *
     * @param  catName  string of cat name, e.g. "Charlie"
     * @return returns true if cat is deleted
     */
    public boolean deleteCat(String catName) {

        boolean result = false;

        String query = "Select * FROM " + "CAT" + " WHERE " + "name" + " =  \"" + catName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Cat cat = new Cat();

        if (cursor.moveToFirst()) {
            cat.setID(Integer.parseInt(cursor.getString(0)));
            db.delete("CAT", "_id" + " = ?",
                    new String[] { String.valueOf(cat.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Used to clear the cat table if needed.
     */
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS CAT");
    }


}

