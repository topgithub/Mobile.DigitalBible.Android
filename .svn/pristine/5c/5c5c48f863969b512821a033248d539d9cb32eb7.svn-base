package org.mcgi.bible.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LibrariesManager extends SQLiteOpenHelper {
	private static final String table = ContentManager.LIBRARIES_TABLE;

	public LibrariesManager(Context context) {
		super(context, table, null, ContentManager.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDatabase =
			"CREATE TABLE " + table + " ("
			+ ContentManager.LIBRARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ContentManager.LIBRARY_NAME + " TEXT NOT NULL, "
			+ ContentManager.LIBRARY_DESCRIPTION + " TEXT NOT NULL, "
			+ ContentManager.LIBRARY_VERSION + " TEXT NOT NULL, "
			+ ContentManager.LIBRARY_APPMINVERSION + " TEXT NOT NULL, "
			+ ContentManager.LIBRARY_APPMAXVERSION + " TEXT NOT NULL, "
			+ ContentManager.LIBRARY_SIZE + " INTEGER, "
			+ ContentManager.LIBRARY_PATH + " TEXT NOT NULL);";
		db.execSQL(createDatabase);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + table);
		onCreate(db);
	}

	@Override
	public void close() {
		super.close();
	}
}

