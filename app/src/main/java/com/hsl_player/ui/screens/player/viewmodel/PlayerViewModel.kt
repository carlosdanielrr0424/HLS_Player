package com.hsl_player.ui.screens.player.viewmodel

import android.content.Context
import android.media.session.MediaController
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel : ViewModel() {

    private val _urlState = MutableStateFlow("")
    val urlState: StateFlow<String> = _urlState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible.asStateFlow()

    lateinit var player: ExoPlayer

    fun initPlayer(context: Context){
        player = ExoPlayer.Builder(context).build()
        player.setMediaItem(MediaItem.fromUri(urlState.value))
        player.prepare()
    }

    fun releasePlayer(){
        player.release()
    }

    fun playPlayer(){
        player.play()
    }

    fun pausePlayer(){
        player.pause()
    }

    fun updateURL(newUrl: String){
        _urlState.value = newUrl
    }

    fun updateIsPlaying(newValue: Boolean){
        _isPlaying.value = newValue
    }

    fun updateIsVisible(newValue: Boolean){
        _isVisible.value = newValue
    }


}