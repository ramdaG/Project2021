 package com.example.project2021.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2021.MainActivity;
import com.example.project2021.R;
import com.example.project2021.profile.ProfileActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

import static com.example.project2021.R.id.facebook_Button;

 public class LoginActivity extends AppCompatActivity {


     private static final int GPS_ENABLE_REQUEST_CODE = 2001;
     private static final int PERMISSIONS_REQUEST_CODE = 100;
     String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


     TextView tv;
     Button button;

     private FirebaseAuth mAuth;
     private FirebaseFirestore db = FirebaseFirestore.getInstance();
     final static String TAG = "LoginActivityT";

     //Twitter
     private Button TwitterButton;
     private TwitterLoginButton TwitterMain;
     private FirebaseAuth.AuthStateListener mAuthListener;

     //google
     private Button btnLogingoogle;
     private GoogleSignInClient mGoogleSignInClient;
     private static final int RC_SIGN_IN = 9001;

     //facebook
     private CallbackManager callbackManager;
     private Button btnLoginfacebook;

     @SuppressLint("WrongViewCast")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);
         mAuth = FirebaseAuth.getInstance();


         //위치정보
         if (!checkLocationServicesStatus()) {
             showDialogForLocationServiceSetting();
         } else {
             checkRunTimePermission();
         }


         //회원가입
         TextView SignUp = findViewById(R.id.SignUpText);
         SignUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                 startActivity(intent);
             }

         });

         //facebook
         callbackManager = CallbackManager.Factory.create();
         btnLoginfacebook = findViewById(facebook_Button);
         btnLoginfacebook.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 LoginManager loginManager = LoginManager.getInstance();
                 loginManager.logInWithReadPermissions(LoginActivity.this,
                         Arrays.asList("email", "public_profile"));
                 loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                     @Override
                     public void onSuccess(LoginResult loginResult) {
                         handleFacebookAccessToken(loginResult.getAccessToken());
                     }

                     @Override
                     public void onCancel() {

                     }

                     @Override
                     public void onError(FacebookException error) {

                     }
                 });
             }
         });


         //google
         btnLogingoogle = findViewById(R.id.google_Button);
         btnLogingoogle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 signIn();
             }
         });
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);


         //twitter
         TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.twitter_consumer_key),
                 getString(R.string.twitter_consumer_secret));
         TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                 .twitterAuthConfig(mTwitterAuthConfig)
                 .build();
         Twitter.initialize(twitterConfig);

         TwitterButton = findViewById(R.id.Twitter_Button);
         TwitterMain = findViewById(R.id.Twitter_Main);

         mAuthListener = new FirebaseAuth.AuthStateListener(){
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                 if (firebaseAuth.getCurrentUser() != null){
                     startActivity(new Intent(LoginActivity.this, MainActivity.class));
                 }
             }
         };

         TwitterMain.setCallback(new Callback<TwitterSession>() {
             @Override
             public void success(Result<TwitterSession> result) {
                 Toast.makeText(LoginActivity.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                 signInToFirebaseWithTwitterSession(result.data);
                 TwitterButton.setVisibility(View.VISIBLE);
                 getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
             }

             @Override
             public void failure(TwitterException exception) {
                 Toast.makeText(LoginActivity.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                 getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
             }
         });

         TwitterButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 TwitterMain.performClick();
             }
         });


         //입력 버튼, 로그인 구현
         button = findViewById(R.id.button_Login);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Login();
             }

         });

     }

     @Override
     public void onBackPressed() {
         super.onBackPressed();
         ActivityCompat.finishAffinity(this);
     }

     private void Login() {
         String email = ((EditText) findViewById(R.id.emaileditText)).getText().toString();
         String password = ((EditText) findViewById(R.id.passwordeditText)).getText().toString();

         if (email.length() > 0 && password.length() > 0) {
             mAuth.signInWithEmailAndPassword(email, password)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 FirebaseUser user = mAuth.getCurrentUser();
                                 startToast("로그인에 성공하였습니다");
                                 goActivity();
                             } else {
                                 if (task.getException() != null) {
                                     startToast(task.getException().toString());
                                 }
                                 startToast(task.getException().toString());
                             }
                         }
                     });
         } else {
             startToast("이메일 또는 비밀번호를 입력해 주세요");

         }

     }

     private void startToast(String msg) {
         Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
     }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         //facebook
         callbackManager.onActivityResult(requestCode, resultCode, data);
         super.onActivityResult(requestCode, resultCode, data);

         //google
         if (requestCode == RC_SIGN_IN) {
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             try {
                 // Google Sign In was successful, authenticate with Firebase
                 GoogleSignInAccount account = task.getResult(ApiException.class);
                 firebaseAuthWithGoogle(account);
             } catch (ApiException e) {
             }
         }


         //위치정보
         switch (requestCode) {
             case GPS_ENABLE_REQUEST_CODE:
                 //사용자가 GPS 활성 시켰는지 검사
                 if (checkLocationServicesStatus()) {
                     if (checkLocationServicesStatus()) {
                         Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                         checkRunTimePermission();
                         return;
                     }
                 }
                 break;
         }
     }

     @Override
     public void onRequestPermissionsResult(int permsRequestCode,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grandResults) {

         if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

             // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

             boolean check_result = true;


             // 모든 퍼미션을 허용했는지 체크합니다.

             for (int result : grandResults) {
                 if (result != PackageManager.PERMISSION_GRANTED) {
                     check_result = false;
                     break;
                 }
             }


             if (check_result) {

                 //위치 값을 가져올 수 있음
                 ;
             } else {
                 // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                 if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                         || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                     Toast.makeText(LoginActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                     finish();
                 } else {
                     Toast.makeText(LoginActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                 }
             }

         }
     }

     void checkRunTimePermission() {

         //런타임 퍼미션 처리
         // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
         int hasFineLocationPermission = ContextCompat.checkSelfPermission(LoginActivity.this,
                 Manifest.permission.ACCESS_FINE_LOCATION);
         int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(LoginActivity.this,
                 Manifest.permission.ACCESS_COARSE_LOCATION);

         if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                 hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

             // 2. 이미 퍼미션을 가지고 있다면
             // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


             // 3.  위치 값을 가져올 수 있음


         } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

             // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
             if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, REQUIRED_PERMISSIONS[0])) {

                 // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                 Toast.makeText(LoginActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                 // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                 ActivityCompat.requestPermissions(LoginActivity.this, REQUIRED_PERMISSIONS,
                         PERMISSIONS_REQUEST_CODE);


             } else {
                 // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                 // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                 ActivityCompat.requestPermissions(LoginActivity.this, REQUIRED_PERMISSIONS,
                         PERMISSIONS_REQUEST_CODE);
             }

         }

     }

     //여기부터는 GPS 활성화를 위한 메소드들
     private void showDialogForLocationServiceSetting() {

         AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
         builder.setTitle("위치 서비스 비활성화");
         builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                 + "위치 설정을 수정하실래요?");
         builder.setCancelable(true);
         builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {
                 Intent callGPSSettingIntent
                         = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
             }
         });
         builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
         });
         builder.create().show();
     }

     public boolean checkLocationServicesStatus() {
         LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

         return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                 || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
     }


     @Override
     protected void onDestroy() {
         super.onDestroy();
     }


     private void goActivity() {
         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
         startActivity(intent);
     }

     //google
     private void signIn() {
         Intent signInIntent = mGoogleSignInClient.getSignInIntent();
         startActivityForResult(signInIntent, RC_SIGN_IN);
     }

     //google
     private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

         AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             // Sign in success, update UI with the signed-in user's information
                             Toast.makeText(LoginActivity.this, "로그인이 성공적으로 되었습니다", Toast.LENGTH_LONG).show();
                             FirebaseUser user = mAuth.getCurrentUser();
                             updateUI(user);
                         } else {
                             // If sign in fails, display a message to the user.
                             Toast.makeText(LoginActivity.this, "로그인이 실패하였습니다", Toast.LENGTH_LONG).show();
                             updateUI(null);
                         }
                     }
                 });
     }

     //facebook
     private void handleFacebookAccessToken(AccessToken token) {

         AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             // 로그인 성공
                             Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                             myStartActivity(ProfileActivity.class);
                         } else {
                             // 로그인 실패
                             Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
     }

     //twitter
     private void signInToFirebaseWithTwitterSession(TwitterSession session){
         AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                 session.getAuthToken().secret);

         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         Toast.makeText(LoginActivity.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG).show();
                         if (!task.isSuccessful()){
                             Toast.makeText(LoginActivity.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                         }
                     }
                 });
     }

     private void updateUI(FirebaseUser user) { //update ui code here
         if (user != null) {
             Intent intent = new Intent(this, MainActivity.class);
             startActivity(intent);
             finish();
         }
     }

     public void myStartActivity(Class c) {
         Intent intent = new Intent(this, c);
         startActivity(intent);
     }
 }
