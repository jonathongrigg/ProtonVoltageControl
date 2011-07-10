package com.jonathongrigg.proton.voltagecontrol;

/*
** Copyright Jonathon Grigg <http://www.jonathongrigg.com> July 2011
** Email me at jonathongrigg@gmail.com if needed for some reason
** 
** Source code licensed under the Open Software License version 3.0
** 	http://www.opensource.org/licenses/osl-3.0
*/

import android.app.ListActivity;
import android.os.Bundle;

public class VoltageList extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

	}

}
