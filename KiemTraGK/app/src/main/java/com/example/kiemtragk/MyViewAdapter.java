package com.example.kiemtragk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.kiemtragk.Model.ItemModel;

import java.text.DecimalFormat;
import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewAdapter.ViewHolder>  {
    List<ItemModel> models;
    int mResource;
    Context mContext;

    public MyViewAdapter(Context context, int resource, List<ItemModel> objects){
        this.mContext = context;
        this.mResource = resource;
        this.models = objects;
    }
    @NonNull
    @Override
    public MyViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(mResource,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter.ViewHolder viewHolder, final int i) {
        final ItemModel model = models.get(i);
        DecimalFormat limit = new DecimalFormat( "#,###,###" );
        viewHolder.edtProductName.setText(model.getProduct_Name());
        viewHolder.edtPrice.setText(limit.format( model.getPrice() ) + " VND");
        viewHolder.edtDescription.setText(model.getDescription());
        viewHolder.edtProducer.setText(model.getProducer());


    }



    @Override
    public int getItemCount() {
        return models.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private EditText edtProductName;
        private EditText edtPrice;
        private  EditText edtDescription;
        private EditText edtProducer;
        private LinearLayout liner1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.edtProductName = itemView.findViewById(R.id.edtProductName);
            this.edtPrice = itemView.findViewById(R.id.edtPrice);
            this.edtDescription = itemView.findViewById(R.id.edtDescription);
            this.edtProducer = itemView.findViewById(R.id.edtProducer);
            this.liner1 = itemView.findViewById( R.id.liner1 );

            liner1.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle( "Select your option" );
            SubjectActivity.model = models.get( this.getAdapterPosition() );
            contextMenu.add( this.getAdapterPosition(), 0 , 0, "Insert" );
            contextMenu.add( this.getAdapterPosition(), 1 , 1, "Update" );
            contextMenu.add( this.getAdapterPosition(), 2 , 2, "Delete" );
        }
    }
}
