/*
Copyright 2015 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.equiv.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.agateau.equiv.R;
import com.agateau.equiv.core.Constants;
import com.agateau.utils.NTemplate;

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
                .put("license_url", Constants.LICENSE_URL)
                .put("github_url", Constants.GITHUB_URL);

        TextView view = (TextView) findViewById(R.id.descriptionTextView);
        assert view != null;
        view.setText(template.toSpanned());
        view.setMovementMethod(LinkMovementMethod.getInstance());

        view = (TextView) findViewById(R.id.versionTextView);
        assert view != null;
        view.setText(getVersion());
    }

    private String getVersion() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Failed to get package info", e);
        }
        return String.format("%s-%s", info.versionName, info.versionCode);
    }
}
