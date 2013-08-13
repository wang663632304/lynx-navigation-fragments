package com.cheshire.navigation_fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

//Created by Stephen Lynx
public class NavigationControllerFragment extends Fragment
{ 
	private int transitionMode;
	private int stackSize;
	private int generatedId;
	private NavigationFragment rootFragment;
	private static final String generatedIDKey = "generatedKey";
	private ControllerFragmentListener listener;
	
	public int getStackSize()
	{
		return stackSize;
	}

	public void setListener (ControllerFragmentListener newListener)
	{
		listener = newListener;
	}

	public void setTransitionMode(int newTransition)
	{
		transitionMode = newTransition;
	}
	
	public void pushFragment(NavigationFragment toPush)
	{
		this.pushFragment(toPush, true);
	}

	public void onSaveInstanceState(Bundle outState) 
	{
		outState.putInt(generatedIDKey, generatedId);
		
		super.onSaveInstanceState(outState);
	}
	
	public void setRootFragment(NavigationFragment newFragment)
	{
		rootFragment = newFragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		FrameLayout frameLayout = new FrameLayout(getActivity());

		frameLayout.setId(generatedId);

		return frameLayout;
	}

	public void onDestroy()
	{
		super.onDestroy();
	}
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) 
		{
			generatedId = FrameLayout.generateViewId();
			
			FragmentTransaction ft = getChildFragmentManager().beginTransaction();

			stackSize =1;

			ft.add(generatedId, getRootFragment()).commit();
		}
		else
		{
			generatedId = savedInstanceState.getInt(generatedIDKey);
		}
	}

	public NavigationFragment getRootFragment() 
	{
		return rootFragment;
	}

	public void popFragment()
	{
		if (stackSize > 1)
		{
			stackSize--;
			
			getChildFragmentManager().popBackStack();

			if (listener!=null)
			{
				listener.controllerChangedStackSize(stackSize,this);
			}
		}
	}

	public void pushFragment(NavigationFragment toPush, boolean addToStack) 
	{
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();

		ft.replace(generatedId, toPush);
		
		ft.setTransition(transitionMode);

		if (addToStack)
		{
			stackSize++;

			ft.addToBackStack(null);

			if (listener!=null)
			{
				listener.controllerChangedStackSize(stackSize, this);
			}
		}

		ft.commit();
	}
}
