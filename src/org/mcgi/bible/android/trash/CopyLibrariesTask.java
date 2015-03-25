package org.mcgi.bible.android.trash;
//package org.mcgi.bible.android.tasks;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import org.mcgi.bible.android.database.ContentManager;
//
//import com.aplit.dev.listeners.SimpleTaskListener;
//import com.aplit.dev.views.ProgressDialogWrapper;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.database.Cursor;
//import android.os.AsyncTask;
//import android.os.Build;
//
//public class CopyLibrariesTask extends AsyncTask<String, String, Boolean> {
//	private Context context;
//	private SimpleTaskListener simpleTaskListener;
//	private ProgressDialogWrapper progressDialog;
//
//	public CopyLibrariesTask(Context context, SimpleTaskListener simpleTaskListener) {
//		this.context = context;
//		this.simpleTaskListener = simpleTaskListener;
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		if (progressDialog == null) {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				progressDialog = new ProgressDialogWrapper(context, "Initializing libraries...");
//			} else {
//				progressDialog = new ProgressDialogWrapper(context, "Initializing libraries...", false, 0);
//			}
//		}
//		progressDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				cancel(true);
//			}
//		});
//		if (!progressDialog.isShowing()) {
//			progressDialog.show();
//		}
//	}
//
//	@Override
//	protected Boolean doInBackground(String... libraryPaths) {
//		int length, totalSize = 0, totalRead = 0;
//		byte[] buffer;
//		InputStream inputStream = null;
//		for (String libraryPath: libraryPaths) {
//			try {
//				inputStream = context.getAssets().open(libraryPath);
//				totalSize += inputStream.available();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		OutputStream outputStream = null;
//		File outputFile = null;
//		String fileName, outputPath, libraryName, libraryDescription, libraryVersion, appMinVersion, appMaxVersion;
//		ContentManager contentManager = new ContentManager();
//		for (String libraryPath: libraryPaths) {
//			try {
//				inputStream = context.getAssets().open(libraryPath);
//				fileName = libraryPath.split("/")[libraryPath.split("/").length-1];
//				outputFile = new File(context.getFilesDir(), fileName);
//				outputStream = new FileOutputStream(outputFile);
//				buffer = new byte[1024];
//				while ((length = inputStream.read(buffer)) != -1) {
//					outputStream.write(buffer);
//					totalRead += length;
//					publishProgress((100 * totalRead / totalSize) + "%");
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//				return false;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//				return false;
//			} finally {
//				try {
//					outputPath = outputFile.getAbsolutePath();
//					if (contentManager.openBibleManager(context, outputPath)) {
//						Cursor cursor = contentManager.selectInfo();
//						if (cursor != null && cursor.getCount() > 0) {
//							cursor.moveToFirst();
//							libraryName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
//							cursor.moveToPosition(1);
//							libraryDescription = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
//							cursor.moveToPosition(2);
//							libraryVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
//							cursor.moveToPosition(3);
//							appMinVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
//							cursor.moveToPosition(4);
//							appMaxVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
//							if (contentManager.openLibrariesManager(context)) {
//								contentManager.insertLibrary(libraryName, libraryDescription, libraryVersion,
//										appMinVersion, appMaxVersion, inputStream.available(), outputPath);
//								contentManager.closeLibrariesManager();
//							}
//						}
//						contentManager.closeBibleManager();
//					}
//					if (inputStream != null) {
//						inputStream.close();
//					}
//					if (outputStream != null) {
//						outputStream.close();
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return true;
//	}
//
//	@Override
//	protected void onProgressUpdate(String... values) {
//		super.onProgressUpdate(values);
//		progressDialog.setMessage("Initializing libraries... " + values[0]);
//	}
//
//	@Override
//	protected void onCancelled(Boolean result) {
//		super.onCancelled();
//		if (progressDialog.isShowing()) {
//			progressDialog.dismiss();
//		}
//	}
//
//	@Override
//	protected void onPostExecute(Boolean result) {
//		super.onPostExecute(result);
//		if (progressDialog.isShowing()) {
//			progressDialog.dismiss();
//		}
//		if (simpleTaskListener != null) {
//			simpleTaskListener.onTaskDone(context, result, -1);
//		}
//	}
//}
//
