package com.wikicoding.composetodolist

import android.Manifest
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.wikicoding.composespeechtotext.VoiceToTextParser
import com.wikicoding.composetodolist.navigation.Navigation
import com.wikicoding.composetodolist.ui.theme.ComposeTodoListTheme
import com.wikicoding.composetodolist.uiscreens.HomeScreen
import com.wikicoding.composetodolist.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {
    val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var canRecord by remember {
                        mutableStateOf(false)
                    }

                    val recordAudioLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            canRecord = isGranted
                        }
                    )

                    LaunchedEffect(key1 = recordAudioLauncher) {
                        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }

                    Navigation(voiceToTextParser)
                }
            }
        }
    }
}