package com.jonathongrigg.proton.voltagecontrol;

/*
** Copyright Jonathon Grigg <http://www.jonathongrigg.com> June 2011
** Email me at jonathongrigg@gmail.com if needed for some reason
** 
** Source code licensed under the Open Software License version 3.0
** 	http://www.opensource.org/licenses/osl-3.0
*/

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.ActionBar.Type;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.OutputStreamWriter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VoltageControl extends GDListActivity {
	
	// Commands
	protected static final String C_UV_MV_TABLE = "cat /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table";
	protected static final String C_LIST_ETC = "ls /etc/";
	protected static final String C_LIST_INIT_D = "ls /etc/init.d/";
	protected static final String C_LIST_SUPER2 = "ls /etc/super2/";

	// Checks
	boolean isSuAvailable = ShellInterface.isSuAvailable();
	
	// UI
	private QuickActionWidget actionGrid;
	private QuickActionGrid voltageGrid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		  	
        super.onCreate(savedInstanceState);
        
        getActionBar().setType(Type.Empty);
        prepareActionGrid();
        prepareVoltageGrid();
        addActionBarItem(ActionBarItem.Type.Edit);
        addActionBarItem(ActionBarItem.Type.Settings);
        
        ItemAdapter adapter;
        try {
            adapter = ItemAdapter.createFromXml(this, R.xml.voltages);
            setListAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        // Error checking, if pass SU check then load kernel voltages
        if (isSuAvailable = false) {
        	Toast.makeText(getBaseContext(), "ERROR: No Root Access!", Toast.LENGTH_LONG).show();
        	finish();
        } else {
        	//load existing voltages with assumption that user is using a compatible kernel
        	getExistingVoltages();
        }
            	/**
            	//check the prefs to see if the user wants voltages in the init
            	SharedPreferences settings = getSharedPreferences("protonSavedPrefs", 0);
            	boolean saveBoot = settings.getBoolean(ProtonPrefs.SAVE_ON_BOOT, false);
            	if (saveBoot) {   //if user wants voltages saved
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
        });**/
    	
        /**
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
            	switch(v.getId()) {
            	  case R.id.button2: 
            		  getExistingVoltages();
            	      break;
            	  case R.id.button3:
            		  //defaultVoltages();
            	      break;
            	  case R.id.button4:
            		  //recommendedVoltages();
            	      break;
            	}
            }
        };
                
    	existingVoltagesButton.setOnClickListener(listener);
    	defaultVoltagesButton.setOnClickListener(listener);
    	recommendedVoltagesButton.setOnClickListener(listener);
    	**/
    }
    
    private void prepareActionGrid() {
        actionGrid = new QuickActionGrid(this);
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_export, R.string.quickaction_apply));
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_compose, R.string.quickaction_save_boot));
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_trashcan, R.string.quickaction_remove));
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_star, R.string.quickaction_donate));
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_info, R.string.quickaction_about));
        actionGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_help, R.string.quickaction_help));

        actionGrid.setOnQuickActionClickListener(actionGridListener);
    }
    
    private void prepareVoltageGrid() {
        voltageGrid = new QuickActionGrid(this);
        voltageGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_star, R.string.quickaction_suggested));
        voltageGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_star, R.string.quickaction_stock));
        voltageGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_star, R.string.quickaction_existing));
      
        voltageGrid.setOnQuickActionClickListener(voltageGridListener);
    }

    private OnQuickActionClickListener actionGridListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
            Toast.makeText(VoltageControl.this, "Action Item " + position + " clicked", Toast.LENGTH_SHORT).show();
        }
    };
    
    private OnQuickActionClickListener voltageGridListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
            Toast.makeText(VoltageControl.this, "Voltage Item " + position + " clicked", Toast.LENGTH_SHORT).show();
        }
    };
    
    private static class MyQuickAction extends QuickAction {
        
        private static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);

        public MyQuickAction(Context ctx, int drawableId, int titleId) {
            super(ctx, buildDrawable(ctx, drawableId), titleId);
        }
        
        private static Drawable buildDrawable(Context ctx, int drawableId) {
            Drawable d = ctx.getResources().getDrawable(drawableId);
            d.setColorFilter(BLACK_CF);
            return d;
        }
        
    }
    
    public void onShowActionGrid(View v) {
        actionGrid.show(v);
    }
    
    public void onShowVoltageGrid(View v) {
        voltageGrid.show(v);
    }
    
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (position) {
        	case 0:
        		onShowVoltageGrid(item.getItemView());
        		break;
            case 1:
                onShowActionGrid(item.getItemView());
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
  
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menuEmergencyBoot:
            downloadEmergencyBoot();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**private void defaultVoltages() {
    	findViewById(R.id.button1).setVisibility(View.VISIBLE);  // show apply button
    	// Edit text boxes
        TextView cpu1400 = (TextView)findViewById(R.id.viewText1400);	// 1400mhz
        TextView cpu1300 = (TextView)findViewById(R.id.viewText1300);	// 1300mhz
        TextView cpu1200 = (TextView)findViewById(R.id.viewText1200);	// 1200mhz
        TextView cpu1000 = (TextView)findViewById(R.id.viewText1000);	// 1000mhz
        TextView cpu800 = (TextView)findViewById(R.id.viewText800);		// 800mhz
        TextView cpu400 = (TextView)findViewById(R.id.viewText400);		// 400mhz
        TextView cpu200 = (TextView)findViewById(R.id.viewText200);		// 200mhz
        TextView cpu100 = (TextView)findViewById(R.id.viewText100);		// 100mhz
        
        // Strings
        String dvString = this.getString(R.string.stock_voltages);
        String[] dv = dvString.split(" ");
        
        // Applying code
        cpu1400.setText(dv[0]);
        cpu1300.setText(dv[1]);
        cpu1200.setText(dv[2]);
        cpu1000.setText(dv[3]);
        cpu800.setText(dv[4]);
        cpu400.setText(dv[5]);
        cpu200.setText(dv[6]);
        cpu100.setText(dv[7]);
    }  
    
    private void customVoltages() {
    	loadSliderData();
	    	if(seekBar1400 == 0 || seekBar1300 == 0 || seekBar1200 == 0 || seekBar1000 == 0 || seekBar800 == 0 || seekBar400 == 0 || seekBar200 == 0 || seekBar100 == 0) {
	    		AlertWindow(getString(R.string.alert_no_custom_uv));
	    	} else {
	    		findViewById(R.id.button1).setVisibility(View.VISIBLE);  // show apply button
	    	}
    	
    	// Edit text boxes
    	TextView cpu1400 = (TextView)findViewById(R.id.viewText1400);
    	TextView cpu1300 = (TextView)findViewById(R.id.viewText1300);
    	TextView cpu1200 = (TextView)findViewById(R.id.viewText1200);
    	TextView cpu1000 = (TextView)findViewById(R.id.viewText1000);
    	TextView cpu800 = (TextView)findViewById(R.id.viewText800);
    	TextView cpu400 = (TextView)findViewById(R.id.viewText400);
    	TextView cpu200 = (TextView)findViewById(R.id.viewText200);
    	TextView cpu100 = (TextView)findViewById(R.id.viewText100);
        
        
        
        // Load data from custom voltage menu
        cpu1400.setText(Integer.toString(seekBar1400));
        cpu1300.setText(Integer.toString(seekBar1300));
        cpu1200.setText(Integer.toString(seekBar1200));
        cpu1000.setText(Integer.toString(seekBar1000));
        cpu800.setText(Integer.toString(seekBar800));
        cpu400.setText(Integer.toString(seekBar400));
        cpu200.setText(Integer.toString(seekBar200));
        cpu100.setText(Integer.toString(seekBar100));
    } 

    private void recommendedVoltages() {
    	findViewById(R.id.button1).setVisibility(View.VISIBLE);  // show apply button
    	// Edit text boxes
        TextView cpu1400 = (TextView)findViewById(R.id.viewText1400);	// 1400mhz
        TextView cpu1300 = (TextView)findViewById(R.id.viewText1300);	// 1300mhz
        TextView cpu1200 = (TextView)findViewById(R.id.viewText1200);	// 1200mhz
        TextView cpu1000 = (TextView)findViewById(R.id.viewText1000);	// 1000mhz
        TextView cpu800 = (TextView)findViewById(R.id.viewText800);		// 800mhz
        TextView cpu400 = (TextView)findViewById(R.id.viewText400);		// 400mhz
        TextView cpu200 = (TextView)findViewById(R.id.viewText200);		// 200mhz
        TextView cpu100 = (TextView)findViewById(R.id.viewText100);		// 100mhz
        
        // Strings
        String rvString = this.getString(R.string.recommended_voltages);
        String[] rv = rvString.split(" ");
        
        // Applying code
        cpu1400.setText(rv[0]);
        cpu1300.setText(rv[1]);
        cpu1200.setText(rv[2]);
        cpu1000.setText(rv[3]);
        cpu800.setText(rv[4]);
        cpu400.setText(rv[5]);
        cpu200.setText(rv[6]);
        cpu100.setText(rv[7]);
    }**/
    
    private void getExistingVoltages() {
    	//findViewById(R.id.button1).setVisibility(View.GONE);  // existing values do not need button
        String existingVoltagesValue = null;
        String[] tableValues;
        StringBuilder voltages = new StringBuilder();
        
        existingVoltagesValue = ShellInterface.getProcessOutput(C_UV_MV_TABLE);
        
        tableValues = existingVoltagesValue.split(" ");
        for (int i = 1; i < tableValues.length; i += 3) {
        	/**
        	//1400mhz
        	if (i == 1) { 		
        		TextView cpu1400 = (TextView)findViewById(R.id.viewText1400);
        		cpu1400.setText(tableValues[i]);
        	}
        	
        	//1300mhz
        	if (i == 4) { 		
        		TextView cpu1300 = (TextView)findViewById(R.id.viewText1300);
        		cpu1300.setText(tableValues[i]);
        	}
        	
        	//1200mhz
        	if (i == 7) { 		
        		TextView cpu1200 = (TextView)findViewById(R.id.viewText1200);
        		cpu1200.setText(tableValues[i]);
        	}
        	
        	//1000mhz
        	if (i == 10) { 		
        		TextView cpu1000 = (TextView)findViewById(R.id.viewText1000);
        		cpu1000.setText(tableValues[i]);
        	}
        	
        	//800mhz
        	if (i == 13) { 		
        		TextView cpu800 = (TextView)findViewById(R.id.viewText800);
        		cpu800.setText(tableValues[i]);
        	}
        	
        	//400mhz
        	if (i == 16) { 		
        		TextView cpu400 = (TextView)findViewById(R.id.viewText400);
        		cpu400.setText(tableValues[i]);
        	}
        	
        	//200mhz
        	if (i == 19) { 		
        		TextView cpu200 = (TextView)findViewById(R.id.viewText200);
        		cpu200.setText(tableValues[i]);
        	}
        	
        	//100mhz
        	if (i == 22) { 		
        		TextView cpu100 = (TextView)findViewById(R.id.viewText100);
        		cpu100.setText(tableValues[i]);
        	}**/

        	voltages.append(tableValues[i]);
        	voltages.append(" ");
        }
	}
    
	private String buildUvCommand(String et) {
		StringBuilder command = new StringBuilder();
		String values = et;
		
		command.append("echo \"");
		command.append(values);
		command.append("\" > /sys/devices/system/cpu/cpu0/cpufreq/UV_mV_table");//
	
		return command.toString();
	}
	
	private void downloadEmergencyBoot() {
		Intent downloadIntent = new Intent(Intent.ACTION_VIEW,
			Uri.parse("http://download.jonathongrigg.com/apps/proton_emergency_boot.zip"));
			startActivity(downloadIntent); 
	}
	
	private void saveBootSettings(String et) {
		
		// Check if ROM is SuperAOSP (doesn't use init.d, uses super2)
		boolean superAosp = false;
		if (ShellInterface.getProcessOutput(C_LIST_ETC).contains("super2")) {
			superAosp = true;
		}
		
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

		if (superAosp == true) {	// If running SuperAosp, script saved in /etc/super2/
			ShellInterface.runCommand("chmod 777 /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control");
			ShellInterface.runCommand("busybox mount -o remount,rw  /system");
			ShellInterface.runCommand("busybox cp /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control /etc/super2/proton_voltage_control");
			ShellInterface.runCommand("busybox mount -o remount,ro  /system");
			Toast.makeText(this, "Settings saved in file \"/etc/super2/proton_voltage_control\"", Toast.LENGTH_LONG).show();
		}
		else {
			ShellInterface.runCommand("chmod 777 /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control");
			ShellInterface.runCommand("busybox mount -o remount,rw  /system");
			ShellInterface.runCommand("busybox cp /data/data/com.jonathongrigg.proton.voltagecontrol/files/proton_voltage_control /etc/init.d/proton_voltage_control");
			ShellInterface.runCommand("busybox mount -o remount,ro  /system");
			Toast.makeText(this, "Settings saved in file \"/etc/init.d/proton_voltage_control\"", Toast.LENGTH_LONG).show();
		}
	}
	
	private void removeBootSettings() {
		
		// Check if ROM is SuperAOSP (doesn't use init.d, uses super2)
		boolean superAosp = false;
		if (!ShellInterface.getProcessOutput(C_LIST_ETC).contains("super2")) {
			superAosp = true;
		}
				
		if (superAosp == false && !ShellInterface.getProcessOutput(C_LIST_INIT_D).contains("proton_voltage_control")) {
			Toast.makeText(getBaseContext(), "Error: No Saved Boot Settings Present", Toast.LENGTH_LONG).show();
		}
		else if (superAosp == true && !ShellInterface.getProcessOutput(C_LIST_SUPER2).contains("proton_voltage_control")) {
			Toast.makeText(getBaseContext(), "Error: No Saved Boot Settings Present", Toast.LENGTH_LONG).show();
		}
		else {
			if (superAosp = true) {
				ShellInterface.runCommand("busybox mount -o remount,rw  /system");
				ShellInterface.runCommand("rm /etc/super2/proton_voltage_control");
				ShellInterface.runCommand("busybox mount -o remount,ro  /system");
				Toast.makeText(this, "Removed settings saved in file \"/etc/super2/proton_voltage_control\"", Toast.LENGTH_LONG).show();
			}
			else {
				ShellInterface.runCommand("busybox mount -o remount,rw  /system");
				ShellInterface.runCommand("rm /etc/init.d/proton_voltage_control");
				ShellInterface.runCommand("busybox mount -o remount,ro  /system");
				Toast.makeText(this, "Removed settings saved in file \"/etc/init.d/proton_voltage_control\"", Toast.LENGTH_LONG).show();
			}
		}
	}	
	
}
