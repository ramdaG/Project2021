package com.example.project2021;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.more);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(drawable);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu);

        bottomNavigationView = findViewById(R.id.bottomnavView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        /*
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            //mDrawerLayout.openDrawer(navigationView);
            menuItem.setChecked(true);
            int id = menuItem.getItemId();

            switch (id){
                case R.id.서울 :
                    Toast.makeText(MainActivity.this, "서울을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.인천 :
                    Toast.makeText(MainActivity.this, "인천을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.부산 :
                    Toast.makeText(MainActivity.this, "부산을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.경기도 :
                    Toast.makeText(MainActivity.this, "경기도를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                    break;
            }
            mDrawerLayout.closeDrawers();
            return false;
        });
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.signOut:
                Toast.makeText(this, "SignOut", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:{
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}