import SwiftUI

// MARK: - FitInput
// Height: 56px, Radius: 12px, Font: 16px
// States: default, focus, error, disabled
// Dark bg: #111213, Light bg: #E4E6E7

public struct FitInput: View {
    // Null when the placeholder already names the field. The rule the screens follow is that
    // the label or the placeholder names it, never both.
    let label: String?
    @Binding var text: String
    var placeholder: String = ""
    var isSecure: Bool = false
    var isError: Bool = false
    var errorText: String? = nil
    var keyboardType: UIKeyboardType = .default
    /// What the field holds, for the system's password manager. Mirrors Compose's
    /// `FitInputContent`; on iOS it becomes `textContentType`.
    var content: UITextContentType? = nil
    var submitLabel: SubmitLabel = .done

    @Environment(\.fitTheme) private var theme
    @FocusState private var isFocused: Bool

    public init(
        _ label: String? = nil,
        text: Binding<String>,
        placeholder: String = "",
        isSecure: Bool = false,
        isError: Bool = false,
        errorText: String? = nil,
        keyboardType: UIKeyboardType = .default,
        content: UITextContentType? = nil,
        submitLabel: SubmitLabel = .done
    ) {
        self.label = label
        self._text = text
        self.placeholder = placeholder
        self.isSecure = isSecure
        self.isError = isError
        self.errorText = errorText
        self.keyboardType = keyboardType
        self.content = content
        self.submitLabel = submitLabel
    }

    public var body: some View {
        VStack(alignment: .leading, spacing: FitSpacing.sp2) {
            // Label
            if let label = label {
                Text(label)
                    .font(FitFont.body1)
                    .foregroundColor(isError ? FitColors.error : theme.textSecondary)
            }

            // Input field
            HStack(spacing: FitSpacing.sp2) {
                if isSecure {
                    SecureField(placeholder, text: $text)
                        .font(FitFont.body1)
                        .foregroundColor(theme.textPrimary)
                        .textContentType(content)
                        .keyboardType(keyboardType)
                        .submitLabel(submitLabel)
                        .focused($isFocused)
                } else {
                    TextField(placeholder, text: $text)
                        .font(FitFont.body1)
                        .foregroundColor(theme.textPrimary)
                        .textContentType(content)
                        .keyboardType(keyboardType)
                        .submitLabel(submitLabel)
                        .focused($isFocused)
                }
            }
            .padding(.horizontal, FitSpacing.sp3)
            .frame(height: FitSize.inputHeight)
            .background(theme.inputBg)
            .clipShape(RoundedRectangle(cornerRadius: FitRadius.input))
            .overlay(
                RoundedRectangle(cornerRadius: FitRadius.input)
                    .stroke(borderColor, lineWidth: borderWidth)
            )

            // Error text
            if let errorText, isError {
                Text(errorText)
                    .font(FitFont.caption)
                    .foregroundColor(FitColors.error)
            }
        }
    }

    private var borderColor: Color {
        if isError { return FitColors.error }
        if isFocused { return theme.focusBorder }
        return .clear
    }

    private var borderWidth: CGFloat {
        (isError || isFocused) ? 1 : 0
    }
}
