package list

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import kotlinx.coroutines.flow.filterNotNull
import model.Asset
import org.koin.compose.koinInject



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListScreen(
    viewModel: AssetListViewModel = koinInject(),
    onAssetClick: (Asset) -> Unit
) {
    val assets by viewModel.assets.collectAsState()
    val errorState by viewModel.error.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }

    // Trigger loadNextPage when the user scrolls near the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .collect { visibleIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                // If close to the end and not currently loading, load next page
                if (visibleIndex >= totalItems - 3 && !isSyncing && assets.isNotEmpty()) {
                    viewModel.loadMore()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artworks") },
                actions = {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchArtworks(it)
                        },
                        placeholder = { Text("Search art...") },
                        singleLine = true
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (errorState != null && assets.isEmpty()) {
                EmptyState("Failed to load results") {
                    viewModel.synchronizeAssets()
                }
                return@Column
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(assets.size) { index ->
                    val asset = assets[index]
                    AssetItem(asset, onClick = onAssetClick)
                }

                // Show placeholders if syncing more items
                if (isSyncing) {
                    // Show three placeholder items
                    items(3) {
                        ShimmerPlaceholderItem()
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String, onRetry: (() -> Unit)? = null) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message)
        Spacer(Modifier.height(16.dp))
        if (onRetry != null) {
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun ShimmerPlaceholderItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerBox(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            ShimmerBox(modifier = Modifier.fillMaxWidth().height(16.dp))
            ShimmerBox(modifier = Modifier.width(100.dp).height(16.dp))
        }
    }
}


@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f)
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}

@Composable
fun AssetItem(asset: Asset, onClick: (Asset) -> Unit) {
    val artistName = asset.principalMaker
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable { onClick(asset) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = asset.imageUrl,
            contentDescription = asset.title,
            modifier = Modifier
                .width(80.dp)
                .height(80.dp),
            loading = { ShimmerBox() },
            error = { Box(modifier = Modifier.background(Color.Red)) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = asset.title, style = MaterialTheme.typography.bodyLarge)
            if (artistName != null) {
                Text(
                    text = artistName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}