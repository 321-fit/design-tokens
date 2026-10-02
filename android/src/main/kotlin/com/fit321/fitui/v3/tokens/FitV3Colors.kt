package com.fit321.fitui.v3.tokens

import androidx.compose.ui.graphics.Color
import com.fit321.fitui.tokens.FitColors

enum class FitV3Look { Tinted, Light }

data class FitV3CanvasSpec(
    val baseStops: List<Pair<Float, Color>>,
    val glowStops: List<Pair<Float, Color>>,
    val glowCenterX: Float,
    val glowCenterY: Float,
    val glowRadius: Float,
)

data class FitV3Palette(
    val look: FitV3Look,
    val canvas: FitV3CanvasSpec,
    val surface: Color,
    val surfaceHairline: Color,
    val surfaceLifted: Boolean,
    val raised: Color,
    val raisedOnCanvas: Color,
    val stackedFace: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val divider: Color,
    val ctaStops: List<Pair<Float, Color>>,
    val ctaLabel: Color,
    val ctaHighlight: Color,
    val ctaShadow: Color,
    val secondaryFill: Color,
    val secondaryBorder: Color,
    val material: Color,
    val materialEdge: Color,
    val materialScrim: Color,
    val bar: Color,
    val pressed: Color,
    val disabled: Color,
    val selectedFill: Color,
    val selectedBorder: Color,
    val segmentedSelected: Color,
    val dashed: Color,
    val circleOutline: Color,
    val actionPrimaryLabel: Color,
    val strip: Color,
    val pickRing: Color,
)

object FitV3Colors {

    val tinted = FitV3Palette(
        look = FitV3Look.Tinted,
        canvas = FitV3CanvasSpec(
            baseStops = listOf(
                0.00f to Color(0xFF0A5F68),
                0.22f to Color(0xFF084653),
                0.48f to Color(0xFF062E3B),
                0.72f to Color(0xFF05202A),
                1.00f to Color(0xFF04161D),
            ),
            glowStops = listOf(
                0.00f to Color(0x9900BED7),
                0.55f to Color(0x1A05E0A6),
                1.00f to Color.Transparent,
            ),
            glowCenterX = 0.5f,
            glowCenterY = -0.08f,
            glowRadius = 1.2f,
        ),
        surface = Color(0x47000000),
        surfaceHairline = Color(0x0FFFFFFF),
        surfaceLifted = false,
        raised = Color(0x24FFFFFF),
        raisedOnCanvas = Color(0x21FFFFFF),
        stackedFace = Color(0xFF2B5F6A),
        textPrimary = Color(0xFFFFFFFF),
        textSecondary = Color(0xBDFFFFFF),
        textTertiary = Color(0x85FFFFFF),
        divider = Color(0x1AFFFFFF),
        ctaStops = listOf(
            0.00f to Color(0xFFFFFFFF),
            0.45f to Color(0xFFF3F7F8),
            1.00f to Color(0xFFE2EBEE),
        ),
        ctaLabel = Color(0xFF12161A),
        ctaHighlight = Color(0xF2FFFFFF),
        ctaShadow = Color(0x73000000),
        secondaryFill = Color(0x1AFFFFFF),
        secondaryBorder = Color(0x2EFFFFFF),
        material = Color(0xCC05181F),
        materialEdge = Color(0x24FFFFFF),
        materialScrim = Color(0x73020C10),
        bar = Color(0xE004161D),
        pressed = Color(0x0FFFFFFF),
        disabled = Color(0x1AFFFFFF),
        selectedFill = Color(0x2405E0A6),
        selectedBorder = Color(0x7305E0A6),
        segmentedSelected = Color(0x29FFFFFF),
        dashed = Color(0x38FFFFFF),
        circleOutline = Color(0x4DFFFFFF),
        actionPrimaryLabel = Color(0xFFFFFFFF),
        strip = Color(0x2E000000),
        pickRing = FitColors.Gray.g500,
    )

    val light = FitV3Palette(
        look = FitV3Look.Light,
        canvas = FitV3CanvasSpec(
            baseStops = listOf(
                0f to Color(0xFFF2F2F7),
                1f to Color(0xFFF2F2F7),
            ),
            glowStops = listOf(
                0.00f to Color(0x3800A7D0),
                0.45f to Color(0x1205E0A6),
                1.00f to Color.Transparent,
            ),
            glowCenterX = 0.5f,
            glowCenterY = 0f,
            glowRadius = 1.2f,
        ),
        surface = FitColors.Gray.white,
        surfaceHairline = Color.Transparent,
        surfaceLifted = true,
        raised = FitColors.Gray.g100,
        raisedOnCanvas = Color(0xBFFFFFFF),
        stackedFace = FitColors.Gray.g200,
        textPrimary = FitColors.Gray.g900,
        textSecondary = FitColors.Gray.g600,
        textTertiary = FitColors.Gray.g500,
        divider = FitColors.Gray.g200,
        ctaStops = listOf(
            0f to FitColors.Blue.b500,
            1f to FitColors.Teal.t500,
        ),
        ctaLabel = FitColors.Gray.white,
        ctaHighlight = Color.Transparent,
        ctaShadow = Color(0x1F000000),
        secondaryFill = FitColors.Gray.white,
        secondaryBorder = FitColors.Gray.g200,
        material = Color(0xFFF2F2F7),
        materialEdge = Color.Transparent,
        materialScrim = Color(0x59000000),
        bar = Color(0xFFF2F2F7),
        pressed = Color(0x0D3C3C43),
        disabled = FitColors.Gray.g200,
        selectedFill = FitColors.Teal.t600.copy(alpha = 0.16f),
        selectedBorder = FitColors.Teal.t600,
        segmentedSelected = FitColors.Gray.white,
        dashed = FitColors.Gray.g300,
        circleOutline = FitColors.Gray.g500,
        actionPrimaryLabel = FitColors.brandPrimary,
        strip = Color(0x0D3C3C43),
        pickRing = FitColors.Gray.g400,
    )

    fun of(look: FitV3Look): FitV3Palette = when (look) {
        FitV3Look.Tinted -> tinted
        FitV3Look.Light -> light
    }
}
