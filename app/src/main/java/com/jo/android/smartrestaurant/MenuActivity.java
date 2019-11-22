package com.jo.android.smartrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jo.android.smartrestaurant.model.Item;
import com.jo.android.smartrestaurant.viewholders.ItemViewHolder;

import static com.jo.android.smartrestaurant.UserHomeActivity.CATEGORY_TITTLE_KEY;
import static com.jo.android.smartrestaurant.UserHomeActivity.RESTAURANT_ID_KEY;

public class MenuActivity extends AppCompatActivity {
   private RecyclerView recyclerViewItems;

    private DatabaseReference itemReference;
    private TextView textViewCategoryTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent=getIntent();
        String whichPart=intent.getStringExtra(CATEGORY_TITTLE_KEY);
        String restaurant_id=intent.getStringExtra(RESTAURANT_ID_KEY);
        Toast.makeText(this, whichPart, Toast.LENGTH_SHORT).show();
        recyclerViewItems=findViewById(R.id.recycler_view_iten);
        textViewCategoryTittle=findViewById(R.id.tv_category_tittle);
        textViewCategoryTittle.setText(whichPart);

        recyclerViewItems.setHasFixedSize(true);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));


        itemReference= FirebaseDatabase.getInstance().getReference().child("menus").child(restaurant_id)
                .child(whichPart);

        FirebaseRecyclerOptions<Item> options= new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(itemReference,Item.class)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_item_list,parent,false);
                ItemViewHolder viewHolder=new ItemViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {

                holder.textViewNAme.setText(model.getName());
                holder.textViewDescription.setText(model.getDescription());
                holder.buttonPrice.setText("EGP "+model.getPrice());


            }
        };
        recyclerViewItems.setAdapter(adapter);
        adapter.startListening();

    }
}
