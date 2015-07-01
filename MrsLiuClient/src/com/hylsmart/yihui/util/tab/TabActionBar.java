package com.hylsmart.yihui.util.tab;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

public class TabActionBar {
    
    private ScrollingTabView mTabScrollView;
    private Context mContext;
    private ArrayList<Tab> mTabs = new ArrayList<Tab>();
    private Tab mSelectedTab;
    
    public void setSelectedNavigationItem(int position) {
       
        selectTab(mTabs.get(position));
    }
    
    public TabActionBar(Context context,ScrollingTabView sContainerView){
        mContext =context;
        mTabScrollView = sContainerView;
    }
    
    public void selectTab(Tab tab) {
      
        if (mSelectedTab == tab) {
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabReselected(mSelectedTab);
//                mTabScrollView.animateToTab(tab.getPosition());
            }
        } else {
            mTabScrollView.setTabSelected(tab != null ? tab.getPosition() : 0);
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabUnselected(mSelectedTab);
            }
            mSelectedTab =  tab;
            if (mSelectedTab != null) {
                mSelectedTab.getCallback().onTabSelected(mSelectedTab);
            }
        }

    }
    public void removeAllTabs() {
        cleanupTabs();
    }

    private void cleanupTabs() {
        if (mSelectedTab != null) {
            selectTab(null);
        }
        mTabs.clear();
        if (mTabScrollView != null) {
            mTabScrollView.removeAllTabs();
        }
    }
    public void removeTabAt(int position) {
        if (mTabScrollView == null) {
            // No tabs around to remove
            return;
        }

        int selectedTabPosition = mSelectedTab != null
                ? mSelectedTab.getPosition() : 0;
        mTabScrollView.removeTabAt(position);
        Tab removedTab = mTabs.remove(position);
        if (removedTab != null) {
            removedTab.setPosition(-1);
        }

        final int newTabCount = mTabs.size();
        for (int i = position; i < newTabCount; i++) {
            mTabs.get(i).setPosition(i);
        }

        if (selectedTabPosition == position) {
            selectTab(mTabs.isEmpty() ? null : mTabs.get(Math.max(0, position - 1)));
        }
    }
    public Tab getSelectedTab() {
        return mSelectedTab;
    }
    
    public void addTab(Tab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    
    public void addTab(Tab tab, int position) {
        addTab(tab, position, mTabs.isEmpty());
    }

   
    public void addTab(Tab tab, boolean setSelected) {
        mTabScrollView.addTab(tab, setSelected);
        configureTab(tab, mTabs.size());
        if (setSelected) {
            selectTab(tab);
        }
    }


    public void addTab(Tab tab, int position, boolean setSelected) {
        mTabScrollView.addTab(tab, position, setSelected);
        configureTab(tab, position);
        if (setSelected) {
            selectTab(tab);
        }
    }
    
    public Tab newTab() {
        return new Tab();
    }
    
    private void configureTab(Tab tab, int position) {
        final Tab tabi = (Tab) tab;
        final TabActionBar.TabListener callback = tabi.getCallback();

        if (callback == null) {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }

        tabi.setPosition(position);
        mTabs.add(position, tabi);

        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }
    public class Tab{
        private TabActionBar.TabListener mCallback;
        private Object mTag;
        private Drawable mIcon;
        private CharSequence mText;
        private CharSequence mContentDesc;
        private int mPosition = -1;
        private View mCustomView;
        private int  mTabbgDrawableId;
      
        public Object getTag() {
            return mTag;
        }

      
        public Tab setTag(Object tag) {
            mTag = tag;
            return this;
        }

        public TabActionBar.TabListener getCallback() {
            return mCallback;
        }

     
        public Tab setTabListener(TabActionBar.TabListener callback) {
            mCallback = callback;
            return this;
        }

       
        public View getCustomView() {
            return mCustomView;
        }

      
        public Tab setCustomView(View view) {
            mCustomView = view;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }
        public Tab setCustomView(int layoutResId) {
            return setCustomView(LayoutInflater.from(mContext)
                    .inflate(layoutResId, null));
        }

      
        public Drawable getIcon() {
            return mIcon;
        }

     
        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

       
        public CharSequence getText() {
            return mText;
        }

       
        public Tab setIcon(Drawable icon) {
            mIcon = icon;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

       
        public Tab setIcon(int resId) {
            return setIcon(mContext.getResources().getDrawable(resId));
        }

        
        public Tab setText(CharSequence text) {
            mText = text;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        
        public Tab setText(int resId) {
            return setText(mContext.getResources().getText(resId));
        }

        
        public void select() {
            selectTab(this);
        }

        
        public Tab setContentDescription(int resId) {
            return setContentDescription(mContext.getResources().getText(resId));
        }

        
        public Tab setContentDescription(CharSequence contentDesc) {
            mContentDesc = contentDesc;
            if (mPosition >= 0) {
                mTabScrollView.updateTab(mPosition);
            }
            return this;
        }

        
        public CharSequence getContentDescription() {
            return mContentDesc;
        }



        public int getmTabbgDrawableId() {
            return mTabbgDrawableId;
        }


        public Tab setmTabbgDrawableId(int mTabbgDrawableId) {
            this.mTabbgDrawableId = mTabbgDrawableId;
            return this;
        }
    }
    
    /**
     * Callback interface invoked when a tab is focused, unfocused, added, or removed.
     */
    public interface TabListener {
        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         */
        public void onTabSelected(Tab tab);

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         */
        public void onTabUnselected(Tab tab);

        /**
         * Called when a tab that is already selected is chosen again by the user.
         * Some applications may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         */
        public void onTabReselected(Tab tab);
    }
    
   


}



