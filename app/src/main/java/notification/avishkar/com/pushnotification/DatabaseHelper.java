package notification.avishkar.com.pushnotification;

/**
 * Created by Rakesh on 10/27/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "mAdvisorDB.db";
    public static final String TABLE_NAME = "PushNtfctn_tbl";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("Create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, MESSAGE TEXT,INSURED TEXT, EMAIL TEXT, PHONE_NUM TEXT, POLICY_NUM TEXT, AMOUNT TEXT, CURRENCY TEXT, DUE_DATE TEXT, NOTES TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    onCreate(db);
    }

    public boolean insertData(String tile, String message, String Insured, String email, String phone,String policy_num, String amount, String currency, String due_Date, String notes)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Title", tile);
        contentValues.put("Message", message);
        contentValues.put("Insured", Insured);
        contentValues.put("Email", email);
        contentValues.put("Phone_Num",phone);
        contentValues.put("Policy_Num",policy_num);
        contentValues.put("Amount",amount);
        contentValues.put("Currency",currency);
        contentValues.put("Due_Date",due_Date);
        contentValues.put("Notes",notes);
        long result = db.insert(TABLE_NAME,null,contentValues);
        return result != -1;
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " ORDER BY ID DESC", null);
        return res;
    }
}
