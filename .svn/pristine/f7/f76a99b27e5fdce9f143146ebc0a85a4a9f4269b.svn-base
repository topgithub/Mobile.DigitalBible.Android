package org.mcgi.bible.android.adapters;

import org.mcgi.bible.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
	public static final String[] list = { "Switch Library", "Go To Verse", "Search", "Exit" };

	private LayoutInflater inflater;

	public MenuAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public String getItem(int position) {
		return list[position];
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
			convertView = inflater.inflate(R.layout.menu_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.menuItemName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position < getCount()) {
			holder.name.setText(getItem(position));
		}
		return convertView;
	}
}


