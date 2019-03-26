package matekgames.com.vreme;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
//Napoved napoved;


    private static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting");

//        napoved = new Napoved(getApplicationContext());


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Tab1");
        adapter.addFragment(new Tab2Fragment(), "Tab2");
        adapter.addFragment(new Tab3Fragment(), "Tab3");
        adapter.addFragment(new Tab4Fragment(), "Tab4");
        adapter.addFragment(new Tab5Fragment(), "Tab5");
        adapter.addFragment(new Tab6Fragment(), "Tab6");
        adapter.addFragment(new Tab7Fragment(),"Tab7");
        viewPager.setAdapter(adapter);
        int limit = (mSectionsPageAdapter.getCount() > 1 ? mSectionsPageAdapter.getCount() - 1 : 2);
        mViewPager.setOffscreenPageLimit(limit);


    }

}

