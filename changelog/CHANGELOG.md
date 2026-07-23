# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [4.0.1] - 2026-07-22

### Fixed

- Corrects fallback source for doki assets
- Fixed settings page sometimes not working correctly due to panels not being created on time
- Remove deprecated and obsolete theme meta keys
- Fix error for adding `id` key to doki theme meta keys

## [4.0.0] - 2026-07-17

### Added

- Support for build 2026.2

## [3.2.1] - 2026-07-16

### Fixed

- Brightens text in the following areas for dark Islands themes:
  - File tree tool window 
  - Status bar
  - Top MainToolbar
  - CompletionPopup (for DxD Rias Onyx theme)
  - Editor Tooltip
  - Main Menu bar
  - Notification Tool Window (error,warning,information)

## [3.2.0] - 2026-07-13

### Changed

- Migrate `Doki Theme Settings` from **GUI Designer forms** to [Kotlin UI DSL v2](https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html)
- Redesign `Doki Theme Settings` page

### Removed

- Remove [Theme Randomizer](https://github.com/Unthrottled/theme-randomizer) plugin promotion in settings

## [3.1.1] - 2026-07-09

### Changed

- Update main window background to a `1:20:1` contrast ratio for all doki islands themes.
- (Islands) Increase border width
- Updates background of Status bar

### Fixed

- (Darcula) Fix: theme names showing blanks for all doki themes
- (Islands) Fix: Active editor tabs in islands not following doki theme coloration
- Fix: Tooltip text being unreadable on current tooltip background

## [3.1.0] - 2026-07-08

### Added

- Can now create your own custom doki color theme! See [NEW_COLOR_THEME](../md_docs/NEW_COLOR_THEME.md).

## [3.0.0] - 2026-07-02

### Added

- Support islands theme for all doki themes

### Changed

- Improve background color to buttons
- Implement border color for buttons
- Update color for scrollbar in editor's tab section

### Removed

- Remove deprecated/unsupported themeMetadata keys

### Fixed

- Alert dialog boxes does not follow current doki theme
- Notification link colors not folowing current doki theme
- links pointing to predecessors repo
- Border for inactive tab not following current doki theme

## [2.2.0] - 2026-03-27

### Changed

- dependencies
- Upgrade Gradle: `9.2.1` -> `9.4.1`

## [2.1.0] - 2025-12-13

### Added

- IDE build support for 2025.3
- New custom task for building dependencies for themes

### Changed

- Dependencies
- Update local repo dependency sources
- Updated gradle: `9.2` -> `9.2.1`

## [2.0.1] - 2025-11-17

### Changed

- Miku Theme: `Visual Line` color now matches `Hard Wrap` color

### Development

- Change multi-project structure: `buildSrc` -> composite builds _(`build-logic`)_
- Adds build caching support

## [2.0.0] - 2025-11-12

### Changed

- Reduce overall Notifications introduced by plugin
- Replace option to toggle discreet mode for **all** promotional plugins. Discreet mode setting will toggle for this
  plugin only.
- Project is now configuration cache compatible
- Increase max heap and metaspace for Gradle project
- Update README

### Removed

- Removes error logging
- Removes test implementations
- Removes deprecated items
- Remove Promotional materials. _(e.g. AniMemes)_ except suggestive content.
- Decouple _AniMemes_ from this plugin completely
- Remove New Update notifications by plugin
- Remove 1st time user notifications
- Remove option to apply theme change animation
- Removes theme switcher from Doki Settings
- Removes legacy project migration algorithms and activities
- Remove workflow files

### Fixed

- Replace deprecated `Alarms()` in place of Kotlin coroutines
- Replace deprecated items with modern items
- Fix [Issue #824](https://github.com/doki-theme/doki-theme-jetbrains/issues/824): Text spacing shrinks when zooming is
  above `100%`
- Fix [Issue #821](https://github.com/doki-theme/doki-theme-jetbrains/issues/821): Left/Right ToolWindow Toolbar not
  following current Doki theme.
- Fix [Issue #819](https://github.com/doki-theme/doki-theme-jetbrains/issues/820): Hatsune Miku theme's Visual Line &
  Hard wrap guides' color matching the editor background, making it hard to see

[Unreleased]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v4.0.1...HEAD
[4.0.1]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v4.0.0...v4.0.1
[4.0.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v3.2.1...v4.0.0
[3.2.1]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v3.2.0...v3.2.1
[3.2.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v3.1.1...v3.2.0
[3.1.1]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v3.1.0...v3.1.1
[3.1.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v3.0.0...v3.1.0
[3.0.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.2.0...v3.0.0
[2.2.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.0.1...v2.1.0
[2.0.1]: https://github.com/ZimCodes/doki-theme-jetbrains/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/ZimCodes/doki-theme-jetbrains/commits/v2.0.0
