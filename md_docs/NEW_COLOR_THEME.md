# New Color Scheme

> Do not like the color scheme of an existing doki theme?
> Want to make your own color variant? This is the place!

In order to submit a color scheme, you must provide a proof of concept. Follow the steps below to achieve this. If you
haven't done already, [setup your development environment](./DEV_SETUP.md).

## Customizing Colors

`masterThemes/definitions` harbors all color definitions for a doki theme.

1. **Navigate** to `masterThemes/definitions`.
2. **Create a copy** of the doki theme, `*.master.definition.json`, you would like to modify and place it in the same
   folder as the original.
3. **Rename** the copied file according to the following rules:

- File name _must_ end with `custom.master.definition.json`.
- `.` must be used in place of spaces. Ex: `kiki.takagi.cool.theme.custom.master.definition.json`
- File name must not be the same name as the original

4. Now open the file and change the following values:

- `name`: The name of your doki color variant
- `displayName`: The name shown to users
- `author`: Your name
- `colors`: This is where you customize your doki theme colors. Replace any `#hexadecimal` color with your own!
  - You can use `doki-build-plugin/assets/template/base.laf.template.json` as your guide to see what each color name
    control.

## Build Themes

> This step is completely optional. Why? Because this step is executed *automatically* if you run the last step,
> [Publish Plugin](#publish-plugin).

This step includes:

- Generating a `plugin.xml`
- Generating doki theme templates for how to build each doki theme
- Building your custom theme (s)
- Relocating your custom theme (s) to `src/main/resources/META-INF`
- Specifying where to find your custom theme (s) in `plugin.xml`

To automate this, use the run configuration, `build [custom <variant-name>]`.

## Create Plugin

Use run configuration, `plugin [custom <variant-name>]`, to build the plugin.

When finished, your newly created plugin can be found at `build/distributions/`!

## Share Plugin

You can share your custom plugin with the community
at [Custom Color Schemes Discussions](https://github.com/ZimCodes/doki-theme-jetbrains/discussions/categories/custom-color-schemes)
with the potential of having it added to the main `custom` plugin!
