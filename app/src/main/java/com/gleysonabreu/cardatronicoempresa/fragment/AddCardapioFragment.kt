package com.gleysonabreu.cardatronicoempresa.fragment


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.activity.CategoryActivity
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Category
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_add_cardapio.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.buttonOpenCategory

/**
 * A simple [Fragment] subclass.
 */
class AddCardapioFragment : Fragment() {

    private lateinit var referenceDatabase: DatabaseReference;
    private lateinit var categorys: DatabaseReference;
    private lateinit var viewOfLayout: View;
    private lateinit var eventListener: ValueEventListener;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_add_cardapio, container, false)

        //Open Add Category
        viewOfLayout.buttonOpenCategory.setOnClickListener {
            var i: Intent = Intent(activity, CategoryActivity::class.java);
            startActivity(i)
        }
            // Settings ChipGroup
            viewOfLayout.chip_group.isSingleSelection = true;


            //Settings firebase
            referenceDatabase = SettingsFirebase.getFirebase();
            categorys = referenceDatabase.child("category");

            eventListener = categorys.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        viewOfLayout.chip_group.removeAllViews();

                        for (cats in p0.children) {
                            var category = cats.getValue(Category::class.java);
                            val chip = Chip(activity);
                            chip.text = category?.nameCategory;
                            chip.setChipBackgroundColorResource(R.color.colorSplash);
                            chip.setTextColor(Color.WHITE);
                            chip.chipCornerRadius = 16F;
                            chip.isCheckable = true;

                            viewOfLayout.chip_group.addView(chip);
                        }

                    }

                });

            viewOfLayout.buttonTest.setOnClickListener {

                var msg = "Chip selecionado: ";
                var chipGroup = viewOfLayout.chip_group;
                var count: Int = chipGroup.childCount;
                if (count == 0) {
                    msg += " nenhum selecionado";
                } else {
                    var i: Int = 0;
                    while (i < count) {
                        var chip: Chip = chipGroup.getChildAt(i) as Chip;
                        if (chip.isChecked == true) {
                            msg += chip.text.toString();
                        }
                        i++;
                    }

                }

                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

            }


            // Inflate the layout for this fragment
            return viewOfLayout;
        }

        fun newInstance(): AddCardapioFragment {
            return AddCardapioFragment();
        }

    override fun onDestroy() {
        super.onDestroy()
        categorys.removeEventListener(eventListener);
        Log.e("ONDESTROY", "Destroiu");
    }


}
