package crm.gobelins.theothers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_call).setOnClickListener(this);
        findViewById(R.id.button_map).setOnClickListener(this);
        findViewById(R.id.button_web).setOnClickListener(this);
        findViewById(R.id.button_email).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }
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
}
