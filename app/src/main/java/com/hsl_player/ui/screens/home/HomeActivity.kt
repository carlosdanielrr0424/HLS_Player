package com.hsl_player.ui.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hsl_player.R
import com.hsl_player.dataClass.HslModel
import com.hsl_player.ui.screens.home.viewmodel.HomeViewModel
import com.hsl_player.ui.screens.player.PlayerActivity
import com.hsl_player.ui.theme.HSLPlayerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
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
fun ViewContainer(){
    val viewModel: HomeViewModel = viewModel()
    val hslDetails: List<HslModel> by viewModel.hslDetails.collectAsState()

    HSLPlayerTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ){
                Text(text = "HLS_Player", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 25.dp), fontSize = 25.sp, fontStyle = FontStyle.Italic)

                RecyclerView(hslDetails)
            }
        }
    }
}

@Composable
fun UserCard(hslDetail: HslModel) {
    val context = LocalContext.current

    ElevatedCard (
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp).fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra("hls", hslDetail)
            }
            context.startActivity(intent)
        }
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Image(
                painter = painterResource(id = R.drawable.baseline_cast_24),
                contentDescription = "image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(RoundedCornerShape(CornerSize(6.dp)))
                    .align(alignment = Alignment.CenterVertically)
            )

            Text(text = hslDetail.name, modifier = Modifier.padding(10.dp, 20.dp))
        }
    }
}

@Composable
fun RecyclerView(hslDetails: List<HslModel>) {
    LazyColumn {
        items(items = hslDetails) {
            UserCard(it)
        }
    }
}