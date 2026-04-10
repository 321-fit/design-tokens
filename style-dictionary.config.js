module.exports = {
  source: ['tokens/**/*.json'],
  platforms: {
    // iOS (Swift)
    ios: {
      transformGroup: 'ios-swift',
      buildPath: 'build/ios/',
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
            token.attributes.category === 'effect' ||
            token.attributes.category === 'component',
        },
      ],
    },

    // Android (XML resources)
    android: {
      transformGroup: 'android',
      buildPath: 'build/android/',
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
            token.attributes.category === 'font',
        },
      ],
    },

    // CSS (prototypes, web)
    css: {
      transformGroup: 'css',
      buildPath: 'build/css/',
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
