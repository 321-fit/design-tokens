package com.fit321.fitui.tokens

import androidx.compose.ui.unit.dp

/**
 * Spacing tokens — Tailwind half-step scale (2 / 4 / 6 / 8 / 10 / 12 /
 * 14 / 16 / 18 / 20 / 24 / 28 / 32 / 40 / 48).
 */
object FitSpacing {
    val sp0   = 0.dp
    val sp0_5 = 2.dp
    val sp1   = 4.dp
    val sp1_5 = 6.dp
    val sp2   = 8.dp
    val sp2_5 = 10.dp
    val sp3   = 12.dp
    val sp3_5 = 14.dp
    val sp4   = 16.dp
    val sp4_5 = 18.dp
    val sp5   = 20.dp
    val sp6   = 24.dp
    val sp7   = 28.dp
    val sp8   = 32.dp
    val sp9   = 40.dp
    val sp10  = 48.dp
}

/**
 * Border-radius tokens — extended scale (4 / 6 / 8 / 10 / 12 / 14 /
 * 16 / 20 / 28 / full).
 */
object FitRadius {
    val r4   = 4.dp
    val r6   = 6.dp
    val r8   = 8.dp
    val r10  = 10.dp
    val r12  = 12.dp
    val r14  = 14.dp
    val r16  = 16.dp
    val r20  = 20.dp
    val r28  = 28.dp
    val full = 9999.dp

    // Semantic aliases
    val xs   = r4
    val sm   = r8
    val md   = r12
    val lg   = r16

    // Component-specific
    val button       = 99.dp
    val input        = r12
    val card         = r16
    val sheet        = r16
    val settingsCard = r16
    val badge        = r6
    val selectRow    = r8
}

/**
 * Component sizes — avatar / button / navbar / icons / etc.
 */
object FitSize {
    // Button (Apple HIG 50dp)
    val buttonLgHeight = 50.dp
    val buttonMdHeight = 44.dp
    val buttonSmHeight = 40.dp

    val tapMin = 44.dp

    // Input
    val inputHeight = 56.dp

    // Avatar (5 semantic sizes)
    val avatarXs = 24.dp
    val avatarSm = 32.dp
    val avatarMd = 40.dp
    val avatarLg = 48.dp
    val avatarXl = 80.dp

    // Navbar
    val navbarHeight = 56.dp
    val navbarItemSize = 56.dp

    // Icons
    val iconSm = 14.dp
    val iconMd = 16.dp
    val iconLg = 24.dp

    val iconBtnSize = 32.dp

    // Settings card
    val settingsCardIcon = 24.dp
    val settingsCardChevron = 16.dp

    // Select row
    val selectRowCheck = 22.dp
    val selectRowDot = 10.dp

    // Checkbox
    val checkboxSize = 28.dp

    // Toggle
    val toggleWidth = 48.dp
    val toggleHeight = 28.dp
    val toggleThumb = 22.dp

    // Stepper
    val stepperHeight = 48.dp
    val stepperButton = 48.dp

    // Toast/Snackbar
    val toastIcon = 16.dp
    val snackbarDot = 6.dp

    // Password eye
    val eyeIcon = 18.dp
}
