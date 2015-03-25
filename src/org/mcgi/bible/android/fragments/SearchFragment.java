package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.adapters.SearchAdapter;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.listeners.SearchListener;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = "SearchFragment";
	private static final String KEY_SEARCH_STRING = "key_search_string";

	private TextView noResultText;
	private ListView listView;
	private SearchListener listener;

	private String searchString;

	public static SearchFragment newInstance(String searchString) {
		SearchFragment fragment = new SearchFragment();
		Bundle arguments = new Bundle();
		arguments.putString(KEY_SEARCH_STRING, searchString);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (SearchListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			this.searchString = bundle.getString(KEY_SEARCH_STRING);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.search, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		noResultText = (TextView) view.findViewById(R.id.searchNoResultTextView);
		listView = (ListView) view.findViewById(R.id.searchListView);
		setSearchString(searchString);
	}


	/** Public Methods */
	public void setSearchString(String searchString) {
		DebugLog.w(getActivity(), TAG, "setSearchString searchString:" + searchString + "***");
		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(this.getActivity(), SharedPreferencesWrapper.getLibraryPath(this.getActivity()))) {
			Cursor cursor = contentManager.searchContent(searchString);
			if (cursor != null && cursor.getCount() > 0) {
				SearchAdapter adapter = new SearchAdapter(this.getActivity(), cursor);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(this);
				noResultText.setVisibility(View.GONE);
			} else {
				noResultText.setVisibility(View.VISIBLE);
			}
			contentManager.closeBibleManager();
		}
	}


	/** Interfaces */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onSearchItemClick(id);
		}
	}
}

