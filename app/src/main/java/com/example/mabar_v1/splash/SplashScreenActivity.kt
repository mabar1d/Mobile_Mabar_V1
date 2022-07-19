package com.example.mabar_v1.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mabar_v1.R
import egolabsapps.basicodemine.videolayout.VideoLayout


class SplashScreenActivity : AppCompatActivity() {

    //var video = findViewById<VideoLayout>(R.id.videoLayout)
    var consLayout = findViewById<ConstraintLayout>(R.id.c_layout_video)
    var pathVideo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*video = VideoLayout(this)
        video.setGravity(VideoLayout.VGravity.centerCrop)
        video.setIsLoop(true)
        video.setSound(true)
        video.setPathOrUrl(pathVideo)
        consLayout.addView(video)*/

    }
}