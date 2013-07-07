package com.kaith.planer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogEvent extends DialogFragment implements OnClickListener {
	FragmentManager fm;
	Handler handler;
	DatabaseHandler db;
	Button btChange, btOk;
	TextView tvName, tvDescription;
	int btId;
	int day;
	String[] bundle = new String[2];

	public DialogEvent(Handler handler, int btId, int day) {
		this.handler = handler;
		this.btId = btId;
		this.day = day;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.event));
		getDialog().setCanceledOnTouchOutside(false);
		View v;
		v = inflater.inflate(R.layout.dialog_event, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		setCancelable(false);
		fm = getFragmentManager();
		db = new DatabaseHandler(getActivity());
		tvName = (TextView) getView().findViewById(R.id.tvName);
		tvDescription = (TextView) getView().findViewById(R.id.tvDescribtion);
		btChange = (Button) getView().findViewById(R.id.btChange);
		btOk = (Button) getView().findViewById(R.id.btOk);
		btChange.setOnClickListener(this);
		btOk.setOnClickListener(this);
		updateViews();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private void updateViews() {
		bundle = db.getEvent(btId, day);
		tvName.setText(bundle[0]);
		tvDescription.setText(bundle[1]);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btChange:
			DialogNew dialog = new DialogNew(new DialogFragmentDismissHandler(), btId, day, true);
			dialog.show(fm, "fragment_dialog");
			break;
		case R.id.btOk:
			getDialog().dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		handler.sendEmptyMessage(0);
	}
	
	private class DialogFragmentDismissHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateViews();
		}

		
	}
}
