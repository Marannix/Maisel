package com.maisel.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
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
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
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
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (uiState.isThemeDialogShown) {
                    ThemeDialog(
                        uiState = uiState,
                        uiAction = uiEvents
                    )
                }
                AccountSection(uiState)
                GeneralSection()
                ThemeSection(
                    uiState = uiState,
                    uiAction = uiEvents
                )
                BuildInformationSection()
            }
        }
    )
}

@Composable
private fun AccountSection(uiState: SettingContract.UiState) {
    SectionTitle("Account")
    SectionCard(
        item = {
            SectionItem(
                profilePicture = uiState.user?.profilePicture,
                imageColor = null,
                title = uiState.user?.username ?: "",
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
    uiState: SettingContract.UiState,
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
                    color = MaterialTheme.extendedColors.cardOnBackgroundColor
                )
                Text(
                    style = MaterialTheme.typography.subtitle2,
                    text = uiState.currentAppTheme.name,
                    color = MaterialTheme.extendedColors.cardOnBackgroundColor
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
    profilePicture: String? = null,
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
        profilePicture?.let {
            Image(
                painter = rememberImagePainter(
                    data = profilePicture ?: R.drawable.ic_son_goku,
                    builder = {
                        crossfade(true)
                        //placeholder(R.drawable.ic_son_goku) //TODO: Placeholder
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 5.dp)
                    .padding(5.dp)
            )
        }

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
    uiState: SettingContract.UiState,
    uiAction: (SettingContract.UiEvents) -> Unit,
) {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            try {
                uiState.appThemes.first { it.appTheme == uiState.currentAppTheme.appTheme }
            } catch (exception: NoSuchElementException) {
                uiState.appThemes.first()
            })
    }

    ThemeAlertDialog(
        title = stringResource(id = R.string.dialog_choose_theme),
        dismissText = stringResource(id = R.string.dialog_back_primary),
        shape = RoundedCornerShape(16.dp),
        confirmText = stringResource(id = R.string.dialog_back_secondary),
        onConfirmClick = { uiAction(SettingContract.UiEvents.OnDialogConfirmed(selectedOption.appTheme)) },
        onDismissRequest = { uiAction(SettingContract.UiEvents.OnDialogDismissed) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        selectedOption = selectedOption.name,
        onOptionSelected = onOptionSelected,
        radioOptions = uiState.appThemes
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
