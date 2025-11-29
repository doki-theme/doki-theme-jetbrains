package io.unthrottled.doki.util

import com.intellij.openapi.Disposable
import com.intellij.util.Alarm
import java.util.LinkedList

fun interface Debouncer {
  fun debounce(toDebounce: () -> Unit)
}

fun interface BufferedDebouncer<T> {
  fun debounceAndBuffer(
    t: T,
    onDebounced: (List<T>) -> Unit,
  )
}

class AlarmDebouncer<T>(
  private val interval: Int,
) :
  Debouncer,
    BufferedDebouncer<T>,
    Disposable {
  private val alarm: Alarm = Alarm(Alarm.ThreadToUse.SWING_THREAD)

  override fun debounce(toDebounce: () -> Unit) {
    performDebounce({
      alarm.addRequest(toDebounce, interval)
    })
  }

  private val buffer = LinkedList<T>()

  override fun debounceAndBuffer(
    t: T,
    onDebounced: (List<T>) -> Unit,
  ) {
    performDebounce({
      alarm.addRequest(
        {
          buffer.add(t)
          onDebounced(buffer.toMutableList())
          buffer.clear()
          previousOnDebounce = null
        },
        interval,
      )
    }) {
      buffer.push(t)
    }
  }

  @Volatile
  private var previousOnDebounce: (() -> Unit)? = null

  private fun performDebounce(
    setupDebounce: () -> Unit,
    onDebounced: () -> Unit = {},
  ) {
    previousOnDebounce?.invoke()
    previousOnDebounce = onDebounced
    alarm.cancelAllRequests()
    setupDebounce()
  }

  override fun dispose() {
    previousOnDebounce = null
    alarm.dispose()
  }
}
