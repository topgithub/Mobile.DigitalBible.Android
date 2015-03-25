package org.mcgi.bible.android.adapters;

import java.util.ArrayList;

import org.mcgi.bible.android.fragments.ContentFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
	private ArrayList<String> contents;

	public PagerAdapter(FragmentManager fragmentManager, ArrayList<String> contents) {
		super(fragmentManager);
		this.contents = contents;
	}

	public void setContents(ArrayList<String> contents) {
		this.contents = contents;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Fragment getItem(int position) {
		ContentFragment fragment = ContentFragment.newInstance(contents.get(position));
		fragment.setFragmentPosition(position);
		return fragment;
	}

	@Override
	public int getItemPosition(Object item) {
		ContentFragment fragment = (ContentFragment) item;
		int position = fragment.getFragmentPosition();
		if (position >= 0) {
			return position;
		}
		return 0;
	}
}

