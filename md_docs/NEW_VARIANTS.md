# New Variant

**NOTE:** There is currently no support for auto-generating variant editor schemes (`.xml`)

## New LAF variant

To create a new LAF variant:
- Navigate to `doki-build-plugin/assets/templates`
- Create a copy of `base.laf.template.json`
- Rename it to this format: `base.<variant-name>.laf.template.json`
- Modify the JSON file. See [JSON Format](#json-format)
- Then simply run the task `./gradlew genVariantTemplates -Pvariant=<variant-name>`

## JSON Format

This is the schema for creating a LaF template:
- `type`: "LAF"
- `name`: "base <variant-name>"
- `ui`:
  `{ themeMetadata keys: color names (defined in 'masterThemes/definitions/<doki-theme>/master.definition.json' or 'colors key')}`
- `icons`: **(Optional)** same as `ui` except focused on icon themeMetadata
- `colors`: **(Optional/Not Recommended)** define new color names in place of hexadecimals. Ex: `accentColor: "#ff3165"`
  I recommend not using this. Use existing `colors` names defined in a doki themes `master.definitions.json` file located in
  `masterThemes/` directory instead.

**NOTE:** The data model for this JSON format is called `AssetTemplateDefinition` located in `Models.kt` of `doki-build-source-jvm/` directory.

### Example
```json
{
  "type": "LAF",
  "name": "base islands",
  "ui": {
    "ToolWindow.background": "headerColor",
    "ToolWindow.Header.background": "headerColor",
    "ToolWindow.Header.inactiveBackground": "headerColor",
    "MainWindow.background": "baseBackground",
    "Island.borderColor": "headerColor",
    "Island.arc": 22,
    "Island.borderWidth": 5,
    "EditorTabs.underlinedBorderColor": "accentColor",
    "EditorTabs.inactiveUnderlinedTabBorderColor": "accentColorLessTransparent",
    "EditorTabs.inactiveUnderlinedTabBackground": "highlightColor",
    "EditorTabs.background": "headerColor"
  },
  "icons": {
    "ColorPalette": {
      "Actions.Green": "accentColor",
      "Actions.Blue": "accentColor",
      "Object.Purple": "awesomePurple"
    }
  },
  "colors": {
    "awesomePurple": "#d506ff"
  }
}
```