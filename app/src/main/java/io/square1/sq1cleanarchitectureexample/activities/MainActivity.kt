package io.square1.sq1cleanarchitectureexample.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.square1.sq1cleanarchitectureexample.adapter.UsersAdapter
import io.square1.sq1cleanarchitectureexample.mappers.BidirectionalMapper
import io.square1.sq1cleanarchitectureexample.model.UIUserModel
import io.square1.sq1cleanarchitectureexample.viewmodel.GetUsersViewModel
import io.square1.storage.model.RLMUserModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var userList = ArrayList<UIUserModel>()
    private val mapper = BidirectionalMapper()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(io.square1.sq1cleanarchitectureexample.R.layout.activity_main)

        val getUsersViewModel = ViewModelProviders.of(this).get(GetUsersViewModel::class.java)
        setupRecycler(userList)

        getUsersViewModel.flowableToLiveData.observe(this,
            Observer<List<RLMUserModel>> { t ->
                t?.let {
                    for (rlmUser:RLMUserModel in ArrayList(t)) {
                        userList.add(mapper.mapToUI(rlmUser))
                    }


                }
                rvUsers.adapter?.notifyDataSetChanged()
            })
    }

    fun setupRecycler(userList: ArrayList<UIUserModel>) {
        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.adapter = UsersAdapter(this, userList)
    }
}
