package org.mcgi.bible.android.wrapper;

import com.aplit.dev.wrappers.DebugLog;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {
	private static final String TAG = "SharedPreferencesWrapper";
	private static final String PREFERENCE_FILE_NAME = "preferenceFilename";

	private static final String LIBRARY_PATH = "digitalBibleLibraryPath";
	private static final String LIBRARY_NAME = "digitalBibleLibraryName";
	private static final String BOOK_NAME = "digitalBibleBookName";
	private static final String BOOK_CONTENT_ID= "digitalBibleBookContentId";
	private static final String BOOK_NUMBER = "digitalBibleBookNumber";
	private static final String BOOK_CHAPTER = "digitalBibleChapter";
	private static final String BOOK_VERSE = "digitalBibleVerse";
	private static final String READING_OPTION_SINGLE_VERSE = "readingOptionSingleVerse";
	private static final String TIMESTAMP = "digitalBibleTimestamp";

	private static SharedPreferences getSharedPreferencesMethod(Context context) {
		return context.getApplicationContext().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
	}


	/** Public Methods */
	public static boolean setLibrary(Context context, String libraryPath, String libraryName) {
		DebugLog.w(context, TAG, "setLibrary libraryPath:" + libraryPath + "|libraryName:" + libraryName + "***");
		return getSharedPreferencesMethod(context.getApplicationContext()).edit()
				.putString(LIBRARY_PATH, libraryPath)
				.putString(LIBRARY_NAME, libraryName)
				.putLong(TIMESTAMP, System.currentTimeMillis()).commit();
	}

	public static String getLibraryPath(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getString(LIBRARY_PATH, "");
	}

	public static String getLibraryName(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getString(LIBRARY_NAME, "");
	}

	public static boolean setBibleInput(Context context, String bookName, long contentId, int bookNumber, int chapter, int verse) {
		DebugLog.w(context, TAG, "setBibleInput bookName:" + bookName + "|contentId:" + contentId +
				"|bookNumber:" + bookNumber + "|chapter:" + chapter + "|verse:" + verse + "***");
		return getSharedPreferencesMethod(context.getApplicationContext()).edit()
				.putString(BOOK_NAME, bookName)
				.putInt(BOOK_NUMBER, bookNumber)
				.putLong(BOOK_CONTENT_ID, contentId)
				.putInt(BOOK_CHAPTER, chapter)
				.putInt(BOOK_VERSE, verse)
				.putLong(TIMESTAMP, System.currentTimeMillis()).commit();
	}

	public static boolean setBibleInput(Context context, String bookName, int bookNumber) {
		DebugLog.w(context, TAG, "setBibleInput bookName:" + bookName + "|bookNumber:" + bookNumber + "***");
		return getSharedPreferencesMethod(context.getApplicationContext()).edit()
				.putString(BOOK_NAME, bookName)
				.putInt(BOOK_NUMBER, bookNumber)
				.putLong(TIMESTAMP, System.currentTimeMillis()).commit();
	}

	public static boolean setBibleInput(Context context, int chapter) {
		DebugLog.w(context, TAG, "setBibleInput chapter:" + chapter + "***");
		return getSharedPreferencesMethod(context.getApplicationContext()).edit()
				.putInt(BOOK_CHAPTER, chapter)
				.putLong(TIMESTAMP, System.currentTimeMillis()).commit();
	}

	public static String getBookName(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getString(BOOK_NAME, "");
	}

	public static long getContentId(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getLong(BOOK_CONTENT_ID, -1);
	}

	public static int getBookNumber(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getInt(BOOK_NUMBER, -1);
	}

	public static int getChapter(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getInt(BOOK_CHAPTER, -1);
	}

	public static int getVerse(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getInt(BOOK_VERSE, -1);
	}

	public static boolean setReadingOption(Context context, boolean singleVerse) {
		DebugLog.w(context, TAG, "setReadingOption singleVerse:" + singleVerse + "***");
		return getSharedPreferencesMethod(context.getApplicationContext()).edit()
				.putBoolean(READING_OPTION_SINGLE_VERSE, singleVerse)
				.putLong(TIMESTAMP, System.currentTimeMillis()).commit();
	}

	public static boolean getReadingOption(Context context) {
		return getSharedPreferencesMethod(context.getApplicationContext()).getBoolean(READING_OPTION_SINGLE_VERSE, true);
	}
}

