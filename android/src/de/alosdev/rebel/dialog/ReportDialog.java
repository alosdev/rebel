package de.alosdev.rebel.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.alosdev.rebel.R;

public class ReportDialog extends DialogFragment implements OnEditorActionListener, OnClickListener {

	public interface ReportDialogListener {
		void onFinishReportDialog(String name, String message);
	}

	private EditText name;
	private EditText message;

	public ReportDialog() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_report, container);
		name = (EditText) view.findViewById(R.id.name);
		message = (EditText) view.findViewById(R.id.message);
		getDialog().setTitle("Report Violence");
		name.requestFocus();
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		name.setOnEditorActionListener(this);
		message.setOnEditorActionListener(this);
		view.findViewById(R.id.ok).setOnClickListener(this);
		return view;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			sendReport();
			return true;
		}
		return false;
	}

	private void sendReport() {
	  ReportDialogListener activity = (ReportDialogListener) getActivity();
	  activity.onFinishReportDialog(name.getText().toString(), message.getText().toString());
	  this.dismiss();
  }

	@Override
  public void onClick(View v) {
		sendReport();
  }
}