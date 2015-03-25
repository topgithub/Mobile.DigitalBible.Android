package org.mcgi.bible.android.trash;
//package org.mcgi.bible.android.fragments;
//
//import org.mcgi.bible.android.R;
//import org.mcgi.bible.android.adapters.BooksAdapter;
//import org.mcgi.bible.android.database.ContentManager;
//import org.mcgi.bible.android.listeners.FragmentListListener;
//import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;
//
//import com.aplit.dev.wrappers.DebugLog;
//
//import android.app.Activity;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ListView;
//
//public class BooksFragment extends Fragment implements
//OnItemClickListener, OnItemLongClickListener {
//	private static final String TAG = "BooksFragment";
//
//	private ListView listView;
//	private FragmentListListener listener;
//
//	public static BooksFragment newInstance() {
//		BooksFragment fragment = new BooksFragment();
//		return fragment;
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		listener = (FragmentListListener) activity;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.books, container, false);
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		listView = (ListView) view.findViewById(R.id.booksListView);
//		listView.setOnItemClickListener(this);
//		listView.setOnItemLongClickListener(this);
//		refreshLibraryPath();
//	}
//
//
//	/** Public Methods */
//	public void refreshLibraryPath() {
//		DebugLog.w(getActivity(), TAG, "refreshLibraryPath");
//		ContentManager contentManager = new ContentManager();
//		if (contentManager.openBibleManager(this.getActivity(), SharedPreferencesWrapper.getLibraryPath(this.getActivity()))) {
//			Cursor cursor = contentManager.selectBooks();
//			if (cursor != null && cursor.getCount() > 0 && listView.getAdapter() != null) {
//				BooksAdapter adapter = (BooksAdapter) listView.getAdapter();
//				adapter.setCursor(cursor);
//				adapter.notifyDataSetChanged();
//			} else {
//				listView.setAdapter(new BooksAdapter(this.getActivity(), cursor));
//			}
//			contentManager.closeBibleManager();
//		}
//	}
//
//
//	/** Interfaces */
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		DebugLog.w(this.getActivity(), TAG, "onItemClick position:" + position + "|id:" + id + "***");
//		if (listener != null) {
//			listener.onFragmentListItemClick(position, id, this);
//		}
//	}
//
//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//		DebugLog.w(this.getActivity(), TAG, "onItemLongClick position:" + position + "|id:" + id + "***");
//		if (listener != null) {
//			listener.onFragmentListItemLongClick(position, id, this);
//		}
//		return false;
//	}
//}
//
