package com.jonathongrigg.proton.voltagecontrol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProtonPrefs extends Activity{

	public final static String THEME_SETTING = "theme";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences protonPreferences = getSharedPreferences("protonSavedPrefs", 0);
		int choosenTheme = protonPreferences.getInt(THEME_SETTING, 1);
		
		if(choosenTheme == 1)
			setContentView(R.layout.settings);
		else  // load default theme
			setContentView(R.layout.settings_proton_theme);
		
				
		findViewById(R.id.saveSettings).setOnClickListener(mOnConfigClickListener);
		
		//load layout options from strings
		Spinner themeSpinner = (Spinner)findViewById(R.id.themeSpinner);
		ArrayAdapter<CharSequence> theme = ArrayAdapter.createFromResource(this, R.array.theme_array, R.layout.spinner_settings);
		theme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		themeSpinner.setAdapter(theme);

        // Update spinner to show currently selected theme
        int theme2 = protonPreferences.getInt(THEME_SETTING, 0);
		themeSpinner.setSelection(theme2);
            
	}
	
	View.OnClickListener mOnConfigClickListener = new View.OnClickListener() {	
			
			public void onClick(View v) {
	
				switch(v.getId())	{
					case R.id.saveSettings:
						SharedPreferences protonPreferences = getSharedPreferences("protonSavedPrefs", 0);
						
						Spinner spinner = (Spinner)findViewById(R.id.themeSpinner);
						int selectedUnit = spinner.getSelectedItemPosition();
						
				        SharedPreferences.Editor editor = protonPreferences.edit();
				        editor.putInt(THEME_SETTING, selectedUnit);
				        editor.commit(); // save settings			        
				        finish();
						break;
				}
			}
	};

}
