package com.leeeyou123.samplelivedata

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.leeeyou123.samplelivedata.api.WanAndroidService
import com.leeeyou123.samplelivedata.data.City
import com.leeeyou123.samplelivedata.viewmodel.RandomViewModel
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val randomLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    // Default creation method
    //    private val viewModel: RandomViewModel by lazy {
    //        ViewModelProvider.NewInstanceFactory().create(RandomViewModel::class.java)
    //    }

    // Use the 'by viewModels()' Kotlin property delegate from the activity-ktx artifact
    // implementation "androidx.activity:activity-ktx:1.2.3"
    private val viewModel: RandomViewModel by viewModels()

    private val cityLiveData: MutableLiveData<City> by lazy {
        MutableLiveData<City>()
    }
    private val cityList by lazy {
        listOf(
            City("ShenZhen", 5),
            City("ChangSha", 3),
            City("GuangZhou", 5),
            City("BeiJing", 5),
            City("WuHan", 4)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())

        useLiveDataWithAlone()
        useLiveDataWithViewModel()
        dataTransform()
        dataTransform2()
        useMediator()
        useCoroutine()
        testViewModel()
    }

    private fun testViewModel() {
        btn_testViewModel.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }
    }

    private fun useCoroutine() {
        btn_coroutine.setOnClickListener {
            liveData {
                emit(WanAndroidService.create().fetchArticles())
            }.observe(this, Observer {
                tv_coroutine.text = it.data.datas.getOrNull(0)?.toString()
            })
        }
    }

    private fun useMediator() {
        val localData = MutableLiveData<City>()
        val serverData = MutableLiveData<City>()
        val mediator = MediatorLiveData<City>()
        val observer: (t: City) -> Unit = {
            mediator.value = it
        }
        mediator.addSource(localData, observer)
        mediator.addSource(serverData, observer)
        btn_mediator.setOnClickListener {
            val index = Random().nextInt(5)
            val city = cityList[index]
            if (index % 2 == 0) {
                city.from = "localData"
                localData.value = city
            } else {
                city.from = "serverData"
                serverData.value = city
            }
        }
        mediator.observe(
            this,
            Observer {
                tv_mediator.text = "from: ${it.from} , city: ${it.name} , star: ${it.star}"
            })
    }

    private fun dataTransform2() {
        btn_data_transform2.setOnClickListener {
            cityLiveData.value = cityList[Random().nextInt(5)]
        }
        Transformations.switchMap(cityLiveData) {
            MutableLiveData(it.name)
        }.observe(this, Observer { tv_data_transform2.text = it })
    }

    private fun dataTransform() {
        btn_data_transform.setOnClickListener {
            cityLiveData.value = cityList[Random().nextInt(5)]
        }
        Transformations.map(cityLiveData) {
            it.name
        }.observe(this, Observer { tv_data_transform.text = it })
    }

    private fun useLiveDataWithViewModel() {
        btn_create_random_num2.setOnClickListener {
            viewModel.obtainRandom().value = Random().nextInt(100)
        }
        viewModel.obtainRandom().observe(this, Observer { tv_show_random2.text = it.toString() })
    }

    private fun useLiveDataWithAlone() {
        btn_create_random_num.setOnClickListener {
            randomLiveData.value = Random().nextInt(100)
        }
        randomLiveData.observe(this, Observer { tv_show_random.text = it.toString() })
    }
}