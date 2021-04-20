package io.unthrottled.doki.util

interface WhenComparator<T> {
  fun test(other: T): Boolean
  operator fun contains(other: T) = test(other)
}

fun <T : Comparable<T>> lt(value: T) = object : WhenComparator<T> {
  override fun test(other: T) = other < value
}

fun <T : Comparable<T>> gt(value: T) = object : WhenComparator<T> {
  override fun test(other: T) = other > value
}
