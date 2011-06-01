package com.jonathongrigg.proton.voltagecontrol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;


public class VoltageControlSlider extends
		PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	/**
	 * {@inheritDoc}
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesName(
			"protonVoltages");
		addPreferencesFromResource(R.xml.settings);
		getPreferenceManager().getSharedPreferences().
			registerOnSharedPreferenceChangeListener(this);
	}
	/**
	 * {@inheritDoc}
	 */
	protected void onDestroy() {
		getPreferenceManager().getSharedPreferences().
			unregisterOnSharedPreferenceChangeListener(this);

		super.onDestroy();
	}
	/**
	 * {@inheritDoc}
	 */
	protected void onResume() {
		super.onResume();
	}
	/**
	 * {@inheritDoc}
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}
}
