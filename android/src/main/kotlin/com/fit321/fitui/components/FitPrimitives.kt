package com.fit321.fitui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fit321.designtokens.R
import com.fit321.fitui.theme.LocalFitTheme
import com.fit321.fitui.tokens.FitColors
import com.fit321.fitui.tokens.FitFont
import com.fit321.fitui.tokens.FitRadius
import com.fit321.fitui.tokens.FitSize
import com.fit321.fitui.tokens.FitSpacing

// ============================================================================
// FitIconBtn — circular 32dp icon-only button
// ============================================================================

enum class FitIconBtnColor { Primary, Brand, Error, Success }

/**
 * Plate style. [Filled] is the default 32dp circle on `surface.high`. [Ghost] drops the
 * plate — for buttons that sit on an already-busy surface (a sheet header next to a
 * descriptor and a status pill), where a filled circle competes with the content it
 * belongs to. Mirrors `.fit-sheet-menu-btn` in the prototype kit.
 */
enum class FitIconBtnStyle { Filled, Ghost }

/**
 * How large the circle is, and how large the glyph inside it.
 *
 * [Sm] is the header affordance — back, menu, refresh. [Lg] is a choice the screen is
 * asking for rather than an action tucked into a corner: the provider circles on the auth
 * screens are the case it was added for. The glyph is not a fixed fraction of the plate —
 * the small one carries a heavier ratio so it stays legible at 32.
 */
enum class FitIconBtnSize(val box: Dp, val glyph: Dp) {
    Sm(FitSize.iconBtnSize, FitSize.iconMd),
    Lg(60.dp, 26.dp)
}

@Composable
fun FitIconBtn(
    icon: ImageVector,
    color: FitIconBtnColor = FitIconBtnColor.Primary,
    tintedBg: Boolean = false,
    style: FitIconBtnStyle = FitIconBtnStyle.Filled,
    size: FitIconBtnSize = FitIconBtnSize.Sm,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = FitIconBtn(
    color = color,
    tintedBg = tintedBg,
    style = style,
    size = size,
    modifier = modifier,
    onClick = onClick,
) { tint, size ->
    Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(size))
}

/**
 * Icon button whose glyph the caller draws — see the note on [FitChip].
 *
 * The slot is handed the tint and size the button would have used, so a drawable passed in
 * still follows [color] and still sits at the right size. A caller that needs a different
 * colour can ignore the tint, which is what a host with its own semantics for the glyph
 * ends up doing until the two agree.
 */
@Composable
fun FitIconBtn(
    color: FitIconBtnColor = FitIconBtnColor.Primary,
    tintedBg: Boolean = false,
    style: FitIconBtnStyle = FitIconBtnStyle.Filled,
    size: FitIconBtnSize = FitIconBtnSize.Sm,
    background: Color? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable (tint: Color, size: Dp) -> Unit
) {
    val theme = LocalFitTheme.current
    val iconColor = when (color) {
        // Header icons carry the same weight as the back chevron so the header reads as one
        // group — `.fit-icon-btn { color: var(--fit-text-primary) }`.
        FitIconBtnColor.Primary -> theme.textPrimary
        FitIconBtnColor.Brand -> FitColors.brandPrimary
        FitIconBtnColor.Error -> FitColors.error
        FitIconBtnColor.Success -> FitColors.success
    }
    val bgColor = when {
        // A plate colour the caller states wins: the tinted variants below are computed
        // alphas, and a host with a `bg.<status>-subtle` token has the value this file
        // should have been using in the first place.
        background != null -> background
        style == FitIconBtnStyle.Ghost -> Color.Transparent
        tintedBg -> when (color) {
            FitIconBtnColor.Error -> FitColors.error.copy(alpha = 0.10f)
            FitIconBtnColor.Brand -> FitColors.brandPrimary.copy(alpha = 0.10f)
            FitIconBtnColor.Success -> FitColors.success.copy(alpha = 0.12f)
            FitIconBtnColor.Primary -> theme.surfaceHigh
        }
        // Dark draws the translucent plate the prototype blurs over the photo behind it
        // (`rgba(117,126,135,0.3)`); light keeps the opaque surface (`.fit-light .fit-icon-btn`).
        // The blur itself has no cheap Compose equivalent — a backdrop filter would mean
        // rendering the layer underneath — so the plate carries the effect on its own.
        theme === FitColors.Theme.dark -> IconBtnPlateDark
        else -> theme.surfaceHigh
    }

    Box(
        modifier = modifier
            .size(size.box)
            .clip(CircleShape)
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon(iconColor, size.glyph)
    }
}

private val IconBtnPlateDark = Color(0xFF757E87).copy(alpha = 0.3f)

// ============================================================================
// FitAvatar — initials in 5 sizes, circle or rounded rect
// ============================================================================

enum class FitAvatarSize(val px: Dp, val fontSp: Int) {
    Xs(FitSize.avatarXs, 10),
    Sm(FitSize.avatarSm, 12),
    Md(FitSize.avatarMd, 14),
    Lg(FitSize.avatarLg, 16),
    Xl(FitSize.avatarXl, 28)
}

enum class FitAvatarShape { Circle, Rect10 }

/**
 * What the avatar stands for.
 *
 * [Face] is a person: initials, covered by their photo when there is one. [Group] is a
 * conversation or cohort that has no single owner — it carries a glyph on a tinted plate
 * instead, so `initials` and `imageUrl` are ignored.
 */
enum class FitAvatarKind { Face, Group }

@Composable
fun FitAvatar(
    initials: String,
    size: FitAvatarSize = FitAvatarSize.Md,
    bg: Brush? = null,
    shape: FitAvatarShape = FitAvatarShape.Circle,
    isPaid: Boolean = false,
    imageUrl: String? = null,
    textColor: Color? = null,
    fontWeight: FontWeight = FontWeight.Medium,
    kind: FitAvatarKind = FitAvatarKind.Face,
    badge: FitAvatarBadge = FitAvatarBadge.None,
    modifier: Modifier = Modifier
) = FitAvatarImpl(
    initials = initials,
    diameter = size.px,
    fontSize = size.fontSp.sp(),
    fontWeight = fontWeight,
    bg = bg,
    shape = shape,
    isPaid = isPaid,
    imageUrl = imageUrl,
    textColor = textColor,
    kind = kind,
    badge = badge,
    modifier = modifier
)

/**
 * Avatar at a size the scale does not carry.
 *
 * Prefer [FitAvatarSize] — it is the scale the screens are drawn against. This overload
 * exists because real layouts do land on sizes between the steps (44, 56, 64), and the
 * alternative to offering them is a consumer rebuilding the avatar to change one number,
 * which is how the initials, the fallback and the paid state drift apart.
 *
 * Initials default to 0.36 of the diameter. The scale's own steps run 0.42 · 0.38 · 0.35 ·
 * 0.33 · 0.35, averaging 0.365, so this sits closer to the scale as a whole than the ratio
 * of any single step does.
 */
@Composable
fun FitAvatar(
    initials: String,
    size: Dp,
    bg: Brush? = null,
    shape: FitAvatarShape = FitAvatarShape.Circle,
    isPaid: Boolean = false,
    imageUrl: String? = null,
    textColor: Color? = null,
    fontSize: TextUnit = (size.value * 0.36f).sp,
    fontWeight: FontWeight = FontWeight.Medium,
    kind: FitAvatarKind = FitAvatarKind.Face,
    badge: FitAvatarBadge = FitAvatarBadge.None,
    modifier: Modifier = Modifier
) = FitAvatarImpl(
    initials = initials,
    diameter = size,
    fontSize = fontSize,
    fontWeight = fontWeight,
    bg = bg,
    shape = shape,
    isPaid = isPaid,
    imageUrl = imageUrl,
    textColor = textColor,
    kind = kind,
    badge = badge,
    modifier = modifier
)

@Composable
private fun FitAvatarImpl(
    initials: String,
    diameter: Dp,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    bg: Brush?,
    shape: FitAvatarShape,
    isPaid: Boolean,
    imageUrl: String?,
    textColor: Color?,
    kind: FitAvatarKind,
    badge: FitAvatarBadge,
    modifier: Modifier
) {
    if (badge == FitAvatarBadge.None) {
        FitAvatarPlate(
            initials = initials,
            diameter = diameter,
            fontSize = fontSize,
            fontWeight = fontWeight,
            bg = bg,
            shape = shape,
            isPaid = isPaid,
            imageUrl = imageUrl,
            textColor = textColor,
            kind = kind,
            modifier = modifier
        )
        return
    }
    // The plate clips to its shape, so a badge drawn inside it would be shaved by the
    // circle. The wrapper is unclipped and only as large as the plate, which keeps the
    // badge whole while the avatar still measures as one avatar.
    Box {
        FitAvatarPlate(
            initials = initials,
            diameter = diameter,
            fontSize = fontSize,
            fontWeight = fontWeight,
            bg = bg,
            shape = shape,
            isPaid = isPaid,
            imageUrl = imageUrl,
            textColor = textColor,
            kind = kind,
            modifier = modifier
        )
        FitAvatarBadgeChip(badge = badge, modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
private fun FitAvatarPlate(
    initials: String,
    diameter: Dp,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    bg: Brush?,
    shape: FitAvatarShape,
    isPaid: Boolean,
    imageUrl: String?,
    textColor: Color?,
    kind: FitAvatarKind,
    modifier: Modifier
) {
    val shapeValue = when (shape) {
        FitAvatarShape.Circle -> CircleShape
        FitAvatarShape.Rect10 -> RoundedCornerShape(10.dp)
    }
    // Group has its own defaults rather than its own hardcoded colours: a cohort tile is
    // brand-tinted unless the screen says otherwise, and screens do — a faceless client
    // placeholder is a plain surface plate with the same glyph on it.
    val plate = bg ?: if (kind == FitAvatarKind.Group) {
        SolidColor(LocalFitTheme.current.bgBrandSubtle)
    } else {
        FitColors.brandGradient
    }
    val ink = textColor ?: if (kind == FitAvatarKind.Group) FitColors.brandSecondary else Color.White
    Box(
        modifier = modifier
            .size(diameter)
            // Settled participants fade whole, background included — `.fit-participant.paid
            // .fit-avatar { opacity: .5 }`. Fading only the content would leave a photo
            // half-transparent over an opaque brand gradient, which tints the face.
            .alpha(if (isPaid) 0.5f else 1f)
            .clip(shapeValue)
            .background(plate, shapeValue),
        contentAlignment = Alignment.Center
    ) {
        if (kind == FitAvatarKind.Group) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_group),
                contentDescription = null,
                tint = ink,
                modifier = Modifier.size(diameter * 0.5f)
            )
            return@Box
        }
        // No photo and no name yet — the picker before anything is filled in. A blank
        // plate reads as broken, so the avatar falls back to a face.
        if (initials.isBlank() && imageUrl.isNullOrBlank()) {
            Icon(
                painter = painterResource(R.drawable.ic_fit_user),
                contentDescription = null,
                tint = ink,
                modifier = Modifier.size(diameter * 0.51f)
            )
            return@Box
        }
        // Initials stay underneath rather than behind a conditional: they are the
        // placeholder while the photo loads and the fallback if it never does, which
        // is what AsyncImage does on the SwiftUI side.
        Text(
            initials.take(2).uppercase(),
            color = ink,
            style = FitFont.body1.copy(fontSize = fontSize, fontWeight = fontWeight)
        )
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize().clip(shapeValue)
            )
        }
    }
}

/**
 * What the chip in an avatar's bottom corner offers.
 *
 * A closed set rather than a content slot: every screen that hand-rolled this pattern
 * wanted one of these two, and each one that supplied its own glyph is how the ring, the
 * plate and the icon size drifted apart in the first place.
 */
enum class FitAvatarBadge { None, Edit, Add }

/**
 * The chip itself — a ring in the screen background around a surface plate, so it reads as
 * detached from the photo underneath rather than painted onto it.
 */
@Composable
private fun FitAvatarBadgeChip(
    badge: FitAvatarBadge,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    ringWidth: Dp = 2.dp
) {
    val theme = LocalFitTheme.current
    val glyph = when (badge) {
        FitAvatarBadge.Edit -> R.drawable.ic_fit_pencil
        FitAvatarBadge.Add -> R.drawable.ic_fit_plus
        FitAvatarBadge.None -> return
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(theme.screenBg)
            .padding(ringWidth)
            .clip(CircleShape)
            .background(theme.surfaceHigh),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(glyph),
            contentDescription = null,
            tint = theme.textPrimary,
            modifier = Modifier.size(size * 0.43f)
        )
    }
}

// ============================================================================
// FitCheckbox — 22dp square with check
// ============================================================================

@Composable
fun FitCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    label: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val rowModifier = if (onCheckedChange != null) {
        modifier.clickable(enabled = enabled) { onCheckedChange(!checked) }
    } else {
        modifier
    }
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FitSpacing.sp3)
    ) {
        Box(
            modifier = Modifier
                .size(FitSize.checkboxSize)
                .clip(RoundedCornerShape(6.dp))
                .background(if (checked) FitColors.Teal.t600 else Color.Transparent)
                .border(2.dp, if (checked) FitColors.Teal.t600 else theme.textTertiary, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(ImageVector.vectorResource(R.drawable.ic_fit_check), null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
        if (label != null) {
            Text(label, style = FitFont.body1, color = theme.textPrimary)
        }
    }
}

// ============================================================================
// FitToggle — iOS-style switch 48×28
// ============================================================================

@Composable
fun FitToggle(
    isOn: Boolean,
    onChange: (Boolean) -> Unit,
    label: String? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    val thumbOffset by animateDpAsState(
        if (isOn) FitSize.toggleWidth - FitSize.toggleThumb - 3.dp else 3.dp,
        label = "toggle-thumb"
    )

    Row(
        modifier = modifier.clickable(enabled = enabled) { onChange(!isOn) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) {
            Text(label, style = FitFont.body1, color = theme.textPrimary)
            Spacer(Modifier.weight(1f))
        }
        Box(
            modifier = Modifier
                .size(FitSize.toggleWidth, FitSize.toggleHeight)
                .clip(CircleShape)
                .background(if (isOn) FitColors.Teal.t500 else theme.surfaceHigher),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                Modifier
                    .offset(x = thumbOffset)
                    .size(FitSize.toggleThumb)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

// ============================================================================
// FitStepper — ±48 buttons + value in middle
// ============================================================================

@Composable
fun FitStepper(
    value: Int,
    onChange: (Int) -> Unit,
    min: Int = 1,
    max: Int = 100,
    unit: String? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalFitTheme.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(FitRadius.md))
            .height(FitSize.stepperHeight)
    ) {
        StepperButton(Icons.Default.Remove, enabled = value > min) { if (value > min) onChange(value - 1) }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(theme.surfaceHigh),
            contentAlignment = Alignment.Center
        ) {
            Text(
                unit?.let { "$value $it" } ?: value.toString(),
                style = FitFont.button1,
                color = theme.textPrimary
            )
        }
        StepperButton(Icons.Default.Add, enabled = value < max) { if (value < max) onChange(value + 1) }
    }
}

@Composable
private fun StepperButton(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    val theme = LocalFitTheme.current
    Box(
        modifier = Modifier
            .size(FitSize.stepperButton, FitSize.stepperHeight)
            .background(theme.surfaceHigher)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = theme.textPrimary.copy(alpha = if (enabled) 1f else 0.4f))
    }
}

// ============================================================================
// FitBadge — 12dp text pill in 13 color variants
// ============================================================================

enum class FitBadgeStyle {
    Group, Personal, Full, Joined, Pending, Special, Error,
    Neutral, Crm, Danger, Info, Success, Accent, Cash, Card
}

@Composable
fun FitBadge(
    text: String,
    style: FitBadgeStyle,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    bordered: Boolean = false,
    compact: Boolean = false,
) {
    val theme = LocalFitTheme.current
    val (fg, bg) = colorsFor(style, theme)
    val shape = RoundedCornerShape(if (compact) FitRadius.r4 else FitRadius.badge)
    val textStyle = if (compact) {
        FitFont.captionMicro.copy(fontWeight = FontWeight.Medium, letterSpacing = 0.3.sp)
    } else {
        FitFont.caption.copy(fontWeight = FontWeight.Medium)
    }
    Row(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .then(if (bordered) Modifier.border(BorderStroke(1.dp, fg.copy(alpha = 0.3f)), shape) else Modifier)
            .padding(horizontal = if (compact) 6.dp else 10.dp, vertical = if (compact) 2.dp else 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 3.dp else 4.dp),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(if (compact) 10.dp else 12.dp))
        }
        Text(text, style = textStyle, color = fg, maxLines = 1, softWrap = false)
    }
}

private fun colorsFor(s: FitBadgeStyle, theme: FitColors.Theme): Pair<Color, Color> = when (s) {
    FitBadgeStyle.Group    -> FitColors.Blue.b500 to FitColors.Blue.b500.copy(alpha = 0.15f)
    FitBadgeStyle.Personal -> theme.textSecondary to theme.surfaceHigh
    FitBadgeStyle.Full,
    FitBadgeStyle.Danger   -> FitColors.error to FitColors.error.copy(alpha = 0.12f)
    FitBadgeStyle.Joined,
    FitBadgeStyle.Success  -> FitColors.Green.g500 to FitColors.Teal.t500.copy(alpha = 0.12f)
    FitBadgeStyle.Pending  -> FitColors.Yellow.y400 to FitColors.Yellow.y400.copy(alpha = 0.12f)
    FitBadgeStyle.Special,
    FitBadgeStyle.Info     -> FitColors.Blue.b500 to FitColors.Blue.b500.copy(alpha = 0.12f)
    FitBadgeStyle.Error    -> FitColors.error to FitColors.error.copy(alpha = 0.12f)
    FitBadgeStyle.Neutral  -> theme.textTertiary to theme.surfaceHigher
    FitBadgeStyle.Crm      -> FitColors.Teal.t500 to FitColors.Teal.t500.copy(alpha = 0.15f)
    FitBadgeStyle.Accent   -> FitColors.brandPrimary to FitColors.brandPrimary.copy(alpha = 0.12f)
    FitBadgeStyle.Cash     -> FitColors.Teal.t500 to FitColors.Teal.t500.copy(alpha = 0.14f)
    FitBadgeStyle.Card     -> FitColors.externalStripePurple to FitColors.externalStripePurple.copy(alpha = 0.14f)
}

// Helper for Int → .sp
private fun Int.sp() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
