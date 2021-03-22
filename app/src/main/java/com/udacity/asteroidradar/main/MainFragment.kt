package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Sets the adapter
        binding.asteroidRecycler.adapter =
                AsteroidAdapter(AsteroidAdapter.OnClickListener {
                    viewModel.displayAsteroidsDetails(it)
                })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController()
                        .navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.asteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
                when (item.itemId) {
                    R.id.show_week -> {
                        DayWeekAllFilter.SHOW_WEEK
                    }
                    R.id.show_today -> {
                        DayWeekAllFilter.SHOW_TODAY
                    }
                    else -> {
                        DayWeekAllFilter.SHOW_ALL
                    }
                }
        )
        return true
    }
}
