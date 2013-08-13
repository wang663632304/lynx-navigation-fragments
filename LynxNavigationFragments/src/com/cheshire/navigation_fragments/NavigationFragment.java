package com.cheshire.navigation_fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Created by Stephen Lynx
public class NavigationFragment extends Fragment
{
	protected NavigationControllerFragment parent;
	protected boolean paused;
	
	public void onCreate(Bundle savedState)
	{
		super.onCreate(savedState);
		
		if (NavigationControllerFragment.class.isAssignableFrom(getParentFragment().getClass())) 
		{
			parent = (NavigationControllerFragment) getParentFragment();
		}
		
		paused = false;
	}
	
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		paused = true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		TextView v = new TextView(getActivity());
		
		v.setText("You are seeing this because you tried to push a fragment that didnt inherited from TabFragment as the root of a tab or because you forgot to overwrite onCreateView on your fragment. read the logcat for some information kthnxbai");
		
		return v;
	}
}