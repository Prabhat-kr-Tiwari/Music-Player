package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity){

    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return  when (position){
            0-> SongsFragment()
            1-> AlbumFragment()
            else->SongsFragment()
        }
    }

}