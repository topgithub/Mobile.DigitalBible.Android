package org.mcgi.bible.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContentManager {
	public static final int DATABASE_VERSION = 1;

	private static final String BIBLE_INFO_TABLE = "info";
	public static final String BIBLE_INFO_INDEX = "infoindex";
	public static final String BIBLE_INFO_NAME = "infoname";
	public static final String BIBLE_INFORMATION = "information";

	private static final String BIBLE_BOOKS_TABLE = "books";
	public static final String BIBLE_BOOKS_ID = "bookindex";
	public static final String BIBLE_BOOKS_NAME = "bookname";

	private static final String BIBLE_CONTENT_TABLE = "bible";
	public static final String BIBLE_CONTENT_ID = "verseindex";
	public static final String BIBLE_CONTENT_BOOK = "book";
	public static final String BIBLE_CONTENT_CHAPTER = "chapter";
	public static final String BIBLE_CONTENT_VERSE = "verse";
	public static final String BIBLE_CONTENT = "content";

	public static final String LIBRARIES_TABLE = "libraries_table";
	public static final String LIBRARY_ID = "_id";
	public static final String LIBRARY_NAME = "name";
	public static final String LIBRARY_DESCRIPTION = "description";
	public static final String LIBRARY_VERSION = "dataversion";
	public static final String LIBRARY_APPMINVERSION = "appminversion";
	public static final String LIBRARY_APPMAXVERSION = "appmaxversion";
	public static final String LIBRARY_SIZE = "size";
	public static final String LIBRARY_PATH = "path";

	private SQLiteDatabase database;
	private LibrariesManager librariesManager;

	public ContentManager() { }

	private boolean isDatabaseOpen() {
		if (database == null) {
			return false;
		}
		return database.isOpen();
	}

	private SQLiteDatabase openDatabase(String path) {
		try {
			return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void closeDatabase() {
		if (database != null && database.isOpen()) {
			database.close();
		}
	}


	/**TODO Libraries Manager Methods */
	public boolean openLibrariesManager(Context context) {
		if (isDatabaseOpen()) {
			closeDatabase();
		}
		librariesManager = new LibrariesManager(context);
		try {
			database = librariesManager.getWritableDatabase();
			return isDatabaseOpen();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void closeLibrariesManager() {
		librariesManager.close();
	}

	public long insertLibrary(String libraryName, String libraryDescription, String libraryVersion,
			String appMinVersion, String appMaxVersion, int size, String path) {
		ContentValues values = new ContentValues();
		values.put(LIBRARY_NAME, libraryName);
		values.put(LIBRARY_DESCRIPTION, libraryDescription);
		values.put(LIBRARY_VERSION, libraryVersion);
		values.put(LIBRARY_APPMINVERSION, appMinVersion);
		values.put(LIBRARY_APPMAXVERSION, appMaxVersion);
		values.put(LIBRARY_SIZE, size);
		values.put(LIBRARY_PATH, path);
		return database.insert(LIBRARIES_TABLE, null, values);
	}

	public Cursor selectLibrary(long id) {
		String[] columns = { LIBRARY_NAME, LIBRARY_DESCRIPTION, LIBRARY_VERSION,
				LIBRARY_APPMINVERSION, LIBRARY_APPMAXVERSION, LIBRARY_SIZE, LIBRARY_PATH };
		String selection = LIBRARY_ID + " = ?";
		String[] selectionArgs = { String.valueOf(id) };
		String orderBy = LIBRARY_NAME + " ASC, " + LIBRARY_ID + " ASC";
		return database.query(true, LIBRARIES_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public Cursor selectAllLibraries() {
		String[] columns = { LIBRARY_ID, LIBRARY_NAME, LIBRARY_DESCRIPTION, LIBRARY_VERSION,
				LIBRARY_APPMINVERSION, LIBRARY_APPMAXVERSION, LIBRARY_SIZE, LIBRARY_PATH };
		String orderBy = LIBRARY_NAME + " ASC, " + LIBRARY_ID + " ASC";
		return database.query(true, LIBRARIES_TABLE, columns, null, null, null, null, orderBy, null);
	}


	/**TODO Bible Manager Methods */
	public boolean openBibleManager(Context context, String path) {
		if (isDatabaseOpen()) {
			closeDatabase();
		}
		database = this.openDatabase(path);
		return isDatabaseOpen();
	}

	public void closeBibleManager() {
		closeDatabase();
	}

	// Info
	public Cursor selectInfo(int infoIndex) {
		String[] columns = { BIBLE_INFO_INDEX, BIBLE_INFO_NAME, BIBLE_INFORMATION };
		String selection = BIBLE_INFO_INDEX + " = ?";
		String[] selectionArgs = { String.valueOf(infoIndex) };
		String orderBy = BIBLE_INFO_INDEX + " ASC";
		return database.query(true, BIBLE_INFO_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public Cursor selectInfo() {
		String[] columns = { BIBLE_INFO_INDEX, BIBLE_INFO_NAME, BIBLE_INFORMATION };
		String orderBy = BIBLE_INFO_INDEX + " ASC";
		return database.query(true, BIBLE_INFO_TABLE, columns, null, null, null, null, orderBy, null);
	}

	// Books
	public Cursor selectBook(int bookNumber) {
		String[] columns = { BIBLE_BOOKS_ID, BIBLE_BOOKS_NAME };
		String selection = BIBLE_BOOKS_ID + " = ?";
		String[] selectionArgs = { String.valueOf(bookNumber) };
		String orderBy = BIBLE_BOOKS_ID + " ASC";
		return database.query(true, BIBLE_BOOKS_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public Cursor selectBooks() {
		String[] columns = { BIBLE_BOOKS_ID, BIBLE_BOOKS_NAME };
		String orderBy = BIBLE_BOOKS_ID + " ASC";
		return database.query(true, BIBLE_BOOKS_TABLE, columns, null, null, null, null, orderBy, null);
	}

	// Contents
	public Cursor selectChapters(int bookNumber) {
		String[] columns = { BIBLE_CONTENT_CHAPTER };
		String selection = BIBLE_CONTENT_BOOK + " = ?";
		String[] selectionArgs = { String.valueOf(bookNumber) };
		String orderBy = BIBLE_CONTENT_ID + " ASC";
		return database.query(true, BIBLE_CONTENT_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public Cursor selectContent(long contentId) {
		String[] columns = { BIBLE_CONTENT_ID, BIBLE_CONTENT_BOOK, BIBLE_CONTENT_CHAPTER, BIBLE_CONTENT_VERSE, BIBLE_CONTENT };
		String selection = BIBLE_CONTENT_ID + " = ?";
		String[] selectionArgs = { String.valueOf(contentId) };
		return database.query(true, BIBLE_CONTENT_TABLE, columns, selection, selectionArgs, null, null, null, null);
	}

	public Cursor selectContent(int bookNumber, int chapter) {
		String[] columns = { BIBLE_CONTENT_ID, BIBLE_CONTENT_BOOK, BIBLE_CONTENT_CHAPTER, BIBLE_CONTENT_VERSE, BIBLE_CONTENT };
		String selection = BIBLE_CONTENT_BOOK + " = ? AND " + BIBLE_CONTENT_CHAPTER + " = ?";
		String[] selectionArgs = { String.valueOf(bookNumber), String.valueOf(chapter) };
		String orderBy = BIBLE_CONTENT_ID + " ASC";
		return database.query(true, BIBLE_CONTENT_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}

	public Cursor selectContent(int bookNumber, int chapter, int verse) {
		String[] columns = { BIBLE_CONTENT_ID, BIBLE_CONTENT_BOOK, BIBLE_CONTENT_CHAPTER, BIBLE_CONTENT_VERSE, BIBLE_CONTENT };
		String selection = BIBLE_CONTENT_BOOK + " = ? AND " + BIBLE_CONTENT_CHAPTER + " = ? AND " + BIBLE_CONTENT_VERSE + " = ?";
		String[] selectionArgs = { String.valueOf(bookNumber), String.valueOf(chapter), String.valueOf(verse) };
		return database.query(true, BIBLE_CONTENT_TABLE, columns, selection, selectionArgs, null, null, null, null);
	}

	public Cursor searchContent(String content) {
		String[] columns = { BIBLE_CONTENT_ID, BIBLE_CONTENT_BOOK, BIBLE_CONTENT_CHAPTER, BIBLE_CONTENT_VERSE, BIBLE_CONTENT };
		String selection = BIBLE_CONTENT + " like ?";
		String[] selectionArgs = { "%" + content + "%" };
		String orderBy = BIBLE_CONTENT_ID + " ASC";
		return database.query(true, BIBLE_CONTENT_TABLE, columns, selection, selectionArgs, null, null, orderBy, null);
	}
}

