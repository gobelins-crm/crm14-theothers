package crm.gobelins.theothers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 12;
    private ImageView mImageView;
    private ShareActionProvider mShareActionProvider;
    private Uri mImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode && REQUEST_IMAGE_CAPTURE == requestCode) {

            Bitmap imageBitmap = getThumbnailBitmap(mImageUri, mImageView.getWidth(), mImageView.getHeight());
            mImageView.setImageBitmap(imageBitmap);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/jpeg");
            sendIntent.putExtra(Intent.EXTRA_STREAM, mImageUri);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "title");
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.thumbnail);

        findViewById(R.id.button_call).setOnClickListener(this);
        findViewById(R.id.button_map).setOnClickListener(this);
        findViewById(R.id.button_web).setOnClickListener(this);
        findViewById(R.id.button_email).setOnClickListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_photo).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_call:
                performCallIntent();
                break;
            case R.id.button_map:
                performMapIntent();
                break;
            case R.id.button_web:
                performWebIntent();
                break;
            case R.id.button_email:
                performEmailIntent();
                break;
            case R.id.button_send:
                performSendIntent();
                break;
            case R.id.button_photo:
                performPhotoIntent();
                break;
        }
    }

    private void performPhotoIntent() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            mImageUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "theothers_" +
                    String.valueOf(System.currentTimeMillis()) + ".jpg"));
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private void performSendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TITLE, "Mon titre");
        intent.putExtra(Intent.EXTRA_TEXT, "Mon texte");
        intent.setType("text/plain");

        Intent chooser = Intent.createChooser(intent, getString(R.string.chooser_title));
        startActivity(chooser);
    }

    private void performEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"georges@moustaki.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ceci est un beau sujet");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Ceci est un beau texte");
        startActivity(emailIntent);
    }

    private void performWebIntent() {
        Uri address = Uri.parse("http://ddg.gg/");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(webIntent);
    }

    private void performMapIntent() {
        Uri location = Uri.parse("geo:0,0?q=La+Clusaz");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    private void performCallIntent() {
        Uri number = Uri.parse("tel:911");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(callIntent, 0 /*Or PackageManager.MATCH_DEFAULT_ONLY*/);
        if (activities.size() > 0) {
            startActivity(callIntent);
        }
    }

    private Bitmap getThumbnailBitmap(Uri uri, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / width, photoH / height);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(uri.getPath(), bmOptions);
    }
}
