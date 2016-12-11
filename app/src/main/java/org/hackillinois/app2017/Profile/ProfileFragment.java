package org.hackillinois.app2017.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.hackillinois.app2017.R;

public class ProfileFragment extends Fragment {

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
                case R.id.image_profile_site:
                    openLink("portfolio");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_profile, parent, false);

        ImageView linkedin = (ImageView) view.findViewById(R.id.image_profile_linkedin);
        linkedin.setOnClickListener(listener);
        ImageView github = (ImageView) view.findViewById(R.id.image_profile_github);
        github.setOnClickListener(listener);
        ImageView resume = (ImageView) view.findViewById(R.id.image_profile_resume);
        resume.setOnClickListener(listener);
        ImageView portfolio = (ImageView) view.findViewById(R.id.image_profile_site);
        portfolio.setOnClickListener(listener);

        return view;
    }

    private void openLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}
