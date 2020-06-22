# Changelog
---

# 8.0.5 [Consistency & Bug Fixes]

- Wrapped up the last of the major consistency issues!
- Fixed issue with creating unneeded log statements.

# 8.0.4 [Usability & Consistency]

- Improved the usability of the new v8 themes.
- Increased consistency for many small UI elements 

# 8.0.3 [Consistency]

- Updated all the light theme's ignored file status to be the disabled color.

# 8.0.2 [Consistency]

- Small adjustments to Darkness's Dark theme and Konata's Light theme.
- Fixed issue where non-Doki themes would be replaced by Rem after restarting.

# 8.0.1 [Consistency]

- Many small usability and consistency changes.

# 8.0.0 [New Themes!]

- Added 5 new themes based on various new characters!
    - High School DxD:
        - Rias Gremory (Dark Theme)
    - Sword Art Online:
        - Yuuki Asuna (Light Theme)
    - Lucky Star:
        - Izumi Konata (Light Theme/2 Stickers)
    - KonoSuba:
        - Darkness (Light/Dark Theme)

- New users will be given Rem as their first theme now.

![The New Girls](https://doki.assets.unthrottled.io/misc/v8_girls.png?version=1)

# 7.2.3 [Non-Functional]

- Migrated error reporting to https://sentry.io

# 7.2.2 [Non-Functional]

- Migrated away from deprecated platform APIs.
- Made version upgrades more maintainable.

# 7.2.1

- 2020.2 Build Support.

# 7.2.0

- Revisited most of the light themes so they are all consistent with the other themes.
  - Themes affected:
    - Light Monika
    - Light Natsuki
    - Light Yuri
    - Light Sayori
    - Beatrice

# 7.1.2

- Many Non-Functional changes.
- Changed the `collapsed region` (when looking at a diff) divider color.

# 7.1.1

- Made a large amount of small changes.
    - Does that mean I made a medium change? lul dunno.

# 7.1.0

- Updated the look and feel of the light Emilia theme.
- Theme background wallpapers are now available offline.

# 7.0.5

- Non-Functional Changes:
    - Changed how the plugin gets stickers. 
        - I am now able to update the stickers without you having to download a new version.

# 7.0.4

- Settings menu available for 2020 Builds
- Added `Don't show readme on startup` option.
    - That way you can admire your theme's background art!

# 7.0.3

- Fixed issue with unavailable themes showing up in settings.
    - Thanks for reporting the issue!

# 7.0.2

- Better Legacy Theme Migration.

# 7.0.1

- Small Theme consistency and usability tweaks.

# 7.0.0

- Added 5 new themes based on various new characters!
    - Re:Zero:
        - Emilia (Dark/Light Theme)
    - Danganronpa:
        - Mioda Ibuki (Dark/Light Theme)
    - Hatsune Miku (Dark Theme)

- Fixed all reported exceptions. Thank you for submitting them!
    - Status Bar no longer throw exceptions when being disposed
    - 2020.1 EAP for MacOS can now drag editor tabs out of IDE into separate window again.
    
- The themes from Mistress's Menagerie have been moved from the Community Theme Suite.
    - If you where using one of those themes and would like them back feel free to [follow these instructions here](https://github.com/doki-theme/doki-theme-jetbrains/wiki/Ultimate-Theme-Setup)

# 6.2.2

- Migrated theme build process to centralized management strategy.

# 6.2.1

- Updated default font.

# 6.2.0

- Updated Dark Sayori and Natsuki to be more consistent with the other DDLC Dark themes.

# 6.1.0

- Changed a large majority of theme's code color scheme definitions. So some code colors may have changed.
    - Themes affected:
        - Monika Dark
        - Natsuki Dark
        - Sayori Dark
        - Satsuki
        - Megumin
        - Cleo
        - Eleniel
        - Neera
        - Sanya
        - Syrena

# 6.0.3

- A bunch of small look and feel consistency tweaks.
    - For more information please [see the issue](https://github.com/doki-theme/doki-theme-jetbrains/issues/199).

# 6.0.2

- Better PyCharm color support.

# 6.0.1

- 2020 EAP Build Support
    - Settings menu is currently unavailable in the 2020 builds, all actions are in the tool menu though.
- Fixed issue with not being able to select a Doki Theme from the settings menu.

# 6.0.0

- Added new themes based on characters from various anime series!
    - KillLaKill
        - Ryuko Matoi (Dark Theme)
        - Satsuki Kiruin (Light Theme)
    - Re:Zero
        - Ram (Dark Theme)
        - Rem (Dark Theme)
        - Beatrice (Light Theme)
    - KonoSuba
        - Megumin (Dark Theme)

# 5.2.1

- Java 8 Runtime support.
    - Android 4.0 Support

# 5.2.0

- Replaced Idea Vim Icon With Themed Icon
- Made themes easier to maintain so more can be added without significantly increasing maintenance work.
    - As a side effect the light Literature club themes may have changed slightly. 
    
# 5.1.2

- Small Color Consistency improvements.
- Replaced Icons from other plugins to become more consistent with the themes.
    - Buck
    - Android

# 5.1.1

- Fixed some missing Material Directory icons.

# 5.1.0 
- Brought back Configurable Material Icons
    - Icons Brought Back:
        - Directory Icons
        - File Icons
        - PSI Icons
    - Icons are off by default, so be sure to go to the theme settings to turn them on.
- Updated Light Natsuki's color scheme.
- Replacing the SSV Normandy Loading Indicator Icon with a Doki Themed Icon.  
 
# 5.0.3

- Fixed Stickers not showing up in certain environments.

# 5.0.2

- Brought back configurable the theme name in the lower right hand of the IDE.
    - Component no longer takes you to the settings page.

# 5.0.1

- Opt in to Theme transition animation, as that configuration sticks around after removing the plugin.
- Many Small Consistency tweaks (too many to list, but you won't notice because that's the point :)

# 5.0.0

### Complete Theme Re-Write
All themes have been redesigned. 
Newer, Simpler, Better!

- Brand new Look and feel!
    - Same Characters, cleaner look!
    - New Icons! (JetBrains defaults with a twist)
    - New Code Color Schemes!
        - Hopefully more readable! (Please reach out if you have issues)
- Enhanced User Experience
    - Clean Theme Switching 
    - Minimum Work Configurations
    - Plays well with other non-Doki look and feels.
    - Less Glitchy and more color consistent.
    - Fancy Theme Transition Animations.
    - Dark themes in place of "Dark Mode"
        - Dark themes are still there, but they are their own theme now 
        (eg Dark Monika, Dark Yuri). Same goes for light themes, for example: "Light Monika" and "Light Natsuki"
        
 **File Colors may need to be re-enabled if you had them enabled**
    
If something was removed that you miss, please make an issue.
I'll do my best to accommodate.

The Doki Theme is no longer built on top of Material UI, so all inherited configurations are now gone.

The Doki Theme no longer supports Material UI integrations. 
So both themes can not be cleanly used side by side. Sorry for any inconveniences this may cause.

# 4.4.2

- Better themed non-module java file icons.

# 4.4.1

- Restored Editor Tab height for non bundled java runtimes.

# 4.4.0

- 2019.3 Build Support!
- Actually supporting Heart Indicator now.
- Up to date with Material 4.8.1
    - Better PHP Syntax Highlighting
    - More Icons!

# 4.3.3

- New users have to opt-in to file colors.

# 4.3.2

- Small theme usability tweaks

# 4.3.1

- Addressed all reported thrown exceptions.
    - Thanks all for bringing them to my attention!
    - Issues Fixed
        - `java.lang.NullPointerException
               at com.intellij.openapi.wm.impl.IdeRootPane.updateToolbarVisibility(IdeRootPane.java:298)`
        - `java.lang.NullPointerException
               at com.chrisrm.ideaddlc.ui.MTTreeUI.paintRow(MTTreeUI.java:164)`
        -  `java.lang.NullPointerException
                at com.chrisrm.ideaddlc.ui.MTTreeUI.paintRow(MTTreeUI.java:166)`
        -  `com.intellij.openapi.extensions.impl.PicoPluginExtensionInitializationException: org/jetbrains/coverage/gnu/trove/THashMap
                 at com.intellij.openapi.extensions.impl.ExtensionComponentAdapter.createInstance(ExtensionComponentAdapter.java:50)`

# 4.3.0

- Up to date with Material UI from 4.2.1 -> 4.4.5
    - New File/Directory Icons
    - Title bar things, etc.

# 4.2.2

- Implemented exception reporting.
    - You know have the ability to anonymously report an exception being thrown by the Doki-Theme!

# 4.2.1

- Better 2019.2.1 build support.
    - Tab height restoration.
    - More Responsive project tree view.

# 4.2.0

- Cleo's chibi maintains roughly the same proportions as the other chibis. 
- A bunch of small icon improvements (probably only noticeable to me).

# 4.1.0

- Made the chibis in Mistress's Menagerie more vibrant.
    - Most noticeable in the light themes. 

# 4.0.1

- Fixed issue with Dark Sayori Theme not being selectable.

# 4.0.0

- Brand New Theme Suite!
    - Introducing **Mistress's Menagerie**
        - A collection of themes based off of various characters of anthropomorphized species
    - 6 Light and Dark Themes to choose from.
    
- Updated Light Monika's look to be less "Holy cow, green!".
    - See the [GitHub Merge Request](https://github.com/doki-theme/doki-theme-jetbrains/pull/168#issuecomment-520269641) for more details.

- Enhanced plugin's first usage user experience.

- Re-Branded Plugin
    - Updated Plugin Icon
    - Changed Plugin name to "The Doki-Doki Theme"
    - Same themes as before!
    
- Improved Material UI Integration Support
    - Now when switching between Doki-Doki themes and Material UI Themes, the chibi will disappear.

# 3.5.0

- Updated to Material UI 4.2.1!
    - Better Integrations (Less color bleed)
    - More Icons and File Associations
    - Title bar is back
    - Better GO code highlighting
    - Better PHP syntax highlighting?

- Better Windows OS support.

# 3.4.2

- Fixed Title Bar issue in the 2019.2 EAP builds (MacOS).

# 3.4.1

- Addressed non-fatal exceptions in the 2019.2 builds.
    - Yay! No more annoying exceptions.

# 3.4.0

- Added 2019.2 Build support!
- Up to date with Material UI 3.10.0.
- Material UI Icons override Doki Icons for the time being
    - If you want the Doki icons just use the "Toggle Material Icons" action in the Material UI configuration.
    - This issue will be addressed in  a later release.


# 3.3.0

- Added more Kotlin icon associations.
- Better External Theme integration
    - Material UI no longer overrides Doki-Doki icons when the Doki Them is active.
    - Less left over Doki-Doki Icons when Material UI is activated.
    - See the [Github pull request for more details](https://github.com/doki-theme/doki-theme-jetbrains/pull/161).


# 3.2.4

- No longer throws an harmless (but annoying) exception when Material UI is also installed.

# 3.2.3

- Updated with Material 3.9.3
    - Bunch of Icon Associations and new icons
        - Now using default Kotlin Icon
    - Topless Tab Highlight option
    - Rider Icon Bug Fix?
    
# 3.2.2

- No functional changes, sorry!
    - Addressed a bunch of technical debt.
- Fixed startup error on later 2018 JetBrain's Builds.
- Fixed startup error on Windows Android Studio 3.3.2

# 3.2.1

- Fixed settings bug where the `Ḑ̶͍̲̳̫͗̚͠ą̵͉̠̬͙̤͚̯̈͆͆ŗ̴̧̱̾̑k̷̨̹̯̳͕͍̖̰͕̱̟̯̝̓̂̆͌̈́͂̇̅͠͝ ̶̹͙͓͉͔͇͎̼̯̺̻̥͑͒̈͛̇̕͠M̷̨̨̡͖̭̼̲̞̹̺͉̐́̔̔̆̒̾́̑̕͜o̴͙͓͓͈̫̣͔̗̝̾̂̅̅̂̀̔̕͜d̶̲̤̜͆̽͘ę̸͎̪̭̭̣͎̠̎̀͌̋̓̏̏̐̕͜ͅ` option did not switch on or off _Ḑ̶͍̲̳̫͗̚͠ą̵͉̠̬͙̤͚̯̈͆͆ŗ̴̧̱̾̑k̷̨̹̯̳͕͍̖̰͕̱̟̯̝̓̂̆͌̈́͂̇̅͠͝ ̶̹͙͓͉͔͇͎̼̯̺̻̥͑͒̈͛̇̕͠M̷̨̨̡͖̭̼̲̞̹̺͉̐́̔̔̆̒̾́̑̕͜o̴͙͓͓͈̫̣͔̗̝̾̂̅̅̂̀̔̕͜d̶̲̤̜͆̽͘ę̸͎̪̭̭̣͎̠̎̀͌̋̓̏̏̐̕͜ͅ_

# 3.2.0

- Major Settings changes
    - Non-Working custom theme option removed
    - Removed all non-working settings from the "Doki-Doki Theme Settings" form
        - If there is a feature that you want to see again please let me know. I'd be more than happy to bring it back :)
    - Added "Show Settings" under the "Doki-Doki Theme Options" shortcut
    - Added more theme configurable options to the Settings form
        - Such as you can change the selected theme in the configuration menu now.
- Better Z̸̲̺̬̮̈́ḁ̵̛͖͈͗̿̑̓́̄̎̀̌̚l̵̛̼̟̗̱͕̟͎̖̀̀̀̇̋̍͒̃̿̓̌̓͝g̴̨͈̝͚͕͈̀̔͒̍̔̈́̈́̓͘̚͜͝ǫ̶̱̫̤̭̤̩̑̇̎̔͐͊̾̀̚  text handling

# 3.1.4

- A bunch of small theme tweaks (icon adjustments and readability tweaks)!
    - See the [Github Issue for more details.](https://github.com/doki-theme/doki-theme-jetbrains/issues/143)

# 3.1.3

- Settings configuration menu loads now.

# 3.1.2

- Android Studio 3.3.2 Support

# 3.1.1

- Improved SSV Normandy plugin integration.
    - Colors actually change when switching to themes that are not just Monika.

# 3.1.0

- Updated with Material 3.8.0.2
    - Added the "Tab Highlight Position Option" which can be found in the DDLC Options configuration.
    - A bunch of new SVGs and file icon associations!
- Adjusted border coloring for each theme

# 3.0.0

- Brand new light theme look.
    - Changed overall look and feel to have less contrast.
    - Modifications to the syntax highlighting colors.
    - [See this link for changes](https://github.com/doki-theme/doki-theme-jetbrains/pull/137#issuecomment-469076145)
- Small Dark theme tweaks
    - Better button foreground coloring.
    - Yuri and Sayori themes panel backgrounds have less contrast.
- Added a Toggle Light Mode Action
- Added a Wizard action
- Themed project arrow icons
- Enhanced file association for DDLC icon
    - eg if ddlc appears in a folder name then you get the icon.
    
# 2.6.0

- JetBrains 2019 build support!

# 2.5.1

- Supports auto theming integration with the SSV Normandy progress bar plugin.
    - Auto-Theming only works for Intellij at the moment.
- Also fixed theme customization form.

# 2.5.0

- Class icons are now themed to the current club member.
- Changed function icons to be λ

# 2.4.1

- Updated plugin display icon.

# 2.4.0

- Updated to Material UI version 3.5.0

# 2.3.0

- Enhanced Dark theme contrast for select club members.
    - The Monika and Natsuki dark themes have been adjusted slightly to become more consistent with the dark Sayori and Yuri themes.
    - Be sure to take a look when you get the chance!
    
# 2.2.1

- The "Run All Tests in File" gutter icon is now clearly visible on all color schemes.
- AppCode Directory icons no longer clash with the light themes.
- DataGrip Foreign Key column indicator now conforms to the Doki Icon Theme.
- The "Open" icon in the toolbar conforms to the current color scheme completely now.
- Project Tree Directory icons are now more visible in the light themes.

# 2.2.0

- Compatible with Android Studio release candidate 3.3.

# 2.1.0

- Updated to Material UI version 3.3.0
    - For best results please consider upgrading your JetBrains IDEs to build 2018.3.2 or greater 

# 2.0.0

- You can use **Material UI** and **The Doki-Doki Theme** side-by-side now!

  - Installing the Material UI and the Doki-Doki Theme will now work independently of each other! ᕙ(⇀‸↼‶)ᕗ
  - Switching between themes should work fairly seamlessly.
    - For best results you could restart the IDE to remove theme residue.
  
- The 2018.3 builds of the JetBrain's IDEs are now better supported!
  - You should probably move to those updated IDEs if you have not already done so. ᕕ(ᐛ)ᕗ
  
- All known progress indicators now conform more to the icon update
    
# 1.6.1

- Updated Material UI Core to v2.10.6 
    - Small Syntax highlight fixes
    - More File icons
    - Less font issues

# 1.6.0

- Enhanced UnInstallation Experience.
    - When you choose to uninstall/deactivate the plugin, DDLC artifacts will no longer remain in your IDE. (No more special steps)

# 1.5.1

- Progress Indicators now conform more to the icon update.

# 1.5.0

- More Doki-Doki inspired IDE icons! 💕

# 1.4.0

- Now up do date with Material UI version 2.9.5!
    - New and Fixes for Material Icons is the most noticeable. 

# 1.3.2

- A bunch of small tweaks!
    - Monika's Writing Tip of the Day works the same throughout all ides!
    - Inactive text in the IDE now is readable.
    - Natsuki's light theme's dropdown row selections look the same on all operating systems.
    - Removed unnecessary DDLC Toolbar options.
        - You can still fully configure the plugin in the advanced settings (eg. "Settings -> Appearance & Display -> DDLC Theme").   

# 1.3.1

- Updated the location as to where local chibis are stored.
    - You should no longer find a random club_member directory in places it should not be :)

# 1.3.0

- Updated the initial user experience to let you know that the chibis will stick around when you uninstall the plugin
- More remembering your preference as to when you want the club members enabled or not.
- Chibis and Background images are no longer on by default (if you have them on now you should be okay :) 

# 1.2.3

- When editing a non-project file (in dark mode), it is to easy to distinguish that you are currently doing that.

# 1.2.2

- When at a paused breakpoint and navigating the call stack, the execution point now to conforms to the color scheme of the current club member.

# 1.2.1

> Sometimes, when you're writing a poem - or a story - your brain gets fixated on a specific point. If you try so hard to make it perfect, then you'll never make any progress. Just force yourself to get something down on the paper, and tidy it up later! Another way to think about it is this: if you keep your pen in the same spot for too long, you'll just get a big dark puddle of ink. So just move your hand, and go with the flow!

# 1.2.0

- Android Studio 3.1 Support!
    - Running the plugin in the most up to date actually works (unlike when I said it worked when it actually didn't ;)
    
# 1.1.0

- Windows Support!
    - Running the plugin in any JetBrains IDE now looks like it should (less grey, more color)

# 1.0.0

- Dark Themes are now available!!
    - Your favorite club member now has a light and dark theme!
    - Simply just `Toggle Dark Mode` to switch between light and dark!
    - Dark Chibi's can be swapped using `Swap Chibi` (for less yandere club members).
- The Menu and Right Click Drop down windows no longer have different color separators.
- `Toggle Joyfulness` became `Swap Chibi`
- Automatic File Scope color application.
- Enhanced Light Theme Contrast (Most notably title headers and primary buttons) 

# 0.6.1

- Up to date with upstream's version 2.8.3

# 0.6.0

- Up to date with upstream's version 2.8.2 

# 0.5.3

- Version control annotations are now actually usable.

# 0.5.2

- Added Club Member Chibi Support on Windows!
- More able to hide the background image when club members are toggled.

# 0.5.1

- Changed "Monkia" to "Monika"
    
# 0.5.0

- Now up to date with [v2.7.0](https://github.com/ChrisRM/material-theme-jetbrains/releases/tag/v2.7.0)
    - 2018.2 Build Support!
    - Theme Wizard!
    
# 0.4.7

- The chibis now have toggleable emotions!

# 0.4.6

- Color coordination for themes should remain consistent for intellij 2018.2+ :)

# 0.4.5

- Enhanced lookup window usability.

# 0.4.4

- Updated to upstreams' version of 2.4.1.2.

# 0.4.3

- Editor frames now have images of your current club member of choice.

# 0.4.2-beta

- Increased usability of Sayori's theme when comparing differences.

# 0.4.1-beta

- Increased usability of the code lookup/autocomplete window.

# 0.4.0-beta

- Now up to date with [2.4.1](https://github.com/doki-theme/material-theme-jetbrains/releases/tag/v2.4.1-2018.1)

# 0.3.1-beta
- Readable file differences
- Slight change to Monika's editor colors
- Better VCS line changed colors. 

# 0.3.0-beta
- Fork is now up to date with version [2.4.0.3](https://github.com/doki-theme/material-theme-jetbrains/releases/tag/v2.4.0.1-2018.1)
    - This means tha the google analytics is in here as well 

# 0.2.0-beta
- Testing plugin publishing
- New Color Schemes for each Club Member (Not Final)
- Fixed issue with WebStorm not showing club members.

# 0.1.1-beta
- Better plugin links :)

# 0.1.0-beta
- Release
