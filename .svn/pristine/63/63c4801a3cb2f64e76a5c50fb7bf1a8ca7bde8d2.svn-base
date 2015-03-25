package org.mcgi.bible.android.fragments;

import org.mcgi.bible.android.R;

import com.aplit.dev.wrappers.DebugLog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment {
	private static final String TAG = "ContentFragment";
	private static final String KEY_CONTENT = "key_content";

	private String content;
	private int position;

	public static ContentFragment newInstance(String content) {
		ContentFragment fragment = new ContentFragment();
		Bundle arguments = new Bundle();
		arguments.putString(KEY_CONTENT, content);
		fragment.setArguments(arguments);
		return fragment;
	}

	public void setFragmentPosition(int position) {
		this.position = position;
	}

	public int getFragmentPosition() {
		return this.position;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		listener = (ContentListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			this.content = bundle.getString(KEY_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.content, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView body = (TextView) view.findViewById(R.id.contentBody);
//		WebSettings webSettings = body.getSettings();
//		webSettings.setUseWideViewPort(true);
//		body.setWebChromeClient(new WebChromeClient());
//		body.setWebViewClient(new MyWebViewClient());
		if (content != null) {
//			body.loadData(content, "text/html", "UTF-8");//TODO .setText(content);
			body.setText(content);
		}
		DebugLog.w(this.getActivity(), TAG, "content:" + ((content != null) ? content : "NULL") + "***");
	}


	/** Private Classes */
//	private class MyWebViewClient extends WebViewClient {
//		@Override
//		public void onPageFinished(WebView webView, String url) {
//			super.onPageFinished(webView, url);
//			// Column Count is just the number of 'screens' of text. Add one for partial 'screens'
//			int columnCount = (int) (Math.floor(webView.getHeight() / webView.getWidth())+1);
//
//			// Must be expressed as a percentage. If not set then the WebView will not stretch to give the desired effect.
//			int columnWidth = columnCount * 100;
//
//			String js = "var d = document.getElementsByTagName('body')[0];" + 
//					"d.style.WebkitColumnCount=" + columnCount + ";" + 
//					"d.style.WebkitColumnWidth='" + columnWidth + "%';";
//			webView.loadUrl("javascript:(function(){" + js + "})()");
//		}
//	}
}

