package detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    assetId: String,
    assetDetailViewModel: AssetDetailViewModel = koinInject(),
    onBackClick: () -> Unit,
) {
    // Load the asset when the screen appears
    LaunchedEffect(assetId) {
        assetDetailViewModel.loadAsset(assetId)
    }

    val asset by assetDetailViewModel.asset.collectAsState()
    val uiEvents by assetDetailViewModel.uiEvents.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = asset?.title ?: "Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val event = uiEvents) {
            is ui.state.UIEvent.Loading -> {
                // Show a loading indicator
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ui.state.UIEvent.Error -> {
                // Show an error state
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Failed to load details.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { assetDetailViewModel.loadAsset(assetId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                if (asset != null) {
                    val selectedAsset = asset!!
                    // Display the asset details
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = selectedAsset.imageUrl,
                            contentDescription = selectedAsset.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Gray)
                                )
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Red)
                                )
                            },
                            contentScale = ContentScale.FillHeight
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(text = selectedAsset.longTitle ?: "No title", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(16.dp))
                        Text(text = selectedAsset.principalMaker ?: "No artist info", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(16.dp))
                        selectedAsset.productionPlaces
                            ?.takeIf { it.isNotEmpty() }
                            ?.forEach { place ->
                                Text(text = place, style = MaterialTheme.typography.bodyMedium)
                            }
                        Spacer(Modifier.height(16.dp))
                        selectedAsset.materials
                            ?.takeIf { it.isNotEmpty() }
                            ?.forEach { material ->
                                Text(text = material, style = MaterialTheme.typography.bodyMedium)
                            }
                    }
                }
            }
        }
    }
}