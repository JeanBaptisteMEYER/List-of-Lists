package com.jbm.mistplaychallenge.ui.bonus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jbm.mistplaychallenge.R

class BonusFragment : Fragment() {

    private lateinit var bonusViewModel: BonusViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bonusViewModel =
                ViewModelProvider(this).get(BonusViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bonus, container, false)
        val textView: TextView = root.findViewById(R.id.text_bonus)
        bonusViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}