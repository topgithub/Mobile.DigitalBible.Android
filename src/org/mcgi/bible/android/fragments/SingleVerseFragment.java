package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.listeners.SingleVerseListener;
import org.mcgi.bible.android.utilities.Constants;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SingleVerseFragment extends Fragment implements OnClickListener {
	private static final String TAG = "SingleVerseFragment";
	private static final String KEY_PARAMETERS = "key_parameters";

	private SingleVerseListener listener;
	private TextView subtitle;
	private TextView body;

	private String parameters;

	public static SingleVerseFragment newInstance(String parameters) {
		SingleVerseFragment fragment = new SingleVerseFragment();
		Bundle arguments = new Bundle();
		arguments.putString(KEY_PARAMETERS, parameters);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (SingleVerseListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			this.parameters = bundle.getString(KEY_PARAMETERS);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.singleverse, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		subtitle = (TextView) view.findViewById(R.id.singleVerseSubtitle);
		body = (TextView) view.findViewById(R.id.singleVerseBody);
		RelativeLayout previousButton = (RelativeLayout) view.findViewById(R.id.singleVersePreviousButton);
		RelativeLayout nextButton = (RelativeLayout) view.findViewById(R.id.singleVerseNextButton);

		previousButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		setVerse(parameters, false);
	}


	/** Public Methods */
	public void refreshLibraryPath() {
		DebugLog.w(getActivity(), TAG, "refreshLibraryPath");
		setVerse(String.valueOf(SharedPreferencesWrapper.getContentId(getActivity())), true);
	}

	public void setVerse(String parameters, boolean isRefresh) {
		DebugLog.w(getActivity(), TAG, "setVerse parameters:" + parameters + "***");
		SharedPreferencesWrapper.setReadingOption(getActivity(), true);
		this.parameters = parameters;
		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(this.getActivity(), SharedPreferencesWrapper.getLibraryPath(this.getActivity()))) {
			String[] data = parameters.split(Constants.SEPARATOR);
			if (data.length > 1) {
				int bookNumber = Integer.valueOf(data[0]);
				int chapter = Integer.valueOf(data[1]);
				int verse = Integer.valueOf(data[2]);
				Cursor cursor = contentManager.selectBook(bookNumber);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					cursor = contentManager.selectContent(bookNumber, chapter, verse);
					if (cursor != null && cursor.getCount() > 0) {
						cursor.moveToFirst();
						long contentId = cursor.getLong(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_ID));
						SharedPreferencesWrapper.setBibleInput(this.getActivity(), bookName, contentId, bookNumber, chapter, verse);
						String content = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
						body.setText(chapter + ":" + verse + " " + content);
						if (listener != null) {
							listener.onSingleVerseContentLoaded();
						}
					} else if (listener != null) {
						listener.onSingleVerseCancel(!isRefresh);
					}
				}
			} else if (data.length == 1) {
				long contentId = Long.valueOf(data[0]);
				Cursor cursor = contentManager.selectContent(contentId);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					int bookNumber = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_BOOK));
					int chapter = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_CHAPTER));
					int verse = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_VERSE));
					String content = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
					cursor = contentManager.selectBook(bookNumber);
					if (cursor != null && cursor.getCount() > 0) {
						cursor.moveToFirst();
						String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
						SharedPreferencesWrapper.setBibleInput(this.getActivity(), bookName, contentId, bookNumber, chapter, verse);
					}
					body.setText(chapter + ":" + verse + " " + content);
					if (listener != null) {
						listener.onSingleVerseContentLoaded();
					}
				} else if (listener != null) {
					listener.onSingleVerseCancel(!isRefresh);
				}
			}
			contentManager.closeBibleManager();
		}
		subtitle.setText(SharedPreferencesWrapper.getLibraryName(this.getActivity()));
	}

	public void setPreviousVerse() {
		DebugLog.w(getActivity(), TAG, "setPreviousVerse");
		SharedPreferencesWrapper.setReadingOption(getActivity(), true);
		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(getActivity(), SharedPreferencesWrapper.getLibraryPath(getActivity()))) {
			long contentId = SharedPreferencesWrapper.getContentId(getActivity());
			Cursor cursor = contentManager.selectContent(contentId - 1);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				contentId = cursor.getLong(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_ID));
				int bookNumber = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_BOOK));
				int chapter = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_CHAPTER));
				int verse = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_VERSE));
				String content = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
				body.setText(chapter + ":" + verse + " " + content);
				cursor = contentManager.selectBook(bookNumber);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					SharedPreferencesWrapper.setBibleInput(getActivity(), bookName, contentId, bookNumber, chapter, verse);
					if (listener != null) {
						listener.onPreviousVerseLoaded(bookName);
					}
				}
			}
			contentManager.closeBibleManager();
		}
	}

	public void setNextVerse() {
		DebugLog.w(getActivity(), TAG, "setNextVerse");
		SharedPreferencesWrapper.setReadingOption(getActivity(), true);
		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(getActivity(), SharedPreferencesWrapper.getLibraryPath(getActivity()))) {
			long contentId = SharedPreferencesWrapper.getContentId(getActivity());
			Cursor cursor = contentManager.selectContent(contentId + 1);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				contentId = cursor.getLong(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_ID));
				int bookNumber = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_BOOK));
				int chapter = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_CHAPTER));
				int verse = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_VERSE));
				String content = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
				body.setText(chapter + ":" + verse + " " + content);
				cursor = contentManager.selectBook(bookNumber);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					SharedPreferencesWrapper.setBibleInput(getActivity(), bookName, contentId, bookNumber, chapter, verse);
					if (listener != null) {
						listener.onNextVerseLoaded(bookName);
					}
				}
			}
			contentManager.closeBibleManager();
		}
	}


	/** Interfaces */
	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.singleVersePreviousButton:
			if (listener != null) {
				listener.onPreviousButtonClick(this);
			}
			break;
		case R.id.singleVerseNextButton:
			if (listener != null) {
				listener.onNextButtonClick(this);
			}
			break;
		}
	}
}

