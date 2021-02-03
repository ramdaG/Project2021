package com.example.project2021;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.project2021.Login.IntroActivity;
import com.example.project2021.Login.LoginActivity;
import com.example.project2021.home.Comment_item;
import com.example.project2021.home.HomeCommentAdapter;
import com.example.project2021.home.homeFragment;
import com.example.project2021.profile.Memberinfo;
import com.example.project2021.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    DrawerLayout mDrawerLayout;
    private GpsTracker gpsTracker;
    private long backKeyPressedTime = 0;
    private Toast toast;
    ActionBar actionBar;
    homeFragment homeFragment;
    public interface OnBackPressedListener { void onBackPressed(); }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.replace(R.id.fragment, homeFragment.newInstance());
        ft1.commit();
*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //로그인 유지 상태 여부 확인
        if (user == null) {
            myStartActivity(LoginActivity.class);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(ProfileActivity.class);

                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        initLayout();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.서울:
                Toast.makeText(this, "서울을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(0);
                //((homeFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)).showDialog(0);
                break;
            case R.id.인천:
                Toast.makeText(this, "인천을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                //((homeFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)).showDialog(1);
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(1);
                break;
            case R.id.수원:
                Toast.makeText(this, "수원을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(2);
                break;
            case R.id.부산:
                Toast.makeText(this, "부산을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(3);
                break;
            case R.id.울산:
                Toast.makeText(this, "울산을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(4);
                break;
            case R.id.광주:
                Toast.makeText(this, "광주를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(5);
                break;
            case R.id.대구:
                Toast.makeText(this, "대구를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(6);
                break;
            case R.id.대전:
                Toast.makeText(this, "대전을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(7);
                break;
            case R.id.춘천:
                Toast.makeText(this, "춘천을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(8);
                break;
            case R.id.제주:
                Toast.makeText(this, "제주를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(9);
                break;
            case R.id.전주:
                Toast.makeText(this, "전주를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(10);
                break;
            case R.id.포항:
                Toast.makeText(this, "포항을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(11);
                break;
            case R.id.강릉:
                Toast.makeText(this, "강릉을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(12);
                break;
            case R.id.여수:
                Toast.makeText(this, "여수를 선택하셨습니다", Toast.LENGTH_SHORT).show();
                ((homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment()).showDialog(13);
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                myStartActivity(IntroActivity.class);
                startToast("정상적으로 로그아웃되었습니다.");
                break;
            case R.id.signOut:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startToast("성공적으로 탈퇴되었습니다.");
                                                    myStartActivity(IntroActivity.class);
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case android.R.id.home: {
                NavDestination current = NavHostFragment.findNavController(getSupportFragmentManager().getPrimaryNavigationFragment().getFragmentManager().getFragments().get(0)).getCurrentDestination();

                switch (current.getId()) {
                    case R.id.homeFragment:
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;
                    default:
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
      if(System.currentTimeMillis()>backKeyPressedTime+2500){
          backKeyPressedTime = System.currentTimeMillis();
          toast = Toast.makeText(this,"뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG);
          toast.show();
          return;
      }
      if(System.currentTimeMillis() <=backKeyPressedTime + 2500 ){
          ActivityCompat.finishAffinity(this);
          toast.cancel();
      }
    }

    private void initLayout() {


        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.more);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(drawable);

        bottomNavigationView = findViewById(R.id.bottomnavView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        homeFragment homefragment = new homeFragment();

        RecyclerView recyclerView = findViewById(R.id.home_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.scrollToPosition(0);

        ArrayList<Comment_item> mList = new ArrayList<>();
        ArrayList<Memberinfo> memberList = new ArrayList<>();
        HomeCommentAdapter mAdapter =  new HomeCommentAdapter(homefragment, mList, memberList);

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            CollectionReference collectionReference = mFirestore.collection("users");
            collectionReference
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("homefragment", document.getId() + " => " + document.getData());
                            final Memberinfo memberinfo = new Memberinfo(
                                    document.getString("name"),
                                    document.getString("type"),
                                    document.getId());
                            memberList.add(memberinfo);
                        }
                    }
                }
            });
        }
        CollectionReference ref = mFirestore.collection("comments_Incheon");
        ref
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("home", document.getId() + " => " + document.getData());
                        final Comment_item commentItem = new Comment_item(
                                document.getString("user"),
                                document.getString("content"),
                                new Date(document.getDate("date").getTime()));
                        mList.add(commentItem);
                        recyclerView.setAdapter(mAdapter);
                    }
                }
            }
        });
    }
}
