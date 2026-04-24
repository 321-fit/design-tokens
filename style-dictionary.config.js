const StyleDictionary = require('style-dictionary');

// ============================================================
// CUSTOM TRANSFORMS
// ============================================================

// Add `px` suffix to numeric values in size-oriented categories.
// Skip if value is already a string (line-height 1.4, letter-spacing 0,
// font-weight 500) or already has a unit.
StyleDictionary.registerTransform({
  name: 'size/px-suffix',
  type: 'value',
  matcher: (prop) => {
    const pxCategories = ['spacing', 'radius', 'avatar', 'height', 'effect'];
    const fontSizeFields = prop.path.includes('size');
    const componentPxFields = prop.attributes.category === 'component' &&
      typeof prop.value === 'number' &&
      prop.value > 1; // skip opacity etc.

    if (prop.attributes.category === 'font') {
      // Only add px to `size` sub-property, not weight/lineHeight/letterSpacing
      return fontSizeFields && typeof prop.value === 'number';
    }
    if (pxCategories.includes(prop.attributes.category)) {
      return typeof prop.value === 'number';
    }
    if (componentPxFields) return true;
    return false;
  },
  transformer: (prop) => `${prop.value}px`,
});

// Convert line-height multiplier (1.35) to unitless (stay as-is, no change)
// Already handled by default; explicit no-op for clarity.

// ============================================================
// TRANSFORM GROUPS (extend built-ins)
// ============================================================

StyleDictionary.registerTransformGroup({
  name: 'css-fit',
  transforms: [
    'attribute/cti',
    'name/cti/kebab',
    'size/px-suffix',
    'color/css',
  ],
});

StyleDictionary.registerTransformGroup({
  name: 'ios-swift-fit',
  transforms: [
    'attribute/cti',
    'name/cti/pascal',
    'color/UIColorSwift',
    'content/swift/literal',
    'asset/swift/literal',
    'size/swift/remToCGFloat',
    'font/swift/literal',
  ],
});

StyleDictionary.registerTransformGroup({
  name: 'android-fit',
  transforms: [
    'attribute/cti',
    'name/cti/snake',
    'color/hex8android',
    'size/px-suffix',
  ],
});

// ============================================================
// CONFIG
// ============================================================

module.exports = {
  source: ['tokens/**/*.json'],
  platforms: {
    // iOS (Swift) — generated into FitUI package
    ios: {
      transformGroup: 'ios-swift',
      buildPath: 'swift/FitUI/Sources/FitUI/Tokens/Generated/',
      files: [
        {
          destination: 'DesignTokensColors.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensColors',
          filter: (token) => token.attributes.category === 'color',
        },
        {
          destination: 'DesignTokensTypography.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensTypography',
          filter: (token) => token.attributes.category === 'font',
        },
        {
          destination: 'DesignTokensSpacing.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensSpacing',
          filter: (token) =>
            token.attributes.category === 'spacing' ||
            token.attributes.category === 'radius' ||
            token.attributes.category === 'avatar' ||
            token.attributes.category === 'height' ||
            token.attributes.category === 'effect',
        },
        {
          destination: 'DesignTokensComponent.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensComponent',
          filter: (token) => token.attributes.category === 'component',
        },
        {
          destination: 'DesignTokensElevation.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensElevation',
          filter: (token) => token.attributes.category === 'elevation',
        },
        {
          destination: 'DesignTokensAnimation.swift',
          format: 'ios-swift/class.swift',
          className: 'DesignTokensAnimation',
          filter: (token) => token.attributes.category === 'animation',
        },
      ],
    },

    // Android (XML resources)
    android: {
      transformGroup: 'android-fit',
      buildPath: 'build/android/',
      prefix: 'fit',
      files: [
        {
          destination: 'colors.xml',
          format: 'android/colors',
          filter: (token) => token.attributes.category === 'color',
        },
        {
          destination: 'dimens.xml',
          format: 'android/dimens',
          filter: (token) =>
            token.attributes.category === 'spacing' ||
            token.attributes.category === 'radius' ||
            token.attributes.category === 'avatar' ||
            token.attributes.category === 'height' ||
            (token.attributes.category === 'font' && token.path.includes('size')),
        },
      ],
    },

    // CSS (prototypes, web) — drop-in replacement for fit-ui.css hand-rolled tokens
    css: {
      transformGroup: 'css-fit',
      buildPath: 'build/css/',
      prefix: 'fit',
      files: [
        {
          destination: 'tokens.css',
          format: 'css/variables',
          options: {
            outputReferences: true,
          },
        },
      ],
    },

    // JSON flat (for any consumer)
    json: {
      transformGroup: 'web',
      buildPath: 'build/',
      files: [
        {
          destination: 'tokens.json',
          format: 'json/flat',
        },
      ],
    },
  },
};
