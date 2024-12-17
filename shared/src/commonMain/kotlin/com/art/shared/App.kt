package com.art.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import detail.DetailScreen
import ui.theme.ArtAppTheme
import favorite.FavoriteScreen
import list.AssetListScreen
import ui.theme.Gray95

@Composable
fun App() {
    CompositionLocalProvider {
        ArtAppTheme {
            NavHostMain()
        }
    }
}

@Composable
private fun NavHostMain(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = Modifier
            .fillMaxSize()
            .background(Gray95),
    ) {
        composable(route = "list") {
            AssetListScreen(
                onAssetClick = { asset ->
                    navController.navigate("details/${asset.id}") {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = "details/{assetId}",
            arguments = listOf(navArgument("assetId") { type = NavType.StringType })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: return@composable
            DetailScreen(
                assetId = assetId,
                onBackClick = navController::popBackStack
            )
        }

        composable(route = "favorite") {
            FavoriteScreen(
                onAssetClick = { asset ->
                    navController.navigate("details/${asset.id}") {
                        launchSingleTop = true
                    }
                },
                onBack = navController::popBackStack
            )
        }
    }
}