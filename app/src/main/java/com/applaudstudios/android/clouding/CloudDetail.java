package com.applaudstudios.android.clouding;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wjplaud83 on 4/7/17.
 */

public class CloudDetail extends AppCompatActivity implements BasicDialogFragment.BasicDialogListener {

    //   EditText addItemsField;
    //  EditText subtractItemsField;

    int cloudId;
    String cloudName;
    String cloudImagePath;
    float cloudRating;
    String cloudDescription;
    // int productQuantity;

    //  TextView detailProductQuantity;

    Cloud detailCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_detail);

        int queryId = getIntent().getIntExtra("id", -1);

        cloudAssignment(queryId);


        ImageView detailCloudImage = (ImageView) findViewById(R.id.dt_cloud_image);
        TextView detailCloudName = (TextView) findViewById(R.id.dt_cloud_name);
        TextView detailCloudRating = (TextView) findViewById(R.id.dt_cloud_rating);
        TextView detailCloudDescription = (TextView) findViewById(R.id.dt_cloud_description);

        detailCloudImage.setImageBitmap(ImageTools.imageProcess(cloudImagePath));
        detailCloudName.setText(cloudName);
        detailCloudDescription.setText(this.getString(R.string.description) + cloudDescription);
        detailCloudRating.setText(getString(R.string.cloud_rating) + ratingFormat(cloudRating));
    }

    public static String ratingFormat(float rating) {
        if (rating == (long) rating)
            return String.format("%d", (long) rating);
        else
            return String.format("%s", rating);
    }

    private void cloudAssignment(int queryId) {
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        detailCloud = dbHelp.getCloud(queryId);

        cloudId = detailCloud.getId();
        cloudName = detailCloud.getName();
        cloudImagePath = detailCloud.getImageUri();
        cloudRating = detailCloud.getRating();
        cloudDescription = detailCloud.getDescription();
    }

    public void deleteCloud(View view) {
        DialogFragment newFragment = new BasicDialogFragment();
        newFragment.show(getFragmentManager(), "delete");
    }

    public void orderMore(View view) {
        String[] emails = new String[]{getString(R.string.email_address)};
        String subject = getString(R.string.request_for_a_new) + cloudName
                + getString(R.string.shipment);
        String text = getString(R.string.hello) + getString(R.string.request_main)
                + cloudName + getString(R.string.units) + cloudDescription + ".";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void backToList(View view) {
        this.finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        dbHelp.deleteCloud(detailCloud.getId());
        this.finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Nothing here...
    }
/**
 public void addItems(View view) {
 //   int quantity = productQuantity;
 String input = addItemsField.getText().toString();

 if (input.isEmpty()) {
 Toast.makeText(this, R.string.empty_add, Toast.LENGTH_LONG).show();
 } else {

 //      int extraQuantity = Integer.valueOf(input);
 DatabaseHelper dbHelp = new DatabaseHelper(this);
 //  dbHelp.updateQuantity(cloudId, quantity + extraQuantity);
 cloudAssignment(cloudId);
 //  detailProductQuantity.setText(this.getString(R.string.quantity) + String.valueOf(productQuantity));

 }

 }

 public void removeItems(View view) {
 //    int quantity = productQuantity;
 String input = subtractItemsField.getText().toString();

 if (input.isEmpty()) {
 Toast.makeText(this, R.string.empty_sub, Toast.LENGTH_LONG).show();
 } else {
 //  int extraQuantity = Integer.valueOf(input);
 //   if (extraQuantity > quantity) {
 Toast.makeText(this, R.string.larger_than_current, Toast.LENGTH_LONG).show();
 return;
 }

 DatabaseHelper dbHelp = new DatabaseHelper(this);
 //    dbHelp.updateQuantity(cloudId, quantity - extraQuantity);
 cloudAssignment(cloudId);
 //    detailProductQuantity.setText(this.getString(quantity) + String.valueOf(productQuantity));

 } **/
}

