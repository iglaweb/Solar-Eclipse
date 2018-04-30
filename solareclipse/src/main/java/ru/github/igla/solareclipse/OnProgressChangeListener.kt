package ru.github.igla.solareclipse

internal interface OnProgressChangeListener<T> {
    fun onChangeValue(fraction: T)
}