package com.example.review.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.review.Descripition;
import com.example.review.DescripitionBusiness;
import com.example.review.DescripitionPublic;
import com.example.review.HomeActivity;
import com.example.review.R;
import com.example.review.VoteActivity;


public class AddFragment extends Fragment {

    TextView privatebttn, publicbttn, businessbttn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View a= inflater.inflate(R.layout.fragment_add, container, false);

        privatebttn= a.findViewById(R.id.private_revbttn);
        publicbttn= a.findViewById(R.id.public_revbttn);
        businessbttn= a.findViewById(R.id.business_revbttn);

        privatebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), Descripition.class);
                startActivity(intent1);
            }
        });
        
        publicbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                //set background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));
                //set view
                dialog.setContentView(R.layout.public_layout_dialog);
                dialog.show();
            }
        });


        businessbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getActivity());
                //set background transparent
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));
                //set view
                dialog.setContentView(R.layout.buisness_layout_dialog);
                dialog.show();

            }
        });


        return a;



    }


}