package com.maisel.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.maisel.R
import com.maisel.compose.ui.components.dialog.Dialogs.ThemeAlertDialog
import com.maisel.compose.ui.theme.extendedColors

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsContent(
        navHostController = navHostController,
        uiState = uiState,
        uiEvents = viewModel::onUiEvent,
    )
}

@Composable
fun SettingsContent(
    navHostController: NavHostController,
    uiState: SettingContract.UiState,
    uiEvents: (SettingContract.UiEvents) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.body1,
                        text = "Settings",
                        color = MaterialTheme.colors.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.navigateUp()
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back Arrow")
                    }
                },
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(color = MaterialTheme.colors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
            ) {
                if (uiState.isThemeDialogShown) {
                    ThemeDialog(
                        navHostController = navHostController,
                        uiAction = uiEvents
                    )
                }
                AccountSection()
                GeneralSection()
                ThemeSection(uiEvents)
                BuildInformationSection()
            }
        }
    )
}

@Composable
private fun AccountSection() {
    SectionTitle("Account")
    SectionCard(
        item = {
            SectionItem(
                // drawableRes = R.drawable.ic_star,
                imageColor = null,
                title = "Alpha",
                subTitle = "Manage your account",
                onClick = {

                }
            )
        }
    )
}

@Composable
private fun GeneralSection() {
    SectionTitle("General")

    SectionCard(
        item = {
            SectionItem(
                //  drawableRes = R.drawable.ic_notifications,
                title = stringResource(id = R.string.notification),
                subTitle = stringResource(id = R.string.notification_subtitle),
                onClick = {

                }
            )
            SectionItem(
                //   drawableRes = R.drawable.ic_heart,
                imageColor = null,
                title = stringResource(id = R.string.help),
                subTitle = stringResource(id = R.string.help_subtitle),
                onClick = {

                }
            )
            SectionItem(
                drawableRes = null,
                // imageVector = Icons.Filled.Share,
                title = stringResource(id = R.string.help),
                subTitle = stringResource(id = R.string.help_subtitle),
                onClick = {

                }
            )
        },
    )
}

@Composable
private fun ThemeSection(
    uiAction: (SettingContract.UiEvents) -> Unit,
) {
    SectionCard(
        item = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        uiAction(SettingContract.UiEvents.ThemeClicked)
                    }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "Theme",
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = "System default",
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    )

}

@Composable
private fun BuildInformationSection() {
    BuildInformationSectionItem(
        string = stringResource(id = R.string.app_version, "1.0.0"),
    )
}

@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        modifier = Modifier.padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        style = MaterialTheme.typography.body1,
        text = title,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
private fun SectionItem(
    @DrawableRes drawableRes: Int? = null,
    imageVector: ImageVector? = null,
    title: String,
    subTitle: String,
    imageColor: Color? = MaterialTheme.colors.onBackground,
    onClick: () -> Unit?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        drawableRes?.let {
            Image(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 16.dp
                    )
                    .size(16.dp),
                painter = painterResource(id = drawableRes),
                contentDescription = "",
                colorFilter = imageColor?.let {
                    ColorFilter.tint(imageColor)
                })
        }

        if (drawableRes == null) {
            imageVector?.let {
                Image(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp,
                            start = 16.dp
                        )
                        .size(16.dp),
                    imageVector = imageVector,
                    contentDescription = "",
                    colorFilter = imageColor?.let {
                        ColorFilter.tint(imageColor)
                    }
                )
            }
        }

        Column {
            SectionText(
                title = title,
                subTitle = subTitle
            )
        }
    }
}

@Composable
private fun SectionText(
    title: String,
    subTitle: String
) {
    Text(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 16.dp,
            ),
        style = MaterialTheme.typography.body1,
        text = title,
        color = MaterialTheme.colors.onBackground
    )
    Text(
        modifier = Modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 16.dp
            ),
        style = MaterialTheme.typography.subtitle2,
        text = subTitle,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
private fun BuildInformationSectionItem(
    string: String,
    textColor: Color = MaterialTheme.colors.onBackground,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.body1,
            text = string,
            color = textColor,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ThemeDialog(
    navHostController: NavHostController,
    uiAction: (SettingContract.UiEvents) -> Unit,
) {
    ThemeAlertDialog(
        dismissText = stringResource(id = R.string.dialog_back_primary),
        confirmText = stringResource(id = R.string.dialog_back_secondary),
        onConfirmClick = { navHostController.navigateUp() },
        onDismissRequest = { uiAction(SettingContract.UiEvents.OnDialogDismissed) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        radioOptions = listOf(
            stringResource(id = R.string.system_default),
            stringResource(id = R.string.light),
            stringResource(id = R.string.dark)
        ) //TODO: Extract this to ui-state
    )
}

@Composable
private fun SectionCard(
    item: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .background(color = MaterialTheme.colors.background),
        backgroundColor = MaterialTheme.extendedColors.cardBackgroundColor,
        contentColor = MaterialTheme.extendedColors.cardOnBackgroundColor,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            item()
        }
    }
}
