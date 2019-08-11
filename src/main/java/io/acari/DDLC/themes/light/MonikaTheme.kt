/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package io.acari.DDLC.themes.light

import com.chrisrm.ideaddlc.utils.MTAccents
import io.acari.DDLC.themes.DokiDokiTheme
import java.util.stream.Stream
import javax.swing.plaf.ColorUIResource

class MonikaTheme : DokiDokiTheme("monika", "Monika", false, "Monika") {
    override fun getCompletionPopupBackgroundColor(): String = backgroundColorString

    override fun getClubMember(): String = "just_monika.png"

    override fun joyfulClubMember(): String = "just_monika_joy.png"
    override fun getSelectionBackground(): String = SELECTION_BACKGROUND

    override fun getButtonForegroundColor(): String = "14610D"

    override fun getDisabled(): String = DISABLED
    override fun getAccentColor(): String {
        return MTAccents.BREAKING_BAD.hexColor
    }

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xD8FAD4)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x487A52)

    override fun getNotificationsColorString(): String = "C3E88D"

    override fun getTreeSelectionBackgroundColorString(): String = "7fdf70"

    override fun getTreeSelectionForegroundColorString(): String = "000000"

    override fun getHighlightColorString(): String = "ACF3A4"

    override fun getSecondBorderColorString(): String = "d3e1e8"

    override fun getTableSelectedColorString(): String = "7fdf70"

    override fun getContrastColorString(): String = selectionBackgroundColorString

    override fun getDisabledColorString(): String = "000000"

    override fun getSecondaryBackgroundColorString(): String = "D5FAC7"

    override fun getInactiveColorString(): String = "C7EBC0"

    override fun getButtonColorString(): String = "b4fbae"

    override fun getButtonBackgroundColor(): String = "B2F4B0"

    override fun getSelectionForegroundColorString(): String = "447152"

    override fun getSelectionBackgroundColorString(): String = "ADEBA8"

    override fun getTextColorString(): String = "4d6e80"

    override fun getForegroundColorString(): String = "41545E"

    override fun getBackgroundColorString(): String = "D9FAD7"

    override fun getButtonBackgroundResources(): Stream<String> = Stream.concat(super.getButtonBackgroundResources(), Stream.of(
            "Button.mt.color1",
            "Button.mt.background"
    ))

    override fun getMenuBarColorString(): String = "eea588"

    override fun getBorderColorString(): String = "C1FAC6"

    override fun getStartColor(): String = "5FAA51"

    override fun getStopColor(): String = "5EEF1B"

    override fun getNonProjectFileScopeColor(): String = "d0ffc1"

    companion object {
        const val BACKGROUND: String = "fffcfc"
        const val FOREGROUND: String = "A7ADB0" // 167, 173, 176
        const val TEXT: String = "A7ADB0" // 167, 173, 176
        const val SELECTION_BACKGROUND: String = "FFFFFF" // 84, 110, 122
        const val SELECTION_FOREGROUND: String = "000000"
        const val LABEL: String = "546E7A" // 84, 110, 122
        const val DISABLED: String = "81d7f7"//not really important
        const val NON_PROJECT_FILES: String = "feffed"
        const val TEST_FILES: String = "bbff7e"
    }
}
