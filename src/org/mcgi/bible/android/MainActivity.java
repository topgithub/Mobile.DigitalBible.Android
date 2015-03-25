package org.mcgi.bible.android;

import java.io.File;
import java.util.ArrayList;

import org.mcgi.bible.android.adapters.BooksAdapter;
import org.mcgi.bible.android.adapters.ChaptersAdapter;
import org.mcgi.bible.android.database.ContentManager;
import org.mcgi.bible.android.fragments.ContentIndicatorFragment;
import org.mcgi.bible.android.fragments.GoToVerseFragment;
import org.mcgi.bible.android.fragments.LibrariesFragment;
import org.mcgi.bible.android.fragments.ReadingOptionsFragment;
import org.mcgi.bible.android.fragments.SearchFragment;
import org.mcgi.bible.android.fragments.SingleVerseFragment;
import org.mcgi.bible.android.listeners.ContentIndicatorListener;
import org.mcgi.bible.android.listeners.GoToVerseListener;
import org.mcgi.bible.android.listeners.FragmentListListener;
import org.mcgi.bible.android.listeners.ReadingOptionsListener;
import org.mcgi.bible.android.listeners.SearchListener;
import org.mcgi.bible.android.listeners.SingleVerseListener;
import org.mcgi.bible.android.utilities.Constants;
import org.mcgi.bible.android.wrapper.SharedPreferencesWrapper;

import com.aplit.dev.tasks.CopyAssetsTask;
import com.aplit.dev.tasks.CopyAssetsTask.CopyAssetsTaskListener;
import com.aplit.dev.utilities.HardwareUtility;
import com.aplit.dev.utilities.Utilities;
import com.aplit.dev.utilities.Utilities.AlertDialogListener;
import com.aplit.dev.wrappers.DebugLog;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements
CopyAssetsTaskListener, OnItemClickListener, OnItemLongClickListener,
AlertDialogListener, OnBackStackChangedListener, OnEditorActionListener,
FragmentListListener, ReadingOptionsListener, ContentIndicatorListener,
GoToVerseListener, SingleVerseListener, SearchListener {
	private static final String TAG = "MainActivity";

	private DrawerLayout drawerLayout;
//	private EditText searchEditText;
	private ListView drawerBooksList;
	private ListView drawerChaptersList;

	private ActionBarDrawerToggle drawerToggle;
	private ArrayList<String> fragmentTagList;
	private Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugLog.e(this, TAG, "onCreate");
		String deviceId = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
		Toast.makeText(this, "deviceId:" + deviceId + "***", Toast.LENGTH_LONG).show();//TODO

		View view = View.inflate(this, R.layout.main, null);
		drawerLayout = (DrawerLayout) view.findViewById(R.id.mainDrawerLayout);
//		searchEditText = (EditText) view.findViewById(R.id.mainSearch);
		drawerBooksList = (ListView) view.findViewById(R.id.mainDrawerBooksList);
		drawerChaptersList = (ListView) view.findViewById(R.id.mainDrawerChaptersList);
		setContentView(view);

		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawerBooksList.setOnItemClickListener(this);
		drawerBooksList.setOnItemLongClickListener(this);
		drawerChaptersList.setOnItemClickListener(this);
		drawerChaptersList.setOnItemLongClickListener(this);
		drawerChaptersList.setAdapter(null);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawerBooksList);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerChaptersList);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
				R.string.books_string, R.string.chapters_string ) {
			@Override
			public void onDrawerClosed(View view) {
				DebugLog.w(view.getContext(), TAG, "onDrawerClosed");
				invalidateOptionsMenu();
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				DebugLog.w(drawerView.getContext(), TAG, "onDrawerOpened");
				invalidateOptionsMenu();
				if (drawerView == drawerBooksList) {
					((Activity) drawerView.getContext()).getActionBar().setTitle(R.string.books_string);
				}
			}
        };
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = this.getActionBar();
			if (actionBar != null) {
				actionBar.setDisplayHomeAsUpEnabled(true);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					actionBar.setHomeButtonEnabled(true);
				}
				actionBar.setTitle(getTitle());
			}
		}

//		searchEditText.setOnEditorActionListener(this);

		fragmentTagList = new ArrayList<String>();
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(this);

		refreshBooksList();
		ContentManager contentManager = new ContentManager();
		if (contentManager.openLibrariesManager(this)) {
			Cursor cursor = contentManager.selectAllLibraries();
			if (cursor == null || cursor.getCount() == 0) {
				new CopyAssetsTask(this, this, "Initializing libraries...").execute("bibles/001.db3", "bibles/002.db3");
			} else if (!SharedPreferencesWrapper.getLibraryPath(this).contentEquals("") && SharedPreferencesWrapper.getReadingOption(this)) {
				setFragment(Constants.FRAGMENT_SINGLE_VERSE, String.valueOf(SharedPreferencesWrapper.getContentId(this)));
			} else if (!SharedPreferencesWrapper.getLibraryPath(this).contentEquals("") && !SharedPreferencesWrapper.getReadingOption(this)) {
				setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(1));
			} else if (SharedPreferencesWrapper.getLibraryPath(this).contentEquals("")) {
				LibrariesFragment.show(this);
			}
			contentManager.closeLibrariesManager();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		DebugLog.e(this, TAG, "onStart");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		DebugLog.e(this, TAG, "onPostCreate");
		drawerToggle.syncState();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		DebugLog.e(this, TAG, "onRestoreInstanceState");
	}

	@Override
	protected void onResume() {
		super.onResume();
		DebugLog.e(this, TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		DebugLog.e(this, TAG, "onPause");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		DebugLog.e(this, TAG, "onSaveInstanceState");
	}

	@Override
	protected void onStop() {
		super.onStop();
		DebugLog.e(this, TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DebugLog.e(this, TAG, "onDestroy");
		if (popAllBackStack()) {
			fragmentTagList = null;
			currentFragment = null;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DebugLog.e(this, TAG, "onConfigurationChanged");
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		DebugLog.e(this, TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.main_menu, menu);
		boolean flag = currentFragment instanceof SingleVerseFragment;
		menu.findItem(R.id.menuPreviousVerse).setVisible(flag);
		menu.findItem(R.id.menuNextVerse).setVisible(flag);
		MenuItem searchItem = menu.findItem(R.id.menuSearch);
		SearchView searchView = (SearchView) ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
				searchItem.getActionView() : MenuItemCompat.getActionView(searchItem));
		MenuItemCompat.setOnActionExpandListener(searchItem, new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem menuItem) {
				DebugLog.w(getBaseContext(), TAG, "onMenuItemActionCollapse menuItem:" + menuItem.getTitle() + "***");
				// TODO Auto-generated method stub
				return false;
			}
			@Override
			public boolean onMenuItemActionExpand(MenuItem menuItem) {
				DebugLog.w(getBaseContext(), TAG, "onMenuItemActionExpand menuItem:" + menuItem.getTitle() + "***");
				// TODO Auto-generated method stub
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		DebugLog.e(this, TAG, "onPrepareOptionsMenu");
		HardwareUtility.hideSoftKeyboard(this, drawerLayout);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DebugLog.e(this, TAG, "onOptionsItemSelected item:" + item.getTitle() + "***");
		String libraryPath = SharedPreferencesWrapper.getLibraryPath(this);
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (!libraryPath.contentEquals("")) {
			int id = item.getItemId();
			switch (id) {
			case R.id.menuSearch:
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}
//				searchEditText.setVisibility(View.VISIBLE);
//				searchEditText.requestFocus();
//				searchEditText.setText("");
//				Utilities.showSoftKeyboard(this);
				break;
			case R.id.menuGoToVerse:
				GoToVerseFragment.show(this, libraryPath);
				break;
			case R.id.menuSwitchLibrary:
				LibrariesFragment.show(this);
				break;
			case R.id.menuExit:
				Utilities.showAlertbox(this, this.getString(R.string.exit_string),
						this.getString(R.string.do_you_wish_to_exit_string), Constants.ALERTBOX_EXIT, this, this);
				break;
			case R.id.menuPreviousVerse:
				if (currentFragment instanceof SingleVerseFragment) {
					((SingleVerseFragment) currentFragment).setPreviousVerse();
				}
				break;
			case R.id.menuNextVerse:
				if (currentFragment instanceof SingleVerseFragment) {
					((SingleVerseFragment) currentFragment).setNextVerse();
				}
				break;
			}
		} else {
			Utilities.showAlertbox(this, this.getString(R.string.error_string),
					this.getString(R.string.select_library_string), null, -1, null);
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
	public void onBackPressed() {
		//Disabled super.onBackPressed();
		DebugLog.e(this, TAG, "onBackPressed");
		backPress(-1);
	}


	/** Private Methods */
    private void refreshBooksList() {
		DebugLog.w(this, TAG, "refreshBooksList");
		ContentManager contentManager = new ContentManager();
		String libraryPath = SharedPreferencesWrapper.getLibraryPath(this);
		if (!libraryPath.contentEquals("")) {
			if (contentManager.openBibleManager(this, libraryPath)) {
				Cursor cursor = contentManager.selectBooks();
				if (cursor != null && cursor.getCount() > 0 && drawerBooksList.getAdapter() != null) {
					BooksAdapter adapter = (BooksAdapter) drawerBooksList.getAdapter();
					adapter.setCursor(cursor);
					adapter.notifyDataSetChanged();
				} else {
					drawerBooksList.setAdapter(new BooksAdapter(this, cursor));
				}
				contentManager.closeBibleManager();
			}
		}
    }

    private void backPress(final int mode) {
		DebugLog.w(this, TAG, "backPress mode:" + mode + "***");
//		if (searchEditText.getVisibility() == View.VISIBLE) {
//			searchEditText.setVisibility(View.GONE);
//		} else {
			try {
				FragmentManager fragmentManager = this.getSupportFragmentManager();
				int backStackEntryCount = fragmentManager.getBackStackEntryCount();
				DebugLog.w(this, TAG, "backStackEntryCount:" + backStackEntryCount + "***");
				if (backStackEntryCount <= 1) { // One fragment left to display
					Utilities.showAlertbox(this, this.getString(R.string.exit_string),
							this.getString(R.string.do_you_wish_to_exit_string), Constants.ALERTBOX_EXIT, this, this);
				} else {
					fragmentManager.popBackStack();
//					String removedFragmentTag = fragmentTagList.remove(fragmentTagList.size()-1);
//					if (fragmentTagList.size() > 0) {
//						String fragmentTag = fragmentTagList.get(fragmentTagList.size()-1);
//						currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
//						DebugLog.w(this, TAG, "backPress fragmentTag:" + fragmentTag + "|removedFragmentTag:" + removedFragmentTag + "***");
//						if (currentFragment != null && currentFragment instanceof ContentIndicatorFragment) {
//							((ContentIndicatorFragment) currentFragment).refreshLibraryPath();//TODO
//						}
//					}
//					new Handler().postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							switch (mode) {
//							case Constants.MODE_REFRESH_CONTENTINDICATOR:
//								setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(SharedPreferencesWrapper.getChapter(MainActivity.this)));
//								break;
//							}
//						}
//					}, 900);//TODO Use an interface method instead of time-delay scheme.
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}

	private boolean popAllBackStack() {
		DebugLog.w(this, TAG, "popAllBackStack");
		try {
			fragmentTagList = new ArrayList<String>();
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			return fragmentManager.executePendingTransactions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean setFragment(int fragmentId, String parameter) {
		DebugLog.w(this, TAG, "setFragment fragmentId:" + fragmentId + "|parameter:" + parameter + "***");
		try {
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			switch (fragmentId) {
			case Constants.FRAGMENT_CONTENT:
				currentFragment = fragmentManager.findFragmentByTag(Constants.FRAGMENT_STACK_CONTENT);
				if (currentFragment == null) {
					currentFragment = ContentIndicatorFragment.newInstance(parameter);
				}
				fragmentTransaction.replace(R.id.mainFrameLayout, currentFragment, Constants.FRAGMENT_STACK_CONTENT);
				fragmentTagList.add(Constants.FRAGMENT_STACK_CONTENT);
				break;
			case Constants.FRAGMENT_SINGLE_VERSE:
				//fragment = fragmentManager.findFragmentByTag(Constants.FRAGMENT_STACK_SINGLE_VERSE);
				//if (fragment == null) {
					currentFragment = SingleVerseFragment.newInstance(parameter);
				//}
				fragmentTransaction.replace(R.id.mainFrameLayout, currentFragment, Constants.FRAGMENT_STACK_SINGLE_VERSE);
				fragmentTagList.add(Constants.FRAGMENT_STACK_SINGLE_VERSE);
				break;
			case Constants.FRAGMENT_SEARCH:
				currentFragment = fragmentManager.findFragmentByTag(Constants.FRAGMENT_STACK_SEARCH);
				if (currentFragment == null) {
					currentFragment = SearchFragment.newInstance(parameter);
				}
				fragmentTransaction.replace(R.id.mainFrameLayout, currentFragment, Constants.FRAGMENT_STACK_SEARCH);
				fragmentTagList.add(Constants.FRAGMENT_STACK_SEARCH);
				break;
			}
			fragmentTransaction.show(currentFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
			invalidateOptionsMenu();
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return false;
	}


	/** Interfaces */
	@Override
	public void onCopiedAssetFinalization(Context context, File outputFile) {
		DebugLog.w(context, TAG, "onCopiedAssetFinalization outputFile:" + outputFile.getAbsolutePath() + "***");
		ContentManager contentManager = new ContentManager();
		String outputPath = outputFile.getAbsolutePath();
		if (contentManager.openBibleManager(context, outputPath)) {
			Cursor cursor = contentManager.selectInfo();
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				String libraryName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
				cursor.moveToPosition(1);
				String libraryDescription = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
				cursor.moveToPosition(2);
				String libraryVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
				cursor.moveToPosition(3);
				String appMinVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
				cursor.moveToPosition(4);
				String appMaxVersion = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_INFORMATION));
				if (contentManager.openLibrariesManager(context)) {
					contentManager.insertLibrary(libraryName, libraryDescription, libraryVersion,
							appMinVersion, appMaxVersion, (int) outputFile.length(), outputPath);
					contentManager.closeLibrariesManager();
				}
			}
			contentManager.closeBibleManager();
		}
	}

	@Override
	public void onCopyAssetsTaskDone(Context context, boolean success) {
		DebugLog.w(context, TAG, "onTaskDone success:" + success + "***");
		if (success) {
			LibrariesFragment.show(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DebugLog.w(this, TAG, "onItemClick position:" + position + "|id:" + id + "***");
		if (parent == drawerBooksList) {
			ContentManager contentManager = new ContentManager();
			if (contentManager.openBibleManager(this, SharedPreferencesWrapper.getLibraryPath(this))) {
				int bookNumber = position + 1;
				Cursor cursor = contentManager.selectBook(bookNumber);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					SharedPreferencesWrapper.setBibleInput(this, bookName, bookNumber);
					cursor = contentManager.selectChapters(bookNumber);
					if (cursor != null && cursor.getCount() == 1) {
						setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(1));
						drawerChaptersList.setAdapter(null);
						drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerChaptersList);
					} else if (cursor != null && cursor.getCount() > 1) {
						drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawerChaptersList);
						drawerChaptersList.setAdapter(new ChaptersAdapter(this, cursor));
						drawerLayout.openDrawer(drawerChaptersList);
						this.getActionBar().setTitle(bookName);
					}
					drawerLayout.closeDrawer(drawerBooksList);
				}
				contentManager.closeBibleManager();
			}
		} else if (parent == drawerChaptersList) {
			int chapter = position + 1;
			setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(chapter));
			drawerLayout.closeDrawer(drawerChaptersList);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		DebugLog.w(this, TAG, "onItemLongClick position:" + position + "|id:" + id + "***");
		if (parent == drawerBooksList) {
			int bookNumber = position + 1;
			ReadingOptionsFragment.show(this, Constants.LIST_BOOKS + Constants.SEPARATOR + bookNumber);
		} else if (parent == drawerChaptersList) {
			int chapter = position + 1;
			ReadingOptionsFragment.show(this, Constants.LIST_CHAPTERS + Constants.SEPARATOR + chapter);
		}
		return true;
	}

	@Override
	public void onAlertboxNeutralButtonClick(int dialogId) {
		DebugLog.w(this, TAG, "onAlertboxNeutralButtonClick:" + dialogId);
		switch (dialogId) {
		case Constants.ALERTBOX_REPEAT_SEARCH:
//			if (searchEditText.requestFocus()) {
//				Utilities.showSoftKeyboard(this);
//			}
			break;
		}
	}

	@Override
	public void onAlertboxPositiveButtonClick(int dialogId) {
		DebugLog.w(this, TAG, "onAlertboxPositiveButtonClick:" + dialogId);
		switch (dialogId) {
		case Constants.ALERTBOX_EXIT:
			finish();
			break;
		}
	}

	@Override
	public void onAlertboxNegativeButtonClick(int dialogId) {
		DebugLog.w(this, TAG, "onAlertboxNegativeButtonClick:" + dialogId);
		// No Operation
	}

	@Override
	public void onBackStackChanged() {
//		if (searchEditText.getVisibility() == View.VISIBLE) {
//			searchEditText.setVisibility(View.GONE);
//		}
		DebugLog.w(this, TAG, "onBackStackChanged" + ((currentFragment != null) ? " fragmentTag:" + currentFragment.getTag() : "") + "***");
		if (currentFragment != null && currentFragment instanceof ContentIndicatorFragment) {
			String bookName = SharedPreferencesWrapper.getBookName(this);
			if (!bookName.contentEquals("")) {
				this.getActionBar().setTitle(bookName);
			}
		} else if (currentFragment != null && currentFragment instanceof SearchFragment) {
			this.getActionBar().setTitle(R.string.search_result_string);
		}
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		DebugLog.w(this, TAG, "onEditorAction actionId:" + actionId + "|IME_ACTION_SEARCH:" + EditorInfo.IME_ACTION_SEARCH + "***");
		HardwareUtility.hideSoftKeyboard(this, view);
		if (actionId == EditorInfo.IME_ACTION_SEARCH){
//			String searchString = searchEditText.getEditableText().toString().trim();
//			if (searchString != null && !searchString.contentEquals("")) {
//				if (currentFragment != null && currentFragment instanceof SearchFragment) {
//					((SearchFragment) currentFragment).setSearchString(searchString);
//					searchEditText.setVisibility(View.GONE);
//				} else {
//					setFragment(Constants.FRAGMENT_SEARCH, searchString);
//				}
//			} else {
//				Utilities.showAlertbox(this, R.string.error_string, R.string.enter_search_word_string, null, Constants.ALERTBOX_REPEAT_SEARCH, this);
//			}
		}
		return false;
	}

	@Override
	public void onFragmentListItemClick(int position, long id, Fragment fragment) {
		DebugLog.w(this, TAG, "onFragmentListItemClick position:" + position + "|id:" + id + "***");
		ContentManager contentManager = new ContentManager();
		if (fragment instanceof LibrariesFragment) {
			if (contentManager.openLibrariesManager(this)) {
				Cursor cursor = contentManager.selectLibrary(id);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String libraryPath = cursor.getString(cursor.getColumnIndex(ContentManager.LIBRARY_PATH));
					String libraryName = cursor.getString(cursor.getColumnIndex(ContentManager.LIBRARY_NAME));
					SharedPreferencesWrapper.setLibrary(this, libraryPath, libraryName);
					int bookNumber = SharedPreferencesWrapper.getBookNumber(this);
					if (bookNumber != -1 && contentManager.openBibleManager(this, libraryPath)) {
						cursor = contentManager.selectBook(bookNumber);
						if (cursor != null && cursor.getCount() > 0) {
							cursor.moveToFirst();
							String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
							SharedPreferencesWrapper.setBibleInput(this, bookName, bookNumber);
						}
						contentManager.closeBibleManager();
					}
					refreshBooksList();
					if (currentFragment != null) {
						if (currentFragment instanceof ContentIndicatorFragment) {
							((ContentIndicatorFragment) currentFragment).refreshLibraryPath();
						} else if (currentFragment instanceof SingleVerseFragment) {
							((SingleVerseFragment) currentFragment).refreshLibraryPath();
						}
					} else {
						drawerLayout.openDrawer(drawerBooksList);
					}
				}
				contentManager.closeLibrariesManager();
			}
		}
	}

	@Override
	public void onFragmentListDismissed(Fragment fragment) {
		DebugLog.w(this, TAG, "onFragmentListDismissed");
		if (fragment instanceof LibrariesFragment && SharedPreferencesWrapper.getLibraryPath(this).contentEquals("")) {
			finish();
		}
	}

	@Override
	public void onReadingOptionsItemClick(int position, long id, Fragment fragment, String parameter) {
		DebugLog.w(this, TAG, "onReadingOptionsItemClick position:" + position + "|id:" + id + "|parameter:" + parameter + "***");
		String[] data = parameter.split(Constants.SEPARATOR);
		switch (Integer.valueOf(data[0])) {
		case Constants.LIST_BOOKS:
			int bookNumber = Integer.valueOf(data[1]);
			ContentManager contentManager = new ContentManager();
			if (contentManager.openBibleManager(this, SharedPreferencesWrapper.getLibraryPath(this))) {
				Cursor cursor = contentManager.selectBook(bookNumber);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String bookName = cursor.getString(cursor.getColumnIndex(ContentManager.BIBLE_BOOKS_NAME));
					SharedPreferencesWrapper.setBibleInput(this, bookName, bookNumber);
				}
				contentManager.closeBibleManager();
			}
			switch (position) {
			case 0:
				setFragment(Constants.FRAGMENT_SINGLE_VERSE, bookNumber + Constants.SEPARATOR +
						1 + Constants.SEPARATOR + 1);
				break;
			case 1:
				setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(1));
				break;
			}
			drawerLayout.closeDrawer(drawerBooksList);
			break;
		case Constants.LIST_CHAPTERS:
			int chapter = Integer.valueOf(data[1]);
			switch (position) {
			case 0:
				setFragment(Constants.FRAGMENT_SINGLE_VERSE, SharedPreferencesWrapper.getBookNumber(this) + Constants.SEPARATOR +
						chapter + Constants.SEPARATOR + 1);
				break;
			case 1:
				setFragment(Constants.FRAGMENT_CONTENT, String.valueOf(chapter));
				break;
			}
			drawerLayout.closeDrawer(drawerChaptersList);
			break;
		}
	}

	@Override
	public void onContentLoaded() {
		DebugLog.w(this, TAG, "onContentLoaded");
		String bookName = SharedPreferencesWrapper.getBookName(this);
		if (!bookName.contentEquals("")) {
			this.getActionBar().setTitle(bookName);
		}
	}

	@Override
	public void onContentIndicatorCancel() {
		DebugLog.w(this, TAG, "onContentIndicatorCancel");
		onBackPressed();
	}

	@Override
	public void onContentRefreshLibraryPath() {
		DebugLog.w(this, TAG, "onContentRefreshLibraryPath");
//TODO		backPress(Constants.MODE_REFRESH_CONTENTINDICATOR);
	}

	@Override
	public void onVerseObtained(int bookNumber, int chapter, int verse) {
		DebugLog.w(this, TAG, "onVerseObtained bookNumber:" + bookNumber +
				"|chapter:" + chapter + "|verse:" + verse + "***");
		if (currentFragment != null && currentFragment instanceof SingleVerseFragment) {
			((SingleVerseFragment) currentFragment).setVerse(bookNumber + Constants.SEPARATOR +
					chapter + Constants.SEPARATOR + verse, true);
		} else {
			setFragment(Constants.FRAGMENT_SINGLE_VERSE, bookNumber + Constants.SEPARATOR +
					chapter + Constants.SEPARATOR + verse);
		}
		drawerLayout.closeDrawers();
	}

	@Override
	public void onSingleVerseContentLoaded() {
		DebugLog.w(this, TAG, "onSingleVerseContentLoaded");
		String bookName = SharedPreferencesWrapper.getBookName(this);
		if (!bookName.contentEquals("")) {
			this.getActionBar().setTitle(bookName);
		}
	}

	@Override
	public void onSingleVerseCancel(boolean backPress) {
		DebugLog.w(this, TAG, "onSingleVerseCancel backPress:" + backPress + "***");
		if (backPress) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					onBackPressed();
				}
			});
		}
		Utilities.showAlertbox(this, this.getString(R.string.error_string),
				this.getString(R.string.invalid_input_string), null, -1, null);
	}

	@Override
	public void onPreviousButtonClick(Fragment fragment) {
		DebugLog.w(this, TAG, "onPreviousButtonClick");
		if (fragment instanceof SingleVerseFragment) {
			((SingleVerseFragment) fragment).setPreviousVerse();
		}
	}

	@Override
	public void onNextButtonClick(Fragment fragment) {
		DebugLog.w(this, TAG, "onNextButtonClick");
		if (fragment instanceof SingleVerseFragment) {
			((SingleVerseFragment) fragment).setNextVerse();
		}
	}

	@Override
	public void onPreviousVerseLoaded(String bookName) {
		DebugLog.w(this, TAG, "onPreviousVerseLoaded bookName:" + bookName + "***");
		this.getActionBar().setTitle(bookName);
	}

	@Override
	public void onNextVerseLoaded(String bookName) {
		DebugLog.w(this, TAG, "onNextVerseLoaded bookName:" + bookName + "***");
		this.getActionBar().setTitle(bookName);
	}

	@Override
	public void onSearchItemClick(long contentId) {
		DebugLog.w(this, TAG, "onSearchItemClick contentId:" + contentId + "***");
		if (currentFragment != null && currentFragment instanceof SingleVerseFragment) {
			((SingleVerseFragment) currentFragment).setVerse(String.valueOf(contentId), true);
		} else {
			setFragment(Constants.FRAGMENT_SINGLE_VERSE, String.valueOf(contentId));
		}
	}
}

