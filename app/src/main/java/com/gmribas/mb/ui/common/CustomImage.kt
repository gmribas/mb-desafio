package com.gmribas.mb.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.gmribas.mb.R
import com.gmribas.mb.ui.theme.SIZE_64

@Composable
fun CustomImage(
    imageUrl: String,
    teamName: String?,
    modifier: Modifier = Modifier,
    size: Dp = SIZE_64,
    clipShape: RoundedCornerShape = CircleShape
) {
    val context = LocalContext.current

    val imageRequest = remember(imageUrl, context) {
        if (imageUrl.isBlank()) {
            null
        } else {
            ImageRequest.Builder(context)
                .data(imageUrl)
                .build()
        }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (imageRequest != null) {
            AsyncImage(
                model = imageRequest,
                contentDescription = teamName?.let { stringResource(R.string.team_logo_description, it) },
                modifier = Modifier
                    .matchParentSize()
                    .clip(clipShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}
