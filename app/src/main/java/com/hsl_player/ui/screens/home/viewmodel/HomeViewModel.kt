package com.hsl_player.ui.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import com.hsl_player.dataClass.HslModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _hslDetails = MutableStateFlow(
        listOf(
            HslModel(
                "fMP4 m3u8",
                "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
            ),
            HslModel("Live Akamai m3u8",
                "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"),
            HslModel("MP4 m3u8",
                "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.mp4/.m3u8")
        )
    )
    val hslDetails: StateFlow<List<HslModel>> = _hslDetails.asStateFlow()
}