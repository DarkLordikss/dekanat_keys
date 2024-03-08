package com.liid.dekanatkeys.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.liid.dekanatkeys.R
import com.liid.dekanatkeys.databinding.ActivitySheduleBinding
import com.liid.dekanatkeys.fragments.ChooseBuildingFragment
import com.liid.dekanatkeys.fragments.ChooseBuildingFragmentDataPass
import com.liid.dekanatkeys.fragments.ChooseClassroomFragment
import com.liid.dekanatkeys.fragments.ChooseClassroomFragmentDataPass
import com.liid.dekanatkeys.fragments.SheduleFragment
import com.liid.dekanatkeys.helpers.Log

class SheduleActivity : AppCompatActivity(), ChooseBuildingFragmentDataPass,
    ChooseClassroomFragmentDataPass {
    private lateinit var binding: ActivitySheduleBinding
    private var building: String? = null
    private var classroom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.selectedItemId = R.id.bottom_calender_view



        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_account_circle -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_calender_view -> true
                R.id.bottom_check_circle -> {
                    startActivity(Intent(applicationContext, MyApplicationsActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_key -> {
                    startActivity(Intent(applicationContext, KeysActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onChooseBuildingFragmentDataPass(data: String) {
        building = data
        val ft = supportFragmentManager.beginTransaction()
        val fragment1 = ChooseClassroomFragment.newInstance(data)
        ft.replace(binding.fragmentContainerView.id, fragment1)
        ft.commit()
    }

    override fun onChooseClassroomFragmentDataPass(data: String) {
        classroom = data
        val ft = supportFragmentManager.beginTransaction()
        val fragment1 = SheduleFragment.newInstance(building!!, classroom!!)
        ft.replace(binding.fragmentContainerView.id, fragment1)
        ft.commit()
    }
}