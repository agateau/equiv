package com.greenyetilab.equiv.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.greenyetilab.equiv.R;
import com.greenyetilab.equiv.core.Constants;
import com.greenyetilab.utils.NTemplate;

public class AboutActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        NTemplate template = new NTemplate(getText(R.string.about_description));
        template.put("email", Constants.AUTHOR_EMAIL)
                .put("rate_url", Constants.GPLAY_URL)
                .put("gpl_url", Constants.GPL_URL)
                .put("github_url", Constants.GITHUB_URL);

        TextView view = (TextView) findViewById(R.id.descriptionTextView);
        view.setText(template.toSpanned());
        view.setMovementMethod(LinkMovementMethod.getInstance());

        view = (TextView) findViewById(R.id.versionTextView);
        view.setText(Constants.VERSION_NAME);
    }
}
