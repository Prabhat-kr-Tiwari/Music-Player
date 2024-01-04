package com.example.musicplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.example.musicplayer.MainActivity.Companion.musicFiles
import com.example.musicplayer.databinding.ActivityPlayerBinding
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlayerActivity : AppCompatActivity(),  MediaPlayer.OnCompletionListener{
    lateinit var binding: ActivityPlayerBinding
    lateinit var song_name: TextView
    lateinit var artist_name: TextView
    lateinit var duration_played: TextView
    lateinit var duration_total: TextView

    lateinit var cover_art: ImageView
    lateinit var nextBtn: ImageView
    lateinit var prevBtn: ImageView
    lateinit var backBtn: ImageView
    lateinit var shuffleBtn: ImageView
    lateinit var repeatBtn: ImageView
    lateinit var playPauseBtn: FloatingActionButton

    var position = -1

    lateinit var playThread: Thread
    lateinit var prevThread: Thread
    lateinit var nextThread: Thread


    companion object {
        const val TAG = "PRABHAT"
        var listSongs = ArrayList<MusicFiles>()
        lateinit var uri: Uri
        var mediaPlayer: MediaPlayer = MediaPlayer()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        getIntentMethod()
        song_name.text = listSongs.get(position).title
        artist_name.text = listSongs.get(position).artist
        mediaPlayer.setOnCompletionListener(this)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {

                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    val mCurrentPosition = mediaPlayer.currentPosition / 1000
                    binding.seekBar.progress = mCurrentPosition
                    duration_played.text = formattedTime(mCurrentPosition)
                }
                Handler(Looper.getMainLooper()).postDelayed(this, 1000)
            }

        })


    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        playThreadBtn()
        prevThreadBtn()
        nextThreadBtn()
        super.onResume()

    }

    private fun nextThreadBtn() {

        Log.d(TAG, "playThreadBtn: ")
        nextThread = Thread(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: 0")
                binding.idNext.setOnClickListener {
                    Log.d(TAG, "run: ")
                    nextBtnClicked()
                }
            }
        }


        )
        nextThread.start()
    }

    private fun nextBtnClicked() {
        if(mediaPlayer.isPlaying){

            mediaPlayer.stop()
            mediaPlayer.release()
            position=((position+1)% listSongs.size)
            uri=Uri.parse(listSongs.get(position).path)
            mediaPlayer= MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text= listSongs.get(position).title
            artist_name.text= listSongs.get(position).artist
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition
                        duration_played.text = formattedTime(mCurrentPosition)
                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.baseline_pause_24)
            mediaPlayer.start()

        }else{

            mediaPlayer.stop()
            mediaPlayer.release()
            position=((position+1)% listSongs.size)
            uri=Uri.parse(listSongs.get(position).path)
            mediaPlayer= MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text= listSongs.get(position).title
            artist_name.text= listSongs.get(position).artist
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition
                        duration_played.text = formattedTime(mCurrentPosition)
                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })
            mediaPlayer.setOnCompletionListener(this)
            //to change imageview and run time setBackgroundResource()
            playPauseBtn.setBackgroundResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun prevThreadBtn() {
        Log.d(TAG, "playThreadBtn: ")
        prevThread = Thread(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: 0")
                binding.idPrev.setOnClickListener {
                    Log.d(TAG, "run: ")
                    prevBtnClicked()
                }
            }
        }


        )
        prevThread.start()


    }

    private fun prevBtnClicked() {

        if(mediaPlayer.isPlaying){

            mediaPlayer.stop()
            mediaPlayer.release()


            position = if ((position - 1) < 0) listSongs.size - 1 else position - 1

            uri=Uri.parse(listSongs.get(position).path)
            mediaPlayer= MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text= listSongs.get(position).title
            artist_name.text= listSongs.get(position).artist
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition
                        duration_played.text = formattedTime(mCurrentPosition)
                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.baseline_pause_24)
            mediaPlayer.start()

        }else{

            mediaPlayer.stop()
            mediaPlayer.release()
            position = if ((position - 1) < 0) listSongs.size - 1 else position - 1

            uri=Uri.parse(listSongs.get(position).path)
            mediaPlayer= MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            song_name.text= listSongs.get(position).title
            artist_name.text= listSongs.get(position).artist
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition
                        duration_played.text = formattedTime(mCurrentPosition)
                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })
            mediaPlayer.setOnCompletionListener(this)
            playPauseBtn.setBackgroundResource(R.drawable.baseline_play_arrow_24)
        }
    }

    private fun playThreadBtn() {
        Log.d(TAG, "playThreadBtn: ")
        playThread = Thread(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: 0")
                binding.playPause.setOnClickListener {
                    Log.d(TAG, "run: ")
                    playPauseBtnClicked()
                }
            }
        }


        )
        playThread.start()


    }

    private fun playPauseBtnClicked() {

        if (mediaPlayer.isPlaying) {

            Log.d("SONU", "playPauseBtnClicked: ")
            binding.playPause.setImageResource(R.drawable.baseline_play_arrow_24)
            mediaPlayer.pause()
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition
                        duration_played.text = formattedTime(mCurrentPosition)
                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })
        } else {

            playPauseBtn.setImageResource(R.drawable.baseline_pause_24)
            mediaPlayer.start()
            binding.seekBar.max = mediaPlayer.duration / 1000

            runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer.currentPosition / 1000
                        binding.seekBar.progress = mCurrentPosition

                    }
                    Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                }

            })

        }
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalout = ""
        var totalnew = ""
        val seconds: String = (mCurrentPosition % 60).toString()
        val minutes: String = (mCurrentPosition / 60).toString()
        totalout = "${minutes}:${seconds}"
        totalnew = "${minutes}:0${seconds}"

        if (seconds.length == 1) {
            return totalnew
        } else {
            return totalout
        }


    }

    private fun getIntentMethod() {
        position = intent.getIntExtra("position", -1)
        listSongs = musicFiles
        if (listSongs != null) {

            playPauseBtn.setImageResource(R.drawable.baseline_pause_24)
            uri = Uri.parse(listSongs.get(position).path)

        }
        if (mediaPlayer != null) {
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
        } else {

            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer.start()
        }
        binding.seekBar.max = (mediaPlayer.duration / 1000)
        metaData(uri)


    }

    private fun initViews() {
        song_name = binding.songName
        artist_name = binding.songArtist
        duration_played = binding.durationPlayed
        duration_total = binding.durationTotal
        cover_art = binding.coverArt
        nextBtn = binding.idNext
        prevBtn = binding.idPrev
        backBtn = binding.backBtn
        shuffleBtn = binding.shuffle
        repeatBtn = binding.idRepeatOff
        playPauseBtn = binding.playPause

    }

    private fun metaData(uri: Uri) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        val durationTotal = (listSongs.get(position).duration).toInt() / 1000
        binding.durationTotal.text = formattedTime(durationTotal)
        val art: ByteArray? = retriever.embeddedPicture
        val bitmap: Bitmap?

        if (art != null) {

            bitmap=BitmapFactory.decodeByteArray(art,0,art.size)
            imageAnimation(this,cover_art,bitmap!!)
            Palette.from(bitmap).generate(object :Palette.PaletteAsyncListener{
                override fun onGenerated(palette: Palette?) {

                    var swatch=palette?.dominantSwatch
                    if (swatch!=null){
                        val gradient=binding.imgViewGradient
                        val mContainer=binding.mContainer
                        gradient.setBackgroundResource(R.drawable.gradient_bg)
                        mContainer.setBackgroundResource(R.drawable.main_bg)
                        val gradientDrawable=GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(swatch.rgb,0x000000)
                        )
                        gradient.background=gradientDrawable

                        val gradientDrawableBg=GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(swatch.rgb,swatch.rgb)

                        )
                        mContainer.background=gradientDrawableBg
                        binding.songName.setTextColor(swatch.titleTextColor)
                        binding.songArtist.setTextColor(swatch.bodyTextColor)


                    }else{
                        val gradient=binding.imgViewGradient
                        val mContainer=binding.mContainer
                        gradient.setBackgroundResource(R.drawable.gradient_bg)
                        mContainer.setBackgroundResource(R.drawable.main_bg)
                        val gradientDrawable=GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xff000000.toInt(),0x000000)
                        )
                        gradient.background=gradientDrawable

                        val gradientDrawableBg=GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            intArrayOf(0xff000000.toInt(), 0xff000000.toInt())

                        )
                        mContainer.background=gradientDrawableBg
                        binding.songName.setTextColor(Color.WHITE)
                        binding.songArtist.setTextColor(Color.DKGRAY)

                    }
                }

            })

        } else {

            Glide.with(this).asBitmap().load(R.drawable.img).into(cover_art)

            val gradient=binding.imgViewGradient
            val mContainer=binding.mContainer
            gradient.setBackgroundResource(R.drawable.gradient_bg)
            mContainer.setBackgroundResource(R.drawable.main_bg)
            binding.songName.setTextColor(Color.WHITE)
            binding.songArtist.setTextColor(Color.DKGRAY)
        }


    }
    //fade out and fade in animation
    private fun imageAnimation(context: Context,imageView: ImageView,bitmap: Bitmap){

        val animOut=android.view.animation.AnimationUtils.loadAnimation(context,android.R.anim.fade_out)
        val animIn=android.view.animation.AnimationUtils.loadAnimation(context,android.R.anim.fade_in)
        animOut.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {

                Glide.with(context).load(bitmap).into(imageView)
                animIn.setAnimationListener(object :Animation.AnimationListener{
                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }
                })
                imageView.startAnimation(animIn)

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        imageView.startAnimation(animOut)

    }

    override fun onCompletion(p0: MediaPlayer?) {
        nextBtnClicked()
        if (mediaPlayer!=null){
            mediaPlayer= MediaPlayer.create(applicationContext,uri)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener(this)
        }
    }

}