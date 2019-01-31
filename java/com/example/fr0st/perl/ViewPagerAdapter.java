package com.example.fr0st.perl;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch(i){

            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;

            case 1:
                PostsFragment postsFragment = new PostsFragment();
                return postsFragment;

            case 2:
                TrendingFragment trendingFragment = new TrendingFragment();
                return trendingFragment;




                default:
                    return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {
         super.getPageTitle(i);

         switch (i){

             case 0:
                 return "CHATS";

             case 1:
                 return "POSTS";

             case 2:
                 return "TRENDING";

                 default:
                     return null;
         }
    }
}
