package org.mcgi.bible.android.fragments;

import java.util.ArrayList;

import org.mcgi.bible.android.R;
import org.mcgi.bible.android.adapters.PagerAdapter;
import org.mcgi.bible.android.listeners.ContentIndicatorListener;
import org.mcgi.bible.android.listeners.ContentListener;
import org.mcgi.bible.android.tasks.ContentTask;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplit.dev.wrappers.DebugLog;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class ContentIndicatorFragment extends Fragment implements
ContentListener, OnPageChangeListener {
	private static final String TAG = "ContentIndicatorFragment";
	private static final String KEY_PARAMETERS = "key_parameters";

	private TextView subtitle;
	private PageIndicator indicator;
	private ViewPager pager;

	private ContentIndicatorListener listener;
	private String parameters;

	public static ContentIndicatorFragment newInstance(String parameters) {
		ContentIndicatorFragment fragment = new ContentIndicatorFragment();
		Bundle arguments = new Bundle();
		arguments.putString(KEY_PARAMETERS, parameters);
		fragment.setArguments(arguments);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (ContentIndicatorListener) activity;
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
		return inflater.inflate(R.layout.contentindicator, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		subtitle = (TextView) view.findViewById(R.id.contentIndicatorSubtitle);
		indicator = (CirclePageIndicator) view.findViewById(R.id.contentIndicatorCircles);
		pager = (ViewPager) view.findViewById(R.id.contentIndicatorPager);
		indicator.setOnPageChangeListener(this);

		new ContentTask(this.getActivity(), this).execute(Integer.valueOf(parameters));
		subtitle.setText(SharedPreferencesWrapper.getLibraryName(this.getActivity()));
	}


	/** Public Methods */
	public void refreshLibraryPath() {
		if (listener != null) {
			listener.onContentRefreshLibraryPath();
		}
	}


	/** Interfaces */
	@Override
	public void onContentLoaded(ArrayList<String> contents) {
		DebugLog.w(this.getActivity(), TAG, "onContentLoaded contentsCount:" + ((contents != null) ? contents.size() : "NULL") + "***");
		SharedPreferencesWrapper.setReadingOption(getActivity(), false);
		if (contents != null) {
			try {
				if (pager.getAdapter() != null) {
					PagerAdapter adapter = (PagerAdapter) pager.getAdapter();
					adapter.setContents(contents);
					adapter.notifyDataSetChanged();
				} else {
					pager.setAdapter(new PagerAdapter(this.getActivity().getSupportFragmentManager(), contents));
				}
				indicator.setViewPager(pager);
				if (listener != null) {
					listener.onContentLoaded();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (listener != null) {
				listener.onContentIndicatorCancel();
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// No Operation
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// No Operation
	}

	@Override
	public void onPageSelected(int position) {
		DebugLog.w(getActivity(), TAG, "onPageSelected position:" + position + "***");
	}
}

