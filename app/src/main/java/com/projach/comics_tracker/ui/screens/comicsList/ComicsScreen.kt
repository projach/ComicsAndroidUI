package com.projach.comics_tracker.ui.screens.comicsList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.projach.comics_tracker.R
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.ui.composables.ComicItem
import com.projach.comics_tracker.ui.composables.ComicItemSkeleton
import com.projach.comics_tracker.ui.composables.Loader

@Composable
fun ComicsScreen(viewModel: ComicsViewModel) {
    val comics = viewModel.comics.collectAsLazyPagingItems()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    Content(
        comics = comics,
        isLoading = uiState.value.isLoading,
        onDeleteComic = {
            viewModel.onIntent(ComicsIntent.DeleteComic(id = it, onDelete = { comics.refresh() }))
        },
        onComicClick = {
            viewModel.onIntent(ComicsIntent.ComicClick(it))
        },
        onLogoutClick = {
            viewModel.onIntent(ComicsIntent.Logout)
        },
        onAddNewComic = {
            viewModel.onIntent(ComicsIntent.AddNewComic)
        }
    )
}

@Composable
fun Content(
    comics: LazyPagingItems<ComicModel>,
    isLoading: Boolean,
    onDeleteComic: (id: Int) -> Unit,
    onComicClick: (id: Int) -> Unit,
    onLogoutClick: () -> Unit,
    onAddNewComic: () -> Unit
) {
    Loader(isLoading)

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.comics_my_comics),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onLogoutClick) {
                    Icon(painterResource(R.drawable.logout_24px), contentDescription = null)
                }
            }

            Spacer(Modifier.height(6.dp))

            //TODO(add search box after https://github.com/projach/ComicsDB/issues/33 is finished to get the search from server)

            ComicsList(
                comics = comics,
                onDeleteComic = onDeleteComic,
                onComicClick = onComicClick,
                onAddNewComic = onAddNewComic
            )
        }
        if (comics.itemCount != 0) {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(40.dp)
            ) {
                Icon(painter = painterResource(R.drawable.add_24px), contentDescription = null)
            }
        }
    }
}

@Composable
private fun ComicsList(
    comics: LazyPagingItems<ComicModel>,
    onDeleteComic: (id: Int) -> Unit,
    onComicClick: (id: Int) -> Unit,
    onAddNewComic: () -> Unit
) {
    val refresh = comics.loadState.refresh
    val isEmpty = comics.itemCount == 0

    var isPulling by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(refresh) {
        when (refresh) {
            is LoadState.Loading -> Unit
            is LoadState.Error -> {
                showError = true; isPulling = false
            }

            is LoadState.NotLoading -> {
                showError = false; isPulling = false
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isPulling,
        onRefresh = {
            isPulling = true
            comics.refresh()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            isEmpty && showError -> MessageContent(
                icon = painterResource(R.drawable.cloud_off_24px),
                actionIcon = painterResource(R.drawable.refresh_24px),
                title = stringResource(R.string.comics_fetch_error_title),
                description = stringResource(R.string.comics_fetch_error_desc),
                actionLabel = stringResource(R.string.comics_retry),
                isLoading = refresh is LoadState.Loading,
                onAction = comics::refresh
            )

            isEmpty && refresh is LoadState.Loading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) { skeletons(6) }
            }

            isEmpty -> MessageContent(
                icon = painterResource(R.drawable.menu_book_24px),
                actionIcon = painterResource(R.drawable.add_24px),
                title = stringResource(R.string.comics_empty_title),
                description = stringResource(R.string.comics_empty_desc),
                actionLabel = stringResource(R.string.comics_add_comic),
                isLoading = false,
                onAction = onAddNewComic
            )

            else -> ItemsList(comics, onDeleteComic, onComicClick)
        }
    }
}

@Composable
private fun ItemsList(
    comics: LazyPagingItems<ComicModel>,
    onDeleteComic: (id: Int) -> Unit,
    onComicClick: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = comics.itemCount,
            key = comics.itemKey { it.id },
            contentType = comics.itemContentType { "comic" }
        ) { index ->
            comics[index]?.let { comic ->
                ComicItem(
                    comic = comic,
                    modifier = Modifier.fillMaxWidth(),
                    onDeleteComic = { onDeleteComic(comic.id) },
                    onComicClick = { onComicClick(comic.id) }
                )
            }
        }

        when (comics.loadState.append) {
            is LoadState.Error -> item {
                Button(onClick = comics::retry) {
                    Text(stringResource(R.string.comics_retry))
                }
            }

            LoadState.Loading -> skeletons(2)
            else -> Unit
        }
    }
}

private fun LazyListScope.skeletons(count: Int) {
    items(count) { ComicItemSkeleton(Modifier.fillMaxWidth()) }
}

@Composable
private fun MessageContent(
    icon: Painter,
    actionIcon: Painter,
    title: String,
    description: String,
    actionLabel: String,
    isLoading: Boolean,
    onAction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        FilledTonalButton(onClick = onAction, enabled = !isLoading) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Icon(
                    painter = actionIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(actionLabel)
            }
        }
    }
}