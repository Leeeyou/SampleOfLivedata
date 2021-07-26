package com.leeeyou123.samplelivedata

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger.d
import kotlinx.android.synthetic.main.activity_view_model.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ViewModelActivity : AppCompatActivity() {

    private val vm: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)
        initViewModel()
        observerLifecycle()
    }

    private fun initViewModel() {
        vm.setListener { count ->
            runOnUiThread { count.toString().also { tv_timer.text = it } }
        }
        vm.startTimer()
    }

    private fun observerLifecycle() {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                d("onCreate , viewModel is $vm")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                d("onStart")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                d("onResume")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                d("onPause")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                d("onStop")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                d("onDestroy")
            }

        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        d("onRestoreInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        d("onSaveInstanceState")
    }

}

class TimerViewModel : ViewModel() {
    private var count: AtomicInteger = AtomicInteger(0)
    private var onChangeListener: ((Long) -> Any)? = null
    private var timerIsRunning = false
    private val timer by lazy { Timer() }
    private val timerTask by lazy {
        object : TimerTask() {
            override fun run() {
                count.incrementAndGet()
                onChangeListener?.invoke(count.toLong())
            }
        }
    }

    fun setListener(listener: ((Long) -> Any)?) {
        onChangeListener = listener
    }

    fun startTimer() {
        if (!timerIsRunning) {
            timer.schedule(timerTask, 0, 1000L)
            timerIsRunning = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        d("onCleared , viewModel is $this")
    }
}