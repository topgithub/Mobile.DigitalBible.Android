package org.mcgi.bible.android.trash;
//package org.mcgi.bible.android.fragments;
//
//import org.mcgi.bible.android.R;
//import org.mcgi.bible.android.adapters.MenuAdapter;
//import org.mcgi.bible.android.listeners.FragmentListListener;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.AdapterView.OnItemClickListener;
//
//import com.aplit.dev.wrappers.DebugLog;
//
//public class MenuFragment extends DialogFragment implements
//OnItemClickListener {
//	private static final String TAG = "MenuFragment";
//	private static final String Y_OFFSET = "y_offset";
//
//	private ListView listView;
//	private FragmentListListener listener;
//
//	private int yOffset;
//
//	private static MenuFragment newInstance(int yOffset) {
//		MenuFragment fragment = new MenuFragment();
//		Bundle arguments = new Bundle();
//		arguments.putInt(Y_OFFSET, yOffset);
//		fragment.setArguments(arguments);
//		return fragment;
//	}
//
//	public static void show(FragmentActivity fragmentActivity, int yOffset) {
//		FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		Fragment previousFragment = fragmentManager.findFragmentByTag(TAG);
//		if (previousFragment != null) {
//			fragmentTransaction.remove(previousFragment);
//		}
//		fragmentTransaction.addToBackStack(null);
//		fragmentTransaction.commit();
//		MenuFragment dialog = MenuFragment.newInstance(yOffset);
//		if (!dialog.isVisible()) {
//			dialog.show(fragmentManager, TAG);
//		}
//		fragmentManager.popBackStack();
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
//			this.yOffset = bundle.getInt(Y_OFFSET);
//		}
//	}
//
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		View view = View.inflate(this.getActivity(), R.layout.menu, null);
//		listView = (ListView) view.findViewById(R.id.menuList);
//		listView.setOnItemClickListener(this);
//		MenuAdapter adapter = new MenuAdapter(this.getActivity());
//		listView.setAdapter(adapter);
//
//		Dialog dialog = new Dialog(this.getActivity());
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(view);
//		Window window = dialog.getWindow();
//		window.setGravity(Gravity.TOP|Gravity.RIGHT);
//		WindowManager.LayoutParams params = window.getAttributes();
//		params.y = yOffset - 16;
//		params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		window.setAttributes(params);
//
//		return dialog;
//	}
//
//	@Override
//	public void onDismiss(DialogInterface dialog) {
//		super.onDismiss(dialog);
//		if (listener != null) {
//			listener.onFragmentListDismissed(this);
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
//			this.dismiss();
//		}
//	}
//}
//
