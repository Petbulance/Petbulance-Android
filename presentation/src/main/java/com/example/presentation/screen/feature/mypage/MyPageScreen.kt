package com.example.presentation.screen.feature.mypage

import android.Manifest.permission_group.PHONE
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.feature.profile.UserProfile
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulancePrimitives
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.DefaultRoundedCorner
import com.example.presentation.component.ui.LargeRoundedCorner
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.BasicImageBox
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.common.LoginRequiredDialog
import com.example.presentation.component.ui.iconSizeMedium
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.utils.checkUserLoginStatus
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun MyPageScreen(
    navController: NavController,
    argument: MyPageArgument,
    data: MyPageData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    var isLoginRequiredDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val userProfile = data.userProfile
    val currentAppVersion = data.currentAppVersion
    val latestVersion = data.latestAppVersion

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "마이페이지",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = false,
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Drawable(R.drawable.search),
                            { /* TODO : search logic */ }
                        ),
                        Pair(
                            IconResource.Drawable(R.drawable.notificiation),
                            { /* TODO : notification logic */ }
                        )
                    )
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.PROFILE,
                navController = navController
            )
        },
        containerColor = PetbulanceTheme.colorScheme.bg.frame.subtle
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyPageScreenContents(
                userProfile = userProfile,
                currentAppVersion = currentAppVersion,
                latestVersion = latestVersion,
                onChangeProfileButtonClicked = {
                    /* TODO : Navigate to Profile Edit Screen */
                },
                onLoginButtonClicked = {
                    navController.safeNavigate(ScreenDestinations.Login.route)
                },
                onFavoriteReviewButtonClicked = {
                    checkUserLoginStatus(
                        onLoggedIn = { /* TODO : Navigate to Fav Review screen */ },
                        onNotLoggedIn = { isLoginRequiredDialogVisible = true }
                    )
                },
                onRecentArticleButtonClicked = {},
                onWrittenArticleButtonClicked = {},
                onNavigateToStore = { navigateToStore(context) }
            )
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }

    if (isLoginRequiredDialogVisible) {
        LoginRequiredDialog(
            onLoginClicked = {
                navController.safeNavigate(ScreenDestinations.Login.route)
            },
            onSkipClicked = { isLoginRequiredDialogVisible = false }
        )
    }

    BackHandler { navController.safeNavigate(ScreenDestinations.Home.route) }
}

@Composable
private fun MyPageScreenContents(
    userProfile: UserProfile?,
    currentAppVersion: String,
    latestVersion: String,
    onChangeProfileButtonClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    onFavoriteReviewButtonClicked: () -> Unit,
    onRecentArticleButtonClicked: () -> Unit,
    onWrittenArticleButtonClicked: () -> Unit,
    onNavigateToStore: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileCard(
            userProfile = userProfile,
            onChangeProfileButtonClicked = onChangeProfileButtonClicked,
            onLoginButtonClicked = onLoginButtonClicked
        )
        MyPageLinkCard(
            onFavoriteReviewButtonClicked = onFavoriteReviewButtonClicked,
            onRecentArticleButtonClicked = onRecentArticleButtonClicked,
            onWrittenArticleButtonClicked = onWrittenArticleButtonClicked
        )

        OptionCard(
            title = "사용자 설정",
            elements = listOf(
                OptionCardItemData(
                    icon = IconResource.Vector(Icons.Outlined.NotificationsActive),
                    name = "알림 설정",
                    onClicked = { /* TODO : Navigate to Notification Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.eyes),
                    name = "관심사 선택",
                    onClicked = { /* TODO : Navigate to Interest Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Vector(Icons.AutoMirrored.Outlined.Logout),
                    name = "로그인 계정 관리",
                    onClicked = { /* TODO : Navigate to Manage Account Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.security),
                    name = "권한",
                    onClicked = { /* TODO : Navigate to Permission Screen */ }
                ),
            )
        )

        OptionCard(
            title = "작성글 관리",
            elements = listOf(
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.review),
                    name = "영수증 후기 관리",
                    onClicked = { /* TODO : Navigate to Review Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.docs_24), /* TODO : 아이콘 다름 */
                    name = "커뮤니티 게시글 관리",
                    onClicked = { /* TODO : Navigate to Community article Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Vector(Icons.Outlined.Forum), /* TODO : 아이콘 다름 */
                    name = "댓글 관리",
                    onClicked = { /* TODO : Navigate to CommentScreen */ }
                ),
            )
        )

        OptionCard(
            title = "고객 지원",
            elements = listOf(
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.bullhorn),
                    name = "공지사항",
                    onClicked = { /* TODO : Navigate to Notice Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Vector(Icons.Default.HeadsetMic), /* TODO : 아이콘 다름 */
                    name = "고객센터",
                    onClicked = { /* TODO : Navigate to Community article Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.hospital),
                    name = "광고문의",
                    onClicked = { /* TODO : Navigate to advertisement Screen */ }
                ),
                OptionCardItemData(
                    icon = IconResource.Vector(Icons.Outlined.Info),
                    name = "약관 및 정책",
                    onClicked = { /* TODO : Navigate to Policies Screen */ }
                ),
            ),
            isLatestAppVersionVisible = true,
            currentVersion = currentAppVersion,
            latestVersion = latestVersion,
            navigateToStore = { onNavigateToStore() }
        )
    }
}

@Composable
private fun ProfileCard(
    userProfile: UserProfile?,
    onChangeProfileButtonClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                PetbulanceTheme.colorScheme.bg.frame.default,
                DefaultRoundedCorner
            )
            .padding(spacingMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (userProfile != null) {
                BasicImageBox(
                    modifier = Modifier.clip(shape = RoundedCornerShape(150.dp)),
                    size = 60.dp,
                    galleryUri = userProfile.profileImageUrl.toUri(),
                    errorImageResource = R.drawable.default_profile,
                    placeholderImageResource = R.drawable.default_profile
                )
                Column {
                    Text(
                        text = userProfile.nickname,
                        style = MaterialTheme.typography.bodyMedium.emp(),
                        color = PetbulanceTheme.colorScheme.text.primary,
                        softWrap = true,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = userProfile.email,
                        style = MaterialTheme.typography.labelSmall.emp(),
                        color = PetbulanceTheme.colorScheme.text.caption,
                        softWrap = true,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Column {
                    Text(
                        text = "로그인 해주세요",
                        style = MaterialTheme.typography.bodyMedium.emp(),
                        color = PetbulanceTheme.colorScheme.text.primary,
                        softWrap = true,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "회원가입까지 단 3초!",
                        style = MaterialTheme.typography.labelSmall.emp(),
                        color = PetbulanceTheme.colorScheme.text.caption,
                        softWrap = true,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (userProfile != null) {
            BasicButton(
                text = "프로필 수정",
                size = BasicButtonSize.XS,
                buttonType = BasicButtonType.SECONDARY,
                onClicked = { onChangeProfileButtonClicked() },
            )
        } else {
            BasicButton(
                text = "로그인 하기",
                size = BasicButtonSize.XS,
                buttonType = BasicButtonType.SECONDARY,
                onClicked = { onLoginButtonClicked() },
            )
        }
    }
}

@Composable
private fun MyPageLinkCard(
    onFavoriteReviewButtonClicked: () -> Unit,
    onRecentArticleButtonClicked: () -> Unit,
    onWrittenArticleButtonClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                PetbulanceTheme.colorScheme.bg.frame.default,
                DefaultRoundedCorner
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        MyPageLinkCardItem(
            iconRes = IconResource.Vector(Icons.Outlined.FavoriteBorder),
            text = "저장한 리뷰",
            onClicked = onFavoriteReviewButtonClicked
        )
        VerticalDivider(
            color = PetbulanceTheme.colorScheme.border.verySubtle,
            thickness = 1.dp,
            modifier = Modifier.height(12.dp)
        )
        MyPageLinkCardItem(
            iconRes = IconResource.Vector(Icons.Outlined.History),
            text = "최근 본 글",
            onClicked = onRecentArticleButtonClicked
        )
        VerticalDivider(
            color = PetbulanceTheme.colorScheme.border.verySubtle,
            thickness = 1.dp,
            modifier = Modifier.height(12.dp)
        )
        MyPageLinkCardItem(
            iconRes = IconResource.Drawable(R.drawable.mode_edit),
            text = "작성한 글",
            onClicked = onWrittenArticleButtonClicked
        )
    }
}

@Composable
private fun MyPageLinkCardItem(
    iconRes: IconResource,
    text: String,
    onClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingXXS),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClicked() }
    ) {
        BasicIcon(
            iconResource = iconRes,
            contentDescription = "$iconRes",
            size = iconSizeMedium,
            tint = PetbulanceTheme.colorScheme.icon.dark
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.emp(),
            color = PetbulanceTheme.colorScheme.text.primary,
            softWrap = true,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun OptionCard(
    title: String,
    elements: List<OptionCardItemData>,
    isLatestAppVersionVisible: Boolean = false,
    currentVersion: String = "",
    latestVersion: String = "",
    navigateToStore: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                PetbulanceTheme.colorScheme.bg.frame.default,
                LargeRoundedCorner
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.emp(),
            color = PetbulanceTheme.colorScheme.text.primary,
            modifier = Modifier.padding(vertical = spacingXXS)
        )
        elements.forEach { elem ->
            OptionCardItem(elem)
        }
        if (isLatestAppVersionVisible) {
            UpdateToLatestVersionItem(
                data = OptionCardItemData(
                    icon = IconResource.Drawable(R.drawable.version),
                    name = "최신버전 업데이트",
                    onClicked = {
                        if (currentVersion != latestVersion) {
                            navigateToStore()
                        }
                    }
                ),
                currentVersion = currentVersion,
                latestVersion = latestVersion
            )
        }
    }
}

data class OptionCardItemData(
    val icon: IconResource,
    val name: String,
    val onClicked: () -> Unit
)

@Composable
private fun OptionCardItem(
    data: OptionCardItemData
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { data.onClicked() }
            .padding(vertical = spacingSmall)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicIcon(
                iconResource = data.icon,
                contentDescription = data.name,
                size = iconSizeMs,
                tint = PetbulanceTheme.colorScheme.icon.dark
            )
            Text(
                text = data.name,
                style = MaterialTheme.typography.bodyMedium,
                color = PetbulanceTheme.colorScheme.text.primary,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Move",
            tint = PetbulanceTheme.colorScheme.icon.basic
        )
    }
}

@Composable
private fun UpdateToLatestVersionItem(
    data: OptionCardItemData,
    currentVersion: String,
    latestVersion: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { data.onClicked() }
            .padding(vertical = spacingSmall)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicIcon(
                    iconResource = data.icon,
                    contentDescription = data.name,
                    size = iconSizeMs,
                    tint = PetbulanceTheme.colorScheme.icon.dark
                )
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PetbulanceTheme.colorScheme.text.primary,
                )
            }
            Text(
                text = "최신버전: $latestVersion",
                style = MaterialTheme.typography.labelMedium,
                color = PetbulanceTheme.colorScheme.text.caption,
            )
        }

        Text(
            text = currentVersion,
            style = MaterialTheme.typography.bodySmall,
            color = PetbulancePrimitives.Positive.p500,
        )
    }
}

private fun navigateToStore(context: Context) {
    val packageName = context.packageName
    try {
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$packageName".toUri()
        )
        context.startActivity(intent)
    }
}

@Preview(name = "Pixel 9", device = PIXEL_9)
@Preview(name = "Pixel 7 Pro (Wide)", device = Devices.PIXEL_7_PRO)
@Preview(name = "Pixel 5 (Normal)", device = Devices.PIXEL_5)
@Preview(name = "Pixel 3 (Small)", device = Devices.PIXEL_3)
@Preview(name = "Default", device = PHONE)
@Composable
private fun MyPageScreenPreview() {
    PetbulanceTheme {
        MyPageScreen(
            navController = rememberNavController(),
            argument = MyPageArgument(
                intent = { },
                state = MyPageState.Init,
                event = MutableSharedFlow()
            ),
            data = MyPageData.empty().copy(userProfile = null)
        )
    }
}