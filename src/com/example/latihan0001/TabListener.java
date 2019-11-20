package com.example.latihan0001;


import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar;

@SuppressLint("NewApi") public class TabListener implements ActionBar.TabListener{

	Fragment fragment;
	
	public TabListener(Fragment fragment){
		this.fragment = fragment;
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("NewApi") @Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		arg1.replace(R.id.fragment_container, fragment);
	}

	@SuppressLint("NewApi") @Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		arg1.remove(fragment);
	}

}