package com.projach.comics_tracker.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.projach.comics_tracker.R
import com.projach.comics_tracker.domain.models.ComicModel

@Composable
fun ComicItem(
    comic: ComicModel,
    onComicClick: () -> Unit,
    onDeleteComic: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.clickable(onClick = onComicClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.no_image),
                contentDescription = "${comic.name} comic art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = comic.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = comic.writer,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(text = comic.issue, style = MaterialTheme.typography.bodyMedium)

                InfoRow(
                    first = comic.price?.let { R.drawable.sell_24px to it },
                    second = comic.publisher?.let { R.drawable.apartment_24px to it }
                )

                InfoRow(
                    first = comic.releaseDate?.let { R.drawable.calendar_month_24px to it },
                    second = comic.acquiredDate?.let { R.drawable.check_circle_24px to it }
                )
            }
            IconButton(
                onClick = onDeleteComic
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_24px),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    first: Pair<Int, String>?,
    second: Pair<Int, String>?
) {
    if (first == null && second == null) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        first?.let { InfoItem(it.first, it.second) }
        if (first != null && second != null) {
            VerticalDivider(Modifier.height(16.dp))
        }
        second?.let { InfoItem(it.first, it.second) }
    }
}

@Composable
private fun InfoItem(@DrawableRes icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = text, style = MaterialTheme.typography.bodySmall, maxLines = 1)
    }
}


@Composable
fun ComicItemSkeleton(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // cover image
            Box(
                Modifier
                    .width(100.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmer()
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SkeletonLine(width = 0.8f, height = 22.dp)  // title
                SkeletonLine(width = 0.5f, height = 14.dp)  // writer
                SkeletonLine(width = 0.2f, height = 14.dp)  // issue
                SkeletonLine(width = 0.7f, height = 12.dp)  // price | publisher
                SkeletonLine(width = 0.9f, height = 12.dp)  // dates
            }

            // delete button
            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
        }
    }
}

@Composable
private fun SkeletonLine(width: Float, height: Dp) {
    Box(
        Modifier
            .fillMaxWidth(width)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .shimmer()
    )
}

fun Modifier.shimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = -600f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "shimmerOffset"
    )
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)

    background(
        Brush.linearGradient(
            colors = listOf(base, highlight, base),
            start = Offset(offset, 0f),
            end = Offset(offset + 400f, 0f)
        )
    )
}