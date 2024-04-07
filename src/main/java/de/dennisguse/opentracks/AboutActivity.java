package de.dennisguse.opentracks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.dennisguse.opentracks.data.FirestoreCRUDUtil;
import de.dennisguse.opentracks.data.interfaces.ReadCallback;
import de.dennisguse.opentracks.data.models.CRUDConstants;
import de.dennisguse.opentracks.databinding.AboutBinding;
import de.dennisguse.opentracks.ui.util.ViewUtils;
import de.dennisguse.opentracks.util.SystemUtils;

public class AboutActivity extends AbstractActivity {

    private AboutBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.about_preference_title));
        ReadCallback callback = new ReadCallback() {
            @Override
            public void onSuccess(JsonObject data) {


            }

            @Override
            public void onSuccess(ArrayList<JsonObject> data) {
                Log.d("COLLECTION", "Data" + " => " + data.toString());


            }

            @Override
            public void onFailure() {

            }
        };
        FirestoreCRUDUtil dbUtil = FirestoreCRUDUtil.getInstance();

        dbUtil.getUserRuns("ali",callback);


        viewBinding.aboutTextDescription.setText(getString(R.string.about_description));
        viewBinding.aboutTextVersionName.setText(getString(R.string.about_version_name, SystemUtils.getAppVersionName(this)));
        viewBinding.aboutTextVersionCode.setText(getString(R.string.about_version_code, SystemUtils.getAppVersionCode(this)));
        viewBinding.aboutAppUrl.setText(getString(R.string.about_url, getString(R.string.app_web_url)));

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);

        ViewUtils.makeClickableLinks(findViewById(android.R.id.content));
    }

    protected View getRootView() {
        viewBinding = AboutBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewBinding = null;
    }

}
