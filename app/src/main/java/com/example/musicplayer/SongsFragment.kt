package com.example.musicplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.MainActivity.Companion.musicFiles
import com.example.musicplayer.databinding.FragmentSongsBinding

class SongsFragment : Fragment() {
    lateinit var binding: FragmentSongsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSongsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        if (!(musicFiles.size < 1)){

            binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())

            binding.recyclerView.adapter=MusicAdapter(requireContext(), musicFiles)
        }


    }



}