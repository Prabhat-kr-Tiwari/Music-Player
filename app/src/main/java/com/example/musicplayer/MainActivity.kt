package com.example.musicplayer

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.musicplayer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val requestCode = 123
    companion object{
        var musicFiles=ArrayList<MusicFiles>()
        var shuffleBoolean=false
        var repeatBoolean=false
    }



    //boolean for permission denied
    private var permissionDenied = false

    //PERMSSION ARRAY
    private val permissions = arrayOf(
        permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setting up the viewpager
         tabLayout = binding.tabLayout
        viewPager = binding.viewPager
         viewPagerAdapter = ViewPagerAdapter(this)


        checkAndRequestPermissions()
        initViewPager()


    }

    //check if all permission are granted
    private fun areAllPermissionsGranted(): Boolean {
        for (permission in permissions) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun checkAndRequestPermissions() {
        val ungrantedPermissions = ArrayList<String>()

        for (permission in permissions) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ungrantedPermissions.add(permission)
            }
        }

        if (ungrantedPermissions.isNotEmpty()) {
            val permissionsArray = ungrantedPermissions.toTypedArray()
            ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
        }
        if(ContextCompat.checkSelfPermission(applicationContext,permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(permission.WRITE_EXTERNAL_STORAGE),requestCode)
        }else{
            musicFiles=getAllAudio(this)
            initViewPager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == this.requestCode) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                        musicFiles=getAllAudio(this)
                    initViewPager()

                } else {

                    // Permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    Log.d("PRABHAT", "onRequestPermissionsResult: Permission denied")
                    permissionDenied = true
                }
            }
        }
    }

    //getting all the song from storage
    private fun getAllAudio(context: Context): ArrayList<MusicFiles> {
        val tempAudioList = ArrayList<MusicFiles>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,//for path
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID

        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val album = cursor.getString(0)
                val title = cursor.getString(1)
                val duration = cursor.getString(2)
                val path = cursor.getString(3)
                val artist = cursor.getString(4)
                val id = cursor.getString(5)

                val musicFiles = MusicFiles(path, title, artist, album, duration,id)
                //
                Log.e("PRABHAT", "getAllAudio: ${path}   album ${album}", )
                tempAudioList.add(musicFiles)

            }
            cursor.close()


        }
        return tempAudioList
    }
    fun initViewPager(){
        viewPager.adapter = viewPagerAdapter
        tabLayout.let { tabLayout ->
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.getTabAt(position)?.select()
                }
            })

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        viewPager.currentItem = it.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle unselected tab
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Handle tab reselection
                    tab?.let {
                        viewPager.currentItem = it.position
                    }

                }
            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.getTabAt(position)?.select()
                }
            })

            //           tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            // Check for permission status again and handle it
            checkAndRequestPermissions()
            permissionDenied = false
        }
    }



}