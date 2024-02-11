package com.wikicoding.composetodolist.navigation

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.wikicoding.composetodolist.R

@Composable
fun AppBarView(title: String, onBackNavClicked: () -> Unit = {}) {
    val navigationIcon: (@Composable () -> Unit)? =
        /** code to hide the back button if we're in the homepage **/
        if (!title.contains("Todo List")) {
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
            }
        } else {
            null
        }

    /** same as toolbar **/
    TopAppBar(
        title = {
            Text(
                text = title, color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        elevation = 3.dp,
        navigationIcon = navigationIcon,
        backgroundColor = MaterialTheme.colorScheme.background
    )
}