package com.gleysonabreu.cardatronicoempresa.fragment


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gleysonabreu.cardatronicoempresa.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_add_cardapio.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false);

        // Inflate the layout for this fragment
        return viewOfLayout;
    }

    fun newInstance() : HomeFragment{
        return HomeFragment();
    }

}
