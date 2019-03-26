package matekgames.com.vreme;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Data extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "VremeData";

        private static final int DATABASE_VERSION = 2;

        // Database creation sql statement
        private static final String DATABASE_CREATE = "create table Postaje( _id integer primary key AUTOINCREMENT,name text,danVTednu text,regija text,datum text, delDneva text, razmere text,tem text, minTemp text, maxTemp text,veterH text, veterSmer text, veterSunek text,vlaga text,tlak text );";

        public Data(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Method is called during creation of the database
        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        // Method is called during an upgrade of the database,
        @Override
        public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
            Log.w(Data.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            database.execSQL("DROP TABLE IF EXISTS Postaje");
            onCreate(database);
        }
    }