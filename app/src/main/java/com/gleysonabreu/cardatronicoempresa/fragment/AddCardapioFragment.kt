package com.gleysonabreu.cardatronicoempresa.fragment


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.gleysonabreu.cardatronicoempresa.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*

/**
 * A simple [Fragment] subclass.
 */
class AddCardapioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var viewOfLayout = inflater.inflate(R.layout.fragment_add_cardapio, container, false)

        viewOfLayout.chip_group.isSingleSelection = true;
        val chip: Chip = Chip(activity);
        chip.text = "Kotlin";
        chip.setChipBackgroundColorResource(R.color.colorSplash);
        chip.setTextColor(Color.WHITE)
        chip.chipCornerRadius = 16F;
        chip.isCheckable = true;

        viewOfLayout.chip_group.addView(chip);

        val chip2: Chip = Chip(activity);
        chip2.text = "Java";
        chip2.setChipBackgroundColorResource(R.color.colorSplash);
        chip2.setTextColor(Color.WHITE)
        chip2.chipCornerRadius = 16F;
        chip2.isCheckable = true;

        viewOfLayout.buttonTest.setOnClickListener {

            var msg = "Chip selecionado: ";
            var chipGroup = viewOfLayout.chip_group;
            var count: Int = chipGroup.childCount;
            if( count == 0){
                msg += " nenhum selecionado";
            }else{

                var i: Int = 0;
                while (i < count){
                    var chip: Chip = chipGroup.getChildAt(i) as Chip;
                    if(chip.isChecked == true){
                        msg += chip.text.toString();
                    }
                    i++;
                }

            }

            Toast.makeText(activity,msg, Toast.LENGTH_LONG).show();

        }

        viewOfLayout.chip_group.addView(chip2);

        // Inflate the layout for this fragment
        return viewOfLayout;
    }

    fun newInstance() : AddCardapioFragment{
        return AddCardapioFragment();
    }

}
