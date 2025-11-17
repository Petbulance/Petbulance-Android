package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate

/**
 * Bottom Navigation Bar
 *
 * @param selectedItem The currently selected item on the Bottom Navigation Bar
 * @param backgroundColor The background color of the Bottom Navigation Bar. Default is [Color.Transparent]
 * @param navController The instance of [NavController] used for navigation
 */
@Composable
fun BottomNavigationBar(
    selectedItem: CurrentBottomNav,
    backgroundColor: Color = Color.Transparent,
    navController: NavController
) {
    val navItemList = listOf(
        BottomNavInfo(
            label = "홈",
            iconResourceId = R.drawable.home,
            bottomNavType = CurrentBottomNav.HOME,
            onClicked = {
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        ),
        BottomNavInfo(
            label = "병원검색",
            iconResourceId = R.drawable.health_cross,
            bottomNavType = CurrentBottomNav.HOSPITAL,
            onClicked = {
                navController.safeNavigate(ScreenDestinations.HospitalSearch.route)
            }
        ),
        BottomNavInfo(
            label = "병원후기",
            iconResourceId = R.drawable.review,
            bottomNavType = CurrentBottomNav.REVIEW,
            onClicked = {
                /* TODO : navigate to review */
            }
        ),
        BottomNavInfo(
            label = "커뮤니티",
            iconResourceId = R.drawable.community,
            bottomNavType = CurrentBottomNav.COMMUNITY,
            onClicked = {
                /* TODO : navigate to community */
            }
        ),
        BottomNavInfo(
            label = "My",
            iconResourceId = R.drawable.profile,
            bottomNavType = CurrentBottomNav.PROFILE,
            onClicked = {
                navController.safeNavigate(ScreenDestinations.MyPage.route)
            }
        ),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(60.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        navItemList.forEachIndexed { _, item ->
            BottomNavItem(
                iconRes = painterResource(item.iconResourceId),
                itemLabel = item.label,
                isSelected = (selectedItem == item.bottomNavType),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        item.onClicked
                    },
                onClicked = item.onClicked
            )
        }
    }
}


@Composable
private fun BottomNavItem(
    iconRes: Painter,
    itemLabel: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClicked: () -> Unit
) {
    val selectedColor = PetbulanceTheme.colorScheme.icon.gnb.selected
    val unselectedColor = PetbulanceTheme.colorScheme.icon.gnb.default

    val iconColor = if (isSelected) selectedColor else unselectedColor

    Column(
        modifier = modifier.clickable { onClicked() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PetbulanceTheme.colorScheme.bg.frame.default),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = iconRes,
                contentDescription = itemLabel,
                tint = iconColor,
                modifier = Modifier
                    .size(28.dp)
                    .padding(bottom = spacingXXS)
            )
            Text(
                text = itemLabel,
                color = iconColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class BottomNavInfo(
    val label: String,
    val iconResourceId: Int,
    val bottomNavType: CurrentBottomNav,
    val onClicked: () -> Unit
)

enum class CurrentBottomNav {
    HOME, HOSPITAL, REVIEW, COMMUNITY, PROFILE
}

@Preview
@Composable
private fun BottomNavigationBarPreview1() {
    PetbulanceTheme {
        BottomNavigationBar(
            selectedItem = CurrentBottomNav.HOME,
            navController = rememberNavController()
        )
    }
}