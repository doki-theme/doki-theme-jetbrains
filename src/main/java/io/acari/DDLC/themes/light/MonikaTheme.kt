/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Chris Magnussen and Elior Boukhobza
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
import java.awt.Color
import java.util.stream.Stream
import javax.swing.plaf.ColorUIResource

class MonikaTheme : DokiDokiTheme("monika", "Monika", false, "Monika") {

    override fun getClubMember(): String = "just_monika.png"
    override fun joyfulClubMember(): String = "just_monika_joy.png"

    override fun getSelectionBackground(): String = SELECTION_BACKGROUND

    override fun getButtonForegroundColor(): String = "14610D"
    override fun getDisabled(): String = DISABLED

    override fun getAccentColor(): String {
        return MTAccents.BREAKING_BAD.hexColor
    }

    override fun getBackgroundColorResource(): ColorUIResource = ColorUIResource(0xB5FAAA)

    override fun getForegroundColorResource(): ColorUIResource = ColorUIResource(0x487A52)

    override fun getNotificationsColorString(): String = "C3E88D"

    override fun getTreeSelectionBackgroundColorString(): String = "7fdf70"

    override fun getTreeSelectionForegroundColorString(): String = "000000"

    fun getButtonHighlightColorString(): String = "F2F1F1"

    override fun getHighlightColorString(): String = "8ef36f"

    override fun getSecondBorderColorString(): String = "d3e1e8"

    override fun getTableSelectedColorString(): String = "def7a5"

    override fun getContrastColorString(): String = selectionBackgroundColorString

    override fun getDisabledColorString(): String = "000000"

    override fun getSecondaryBackgroundColorString(): String = "B0FA9C"

    override fun getInactiveColorString(): String = "C7EBC0"

    override fun getButtonColorString(): String = "b4fbae"

    override fun getButtonBackgroundColor(): String = "a0f49a"

    override fun getSelectionForegroundColorString(): String = "447152"

    override fun getSelectionBackgroundColorString(): String = "99eb99"

    override fun getTextColorString(): String = "4d6e80"

    override fun getForegroundColorString(): String = "41545E"

    override fun getBackgroundColorString(): String = "AEFAB2"

    override fun getButtonBackgroundResources(): Stream<String> = Stream.concat(super.getButtonBackgroundResources(), Stream.of(
            "Button.mt.color1",
            "Button.mt.background"
    ))


    override fun getBorderColor(): Color = Color.getHSBColor(62f, 91f, 149f)

    override fun getStartColor(): String = "5FAA51"

    override fun getStopColor(): String = "5EEF1B"

    override fun getNonProjectFileScopeColor(): String = "EBFFE2"





    companion object {
        val BACKGROUND = "fffcfc"
        val FOREGROUND = "A7ADB0" // 167, 173, 176
        val TEXT = "A7ADB0" // 167, 173, 176
        val SELECTION_BACKGROUND = "FFFFFF" // 84, 110, 122
        val SELECTION_FOREGROUND = "000000"
        val LABEL = "546E7A" // 84, 110, 122
        val DISABLED = "81d7f7"//not really important
        val NON_PROJECT_FILES = "feffed"
        val TEST_FILES = "bbff7e"
    }
}
