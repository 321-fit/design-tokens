
//
// DesignTokensAnimation.swift
//

// Do not edit directly
// Generated on Fri, 24 Apr 2026 19:46:27 GMT


import UIKit

public class DesignTokensAnimation {
    public static let animationDurationBase = 200ms /* Fade, slide, tap scale */
    public static let animationDurationFast = 150ms /* Hover, button press, minor state */
    public static let animationDurationInstant = 120ms /* Context menu pop-in, tiny feedback */
    public static let animationDurationMedium = 250ms /* Sheet slide-up, dialog */
    public static let animationDurationShimmer = 1400ms /* Skeleton shimmer pulse */
    public static let animationDurationSlow = 300ms /* Progress fill, expand/collapse */
    public static let animationDurationSpinner = 800ms /* Loading spinner rotation */
    public static let animationEasingIn = cubic-bezier(0.55, 0.055, 0.675, 0.19) /* UI in — exit animations */
    public static let animationEasingInOut = cubic-bezier(0.645, 0.045, 0.355, 1) /* Symmetrical — back-and-forth */
    public static let animationEasingLinear = linear /* Spinner, shimmer — constant motion */
    public static let animationEasingOut = cubic-bezier(0.25, 0.46, 0.45, 0.94) /* Default UI out — enter animations */
    public static let animationEasingStandard = ease /* CSS default ease — generic transitions */
}
