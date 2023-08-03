import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "myMBSDatabase.db"
        private const val DATABASE_VERSION = 1

        // Define your table and column names here
        // Example:
         private const val TABLE_NAME = "my_table"
         private const val COLUMN_ID = "id"
         private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create your tables and define the schema here
        // Example:
         val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT)"
         db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade, if necessary
    }
}