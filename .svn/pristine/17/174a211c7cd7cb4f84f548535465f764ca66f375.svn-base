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

public class LibrariesAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Cursor cursor;

	public LibrariesAdapter(Context context, Cursor cursor) {
		this.inflater = LayoutInflater.from(context);
		this.cursor = cursor;
	}

	@Override
	public int getCount() {
		return this.cursor.getCount();
	}

	@Override
	public String getItem(int position) {
		cursor.moveToPosition(position);
		return  this.cursor.getString(cursor.getColumnIndex(ContentManager.LIBRARY_NAME));
	}

	@Override
	public long getItemId(int position) {
		cursor.moveToPosition(position);
		return this.cursor.getLong(cursor.getColumnIndex(ContentManager.LIBRARY_ID));
	}

	private class ViewHolder {
		TextView title;
		TextView description;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.libraries_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.librariesItemTitle);
			holder.description = (TextView) convertView.findViewById(R.id.librariesItemDescription);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < getCount()) {
			cursor.moveToPosition(position);
			holder.title.setText(getItem(position));
			holder.description.setText(this.cursor.getString(cursor.getColumnIndex(ContentManager.LIBRARY_DESCRIPTION)));
		}
		return convertView;
	}
}

