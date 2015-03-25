package org.mcgi.bible.android.listeners;

import android.support.v4.app.Fragment;

public interface SingleVerseListener {
	public abstract void onSingleVerseContentLoaded();
	public abstract void onSingleVerseCancel(boolean backPress);
	public abstract void onPreviousButtonClick(Fragment fragment);
	public abstract void onNextButtonClick(Fragment fragment);
	public abstract void onPreviousVerseLoaded(String bookName);
	public abstract void onNextVerseLoaded(String bookName);
}

