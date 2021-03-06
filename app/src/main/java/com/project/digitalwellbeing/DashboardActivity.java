package com.project.digitalwellbeing;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.project.digitalwellbeing.service.DigitalWellBeingService;
import com.project.digitalwellbeing.utils.BrowserObserver;
import com.project.digitalwellbeing.utils.CommonDataArea;
import com.project.digitalwellbeing.utils.CommonFunctionArea;
import com.project.digitalwellbeing.utils.Popup;

import java.util.Date;
import java.util.List;

import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.android.browser.Search;
import me.everything.providers.core.Data;

import static com.project.digitalwellbeing.utils.CommonDataArea.editor;
import static com.project.digitalwellbeing.utils.CommonDataArea.sharedPreferences;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout pairKeyLinearLayout, taskLinearLayout, googleFitLinearLayout, webHistoryLinearLayout, callLogLinearLayout, locationLinearLayout, appUsageLinearLayout, recentActivitiesLinearLayout, lockDeviceLinearLayout;
    TextView pairKeytext, tasktext, googleFitText, webHistoryText, callLogText, locationText, appusageText, recentActivitiesText, lockDeviceText;
    private Toolbar toolbar;
    private BrowserObserver browserObserver;
    private DigitalWellBeingService mDigitalWellBeingService;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        if (checkPermission()) {
            initViews();
            getAllData();

            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mDigitalWellBeingService = new DigitalWellBeingService();
                    mServiceIntent = new Intent(DashboardActivity.this, mDigitalWellBeingService.getClass());
                    if (!isMyServiceRunning(mDigitalWellBeingService.getClass())) {
                        startService(mServiceIntent);
                    }
                    Log.d("I'mahandler","hIiii>>");

                }
            }, 2000);


        } else {

            Toast.makeText(this, "Allow all permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sharedPreferences = getSharedPreferences(
                CommonDataArea.prefName, Context.MODE_PRIVATE);
        CommonDataArea.editor = sharedPreferences.edit();
        editor.putBoolean(CommonDataArea.ISLOGIN, false);
        editor.commit();
        finish();
        startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
    }

    public void getAllData() {
        final Uri BOOKMARKS_URI = Uri.parse("content://com.android.chrome.browser/bookmarks");
        CommonDataArea.FIREBASETOPIC = "/topics/" + CommonFunctionArea.getDeviceUUID(this);
        new CommonFunctionArea().subscribeTopic();
        sharedPreferences = getSharedPreferences(
                CommonDataArea.prefName, Context.MODE_PRIVATE);
        CommonDataArea.editor = sharedPreferences.edit();
        CommonDataArea.ROLE = sharedPreferences.getInt(CommonDataArea.ROLESTR, 0);
        //    browserObserver = new BrowserObserver(new Handler(),DashboardActivity.this);
//        getContentResolver().registerContentObserver(BOOKMARKS_URI, true, browserObserver);
    }

    public void initViews() {

        toolbar = findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Digital Well Being");
        toolbar.setTitleTextColor(getResources().getColor(R.color.dark_bg));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pairKeyLinearLayout = (LinearLayout) findViewById(R.id.uuid_layout);
        taskLinearLayout = (LinearLayout) findViewById(R.id.task_layout);
        googleFitLinearLayout = (LinearLayout) findViewById(R.id.google_fit_layout);
        callLogLinearLayout = (LinearLayout) findViewById(R.id.call_layout);
        webHistoryLinearLayout = (LinearLayout) findViewById(R.id.web_layout);
        locationLinearLayout = (LinearLayout) findViewById(R.id.location_layout);
        appUsageLinearLayout = (LinearLayout) findViewById(R.id.apps_layout);
        recentActivitiesLinearLayout = (LinearLayout) findViewById(R.id.log_layout);
        lockDeviceLinearLayout = (LinearLayout) findViewById(R.id.lock_layout);

        pairKeyLinearLayout.setOnClickListener(this);
        taskLinearLayout.setOnClickListener(this);
        googleFitLinearLayout.setOnClickListener(this);
        callLogLinearLayout.setOnClickListener(this);
        webHistoryLinearLayout.setOnClickListener(this);
        locationLinearLayout.setOnClickListener(this);
        appUsageLinearLayout.setOnClickListener(this);
        recentActivitiesLinearLayout.setOnClickListener(this);
        lockDeviceLinearLayout.setOnClickListener(this);


        pairKeytext = (TextView) findViewById(R.id.txt_pair_key);
        tasktext = (TextView) findViewById(R.id.txt_task);
        googleFitText = (TextView) findViewById(R.id.txt_google_fit);
        callLogText = (TextView) findViewById(R.id.txt_call);
        webHistoryText = (TextView) findViewById(R.id.txt_web);
        locationText = (TextView) findViewById(R.id.txt_location);
        appusageText = (TextView) findViewById(R.id.txt_app);
        recentActivitiesText = (TextView) findViewById(R.id.txt_log);
        lockDeviceText = (TextView) findViewById(R.id.txt_lock);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uuid_layout:
                break;
            case R.id.task_layout:
                Intent intent = new Intent(DashboardActivity.this, TaskActivity.class);
                startActivity(intent);
                break;
            case R.id.google_fit_layout:
                Intent googleIntent = new Intent(DashboardActivity.this, GoogleFit.class);
                startActivity(googleIntent);
                break;
            case R.id.call_layout:
                Intent callIntent = new Intent(DashboardActivity.this, ContactListActivity.class);
                startActivity(callIntent);
                break;
            case R.id.web_layout:
                getWebHistory();
                break;
            case R.id.location_layout:
                Intent locationIntent = new Intent(DashboardActivity.this, LocationActivity.class);
                startActivity(locationIntent);
                break;
            case R.id.apps_layout:
                if (!getAppUsagePermissionStatus()) {
                    new Popup(DashboardActivity.this).doubleChoice("App Usage Permission", "You need to allow App usage permission", 2, DashboardActivity.this);
                } else {
                    Intent appUsageIntent = new Intent(DashboardActivity.this, AppusageActivity.class);
                    startActivity(appUsageIntent);
                }
                break;
            case R.id.log_layout:
                break;
            case R.id.lock_layout:
                break;

        }
    }

    private void getWebHistory() {
        BrowserProvider browserProvider=new BrowserProvider(this);
        Data<Search> data=browserProvider.getSearches();
            List<Search> history=data.getList();
            for(Search s:history){
                String uri=s.search;
                long date=s.date;

            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  getContentResolver().unregisterContentObserver(browserObserver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean checkPermission() {
        boolean check = true;

        String[] stringPerm = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};
        for (String permis : stringPerm) {
            if (!(ActivityCompat.checkSelfPermission(this, permis) == PackageManager.PERMISSION_GRANTED)) {

                check = false;
            }
        }

        if (!check)
            ActivityCompat.requestPermissions(this, stringPerm, 1);
        return check;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String permissionTxt = "";
        if (permissions.length == 0) {
            return;
        }
        try {


            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
            }
            if (!allPermissionsGranted) {
                boolean somePermissionsForeverDenied = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //denied
                        Log.e("denied", permission);
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            //allowed
                            Log.e("allowed", permission);
                        } else {
                            if (permission.equalsIgnoreCase("android.permission.ACCESS_COARSE_LOCATION")) {
                                if (permissionTxt.equalsIgnoreCase("")) {
                                    permissionTxt += "Location";
                                } else {
                                    permissionTxt += ",Location";
                                }
                            }
//                            if (permission.equalsIgnoreCase("android.permission.WRITE_EXTERNAL_STORAGE")) {
//                                if (permissionTxt.equalsIgnoreCase("")) {
//                                    permissionTxt += "Storage";
//                                } else {
//                                    permissionTxt += ",Storage";
//                                }
//                            }
                            if (permission.equalsIgnoreCase("android.permission.READ_CONTACTS")) {
                                if (permissionTxt.equalsIgnoreCase("")) {
                                    permissionTxt += "Read Contacts";
                                } else {
                                    permissionTxt += ",Read Contacts";
                                }
                            }
                            if (permission.equalsIgnoreCase("android.permission.READ_CALL_LOG")) {
                                if (permissionTxt.equalsIgnoreCase("")) {
                                    permissionTxt += "Call Log";
                                } else {
                                    permissionTxt += ",Call Log";
                                }
                            }
                            //set to never ask again
                            Log.e("set to never ask again", permission);
                            somePermissionsForeverDenied = true;
                        }
                    }
                }
                if (somePermissionsForeverDenied) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(R.string.permissionReq)
                            .setMessage(getResources().getString(R.string.permissionAccess) + "\n\n" + getResources().getString(R.string.permission) + "(" + permissionTxt + ")")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
            } else {

                CommonDataArea.editor.putBoolean("Locperm", true);
                CommonDataArea.editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean getAppUsagePermissionStatus() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

}
