package org.mcgi.bible.android.trash;
//package org.mcgi.bible.android.fragments;
//
//import org.mcgi.bible.android.R;
//import org.mcgi.bible.android.adapters.ChaptersAdapter;
//import org.mcgi.bible.android.database.ContentManager;
//import org.mcgi.bible.android.listeners.FragmentListListener;
//import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;
//
//import android.app.Activity;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ListView;
//import android.widget.AdapterView.OnItemClickListener;
//
//import com.aplit.dev.wrappers.DebugLog;
//
//public class ChaptersFragment extends Fragment implements
//OnItemClickListener, OnItemLongClickListener {
//	private static final String TAG = "ChaptersFragment";
//	private static final String KEY_PARAMETERS = "key_parameters";
//
//	private ListView listView;
//	private FragmentListListener listener;
//
//	private String parameters;
//
//	public static ChaptersFragment newInstance(String parameters) {
//		ChaptersFragment fragment = new ChaptersFragment();
//		Bundle arguments = new Bundle();
//		arguments.putString(KEY_PARAMETERS, parameters);
//		fragment.setArguments(arguments);
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
//		Bundle bundle = this.getArguments();
//		if (bundle != null) {
//			this.parameters = bundle.getString(KEY_PARAMETERS);
//		}
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
//		ContentManager contentManager = new ContentManager();
//		if (contentManager.openBibleManager(this.getActivity(), SharedPreferencesWrapper.getLibraryPath(this.getActivity()))) {
//			int bookNumber = Integer.valueOf(parameters);
//			Cursor cursor = contentManager.selectBook(bookNumber);
//			if (cursor != null && cursor.getCount() > 0) {
//				cursor.moveToFirst();
//				String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
//				SharedPreferencesWrapper.setBibleInput(getActivity(), bookName, bookNumber);
//				cursor = contentManager.selectChapters(bookNumber);
//				if (cursor != null && cursor.getCount() > 0) {
//					ChaptersAdapter adapter = new ChaptersAdapter(this.getActivity(), cursor);
//					listView.setAdapter(adapter);
//					listView.setOnItemClickListener(this);
//					listView.setOnItemLongClickListener(this);
//				}
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
