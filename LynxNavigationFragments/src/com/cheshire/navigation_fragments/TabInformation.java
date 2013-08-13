package com.cheshire.navigation_fragments;

import android.app.Fragment;

//Created by Stephen Lynx
public class TabInformation 
{
	public String label;
	public NavigationFragment rootFragment;
	public Integer iconId;
	public Integer defaultTransition;
	public Fragment fragment;

	public TabInformation(String label, NavigationFragment rootFragment, Integer iconId, Integer defaultTransition)
	{
		this.label = label;
		this.rootFragment = rootFragment;
		this.iconId = iconId;
		this.defaultTransition = defaultTransition;
	}
}
