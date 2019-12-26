import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class BuildThemes: DefaultTask() {
  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run(){
    println("Yeet")
  }
}