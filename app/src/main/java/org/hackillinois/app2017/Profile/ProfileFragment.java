package org.hackillinois.app2017.Profile;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class ProfileFragment extends Fragment {

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.linkedin_link:
                    openLink("http://www.linkedin.com");
                    break;
                case R.id.github_link:
                    openLink("http://github.com");
                    break;
                case R.id.resume_link:
                    openLink("resume");
                    break;
                case R.id.portfolio_link:
                    openLink("portfolio");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.layout_profile, parent, false);

        ImageView linkedin = (ImageView) view.findViewById(R.id.linkedin_link);
        linkedin.setOnClickListener(listener);
        ImageView github = (ImageView) view.findViewById(R.id.github_link);
        github.setOnClickListener(listener);
        ImageView resume = (ImageView) view.findViewById(R.id.resume_link);
        resume.setOnClickListener(listener);
        ImageView portfolio = (ImageView) view.findViewById(R.id.portfolio_link);
        portfolio.setOnClickListener(listener);

        return view;
    }

    private void openLink(String link){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}
