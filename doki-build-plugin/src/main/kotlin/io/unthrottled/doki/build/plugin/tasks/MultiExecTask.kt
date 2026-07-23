package io.unthrottled.doki.build.plugin.tasks

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class MultiExecTask : Exec() {

  init {
    group = "doki"
    description = "Executes a series of commands in the command line"
    startDirectory.convention(project.layout.projectDirectory)
  }

  enum class OSType {
    LINUX, WINDOWS, AUTO
  }

  @get:InputDirectory
  abstract val startDirectory: DirectoryProperty

  @get:Input
  abstract val commandExecMap: MapProperty<OSType, List<String>>

  @TaskAction
  override fun exec() {
    val startDir = startDirectory.get().asFile
    workingDir(startDir)
    val isWindows:Boolean = System.getProperty("os.name").lowercase().contains("windows")
    val commandsMap: Map<OSType,List<String>> = commandExecMap.get()
    val joinedCmds: String = joinedCommands(isWindows,commandsMap)
    if (isWindows) {
      commandLine("cmd", "/c", joinedCmds)
    } else {
      commandLine("sh", "-c", "'$joinedCmds'")
    }
    super.exec()
  }

  fun joinedCommands(isWindows: Boolean, cmdMap: Map<OSType,List<String>>): String{
    val osType: OSType = if (isWindows) OSType.WINDOWS else OSType.LINUX
    val commands = cmdMap[OSType.AUTO] ?: cmdMap[osType]
    return commands?.joinToString(" && ") ?: ""
  }
}