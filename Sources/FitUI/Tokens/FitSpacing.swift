import SwiftUI

// MARK: - 321Fit Spacing & Radius
// Source: design-tokens/tokens/spacing.json + components.json

public enum FitSpacing {
    public static let sp0: CGFloat = 0
    public static let sp1: CGFloat = 4
    public static let sp2: CGFloat = 8
    public static let sp3: CGFloat = 12
    public static let sp4: CGFloat = 16
    public static let sp5: CGFloat = 20
    public static let sp6: CGFloat = 24
    public static let sp7: CGFloat = 32
    public static let sp8: CGFloat = 40
    public static let sp9: CGFloat = 48
}

public enum FitRadius {
    public static let xs: CGFloat = 4
    public static let sm: CGFloat = 8
    public static let md: CGFloat = 12
    public static let lg: CGFloat = 16
    public static let full: CGFloat = 9999

    // Component-specific
    public static let button: CGFloat = 99
    public static let input: CGFloat = 12
    public static let card: CGFloat = 16
    public static let sheet: CGFloat = 16
    public static let settingsCard: CGFloat = 16
    public static let badge: CGFloat = 6
    public static let selectRow: CGFloat = 8
}

public enum FitSize {
    // Button (per feedback_spacing_typography — Apple HIG 50pt)
    public static let buttonLgHeight: CGFloat = 50   // footer primary CTA
    public static let buttonMdHeight: CGFloat = 44   // card-level CTA, tap-target min
    public static let buttonSmHeight: CGFloat = 40   // compact in sheets / cards
    public static let buttonHeight: CGFloat = buttonLgHeight   // legacy alias

    public static let tapMin: CGFloat = 44            // iOS HIG minimum tap target

    // Input
    public static let inputHeight: CGFloat = 56

    // Avatar (5 semantic sizes per spacing.json)
    public static let avatarXs: CGFloat = 24
    public static let avatarSm: CGFloat = 32
    public static let avatarMd: CGFloat = 40          // standard list avatar
    public static let avatarLg: CGFloat = 48          // event sheet / card avatar
    public static let avatarXl: CGFloat = 80          // profile hero

    // Navbar
    public static let navbarHeight: CGFloat = 56
    public static let navbarItemSize: CGFloat = 56

    // Icons
    public static let iconSm: CGFloat = 14
    public static let iconMd: CGFloat = 16
    public static let iconLg: CGFloat = 24

    // Header / toolbar
    public static let iconBtnSize: CGFloat = 32

    // Settings card
    public static let settingsCardIcon: CGFloat = 24
    public static let settingsCardChevron: CGFloat = 16

    // Select row
    public static let selectRowCheck: CGFloat = 22
    public static let selectRowDot: CGFloat = 10

    // Checkbox
    public static let checkboxSize: CGFloat = 28

    // Toggle
    public static let toggleWidth: CGFloat = 48
    public static let toggleHeight: CGFloat = 28
    public static let toggleThumb: CGFloat = 22

    // Stepper
    public static let stepperHeight: CGFloat = 48
    public static let stepperButton: CGFloat = 48

    // Toast/Snackbar
    public static let toastIcon: CGFloat = 16
    public static let snackbarDot: CGFloat = 6

    // Password eye
    public static let eyeIcon: CGFloat = 18
}
