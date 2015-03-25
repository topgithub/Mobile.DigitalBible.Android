package org.mcgi.bible.android.tasks;

import java.util.ArrayList;

import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.listeners.ContentListener;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import com.aplit.dev.views.ProgressDialogWrapper;
import com.aplit.dev.wrappers.DebugLog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;

public class ContentTask extends AsyncTask<Integer, Void, ArrayList<String> > {
	private static final String TAG = "ContentTask";

	private Context context;
	private ContentListener listener;

	private ProgressDialogWrapper progressDialog;

	public ContentTask(Context context, ContentListener listener) {
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progressDialog == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				progressDialog = new ProgressDialogWrapper(context, "Retrieving content...");
			} else {
				progressDialog = new ProgressDialogWrapper(context, "Retrieving content...", false, 0);
			}
		}
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	@Override
	protected ArrayList<String> doInBackground(Integer... params) {
		ContentManager contentManager = new ContentManager();
		if (contentManager.openBibleManager(context, SharedPreferencesWrapper.getLibraryPath(context))) {
			int chapter = params[0];
			DebugLog.w(context, TAG, "doInBackground chapter:" + chapter + "***");
			SharedPreferencesWrapper.setBibleInput(context, chapter);
			ArrayList<String> contents = new ArrayList<String>();
			Cursor cursor = contentManager.selectContent(SharedPreferencesWrapper.getBookNumber(context), chapter);
			if (cursor != null && cursor.getCount() > 0) {
				StringBuilder stringBuilder;
				String content;
				int verse;
				for (int i = 0; i < cursor.getCount(); ) {
					stringBuilder = new StringBuilder();
//					stringBuilder.append("<html><body>");//TODO
					for (int j = 0; j < 10 && i < cursor.getCount(); j++) {
						cursor.moveToPosition(i);
						verse = cursor.getInt(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT_VERSE));
						content = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_CONTENT));
						stringBuilder.append(chapter).append(":").append(verse).append(" ").append(content);
						if (i + 1 < cursor.getCount()) {
							stringBuilder.append('\n');
						}
						DebugLog.i(context, TAG, "verseCount:" + cursor.getCount() + "|verse:" + verse + "|content:" + content + "***");
						i += 1;
					}
//					stringBuilder.append("</body></html>");//TODO
					contents.add(stringBuilder.toString());
				}
				return contents;
			}
			contentManager.closeBibleManager();
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		DebugLog.w(context, TAG, "onPostExecute resultCount:" + ((result != null) ? result.size() : "NULL") + "***");
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (listener != null) {
			listener.onContentLoaded(result);
		}
	}
}

