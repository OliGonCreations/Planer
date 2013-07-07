package com.kaith.planer;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	Calendar calendar = Calendar.getInstance();
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	DatabaseHandler db;
	FragmentManager fm;
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		db = new DatabaseHandler(this);
		fm = getSupportFragmentManager();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day >= 2)
			mViewPager.setCurrentItem(day - 2);
		else if (day == 1)
			mViewPager.setCurrentItem(6);
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_print:
			PrintDialog PrintDialog = new PrintDialog();
			PrintDialog.show(fm, "fragment_dialog_erase");
			break;
		case R.id.menu_delete_all:
			EraseDialog EraseDialog = new EraseDialog();
			EraseDialog.show(fm, "fragment_dialog_erase");
			break;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.menu_about:
			AboutDialog AboutDialog = new AboutDialog();
			AboutDialog.show(fm, "fragment_dialog_about");
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			switch (i) {
			case 0:
				fragment = new FragmentMonday();
				break;
			case 1:
				fragment = new FragmentTuesday();
				break;
			case 2:
				fragment = new FragmentWednesday();
				break;
			case 3:
				fragment = new FragmentThursday();
				break;
			case 4:
				fragment = new FragmentFriday();
				break;
			case 5:
				fragment = new FragmentSaturday();
				break;
			case 6:
				fragment = new FragmentSunday();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 7;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.monday);
			case 1:
				return getString(R.string.tuesday);
			case 2:
				return getString(R.string.wednesday);
			case 3:
				return getString(R.string.thursday);
			case 4:
				return getString(R.string.friday);
			case 5:
				return getString(R.string.saturday);
			case 6:
				return getString(R.string.sunday);

			}
			return null;
		}
	}

	public class AboutDialog extends DialogFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setCancelable(false);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			getDialog().setTitle(getResources().getString(R.string.menu_about));
			getDialog().setCanceledOnTouchOutside(false);
			View v = inflater.inflate(R.layout.dialog_about, container, false);
			Button cancel = (Button) v.findViewById(R.id.dialog_cancel);
			TextView tv = (TextView) v.findViewById(R.id.tvAbout);

			Linkify.addLinks(tv, Linkify.ALL);
			cancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					getDialog().dismiss();
				}
			});

			return v;
		}

	}

	public class EraseDialog extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.dialog_erase).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					db.deleteAll();
				}
			}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});
			return builder.create();
		}
	}

	public class PrintDialog extends DialogFragment {
		EditText adressField;
		Button cancel, send;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			getDialog().setTitle(getResources().getString(R.string.menu_print));
			getDialog().setCanceledOnTouchOutside(false);
			View v = inflater.inflate(R.layout.dialog_print, container, false);
			cancel = (Button) v.findViewById(R.id.btCancel);
			send = (Button) v.findViewById(R.id.btOk);
			adressField = (EditText) v.findViewById(R.id.etAdress);

			String defaultAdress = sp.getString("pref_mail", "");
			adressField.setText(defaultAdress);

			cancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					getDialog().dismiss();
				}
			});

			send.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					String adress = adressField.getText().toString();
					String text = db.getAll();
					Intent i = new Intent(Intent.ACTION_SEND);
					i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.dialog_print_subject));
					i.putExtra(Intent.EXTRA_EMAIL, new String[] { adress });
					i.putExtra(Intent.EXTRA_TEXT, text);
					i.setType("message/rfc822");
					Intent choose = Intent.createChooser(i, "Select email app:");
					startActivity(choose);
				}
			});

			return v;
		}
	}
}
