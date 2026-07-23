Doki Theme: Jetbrains IDEs - Maintenance
---

![GitHub Release](https://img.shields.io/github/v/release/ZimCodes/doki-theme-jetbrains?display_name=tag&label=Version)

> [!IMPORTANT]
> ***This repo was created to provide maintenance for the main project
repo, [Unthrottled / doki-theme-jetbrains](https://github.com/doki-theme/doki-theme-jetbrains), until
productivity occurs
there once again!**
>
> *Can't let the creator's awesome work go to waste now, can we?*
## Quick Theme Preview

![Themes](./assets/screenshots/themes.webp)

<div align="center"> 
    <h3>Code Font <a href="https://rubjo.github.io/victor-mono/">Victor Mono</a></h3>
</div>


# [Complete Theme Album.](./albums/complete_theme_album.md)

---

Cute themes based on cute anime characters. With over **60** themes, the one you like the best, is probably here.

You can choose themes based on characters from these various Anime, Manga, or Visual Novels:

<details>
  <summary>All Featured Titles</summary>

- AzurLane
- Blend S
- Charlotte
- Chuunibyou, Love, & Other Delusions
- Code Geass
- Daily Life with a Monster Girl
- DanganRonpa
- Doki-Doki Literature Club
- Don't Toy With Me, Miss Nagatoro
- Fate/Type-Moon
- Future Diary
- Gate
- Guilty Crown
- Haikyu!!
- High School DxD
- Jahy-sama Will Not Be Discouraged!
- Kakegurui
- Kill La Kill
- KonoSuba
- Love Live!
- Lucky Star
- Miss Kobayashi's Dragon Maid
- Monogatari
- NekoPara
- Neon Genesis Evangelion
- One Punch Man
- OreGairu
- Quintessential Quintuplets
- Re:Zero
- Rising of the Shield Hero
- Sewayaki Kitsune no Senko-san
- Shokugeki no Soma
- Steins Gate
- Sword Art Online
- That Time I Got Reincarnated as a Slime
- Toaru Majutsu no Index
- Yuru Camp

</details>

## Installation

- Manually:

  Download the [latest release](https://github.com/ZimCodes/doki-theme-jetbrains/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

# Documentation

- [Configuration](#configuration)
  - [General Settings](#general-settings)
    - [Discreet Mode](#discreet-mode)
    - [Status bar Theme Name](#status-bar-theme-name)
    - [Background Images](#background-images)
    - [Suggestive Content](#suggestive-content)
  - [Stickers](#sticker)
    - [Content Type](#content-type)
    - [Save Sticker Position](#save-positioning)
    - [Dimension Capping](#dimension-cap)
    - [Small Stickers](#small-stickers)
  - [Fonts](#fonts)
- [Miscellaneous](#miscellaneous)
  - [Custom Icons](#doki-icons) 
  - [Quick Theme Switch](#quick-theme-switch)
  - [Plugin Art](#plugin-art)
  - [Helping the community](#enjoying-the-plugin)
  - [Contributions](#contributions)
  - [Changelog](#changelog)

# Configuration

You can access the settings menu here: 

<kbd>Preferences</kbd> > <kbd>Appearance & Behavior</kbd> > <kbd>Doki Theme Settings</kbd>

## General Settings

### Discreet Mode

<img src="https://user-images.githubusercontent.com/15972415/132107791-3d87fa95-3b36-46b1-a49f-ff45a1fb032c.png" alt="See No Problem" align="right" />

Are you still a closeted weeb? Do you still feel shame about liking anime? Does your job require you to not have fun?
Instead of addressing the real problems, you can just make your problems invisible with `Discreet Mode`!
This will clear any anime content from the IDE, and will even hide the theme name in the status bar. That way
you do not have to explain anything, to anyone (except for the heart themed folder icons). 
When the coast is clear, just uncheck the config or toggle the action, and your previous settings will be restored.

### Status bar Theme Name

**Name in status bar** will put the name of the character, your current theme is based on, in the status bar.

**Aqua**

![image](https://user-images.githubusercontent.com/15972415/108612695-aedba280-73b0-11eb-8e59-2e3229918111.png)


### Background Images

**Background Wallpaper** is probably one of the best features of the plugin.
This feature will set the background image to the current theme's official wallpaper.

> [!IMPORTANT]
> When this feature is enabled, if the plugin is uninstalled, the wallpaper will
> remain. You can turn off this feature or use the `Set Background Image` action to adjust.

**Asuna Dark**

![Editor Wallpaper](./assets/readmeAssets/wallpaper_setting.png)


**Empty Editor Background** sets the background image of the frame, 
that appears when all tabs are closed, with the current theme's official wallpaper.

**Asuna Dark**

![Empty Frame Wallpaper](./assets/readmeAssets/empty_frame_wallpaper.png)

#### Suggestive Content

<div align="center">
    <img src="https://doki.assets.unthrottled.io/misc/suggestive/cultured.gif" ></img>
</div>

So I thought it was a good idea to add a bit of culture to this plugin.
<sup><sup>Ya boi is horny on main.</sup></sup>

I will give you a bit of a warning before you install suggestive content.
Some of us are professional Otaku, who want to remain, well...professional.
Don't worry if you choose to continue, I won't ask you again for that specific theme.

Applies for the following content:

- Rias Onyx: Secondary Content


## Sticker Settings

**Show sticker** allows you to control the presence of the cute sticker in the bottom right-hand corner of your IDE.

**Allow Positioning** allows you to move your cute sticker out of the way when you need to read the logs.
You won't be able to click through the sticker though, so you'll need to move it or toggle this action.

> [!NOTE]
> [small stickers](#small-stickers) will ignore this setting. All small stickers are movable by default because their windows are not uniform.
> So they may need to be moved base on which window they show up in.

**Mai Dark**

![Moveable Sticker](./assets/readmeAssets/moveable_stickers.gif)

**Use Custom Sticker** allows you to be able to set the image to be used for all the doki-themes.
Custom stickers will remain on your IDE for as long as this checkbox is activated.
Allowed image types: jpg, png, gif

**Aqua**

![Custom sticker](./assets/readmeAssets/custom_sticker.gif)

**Ignore Scaling** allows you to bypass automatic application up-scaling, in order to display the sticker in its true
dimensions.
Mostly good getting around Windows' crummy image upscaling.

**Hide on hover** when enabled, allows you to set the duration your mouse has to be hovered over the sticker before it
hides.
So you can see what was hiding behind it. There is also an action call `Hide on Hover` that you can invoke to toggle
this functionality.

**Genos**

https://user-images.githubusercontent.com/15972415/166341110-042e9bfa-08c0-49f3-8d60-02168c07ea97.mp4

### Save Positioning

Any stickers that are allowed to be positioned (all small stickers are positionable), allows you to save the relative
position in a certain type of window. Just by `double-clicking` the sticker, stickers for that type of window will be
saved for later use.

Types of distinct windows:

- IDE Window
- Secondary window (the one where you drag a tab out of the IDE window)
- Any Dialog Window

**Reset Margin** allows you to restore the original default margins for all windows.
This will also move all active stickers back to the default position.


### Content Type

Some themes have more than one set of images, this allows you to switch in-between each of them.

Themes that have more than one sticker:

- Essex
- Hatsune Miku
- Sakurajima Mai
- Kanna Kamui
- Izumi Konata
- Just Monika (Light/Dark)
- Sayori (Light/Dark)
- Natsuki (Light/Dark)
- Yuri (Light/Dark)
- Rias (Onyx)

**Monika Dark**

![Secondary Sticker](./assets/readmeAssets/secondary_stickers.gif)

### Dimension Cap

Stickers are sometimes big and could get in the way. Thankfully, if you find that this is the case, you can cap the maximum dimensions of the primary displayed sticker. This also works for custom stickers as well!

I want to maintain the original aspect ratio of the image, so I will take the largest dimension and cap the maximum dimension to that it to
that. That way you can still see the same image, just smaller.

`-1` is the default behavior and does not constrain the dimension of the sticker

### Small Stickers

With the introduction of [Dimension Capping](#dimension-cap), you can now satisfy your need to have stickers in all the places!

All small stickers are put in dialog windows whose height is greater than a specified limit. That way you do not end up with a sticker getting in the way.

All small stickers are also move-able by default, that way you can scoot them out of the way.
Meaning that they ignore the `Allow Positioning` setting.

**Megumin**

https://user-images.githubusercontent.com/15972415/147772756-c64a7422-8e63-4f1b-bcd5-6fe7e27c5b45.mp4

## Fonts

Since the JetBrains IDEs don't have a convenient way to globally apply font changes across color schemes, I had to make my own.

**Global Editor Font Size** will override any/all font size settings for your code editor font size.
This only applies to any Doki Themes. You must enable the `Override Editor Font Size` for this feature to take effect.

**Global Console Font** will override any/all console font settings.
This only applies to any Doki Themes. You must enable the `Override Console Font` for this feature to take effect.


# Miscellaneous

## Doki Icons

<div align="center">
    <img src="https://doki.assets.unthrottled.io/misc/doki_icons_logo.svg" ></img>
</div>

**Emilia Light**

![Icons Preview](./assets/readmeAssets/doki_icons.png)

Give your IDE that extra something with the [Doki Theme Icons](https://github.com/doki-theme/doki-theme-icons-jetbrains#doki-theme-icons-jetbrains).
Custom tailored to work well with your favorite theme! 

## Quick Theme Switch

You've got a ton of new themes now, and you want to take a quick peek at each of them.
To do that, use the shortcut: <kbd>⌘ Cmd</kbd> + <kbd>\`</kbd> _(MacOS)_ | <kbd>CTRL</kbd> + <kbd>\`</kbd> _(Others)_

![Quick Switch](./assets/readmeAssets/quick_switch.gif)

## Plugin Art

In order to prevent this plugin from being a huge artifact, the assets used for the background and stickers are downloaded from https://doki.assets.unthrottled.io when needed.
This allows me to update the assets without users needing to update the plugin as well.

I also have them committed to GitHub as well at https://github.com/doki-theme/doki-theme-assets .
You should be able to find what you are looking for in the `backgrounds/wallpapers` & `stickers` directories.

## Enjoying the plugin?

Great! I am glad you like it!

Be sure to ⭐ and share it with other weebs!

Also, giving it a good [review on the plugins page](https://plugins.jetbrains.com/plugin/10804-doki-doki-literature-club-theme) will help this plugin become visible to more otaku!

## Contributions?

If you would like to contribute to the project, please feel free to create a pull request with your ideas!

Also check out the [CONTRIBUTING.md](./CONTRIBUTING.md).

Some ways to contribute:
- Create new icons.
  - Here's an [Icon TODO List](https://github.com/doki-theme/doki-theme-icons/blob/main/todo) created by Unthrottled which includes icons to be added in future versions.
- Add cool new features
- Update existing themes and icons providing that doki vibe
- **NEW**: Create a new doki theme color scheme. See [NEW_COLOR_THEME.md](./md_docs/NEW_COLOR_THEME.md)

## Changelog

Did you know a changelog exists?
[You can find it here!](./changelog/CHANGELOG.md)

---

# Attributions

Project uses icons from [Twemoji](https://github.com/twitter/twemoji). Graphics licensed under CC-BY
4.0: https://creativecommons.org/licenses/by/4.0/

---

<div align="center">
    <img src="https://doki.assets.unthrottled.io/misc/logo_v2.svg" ></img>
</div>


