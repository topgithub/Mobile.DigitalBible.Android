package org.mcgi.bible.android.adapters;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.database.ContentManager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChaptersAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Cursor cursor;

	public ChaptersAdapter(Context context, Cursor cursor) {
		this.inflater = LayoutInflater.from(context);
		this.cursor = cursor;
	}

	@Override
	public int getCount() {
		return this.cursor.getCount();
	}

	@Override
	public String getItem(int position) {
		this.cursor.moveToPosition(position);
		return this.cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_CHAPTER));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.books_chapters_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.booksChaptersItemName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < getCount()) {
			holder.name.setText("Chapter " + getItem(position));
		}
		return convertView;
	}
}

