package org.mcgi.bible.android.adapters;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	private Cursor cursor;

	public SearchAdapter(Context context, Cursor cursor) {
		this.context = context;
		this.cursor = cursor;
	}

	@Override
	public int getCount() {
		return this.cursor.getCount();
	}

	@Override
	public String getItem(int position) {
		this.cursor.moveToPosition(position);
		return this.cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
	}

	@Override
	public long getItemId(int position) {
		this.cursor.moveToPosition(position);
		return this.cursor.getLong(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_ID));
	}

	private class ViewHolder {
		TextView reference;
		TextView content;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_item, null);
			holder = new ViewHolder();
			holder.reference = (TextView) convertView.findViewById(R.id.searchItemBookNameChapterVerse);
			holder.content = (TextView) convertView.findViewById(R.id.searchItemContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < getCount()) {
			this.cursor.moveToPosition(position);
			int bookNumber = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_BOOK));
			int chapter = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_CHAPTER));
			int verse = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_VERSE));
			ContentManager contentManager = new ContentManager();
			if (contentManager.openBibleManager(context, SharedPreferencesWrapper.getLibraryPath(context))) {
				Cursor cursorBook = contentManager.selectBook(bookNumber);
				if (cursorBook != null && cursorBook.getCount() > 0) {
					cursorBook.moveToFirst();
					String bookName = cursorBook.getString(cursorBook.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					holder.reference.setText(bookName + " " + chapter + ":" + verse);
				}
				contentManager.closeBibleManager();
			}
			holder.content.setText(getItem(position));
		}
		return convertView;
	}
}

