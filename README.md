Doki Theme: Jetbrains IDEs
---

![Downloads](https://img.shields.io/jetbrains/plugin/d/10804)
![Rating](https://img.shields.io/jetbrains/plugin/r/rating/10804)
![Version](https://img.shields.io/jetbrains/plugin/v/10804)
![Build](https://github.com/doki-theme/doki-theme-jetbrains/workflows/Release/badge.svg)

### https://doki-theme.unthrottled.io/

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

- Using IDE built-in plugin system:

  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "The Doki Theme"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/doki-theme/doki-theme-jetbrains/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

# Documentation

- [Configuration](#configuration)
  - [General Settings](#general-settings)
    - [Content Settings](#content-settings)
      - [Discreet Mode](#discreet-mode)
      - [Stickers](#sticker)
      - [Content Type](#content-type)
      - [Save Sticker Position](#save-positioning)
      - [Dimension Capping](#dimension-cap)
      - [Small Stickers](#small-stickers)
      - [Background Images](#background-images)
      - [Suggestive Content](#suggestive-content)
      - [Misc Content Settings](#misc-content-settings)
    - [Other Settings](#other-settings)
      - [Promotional Content](#promotional-content)
  - [Fonts](#fonts)
- [Miscellaneous](#miscellaneous)
  - [Contributing](#contributing) 
  - [Quick Theme Switch](#quick-theme-switch)
  - [Theme Requests](#theme-requests)
  - [Helping the community](#enjoying-the-plugin)
  - [Feature Requests](#contributions)
  - [Frequent Updates](#release-channel)
  - [Changelog](#changelog)

# Configuration

You can access the settings menu here: 

<kbd>Preferences</kbd> > <kbd>Appearance & Behavior</kbd> > <kbd>Doki Theme Settings</kbd>

## General Settings

**Current Theme** allows you to change the look and feel of your IDE with one of the plugin's supplied themes.

### Content Settings

This allows you to control the decoration of your IDE.

You can be a conservative weeb or an obvious otaku. 
Customize to your heart's content!

## Discreet Mode

<img src="https://user-images.githubusercontent.com/15972415/132107791-3d87fa95-3b36-46b1-a49f-ff45a1fb032c.png" alt="See No Problem" align="right" />

Are you still a closeted weeb? Do you still feel shame about liking anime? Does your job require you to not have fun?
Instead of addressing the real problems, you can just make your problems invisible with `Discreet Mode`!
This will clear any anime content from the IDE, and will even hide the theme name in the status bar. That way
you do not have to explain anything, to anyone (except for the heart themed folder icons). 
When the coast is clear, just uncheck the config or toggle the action, and your previous settings will be restored.


This plugin is also integrated with the [Anime Meme Plugin](https://github.com/ani-memes/AMII#discreet-mode), for the ultimate shame hiding experience.
Enabling/disabling `Discreet Mode` for this plugin will enable/disable `Discrete Mode` in the other plugin. However, this does not work the other way around. 
Only The Doki Theme can enable/disable modes across plugins.


#### Sticker

**Show sticker** allows you to control the presence of the cute sticker in the bottom right-hand corner of your IDE.

**Allow Positioning** allows you to move your cute sticker out of the way when you need to read the logs.
You won't be able to click through the sticker though, so you'll need to move it or toggle this action.

**Note:** [small stickers](#small-stickers) will ignore this setting. All small stickers are movable by default because their windows are not uniform.
So they may need to be moved base on which window they show up in.

![Secondary Sticker](./assets/readmeAssets/moveable_stickers.gif)

**Use Custom Sticker** allows you to be able to set the image to be used for all the doki-themes.
Allowed image types: jpg, png, gif

![Custom sticker](./assets/readmeAssets/custom_sticker.gif)

**Ignore Scaling** allows you to bypass automatic application up-scaling, in order to display the sticker in its true
dimensions.
Mostly good getting around Windows' crummy image upscaling.

**Hide on hover** when enabled, allows you to set the duration your mouse has to be hovered over the sticker before it
hides.
So you can see what was hiding behind it. There is also an action call `Hide on Hover` that you can invoke to toggle
this functionality.

https://user-images.githubusercontent.com/15972415/166341110-042e9bfa-08c0-49f3-8d60-02168c07ea97.mp4

#### Save Positioning

Any stickers that are allowed to be positioned (all small stickers are positionable), allows you to save the relative
position in a certain type of window. Just by `double-clicking` the sticker, stickers for that type of window will be
saved for later use.

Types of distinct windows:

- IDE Window
- Secondary window (the one where you drag a tab out of the IDE window)
- Any Dialog Window

**Reset Margin** allows you to restore the original default margins for all windows. 
This will also move all active stickers back to the default position.


#### Content Type

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

  ![Secondary Sticker](./assets/readmeAssets/secondary_stickers.gif)

#### Dimension Cap

Stickers are sometimes big and could get in the way. Thankfully, if you find that this is the case, you can cap the maximum dimensions of the primary displayed sticker. This also works for custom stickers as well!

I want to maintain the original aspect ratio of the image, so I will take the largest dimension and cap the maximum dimension to that it to
that. That way you can still see the same image, just smaller.

`-1` is the default behavior and does not constrain the dimension of the sticker

#### Small Stickers

With the introduction of [Dimension Capping](#dimension-cap), you can now satisfy your need to have stickers in all the places!

All small stickers are put in dialog windows whose height is greater than a specified limit. That way you do not end up with a sticker getting in the way.

All small stickers are also move-able by default, that way you can scoot them out of the way. 
Meaning that they ignore the `Allow Positioning` setting.

https://user-images.githubusercontent.com/15972415/147772756-c64a7422-8e63-4f1b-bcd5-6fe7e27c5b45.mp4

#### Background Images

**Background Wallpaper** is probably one of the best features of the plugin.
This feature will set the background image to the current theme's official wallpaper.

> Important: When this feature is enabled, if the plugin is uninstalled, the wallpaper will
> remain. You can turn off this feature or use the `Set Background Image` action to adjust.

![Editor Wallpaper](./assets/readmeAssets/wallpaper_setting.png)


**Empty Editor Background** sets the background image of the frame, 
that appears when all tabs are closed, with the current theme's official wallpaper.

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


#### Misc Content Settings

**Name in status bar** will put the name of the character, your current theme is based on, in the status bar. 

![image](https://user-images.githubusercontent.com/15972415/108612695-aedba280-73b0-11eb-8e59-2e3229918111.png)


### Other Settings

**Notification Opacity:** Are you tired of pesky notifications covering up your sticker?
I know I was, now you can fix that problem. You are free to adjust the opacity of the notification window as you please.
You can hit `Apply` to test out your new settings!

![Notification Opacity](./assets/readmeAssets/notification_opacity.gif)

**Frameless Mode** is a feature only available on MacOS, and gives your IDE the frameless look and feel.
Note: Since 2022.2 JetBrains platforms natively support this feature. So this implementation will collide with the native one.

![Frameless mode](./assets/readmeAssets/frameless.png)

**Theme Change Animation** enables the neat fade-in animation effect as you change your theme.
You can see a rough example in the [quick theme switch section.](#quick-theme-switch)

### Promotional Content

I am a weeb who builds a lot of neat weebie things. A lot of which I think you will enjoy.
By default, I have opted you into receiving a _single_ promotion to the [AniMeme Plugin](https://github.com/ani-memes/AMII#amii-anime-meme-ide-integration).
This notification window _will_ show up when your IDE has been idle, and you've met these following conditions:

- You are online
- You have weeb content enabled (sticker or background)
- It's been at least 7 days since you've installed the Doki Theme.

You will also only ever see the promotion once on each machine, the config is shared across IDEs on your machine.
You can opt yourself out at any time before then. 
**Note**: if you've opted out, for the best experience, be sure to restart any of your open IDEs (eg: Intellij, Webstorm) if you have the Doki Theme installed across IDEs.

I'm only promoting one item, and that isn't going to change anytime soon.
Thanks for your understanding and I hope you enjoy the stuff I've built!

## Fonts

Since the JetBrains IDEs don't have a convenient way to globally apply font changes across color schemes, I had to make my own.

**Global Editor Font Size** will override any/all font size settings for your code editor font size.
This only applies to any Doki Themes. You must enable the `Override Editor Font Size` for this feature to take effect.

**Global Console Font** will override any/all console font settings.
This only applies to any Doki Themes. You must enable the `Override Console Font` for this feature to take effect.


# Miscellaneous

## Contributing

If you want to get your workstation set up to work on the plugin,
then you'll want to check out the [CONTRIBUTING.md](./CONTRIBUTING.md) for instructions on what is needed.

## Quick Theme Switch

You've got a ton of new themes now, and you want to take a quick peek at each of them.
Well you can use the [Quick Switch Scheme](https://www.jetbrains.com/help/idea/switching-between-schemes.html)
to browse the `Look and Feel`.

![Quick Switch](./assets/readmeAssets/quick_switch.gif)

## Theme Requests

If you want your main squeeze to be featured in the Doki Theme suite, feel free to [submit a theme request](https://github.com/doki-theme/doki-master-theme/issues).

## Enjoying the plugin?

Great! I am glad you like it!

Be sure to ⭐ and share it with other weebs!

Also, giving it a good [review on the plugins page](https://plugins.jetbrains.com/plugin/10804-doki-doki-literature-club-theme) will help this plugin become visible to more otaku!

## Contributions?

I think your voice needs to be heard! You probably have good ideas, so feel free to submit your feedback as [an issue](https://github.com/doki-theme/doki-theme-jetbrains/issues/new).

Help make this plugin better!

## Release channel

Want updates sooner? I have a [canary release channel](https://github.com/Unthrottled/jetbrains-plugin-repository) that you can set up to get the latest and greatest!

## Changelog

Did you know I keep a changelog?
[You can find it here!](./changelog/CHANGELOG.md)

---

# Attributions

Project uses icons from [Twemoji](https://github.com/twitter/twemoji). Graphics licensed under CC-BY
4.0: https://creativecommons.org/licenses/by/4.0/

---

<div align="center">
    <img src="https://doki.assets.unthrottled.io/misc/logo.svg" ></img>
</div>


