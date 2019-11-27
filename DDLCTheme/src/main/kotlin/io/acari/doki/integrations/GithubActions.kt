package io.acari.doki.integrations

import com.intellij.ide.BrowserUtil
import java.awt.event.ActionListener
import java.net.URI

object GithubActions {

  fun createChangelogAction(): ActionListener = ActionListener{
    val uri = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/blob/master/docs/CHANGELOG.md")
    BrowserUtil.browse(uri)
  }

  fun createViewIssuesAction(): ActionListener = ActionListener{
    val uri = URI("https://github.com/cyclic-reference/ddlc-jetbrains-theme/issues")
    BrowserUtil.browse(uri)
  }
}