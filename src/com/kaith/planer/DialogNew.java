package com.kaith.planer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class DialogNew extends DialogFragment implements OnClickListener {

	Handler handler;
	Button btCancel, btOk;
	EditText etName, etDescription;
	DatabaseHandler db;
	int btId;
	int day;
	boolean edit;

	public DialogNew(Handler handler, int btId, int day, boolean edit) {
		this.handler = handler;
		this.btId = btId;
		this.day = day;
		this.edit = edit;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setTitle(getResources().getString(R.string.new_event));
		getDialog().setCanceledOnTouchOutside(false);
		View v;
		v = inflater.inflate(R.layout.dialog_new, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		setCancelable(false);
		db = new DatabaseHandler(getActivity());
		etName = (EditText) getView().findViewById(R.id.etName);
		etDescription = (EditText) getView().findViewById(R.id.etDescription);
		btCancel = (Button) getView().findViewById(R.id.btCancel);
		btOk = (Button) getView().findViewById(R.id.btOk);
		btCancel.setOnClickListener(this);
		btOk.setOnClickListener(this);
		if (edit) {
			String[] event = db.getEvent(btId, day);
			etName.setText(event[0]);
			etDescription.setText(event[1]);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btCancel:
			getDialog().dismiss();
			break;
		case R.id.btOk:
			db.deleteEvent(btId, day);
			db.addVeranstaltung(new Veranstaltung(etName.getText().toString(), etDescription.getText().toString(), btId, 1, day));
			getActivity().startService(new Intent(getActivity(), EventsUpdateService.class));
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

}
