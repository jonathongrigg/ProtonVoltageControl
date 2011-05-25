package com.jonathongrigg.proton.voltagecontrol;

/*
** Copyright Jonathon Grigg <http://www.jonathongrigg.com> May 2011
** Email me at jonathongrigg@gmail.com if needed for some reason
** 
** Source code licensed under the Open Software License version 3.0
** 	http://www.opensource.org/licenses/osl-3.0
*/

import java.io.OutputStreamWriter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class VoltageControl extends Activity {
	
	// Commands
	protected static final String C_UV_MV_TABLE = "cat /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	protected static final String C_LIST_INIT_D = "ls /etc/init.d/";
	// Checks
	boolean isSuAvailable = ShellInterface.isSuAvailable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//HIDE KEYBOARD UNTIL A TEXT FIELD IS CLICKED
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		  	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if (isSuAvailable = false) {
        	Toast.makeText(getBaseContext(), "ERROR: No Root Access!", Toast.LENGTH_LONG).show();
        	finish();
        }
        
        //final EditText newVoltages = (EditText) findViewById(R.id.editText1);
    	Button applyVoltagesButton = (Button) findViewById(R.id.button1);
    	Button existingVoltagesButton = (Button) findViewById(R.id.button2);
    	Button defaultVoltagesButton = (Button) findViewById(R.id.button3);
    	Button recommendedVoltagesButton = (Button) findViewById(R.id.button4);
    	Button removeBootSettingsButton = (Button) findViewById(R.id.button5);
    	final CheckBox saveOnBootCheckBox = (CheckBox) findViewById(R.id.checkBox1);
    	
    	//change the bg color of the lower buttons
    	removeBootSettingsButton.getBackground().setColorFilter(0xFF8d2122, PorterDuff.Mode.MULTIPLY);
    	applyVoltagesButton.getBackground().setColorFilter(0xFF8d2122, PorterDuff.Mode.MULTIPLY);
    	existingVoltagesButton.getBackground().setColorFilter(0xFF8d2122, PorterDuff.Mode.MULTIPLY);
    	defaultVoltagesButton.getBackground().setColorFilter(0xFF8d2122, PorterDuff.Mode.MULTIPLY);
    	recommendedVoltagesButton.getBackground().setColorFilter(0xFF8d2122, PorterDuff.Mode.MULTIPLY);
    	
        applyVoltagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText placeholder = (EditText) findViewById(R.id.editText1400);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 1.4ghz", Toast.LENGTH_LONG).show(); }
            		String finalVoltage = placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText1300);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 1.3ghz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText1200);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 1.2ghz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText1000);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 1ghz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText800);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 800mhz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText400);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 400mhz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText200);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 200mhz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            	placeholder = (EditText) findViewById(R.id.editText100);
            		if ((placeholder.getText().toString().equals(""))) { Toast.makeText(getBaseContext(), "Error: No Voltage Entered for 100mhz", Toast.LENGTH_LONG).show(); }
            		finalVoltage = finalVoltage + " " + placeholder.getText().toString();
            		
            		
            		
            		
            	//newVoltages.setText("HAH!" + finalVoltage);
            
            	if (saveOnBootCheckBox.isChecked()) {
            		if (finalVoltage.length() > 27) {
                		ShellInterface.runCommand(buildUvCommand(finalVoltage));
                		saveBootSettings(finalVoltage);
                		}
                		else {
                			Toast.makeText(getBaseContext(), "Error: Missing Voltages", Toast.LENGTH_LONG).show();
                		}
            	}
            	else {
            		if (finalVoltage.length() > 27) {
            		ShellInterface.runCommand(buildUvCommand(finalVoltage));
            		Toast.makeText(getBaseContext(), "Voltages Applied Successfully", Toast.LENGTH_SHORT).show();
            		}
            		else {
            			Toast.makeText(getBaseContext(), "Error: Missing Voltages", Toast.LENGTH_LONG).show();
            		}
            	}  
            }
        });
    	
        existingVoltagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getExistingVoltages();
            }
        });
        
        defaultVoltagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //newVoltages.setText(R.string.stock_voltages);
                
                //1400mhz
                EditText cpu1400 = (EditText)findViewById(R.id.editText1400);
        		cpu1400.setText("1450");
        		//1300mhz
                EditText cpu1300 = (EditText)findViewById(R.id.editText1300);
        		cpu1300.setText("1400");
        		//1200mhz
                EditText cpu1200 = (EditText)findViewById(R.id.editText1200);
        		cpu1200.setText("1350");
        		//1000mhz
                EditText cpu1000 = (EditText)findViewById(R.id.editText1000);
        		cpu1000.setText("1250");
        		//800mhz
                EditText cpu800 = (EditText)findViewById(R.id.editText800);
        		cpu800.setText("1200");
        		//400mhz
                EditText cpu400 = (EditText)findViewById(R.id.editText400);
        		cpu400.setText("1050");
        		//200mhz
                EditText cpu200 = (EditText)findViewById(R.id.editText200);
        		cpu200.setText("950");
        		//100mhz
                EditText cpu100 = (EditText)findViewById(R.id.editText100);
        		cpu100.setText("950");
            }
        });
        
        recommendedVoltagesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //newVoltages.setText(R.string.recommended_voltages);
                
                //1400mhz
                EditText cpu1400 = (EditText)findViewById(R.id.editText1400);
        		cpu1400.setText("1425");
        		//1300mhz
                EditText cpu1300 = (EditText)findViewById(R.id.editText1300);
        		cpu1300.setText("1375");
        		//1200mhz
                EditText cpu1200 = (EditText)findViewById(R.id.editText1200);
        		cpu1200.setText("1325");
        		//1000mhz
                EditText cpu1000 = (EditText)findViewById(R.id.editText1000);
        		cpu1000.setText("1225");
        		//800mhz
                EditText cpu800 = (EditText)findViewById(R.id.editText800);
        		cpu800.setText("1175");
        		//400mhz
                EditText cpu400 = (EditText)findViewById(R.id.editText400);
        		cpu400.setText("1025");
        		//200mhz
                EditText cpu200 = (EditText)findViewById(R.id.editText200);
        		cpu200.setText("925");
        		//100mhz
                EditText cpu100 = (EditText)findViewById(R.id.editText100);
        		cpu100.setText("925");
        		
        		
        		
            }
        });
        
        removeBootSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeBootSettings();
            }
        });
    }
    
    private void getExistingVoltages() {
        String existingVoltagesValue = null;
        String[] tableValues;
        StringBuilder voltages = new StringBuilder();
        
        existingVoltagesValue = ShellInterface.getProcessOutput(C_UV_MV_TABLE);
        
        tableValues = existingVoltagesValue.split(" ");
        for (int i = 1; i < tableValues.length; i += 3) {
        	
        	//1400mhz
        	if (i == 1) { 		
                EditText cpu1400 = (EditText)findViewById(R.id.editText1400);
        		cpu1400.setText(tableValues[i]);
        	}
        	
        	//1300mhz
        	if (i == 4) { 		
                EditText cpu1300 = (EditText)findViewById(R.id.editText1300);
        		cpu1300.setText(tableValues[i]);
        	}
        	
        	//1200mhz
        	if (i == 7) { 		
                EditText cpu1200 = (EditText)findViewById(R.id.editText1200);
        		cpu1200.setText(tableValues[i]);
        	}
        	
        	
        	//1000mhz
        	if (i == 10) { 		
                EditText cpu1000 = (EditText)findViewById(R.id.editText1000);
        		cpu1000.setText(tableValues[i]);
        	}
        	
        	//800mhz
        	if (i == 13) { 		
                EditText cpu800 = (EditText)findViewById(R.id.editText800);
        		cpu800.setText(tableValues[i]);
        	}
        	
        	//400mhz
        	if (i == 16) { 		
                EditText cpu400 = (EditText)findViewById(R.id.editText400);
        		cpu400.setText(tableValues[i]);
        	}
        	
        	//200mhz
        	if (i == 19) { 		
                EditText cpu200 = (EditText)findViewById(R.id.editText200);
        		cpu200.setText(tableValues[i]);
        	}
        	
        	//100mhz
        	if (i == 22) { 		
                EditText cpu100 = (EditText)findViewById(R.id.editText100);
        		cpu100.setText(tableValues[i]);
        	}
        	
        	
        	
        	voltages.append(tableValues[i]);
        	voltages.append(" ");
        }
       // et.setText(voltages.toString());
	}
    
	private String buildUvCommand(String et) {
		StringBuilder command = new StringBuilder();
		String values = et;
		
		command.append("echo \"");
		command.append(values);
		command.append("\" > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");//
	
		return command.toString();
	}
	
	private void saveBootSettings(String et) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(
					"proton_voltage_control", 0));
			String tmp = "#!/system/bin/sh\n"
					+"# Proton Voltage Control 'Set on Boot' file \n"
					+ buildUvCommand(et);
			out.write(tmp);
			out.close();
		} catch (java.io.IOException e) {
			Toast.makeText(this, "Error: file not saved!", Toast.LENGTH_LONG).show();
		}

		ShellInterface.runCommand("chmod 777 /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control");
		ShellInterface.runCommand("busybox mount -o remount,rw  /system");
		ShellInterface.runCommand("busybox cp /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control /etc/init.d/proton_voltage_control");
		ShellInterface.runCommand("busybox mount -o remount,ro  /system");
		Toast.makeText(this, "Settings saved in file \"/etc/init.d/proton_voltage_control\"", Toast.LENGTH_LONG).show();
	}
	
	private void removeBootSettings() {
		if (!ShellInterface.getProcessOutput(C_LIST_INIT_D).contains("proton_voltage_control")) {
			Toast.makeText(getBaseContext(), "Error: No Saved Boot Settings Present", Toast.LENGTH_LONG).show();
		}
		else {
			ShellInterface.runCommand("busybox mount -o remount,rw  /system");
			ShellInterface.runCommand("rm /etc/init.d/proton_voltage_control");
			ShellInterface.runCommand("busybox mount -o remount,ro  /system");
			Toast.makeText(this, "Removed settings saved in file \"/etc/init.d/proton_voltage_control\"", Toast.LENGTH_LONG).show();
		}
	}
}
