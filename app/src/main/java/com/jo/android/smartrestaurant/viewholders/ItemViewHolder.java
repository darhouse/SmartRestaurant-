package com.jo.android.smartrestaurant.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jo.android.smartrestaurant.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewNAme,textViewDescription;
   public Button buttonPrice;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewNAme=itemView.findViewById(R.id.tv_item_name);
        textViewDescription=itemView.findViewById(R.id.tv_item_description);
        buttonPrice=itemView.findViewById(R.id.button_price);


    }
}
