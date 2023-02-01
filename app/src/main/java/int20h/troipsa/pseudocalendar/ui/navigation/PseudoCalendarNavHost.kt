package int20h.troipsa.pseudocalendar.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun PseudoCalendarNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Calendar.route
) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        Screen.Calendar,
        Screen.Contacts
    )

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    BottomNavigationItem(
                        selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == screen.route } == true,
                        icon = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = stringResource(id = screen.resourceId))
                        },
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Calendar.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calendar.route) { Calendar(navController) }
            composable(Screen.Contacts.route) { Contacts(navController) }
        }
    }
}

@Composable
fun Calendar(navController: NavHostController) {
    Column() {
        Text(text = stringResource(Screen.Calendar.resourceId))
    }
}

@Composable
fun Contacts(navController: NavHostController) {
    Column() {
        Text(text = stringResource(Screen.Contacts.resourceId))
    }
}
