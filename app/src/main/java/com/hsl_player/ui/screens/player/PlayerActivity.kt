package com.hsl_player.ui.screens.player

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import com.hsl_player.R
import com.hsl_player.dataClass.HslModel
import com.hsl_player.ui.screens.player.viewmodel.PlayerViewModel
import com.hsl_player.ui.theme.HSLPlayerTheme
import kotlinx.coroutines.delay

class PlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewContainer()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ViewContainer() {
    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent

    val hslDetail = intent!!.getSerializableExtra("hls") as HslModel

    val viewModel: PlayerViewModel = viewModel()
    viewModel.updateURL(hslDetail.url)

    HSLPlayerTheme {
        Scaffold(topBar = { Toolbar(hslDetail.name) }) { innerPadding ->
            val isPlaying: Boolean by viewModel.isPlaying.collectAsState()
            val isVisible: Boolean by viewModel.isVisible.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                VideoContainer(viewModel)

                PlayerControls(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    isPlaying = { isPlaying },
                    isVisible = { isVisible },
                    onPauseToggle = {
                        if (isPlaying) {
                            viewModel.pausePlayer()
                        } else {
                            viewModel.playPlayer()
                        }
                        viewModel.updateIsPlaying(isPlaying.not())
                        Thread.sleep(500)
                        viewModel.updateIsVisible(false)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: String) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = { context.findActivity()!!.finish() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
    )
}

@Composable
fun VideoContainer(viewModel: PlayerViewModel) {
    val context = LocalContext.current

    viewModel.initPlayer(context)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = viewModel.player
                useController = false
                setOnClickListener {
                    if (viewModel.isVisible.value){
                        viewModel.updateIsVisible(false)
                    }else{
                        viewModel.updateIsVisible(true)
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    isVisible: () -> Boolean,
    onPauseToggle: () -> Unit
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(modifier = modifier, visible = visible, enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = modifier.background(Color.Black.copy(alpha = 0.2f))) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(modifier = Modifier.size(40.dp), onClick = onPauseToggle) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        colorFilter = ColorFilter.tint(colorResource(id = R.color.teal_200)),
                        contentScale = ContentScale.Crop,
                        painter =
                        if (isVideoPlaying) {
                            painterResource(id = R.drawable.baseline_pause_40)
                        } else {
                            painterResource(id = R.drawable.baseline_play_arrow_40)
                        },
                        contentDescription = "Play/Pause"
                    )
                }
            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

