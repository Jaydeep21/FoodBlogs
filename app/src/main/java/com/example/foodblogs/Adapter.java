package com.example.foodblogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.sql.Blob;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    private ItemClickListener itemClickListener;
    private List<ModelClass> modelClassesList;
    private Context ctx;


    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Adapter(Context ctx, List<ModelClass> modelClassesList) {
        this.ctx = ctx;
        this.modelClassesList = modelClassesList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {
        ModelClass mc = modelClassesList.get(i);
        viewholder.imageView.setImageBitmap(BitmapFactory.decodeFile(mc.getImage()));
        viewholder.dishName.setText(mc.getTitle());
        viewholder.description.setText(mc.getDescription());

        viewholder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ModelClass item = modelClassesList.get(i);
                        itemClickListener.onClick(item.getEmail(),item.getTitle(),item.getCusine(),item.getCourse(),item.getImage(),item.getDescription(),item.getRecipie());
                        //item.getVideo();
                    }
                }
        );
        //viewholder.setData(resource,title,description);
    }

    @Override
    public int getItemCount() {
        return modelClassesList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private TextView email;
        private TextView dishName;
        private TextView cusine;
        private TextView course;
        private ImageView imageView;
        private VideoView video;
        private TextView description;
        private TextView recipie;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            dishName = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);

            //setData(imageView,dishName,description);
        }
        private void setData(byte[] image, String titleText, String descriptionText){
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, itemView.getWidth(),
                    itemView.getHeight(), false));
            dishName.setText(titleText);
            description.setText(descriptionText);

        }
    }
    public interface ItemClickListener {
        void onClick(String email, String dishName, String cusine, String course, String image,  String description, String recipie);
        // TODO: Bitmap video
    }
}
