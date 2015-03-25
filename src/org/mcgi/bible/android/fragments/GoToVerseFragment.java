package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.adapters.GoToAdapter;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.listeners.GoToVerseListener;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import com.aplit.dev.utilities.HardwareUtility;
import com.aplit.dev.utilities.Utilities;
import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GoToVerseFragment extends DialogFragment implements
OnClickListener, OnItemSelectedListener, OnEditorActionListener {
	private static final String TAG = "GoToVerseFragment";
	private static final String ARG_LIBRARY_PATH = "arg_library_path";

	private Spinner booksSpinner;
	private EditText chapterEdit;
	private EditText verseEdit;

	private String libraryPath;
	private GoToVerseListener listener;

	private static GoToVerseFragment newInstance(String libraryPath) {
		GoToVerseFragment fragment = new GoToVerseFragment();
		Bundle arguments = new Bundle();
		arguments.putString(ARG_LIBRARY_PATH, libraryPath);
		fragment.setArguments(arguments);
		return fragment;
	}

	public static void show(FragmentActivity fragmentActivity, String libraryPath) {
		FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment previousFragment = fragmentManager.findFragmentByTag(TAG);
		if (previousFragment != null) {
			fragmentTransaction.remove(previousFragment);
		}
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		GoToVerseFragment dialog = GoToVerseFragment.newInstance(libraryPath);
		if (!dialog.isVisible()) {
			dialog.show(fragmentManager, TAG);
		}
		fragmentManager.popBackStack();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (GoToVerseListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			this.libraryPath = bundle.getString(ARG_LIBRARY_PATH);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(this.getActivity(), R.layout.gotoverse, null);
		booksSpinner = (Spinner) view.findViewById(R.id.goToVerseSpinner);
		chapterEdit = (EditText) view.findViewById(R.id.goToVerseEditChapter);
		verseEdit = (EditText) view.findViewById(R.id.goToVerseEditVerse);
		RelativeLayout goToVerseButton = (RelativeLayout) view.findViewById(R.id.goToVerseButton);
		verseEdit.setOnEditorActionListener(this);

		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(this.getActivity(), libraryPath)) {
			Cursor cursor = contentManager.selectBooks();
			if (cursor != null && cursor.getCount() > 0) {
				GoToAdapter adapter = new GoToAdapter(this.getActivity(), cursor);
				booksSpinner.setAdapter(adapter);
				booksSpinner.setOnItemSelectedListener(this);
				int lastBookNumber = SharedPreferencesWrapper.getBookNumber(getActivity());
				if (lastBookNumber != -1) {
					booksSpinner.setSelection(lastBookNumber - 1);
				}
			}
			contentManager.closeBibleManager();
		}

		Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);

		goToVerseButton.setOnClickListener(this);
		return dialog;
	}


	/** Private Methods */
	private void goToVerseClicked() {
		String chapterInput = chapterEdit.getEditableText().toString().trim();
		String verseInput = verseEdit.getEditableText().toString().trim();
		if (chapterInput.contentEquals("") || verseInput.contentEquals("")) {
			Utilities.showAlertbox(this.getActivity(), this.getActivity().getString(R.string.error_string),
					this.getActivity().getString(R.string.fill_out_fields_string), null, -1, null);
		} else {
			int bookNumber = 1 + booksSpinner.getSelectedItemPosition();
			int chapter = Integer.valueOf(chapterInput);
			int verse = Integer.valueOf(verseInput);
			listener.onVerseObtained(bookNumber, chapter, verse);
			this.dismiss();
		}
	}


	/** Interfaces */
	@Override
	public void onClick(View view) {
		HardwareUtility.hideSoftKeyboard(this.getActivity(), view);
		int id = view.getId();
		DebugLog.w(this.getActivity(), TAG, "onClick id:" + id + "***");
		switch (id) {
		case R.id.goToVerseButton:
			goToVerseClicked();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		DebugLog.w(this.getActivity(), TAG, "onItemSelected position:" + position + "|id:" + id + "***");
		// No Operation
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		DebugLog.w(this.getActivity(), TAG, "onNothingSelected");
		// No Operation
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH){
			goToVerseClicked();
		}
		return false;
	}
}

