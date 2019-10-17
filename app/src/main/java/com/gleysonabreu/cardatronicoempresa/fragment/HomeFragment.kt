package com.gleysonabreu.cardatronicoempresa.fragment


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.gleysonabreu.cardatronicoempresa.R
import com.gleysonabreu.cardatronicoempresa.adapter.AdapterCardapio
import com.gleysonabreu.cardatronicoempresa.helper.SettingsFirebase
import com.gleysonabreu.cardatronicoempresa.model.Cardapio
import com.google.android.material.chip.Chip
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.*
import kotlinx.android.synthetic.main.fragment_add_cardapio.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.recyclerMenuItems

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private var listMenu: ArrayList<Cardapio> = ArrayList<Cardapio>();
    private lateinit var firebaseRef: DatabaseReference;
    private lateinit var childEvent: ChildEventListener;
    private lateinit var cardapioRef: DatabaseReference;
    private lateinit var  linearLayoutManager: LinearLayoutManager;
    private lateinit var  viewOfLayout: View;
    private var keyList: ArrayList<String> = ArrayList<String>();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false);

        linearLayoutManager = LinearLayoutManager(activity);

        //Settings Firebase
        firebaseRef = SettingsFirebase.getFirebase();
        cardapioRef = firebaseRef.child("cardapio");

        // Settings Adapter
        viewOfLayout.recyclerMenuItems.adapter = AdapterCardapio(listMenu, activity!!)
        viewOfLayout.recyclerMenuItems.layoutManager = linearLayoutManager;


        // Inflate the layout for this fragment
        return viewOfLayout;
    }

    fun newInstance() : HomeFragment{
        return HomeFragment();
    }

    override fun onStart() {
        super.onStart()
        listMenu.clear();
        keyList.clear();

        childEvent = cardapioRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                var cardapioChanged = p0.getValue(Cardapio::class.java);
                var index = keyList.indexOf(p0.key);

                if (cardapioChanged != null) {
                    listMenu.set(index, cardapioChanged);
                    viewOfLayout.recyclerMenuItems.adapter?.notifyDataSetChanged();
                };

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                var newItemmenu = p0.getValue(Cardapio::class.java);
                if (newItemmenu != null) {
                    listMenu.add(newItemmenu)
                    p0.key?.let { keyList.add(it) };
                };

                viewOfLayout.recyclerMenuItems.adapter?.notifyDataSetChanged();


            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var index: Int = keyList.indexOf(p0.key);
                listMenu.removeAt(index);
                keyList.removeAt(index);
                viewOfLayout.recyclerMenuItems.adapter?.notifyDataSetChanged();

            }

        });

    }

    override fun onStop() {
        super.onStop()
        cardapioRef.removeEventListener(childEvent);
    }

}
