package com.example.review;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.review.Fragments.AddFragment;
import com.example.review.Fragments.HomeFragment;
import com.example.review.Fragments.ProfileFragment;
import com.example.review.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //this line hides statusbar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



      navigationView = findViewById(R.id.navigation_view);

      Bundle intent = getIntent().getExtras();
      if(intent!=null){
          String publisher = intent.getString("publisherid");

          SharedPreferences.Editor editor= getSharedPreferences("PREFS", MODE_PRIVATE).edit();
          editor.putString("profileid", publisher);
          editor.apply();
          getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new ProfileFragment()).commit();
      }else {
          getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, new HomeFragment()).commit();
      }

      navigationView.setSelectedItemId(R.id.home);


      navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {

              Fragment fragment = null;

              switch (item.getItemId()){
                  case R.id.home:
                      fragment= new HomeFragment();
                      break;

                  case R.id.add:
                      fragment= new AddFragment();
                      break;




                  case R.id.search:
                      fragment= new SearchFragment();
                      break;




              }

              getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, fragment).commit();

              return true;
          }
      });






    }
}