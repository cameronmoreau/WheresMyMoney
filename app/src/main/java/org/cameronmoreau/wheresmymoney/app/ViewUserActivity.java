package org.cameronmoreau.wheresmymoney.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import org.cameronmoreau.wheresmymoney.R;

/**
 * Created by Cameron on 11/25/2014.
 */
public class ViewUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Fragment f = null;
        String activityType = getIntent().getExtras().getString("Type");
        if(activityType.equals("user")) f = new ViewUserFragment();
        else if(activityType.equals("all")) f = new ViewAllFragment();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, f)
                    .commit();
        }
    }
}
