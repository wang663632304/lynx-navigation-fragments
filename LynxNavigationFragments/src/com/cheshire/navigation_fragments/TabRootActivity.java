package com.cheshire.navigation_fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

//Created by Stephen Lynx
public class TabRootActivity extends Activity implements TabListener, ControllerFragmentListener
{
	private NavigationControllerFragment currentFragment;
	private ArrayList <Tab> tabs;
	private ArrayList<TabInformation> fragmentInformations;

	public TabRootActivity()
	{
		super();
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{

	}

	public void setTabInformations(ArrayList<TabInformation> newInformations)
	{
		fragmentInformations = newInformations;
	}	

	protected void onSaveInstanceState(Bundle outState) 
	{
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());

		super.onSaveInstanceState(outState);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		Fragment mFragment = fragmentInformations.get(tabs.indexOf(tab)).fragment;

		if (mFragment != null)
		{
			ft.detach(mFragment);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			if (currentFragment!=null)
			{
				currentFragment.popFragment();
			}
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void controllerChangedStackSize(int newStackSize, NavigationControllerFragment controller)
	{
		if (controller != currentFragment)
		{
			return;
		}
		
		if (newStackSize> 1)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final ActionBar bar = getActionBar();

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabs = new ArrayList<Tab>();

		if (fragmentInformations == null || fragmentInformations.size()==0)
		{
			System.out.println("Tab list null or empty at the creation of the tabs.");
			
			fragmentInformations = new ArrayList<TabInformation>();
			
			fragmentInformations.add(new TabInformation("Null list of tabs", null, null, null));
		}

		for (TabInformation tabInfo: fragmentInformations) 
		{
			Tab newTab = bar.newTab();

			newTab.setText((CharSequence) tabInfo.label);

			if (tabInfo.iconId != null)
			{
				newTab.setIcon((Integer) tabInfo.iconId);
			}

			newTab.setTabListener(this);

			tabs.add(newTab);

			Fragment temp = getFragmentManager().findFragmentByTag(Integer.toString(fragmentInformations.indexOf(tabInfo)));

			tabInfo.fragment = temp;

			if (temp != null && !temp.isDetached())
			{
				FragmentTransaction ft = getFragmentManager().beginTransaction();

				ft.detach(temp);

				ft.commit();
			}
			
			bar.addTab(newTab);
		}

		if (savedInstanceState != null) 
		{
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		currentFragment = (NavigationControllerFragment) fragmentInformations.get(tabs.indexOf(tab)).fragment;

		if (currentFragment == null)
		{
			currentFragment = (NavigationControllerFragment) Fragment.instantiate(this, NavigationControllerFragment.class.getName(),null);

			currentFragment.setListener(this);
			
			if (fragmentInformations.get(tabs.indexOf(tab)).defaultTransition!=null)
			{
				currentFragment.setTransitionMode((Integer) fragmentInformations.get(tabs.indexOf(tab)).defaultTransition);
			}
			else
			{
				currentFragment.setTransitionMode(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);				
			}

			Object desiredRootFragment = fragmentInformations.get(tabs.indexOf(tab)).rootFragment;

			if (desiredRootFragment == null || !NavigationFragment.class.isAssignableFrom(desiredRootFragment.getClass()))
			{
				System.out.println("Tried to use a root fragment that didnt inherited from TabFragment for tab with name "+ fragmentInformations.get(tabs.indexOf(tab)).label+".");
				currentFragment.setRootFragment(new NavigationFragment());
			}
			else
			{
				currentFragment.setRootFragment((NavigationFragment) fragmentInformations.get(tabs.indexOf(tab)).rootFragment);
			}

			fragmentInformations.get(tabs.indexOf(tab)).fragment = currentFragment;;

			ft.add(android.R.id.content, currentFragment, Integer.toString(tabs.indexOf(tab)));
		} 
		else
		{
			ft.attach(currentFragment);
		}

		currentFragment.setListener(this);
		
		if (currentFragment.getStackSize()>1)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}
}