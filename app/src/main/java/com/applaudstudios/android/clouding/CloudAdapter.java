package com.applaudstudios.android.clouding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjplaud83 on 4/7/17.
 */

public class CloudAdapter extends RecyclerView.Adapter<CloudAdapter.CloudViewHolder> {

    private Context mContext;
    private List<Cloud> mCloudList = new ArrayList<>();

    public CloudAdapter(Context context, List<Cloud> cloudList) {
        mContext = context;
        mCloudList = cloudList;
    }

    // to work the Recycler View
    public void setCloudList(List<Cloud> cloudList) {
        mCloudList.clear();
        mCloudList.addAll(cloudList);
        notifyDataSetChanged();
    }

    @Override
    public CloudViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.applaudstudios.android.clouding.R.layout.cloud_list, parent, false);

        return new CloudViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CloudViewHolder holder, int position) {

        Cloud currentCloud = mCloudList.get(position);

        /*
        This is way better, and the lost frames warnings are gone. Although, I still think that
        the whole image is getting reloaded every time the Sell button is pressed, only difference
        is that I don't see it slowing down the whole app.
        */
        loadBitmap(currentCloud.getImageUri(), holder.cloudImage);

        holder.cloudName.setText(currentCloud.getName());
        holder.cloudDescription.setText(mContext.getString(com.applaudstudios.android.clouding.R.string.description) + currentCloud.getDescription());
        holder.cloudRating.setText(mContext.getString(com.applaudstudios.android.clouding.R.string.cloud_rating) + ratingFormat(currentCloud.getRating()));

    }

    public static String ratingFormat(float rating) {
        if (rating == (long) rating)
            return String.format("%d", (long) rating);
        else
            return String.format("%s", rating);
    }

    @Override
    public int getItemCount() {
        return (null != mCloudList ? mCloudList.size() : 0);
    }

    private void cloudSale(int position) {
        Cloud cloud = mCloudList.get(position);

      //  if (quantity > 0) {
         DatabaseHelper dbHelp = new DatabaseHelper(mContext);

            //All I want to update is one text view...
            mCloudList.clear();
            mCloudList.addAll(dbHelp.getAllClouds());
            notifyDataSetChanged();

        }
      //  else { Toast.makeText(mContext, com.applaudstudios.android.clouding.R.string.no_inventory, Toast.LENGTH_SHORT).show();



    private void openDetailView(int position) {
        Intent intent = new Intent(mContext, CloudDetail.class);
        Cloud product = mCloudList.get(position);
        int productId = product.getId();
        intent.putExtra("id", productId);
        ((Activity) mContext).startActivityForResult(intent, 2);
    }

    public class CloudViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView cloudImage;
        protected TextView cloudName;
        protected TextView cloudDescription;
        protected TextView cloudRating;
        protected Button sellButton;

        public CloudViewHolder(View view) {
            super(view);
            sellButton = (Button) view.findViewById(com.applaudstudios.android.clouding.R.id.sell_button);
            cloudImage = (ImageView) view.findViewById(com.applaudstudios.android.clouding.R.id.cloud_image);
            cloudName = (TextView) view.findViewById(com.applaudstudios.android.clouding.R.id.cloud_name);
            cloudDescription = (TextView) view.findViewById(com.applaudstudios.android.clouding.R.id.cloud_description);
            cloudRating = (TextView) view.findViewById(com.applaudstudios.android.clouding.R.id.cloud_rating);
            sellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   cloudSale(getLayoutPosition());
                }
            });
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            openDetailView(getLayoutPosition());
        }
    }

    public void loadBitmap(String imagePath, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(imagePath);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String imagePath;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imagePath = params[0];
            return ImageTools.imageProcess(imagePath);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
