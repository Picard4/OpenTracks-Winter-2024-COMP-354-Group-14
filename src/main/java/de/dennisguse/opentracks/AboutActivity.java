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

import java.util.HashMap;
import java.util.Map;

import de.dennisguse.opentracks.databinding.AboutBinding;
import de.dennisguse.opentracks.ui.util.ViewUtils;
import de.dennisguse.opentracks.util.SystemUtils;

public class AboutActivity extends AbstractActivity {

    private AboutBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.about_preference_title));
        //checkDBConnection("FirstName","LastName");

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
    void checkDBConnection(String firstName, String lastName)

    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>(); //Tester code to check DB connection from Firestore Doc s.
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("ADDED", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR", "Error adding user", e);
                    }
                });
    }
}
