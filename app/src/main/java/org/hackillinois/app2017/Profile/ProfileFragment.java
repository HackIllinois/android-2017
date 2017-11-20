package org.hackillinois.app2017.Profile;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.hackillinois.app2017.Backend.APIHelper;
import org.hackillinois.app2017.Backend.RequestManager;
import org.hackillinois.app2017.Login.LoginActivity;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_qrcode) ImageView qrcode;
    @BindView(R.id.image_profile_github) ImageView github;
    @BindView(R.id.image_profile_linkedin) ImageView linkedin;
    @BindView(R.id.image_profile_resume) ImageView resume;

    @BindView(R.id.profile_name) TextView name;
    @BindView(R.id.profile_diet) TextView diet;
    @BindView(R.id.profile_major) TextView major;
    @BindView(R.id.profile_major_title) TextView majorTitle;
    @BindView(R.id.profile_university) TextView university;
    @BindView(R.id.profile_university_title) TextView universityTitle;
    @BindView(R.id.profile_yearofgraduation) TextView yearOfGraduation;
    @BindView(R.id.profile_yearofgraduation_title) TextView yearOfGraduationTitle;
    private Unbinder unbinder;

    private SharedPreferences sharedPreferences;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.image_profile_linkedin:
                    openLink("http://www.linkedin.com/in/" + sharedPreferences.getString("linkedin", ""));
                    break;
                case R.id.image_profile_github:
                    openLink("http://github.com/" + sharedPreferences.getString("github", ""));
                    break;
                case R.id.image_profile_resume:
                    getResume();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_profile, parent, false);
        unbinder = ButterKnife.bind(this, view);

        linkedin.setOnClickListener(listener);
        github.setOnClickListener(listener);
        resume.setOnClickListener(listener);

        Bitmap qrCodeBitmapOfID = Utils.getQRCodeBitmap(getContext());
        qrcode.setImageBitmap(qrCodeBitmapOfID);
        qrcode.setOnClickListener(v -> Utils.showFullScreenQRCode(v.getContext()));

        name.setText(sharedPreferences.getString("firstName", "N/A") + " " + sharedPreferences.getString("lastName", "N/A"));

        if (sharedPreferences.getString("diet", "N/A").equalsIgnoreCase("NONE")) {
            diet.setText("No Dietary Restrictions");
        } else {
            diet.setText(sharedPreferences.getString("diet", "N/A"));
        }

        university.setText(sharedPreferences.getString("school", "Error"));
        major.setText(sharedPreferences.getString("major", "Error"));
        yearOfGraduation.setText(sharedPreferences.getString("graduationYear", "Error"));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to log out?")
                        .setTitle("Log Out")
                        .setPositiveButton("Log Out", (dialog, which) -> logOut())
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                        .create();

                alertDialog.show();

                return true;
            default:
                break;
        }

        return false;
    }

    private void openLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void logOut() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        sharedPreferences.edit().clear().apply();
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getResume() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", sharedPreferences.getString("auth", ""));

        final Request<byte[]> request = new Request<byte[]>(
                Request.Method.GET,
                APIHelper.RESUME_ENDPOINT + sharedPreferences.getString("resumeId",null),
                error -> Log.e("GETPDF","Couldn't download pdf") //TODO handle
        ) {
            @Override
            protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
                return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            protected void deliverResponse(byte[] response) {
                Uri path = savePDF("resume", response);
                readPDF(path);
            }

            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        Log.d("VolleyResume", request.getUrl());

        RequestManager.getInstance(getContext()).addToRequestQueue(request);

    }

    private Uri savePDF(String filename, byte[] pdfBytes) {
        try {
            File pdfFile = new File(getContext().getExternalCacheDir(), filename + ".pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(pdfBytes);
            fos.flush();
            fos.close();
            Log.i("SAVEPDF","saving to " + pdfFile.getAbsolutePath());
            return Uri.fromFile(pdfFile);
        } catch (IOException e) {
            Log.i("SAVEPDF", "Couldn't write pdf");
            Toast.makeText(getContext(),"Can't get PDF", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void readPDF(Uri filename){
        Intent target = new Intent(Intent.ACTION_VIEW);
        Log.i("READPDF","reading from " + filename.toString());
        target.setDataAndType(filename,"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
}
