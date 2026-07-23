Development Setup
---

## Requirements

- Java 25+
- IntelliJ IDEA
- Plugin DevKit (IntelliJ bundled plugin)
- Typescript
- Node.js 24+
- yarn v4+

## Getting Dependencies

doki-theme-jetbrains utilizes Gradle to make automating tasks easier. However, run configurations are provided to make
development easier. See [IntelliJ Run Configuration](#intellij-run-configuration).

The first thing we will need is to grab all the other doki projects doki-theme-jetbrains depends on. To do this use the
run configuration,`initDokiProject`.

### Getting Latest Changes

If you ever need to pull the latest changes from remote repos for *all* repos use `update repos`.

## About Variant Templates

Templates provides instructions for mapping and building both `<doki-theme>.theme.json` & `<doki-theme>.xml` files which
IntelliJ uses to construct a doki theme.

A variant is the type of style that can be generated into a plugin. Right now these are the types that can be generated:

- `islands`
- `darcula`
- `custom-<variant-name>`. Ex: `custom-islands`

### Extra: New Variant Templates

If you would like to add a new variant template, see [NEW_VARIANTS.md](./md_docs/NEW_VARIANTS.md)

## Building Themes

The next step is to build these variant templates. Use any of the appropriate run configurations you want to build:

- `build [islands]`
- `build [darcula]`

To build custom doki variants see [NEW_COLOR_THEME.md](./NEW_COLOR_THEME.md)

## Extra: Building a Plugin

Once everything above have been completed, we can now create a plugin using
[CONTRIBUTING.md](../../doki-theme-icons-jetbrains/CONTRIBUTING.md)

- `plugin [darcula]`
- `plugin [islands]`

**NOTE:** Using `plugin [<variant-name>]` automatically calls `build [<variant-name>]` before executing. **This means you can _skip_
`build [<variant-name>]`
if you ever decide to just run `plugin [<variant-name>]`!

## Testing Plugin

There are 2 ways to test the plugin.

### `runIde` Task

Use run configuration, `test plugin [<variant-name>]`.

### Build and Use Method

This method involves building the plugin and installing it on your own IDE.

1. Use run configuration, `create plugin`
2. Navigate to `Settings > Plugins ⚙️ > Install plugin from disk.`
3. Select `doki-theme-icons-jetbrains-<version>.zip` found in the `build/distributions` folder to install plugin.

## IntelliJ Run Configuration

The project comes with preset **Run** configurations. To access them, click on the dropdown menu found in this image:
![Run Configuration Location](../assets/contributingAssets/run_location.jpg)