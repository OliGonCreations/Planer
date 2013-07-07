package com.kaith.planer;

import java.util.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;

public class FragmentSunday extends Fragment implements OnClickListener, OnLongClickListener {

	public static FragmentManager fm;
	public static DatabaseHandler db;
	public static int hour, day;
	public int dayOfWeek = 1;
	int hourCount = 14;
	View v;
	ScrollView sv;
	LinearLayout[] layout = new LinearLayout[hourCount];
	Button[] bt = new Button[hourCount];
	int[] layoutId = { R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7, R.id.ll8, R.id.ll9, R.id.ll10, R.id.ll11, R.id.ll12,
			R.id.ll13, R.id.ll14 };
	int[] buttonId = { R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9, R.id.bt10, R.id.bt11, R.id.bt12,
			R.id.bt13, R.id.bt14 };
	Calendar calendar = Calendar.getInstance();

	public FragmentSunday() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.timetable, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initialize();
		findView();
		updateViews();
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		updateViews();
		super.onResume();
	}

	private void initialize() {
		fm = getFragmentManager();
		db = new DatabaseHandler(getActivity());
		day = calendar.get(Calendar.DAY_OF_WEEK);
		for (int i = 7; i < 21; i++) {
			if (calendar.get(Calendar.HOUR_OF_DAY) == i) {
				hour = i - 7;
			}
		}
	}

	private void findView() {
		sv = (ScrollView) getView().findViewById(R.id.sv);
		for (int i = 0; i < hourCount; i++) {
			layout[i] = (LinearLayout) getView().findViewById(layoutId[i]);
			bt[i] = (Button) getView().findViewById(buttonId[i]);
			bt[i].setOnClickListener(this);
			bt[i].setOnLongClickListener(this);
		}
	}

	private void updateViews() {
		String btText = getResources().getString(R.string.btText);
		String[] bundle = new String[2];
		for (int i = 0; i < bt.length; i++) {
			bt[i].setText(btText);
			try {
				bundle = db.getEvent(bt[i].getId(), dayOfWeek);
				if (bundle[0].trim().length() > 0) {
					bt[i].setText(bundle[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (day == dayOfWeek) {
			for (int i = 0; i < hour; i++)
				layout[i].setBackgroundColor(Color.DKGRAY);
			layout[hour].setBackgroundColor(getResources().getColor(R.color.orange));
		}
		if (hour >= 6) {
			sv.post(new Runnable() {
				public void run() {
					sv.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}

	public void onClick(View v) {
		if (((Button) v).getText().toString().equals(getResources().getString(R.string.btText))) {
			DialogNew dialog = new DialogNew(new DialogFragmentDismissHandler(), v.getId(), dayOfWeek, false);
			dialog.show(fm, "fragment_dialog");
		} else {
			DialogEvent dialog = new DialogEvent(new DialogFragmentDismissHandler(), v.getId(), dayOfWeek);
			dialog.show(fm, "fragment_dialog_entry");
		}

	}

	public boolean onLongClick(final View v) {
		final PopupMenu popupMenu = new PopupMenu(getActivity(), v);
		popupMenu.inflate(R.menu.menu_popup);
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.menu_popup_delete:
					db.deleteEvent(v.getId(), dayOfWeek);
					updateViews();
					return true;

				default:
					return false;
				}
			}
		});
		popupMenu.show();
		return false;
	}

	private class DialogFragmentDismissHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			updateViews();
		}
	}
}
