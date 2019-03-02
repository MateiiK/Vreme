package matekgames.com.vreme;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RuntimePermissionActivity extends AppCompatActivity {

    private static final String TAG_RUNTIME_PERMISSION = "TAG_RUNTIME_PERMISSION";

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permission);

        setTitle("dev2qa.com - Android Runtime Permission Example");

        // List all granted permissions.
        Button listGrantedPermissionButton = findViewById(R.id.runtime_permission_list_granted);
        listGrantedPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First get package name.
                String packageName = getPackageName();
                List<String> permissionList = getAllPermissions(packageName);

                StringBuffer grantedPermissionBuf = new StringBuffer();

                grantedPermissionBuf.append("Package ");
                grantedPermissionBuf.append(packageName);
                grantedPermissionBuf.append(" has been granted below permissions : \r\n\r\n");

                int size = permissionList.size();
                for(int i=0;i<size;i++)
                {
                    String permission = permissionList.get(i);
                    // If this permission has been granted.
                    if(hasRuntimePermission(getApplicationContext(), permission)) {
                        grantedPermissionBuf.append(permissionList.get(i));

                        if (i < size - 1) {
                            grantedPermissionBuf.append(",\r\n\r\n");
                        }
                    }
                }

                // Show user granted permissions list in a alert dialog.
                AlertDialog alertDialog = new AlertDialog.Builder(RuntimePermissionActivity.this).create();
                alertDialog.setMessage(grantedPermissionBuf.toString());
                alertDialog.show();
            }
        });

        // Process camera permission.
        Button cameraPermissionButton = findViewById(R.id.runtime_permission_camera);
        cameraPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.CAMERA)) {
                    requestRuntimePermission(RuntimePermissionActivity.this, Manifest.permission.CAMERA, PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(RuntimePermissionActivity.this, "Manifest.permission.CAMERA permission already has been granted", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Process accounts permission.
        Button accountsPermissionButton = findViewById(R.id.runtime_permission_accounts);
        accountsPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
                    requestRuntimePermission(RuntimePermissionActivity.this, Manifest.permission.GET_ACCOUNTS, PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(RuntimePermissionActivity.this, "Manifest.permission.GET_ACCOUNTS permission already has been granted", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Process gps location permission.
        Button locationPermissionButton = findViewById(R.id.runtime_permission_location);
        locationPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestRuntimePermission(RuntimePermissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(RuntimePermissionActivity.this, "Manifest.permission.ACCESS_FINE_LOCATION permission already has been granted", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Process write external storage permission.
        Button storagePermissionButton = findViewById(R.id.runtime_permission_storage);
        storagePermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestRuntimePermission(RuntimePermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(RuntimePermissionActivity.this, "Manifest.permission.WRITE_EXTERNAL_STORAGE permission already has been granted", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Process phone call permission.
        Button phoneCallPermissionButton = findViewById(R.id.runtime_permission_phone_call);
        phoneCallPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasRuntimePermission(getApplicationContext(), Manifest.permission.CALL_PHONE)) {
                    requestRuntimePermission(RuntimePermissionActivity.this, Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE);
                } else {
                    Toast.makeText(RuntimePermissionActivity.this, "Manifest.permission.CALL_PHONE permission already has been granted", Toast.LENGTH_LONG).show();
                    makePhoneCall("10086");
                }
            }
        });
    }

    // Make a phone call.
    private void makePhoneCall(String phoneNumber)
    {
        // Prepare phone call string uri.
        String phoneCallString = "tel:"+phoneNumber;
        Uri phoneCallUri = Uri.parse(phoneCallString);

        // Prepare phone call intent.
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(phoneCallUri);

        // Start phone call activity and make phone call.
        try {
            startActivity(intent);
        }catch(SecurityException ex)
        {
            Log.e(TAG_RUNTIME_PERMISSION, "makePhoneCall: " + ex.getMessage(), ex);
        }
    }

    // This method is used to check whether current app has required runtime permission.
    private boolean hasRuntimePermission(Context context, String runtimePermission)
    {
        boolean ret = false;

        // Get current android os version.
        int currentAndroidVersion = Build.VERSION.SDK_INT;

        // Build.VERSION_CODES.M's value is 23.
        if(currentAndroidVersion > Build.VERSION_CODES.M)
        {
            // Only android version 23+ need to check runtime permission.
            if(ContextCompat.checkSelfPermission(context, runtimePermission) == PackageManager.PERMISSION_GRANTED)
            {
                ret = true;
            }
        }else
        {
            ret = true;
        }
        return ret;
    }

    /* Request app user to allow the needed runtime permission.
       It will popup a confirm dialog , user can click allow or deny. */
    private void requestRuntimePermission(Activity activity, String runtimePermission, int requestCode)
    {
        ActivityCompat.requestPermissions(activity, new String[]{runtimePermission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If this is our permission request result.
        if(requestCode==PERMISSION_REQUEST_CODE)
        {
            if(grantResults.length > 0)
            {
                // Construct result message.
                StringBuffer msgBuf = new StringBuffer();
                int grantResult = grantResults[0];
                if(grantResult==PackageManager.PERMISSION_GRANTED)
                {
                    msgBuf.append("You granted below permissions, you can do the action again to use the permission : ");
                }else
                {
                    msgBuf.append("You denied below permissions : ");
                }

                // Add granted permissions to the message.
                if(permissions!=null)
                {
                    int length = permissions.length;
                    for(int i=0;i<length;i++)
                    {
                        String permission = permissions[i];
                        msgBuf.append(permission);

                        if(i<length-1) {
                            msgBuf.append(",");
                        }
                    }
                }

                // Show result message.
                Toast.makeText(getApplicationContext(), msgBuf.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    // Return all this app required permissions defined in AndroidManifest.xml file..
    private List<String> getAllPermissions(String packageName)
    {
        List<String> ret = new ArrayList<String>();
        try {

            // Get package info.
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Loop to add all package requested permissions to string list.
            int length = packageInfo.requestedPermissions.length;
            for(int i=0;i<length;i++)
            {
                String requestPermission = packageInfo.requestedPermissions[i];
                ret.add(requestPermission);
            }
        }catch (PackageManager.NameNotFoundException ex)
        {
            Log.e(TAG_RUNTIME_PERMISSION, "getAllGrantedPermissions: " + ex.getMessage(), ex);
        }finally {
            return ret;
        }
    }
}