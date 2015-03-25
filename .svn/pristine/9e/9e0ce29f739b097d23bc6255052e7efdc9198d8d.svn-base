package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.adapters.ReadingOptionsAdapter;
import org.mcgi.bible.android.listeners.ReadingOptionsListener;

import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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

public class ReadingOptionsFragment extends DialogFragment implements
OnItemClickListener {
	private static final String TAG = "ReadingOptionsFragment";
	private static final String ARG_PARAMETER = "arg_parameter";

	private String parameter;
	private ReadingOptionsListener listener;

	private static ReadingOptionsFragment newInstance(String parameter) {
		ReadingOptionsFragment fragment = new ReadingOptionsFragment();
		Bundle arguments = new Bundle();
		arguments.putString(ARG_PARAMETER, parameter);
		fragment.setArguments(arguments);
		return fragment;
	}

	public static void show(FragmentActivity fragmentActivity, String parameter) {
		FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment previousFragment = fragmentManager.findFragmentByTag(TAG);
		if (previousFragment != null) {
			fragmentTransaction.remove(previousFragment);
		}
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		ReadingOptionsFragment dialog = ReadingOptionsFragment.newInstance(parameter);
		if (!dialog.isVisible()) {
			dialog.show(fragmentManager, TAG);
		}
		fragmentManager.popBackStack();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (ReadingOptionsListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			this.parameter = bundle.getString(ARG_PARAMETER);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = View.inflate(this.getActivity(), R.layout.readingoptions, null);
		ListView listView = (ListView) view.findViewById(R.id.readingOptionsListView);
		listView.setOnItemClickListener(this);
		ReadingOptionsAdapter adapter = new ReadingOptionsAdapter(this.getActivity());
		listView.setAdapter(adapter);

		Dialog dialog = new Dialog(this.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);

		return dialog;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}


	/** Interfaces */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DebugLog.w(this.getActivity(), TAG, "onItemClick position:" + position + "|id:" + id + "***");
		if (listener != null) {
			listener.onReadingOptionsItemClick(position, id, this, parameter);
			this.dismiss();
		}
	}
}

