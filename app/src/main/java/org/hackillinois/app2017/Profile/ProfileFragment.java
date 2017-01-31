package org.hackillinois.app2017.Profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.solver.SolverVariable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hackillinois.app2017.R;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.hackillinois.app2017.R.drawable.linkedin;

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


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.image_profile_linkedin:
                    openLink("http://www.linkedin.com");
                    break;
                case R.id.image_profile_github:
                    openLink("http://github.com");
                    break;
                case R.id.image_profile_resume:
                    openLink("resume");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_profile, parent, false);
        ButterKnife.bind(this, view);

        linkedin.setOnClickListener(listener);
        github.setOnClickListener(listener);
        resume.setOnClickListener(listener);

        Typeface brandon_med = Typeface.createFromAsset(view.getContext().getAssets(), "Brandon_med");
        Typeface brandon_reg = Typeface.createFromAsset(view.getContext().getAssets(), "Brandon_reg");
        Typeface gotham_med = Typeface.createFromAsset(view.getContext().getAssets(), "Gotham-Medium");

        name.setTypeface(gotham_med);
        diet.setTypeface(brandon_med);
        universityTitle.setTypeface(brandon_reg);
        university.setTypeface(brandon_reg);
        majorTitle.setTypeface(brandon_reg);
        major.setTypeface(brandon_reg);
        yearOfGraduationTitle.setTypeface(brandon_reg);
        yearOfGraduation.setTypeface(brandon_reg);

        return view;
    }

    private void openLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}
