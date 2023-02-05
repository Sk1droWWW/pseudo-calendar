package int20h.troipsa.pseudocalendar.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import int20h.troipsa.pseudocalendar.ui.calendar.CalendarScreen
import int20h.troipsa.pseudocalendar.ui.contacts.ContactsScreen
import int20h.troipsa.pseudocalendar.ui.day_schedule.DayScheduleScreen
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.ui.event.EventScreen


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
                                painter = painterResource(id = screen.iconRes ?: R.drawable.ic_calendar),
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
            navController = navController,
            startDestination = Screen.Calendar.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    navController = navController,
                    navigateToDaySchedule = { day ->
                        navController.navigate(Screen.DaySchedule.withArgs(day.toString()))
                    }
                )
            }

            composable(Screen.Contacts.route) { ContactsScreen(navController) }

            composable(
                route = Screen.DaySchedule.withArgsFormat(Screen.DaySchedule.epochDay),
                arguments = listOf(
                    navArgument(Screen.DaySchedule.epochDay) { type = NavType.LongType }
                )
            ) { navBackStackEntry ->
                DayScheduleScreen(
                    epochDay = navBackStackEntry.arguments?.getLong(Screen.DaySchedule.epochDay),
                    navigateToEvent = { eventId ->
                        navController.navigate(Screen.Event.withArgs(eventId.toString()))
                    },
                    popBackStack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Event.withArgsFormat(Screen.Event.eventId),
                arguments = listOf(
                    navArgument(Screen.Event.eventId) { type = NavType.IntType }
                )
            ) { navBackStackEntry ->
                EventScreen(
                    eventId = navBackStackEntry.arguments?.getInt(Screen.Event.eventId),
                    popBackStack = { navController.popBackStack() }
                )
            }
        }
    }
}
