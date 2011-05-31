package com.jonathongrigg.proton.voltagecontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;



public class SeekBarDialogPreference extends
		DialogPreference implements SeekBar.OnSeekBarChangeListener {
	// Layout widgets.
	private SeekBar seekBar = null;
	private TextView valueText = null;

	// Custom xml attributes.
	private int maximumValue = 0;
	private int minimumValue = 0;
	private int stepSize = 0;
	private String units = null;

	private int value = 0;

	/**
	 * The SeekBarDialogPreference constructor.
	 * @param context of this preference.
	 * @param attrs custom xml attributes.
	 */
	public SeekBarDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
			R.styleable.SeekBarDialogPreference);

		maximumValue = typedArray.getInteger(
			R.styleable.SeekBarDialogPreference_maximumValue, 0);
		minimumValue = typedArray.getInteger(
			R.styleable.SeekBarDialogPreference_minimumValue, 0);
		stepSize = typedArray.getInteger(
			R.styleable.SeekBarDialogPreference_stepSize, 1);
		units = typedArray.getString(
			R.styleable.SeekBarDialogPreference_units);

		typedArray.recycle();
	}
	/**
	 * {@inheritDoc}
	 */
	protected View onCreateDialogView() {
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		View view = layoutInflater.inflate(
			R.layout.seekbardialogpreference_layout, null);

		seekBar = (SeekBar)view.findViewById(R.id.seekbar);
		valueText = (TextView)view.findViewById(R.id.valueText);

		// Get the persistent value and correct it for the minimum value.
		value = getPersistedInt(minimumValue) - minimumValue;

		// You're never know...
		if (value < 0) {
			value = 0;
		}

		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setKeyProgressIncrement(stepSize);
		seekBar.setMax(maximumValue - minimumValue);
		seekBar.setProgress(value);

		return view;
	}
	/**
	 * {@inheritDoc}
	 */
	public void onProgressChanged(SeekBar seek, int newValue,
			boolean fromTouch) {
		// Round the value to the closest integer value.
		if (stepSize >= 1) {
			value = Math.round(newValue/stepSize)*stepSize;
		}
		else {
			value = newValue;
		}

		// Set the valueText text.
		valueText.setText(String.valueOf(value + minimumValue) +
			(units == null ? "" : units));

		callChangeListener(value);
	}
	/**
	 * {@inheritDoc}
	 */
	public void onStartTrackingTouch(SeekBar seek) {
	}
	/**
	 * {@inheritDoc}
	 */
	public void onStopTrackingTouch(SeekBar seek) {
	}
	/**
	 * {@inheritDoc}
	 */
	public void onClick(DialogInterface dialog, int which) {
		// if the positive button is clicked, we persist the value.
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (shouldPersist()) {
				persistInt(value + minimumValue);
			}
		}

		super.onClick(dialog, which);
	}
}
