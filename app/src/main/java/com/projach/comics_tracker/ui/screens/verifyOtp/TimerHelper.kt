package com.projach.comics_tracker.ui.screens.verifyOtp

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TimerHelper(
    private val scope: CoroutineScope,
    private val handle: SavedStateHandle,
    private val onFinished: () -> Unit,
) {
    private val _remainingMs = MutableStateFlow(0L)
    val remainingMs: StateFlow<Long> = _remainingMs

    private var job: Job? = null
    private var endTime: Long?
        get() = handle["end_time"]
        set(t) {
            handle["end_time"] = t
        }

    init {
        endTime?.let(::tick)
    }

    fun start(durationMs: Long) {
        val end = SystemClock.elapsedRealtime() + durationMs
        endTime = end
        tick(end)
    }

    fun stop() {
        job?.cancel()
        endTime = null
        _remainingMs.value = 0
    }

    private fun tick(end: Long) {
        job?.cancel()
        job = scope.launch {
            while (true) {
                val left = (end - SystemClock.elapsedRealtime()).coerceAtLeast(0)
                _remainingMs.value = left
                if (left == 0L) {
                    endTime = null
                    onFinished()
                    break
                }
                delay((left % 1000L + 1).milliseconds)
            }
        }
    }
}