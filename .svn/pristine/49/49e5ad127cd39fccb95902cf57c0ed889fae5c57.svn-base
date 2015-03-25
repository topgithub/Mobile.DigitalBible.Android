package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.adapters.LibrariesAdapter;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.listeners.FragmentListListener;

import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LibrariesFragment extends DialogFragment implements
OnItemClickListener {
	private static final String TAG = "LibrariesFragment";

	private ListView listView;
	private FragmentListListener listener;

	private static LibrariesFragment newInstance() {
		LibrariesFragment fragment = new LibrariesFragment();
		return fragment;
	}

	public static void show(FragmentActivity fragmentActivity) {
		FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment previousFragment = fragmentManager.findFragmentByTag(TAG);
		if (previousFragment != null) {
			fragmentTransaction.remove(previousFragment);
		}
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		LibrariesFragment dialog = LibrariesFragment.newInstance();
		if (!dialog.isVisible()) {
			dialog.show(fragmentManager, TAG);
		}
		fragmentManager.popBackStack();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentListListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(this.getActivity(), R.layout.libraries, null);
		listView = (ListView) view.findViewById(R.id.librariesListView);
		listView.setOnItemClickListener(this);
		ContentManager contentManager = new ContentManager();
		if (contentManager.openLibrariesManager(this.getActivity())) {
			Cursor cursor = contentManager.selectAllLibraries();
			if (cursor != null && cursor.getCount() > 0) {
				LibrariesAdapter adapter = new LibrariesAdapter(this.getActivity(), cursor);
				listView.setAdapter(adapter);
			}
			contentManager.closeLibrariesManager();
		}

		Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);

		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (listener != null) {
			listener.onFragmentListDismissed(this);
		}
	}


	/** Interfaces */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DebugLog.w(this.getActivity(), TAG, "onItemClick position:" + position + "|id:" + id + "***");
		if (listener != null) {
			listener.onFragmentListItemClick(position, id, this);
			this.dismiss();
		}
	}
}

