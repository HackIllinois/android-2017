package org.hackillinois.app2017.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

import org.hackillinois.app2017.Login.LoginActivity;
import org.hackillinois.app2017.MainActivity;
import org.hackillinois.app2017.R;
import org.hackillinois.app2017.Utils;

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
                    openLink("http://www.linkedin.com/" + sharedPreferences.getString("linkedin", ""));
                    break;
                case R.id.image_profile_github:
                    openLink("http://github.com/" + sharedPreferences.getString("github", ""));
                    break;
                case R.id.image_profile_resume:
                    openLink("resume");
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(MainActivity.sharedPrefsName, Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_profile, parent, false);
        unbinder = ButterKnife.bind(this, view);

        linkedin.setOnClickListener(listener);
        github.setOnClickListener(listener);
        resume.setOnClickListener(listener);

        Typeface brandon_med = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Brandon_med.otf");
        Typeface brandon_reg = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Brandon_reg.otf");
        Typeface gotham_med = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Gotham-Medium.otf");

        Bitmap qrCodeBitmapOfID = getQRCodeFromID();
        qrcode.setImageBitmap(qrCodeBitmapOfID);
        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showFullScreenQRCode(v.getContext());
            }
        });

        name.setTypeface(gotham_med);
        diet.setTypeface(brandon_med);
        universityTitle.setTypeface(brandon_reg);
        university.setTypeface(brandon_reg);
        majorTitle.setTypeface(brandon_reg);
        major.setTypeface(brandon_reg);
        yearOfGraduationTitle.setTypeface(brandon_reg);
        yearOfGraduation.setTypeface(brandon_reg);

        name.setText(sharedPreferences.getString("firstName", "N/A") + " " + sharedPreferences.getString("lastName", "N/A"));
        // TODO: Set Diet
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
                logOut();
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

    public Bitmap getQRCodeFromID() {
        return QRCode.from(sharedPreferences.getString("id","N/A")).withSize(400,400).bitmap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
