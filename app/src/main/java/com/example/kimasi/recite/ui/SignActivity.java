package com.example.kimasi.recite.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimasi.recite.R;
import com.example.kimasi.recite.ReciteInfo;
import com.example.kimasi.recite.module.MyUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0; //身份一致

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
  //用来处理密码联网验证
    private UserLoginTask mAuthTask = null; //记录登陆，实现异步操作,并提供接口反馈当前异步执行的程度

    // UI references.
    private AutoCompleteTextView mEmailView; //应该是自动补全
    private EditText mUserView;
    private EditText mPasswordView;
    private EditText mPasswordView2;//注册密码验证两次
    private View mProgressView;
    private View mLoginFormView;

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    String name;
    String email;
    String password ;
    String password2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        preferences=getSharedPreferences("fpeizhi", MODE_PRIVATE);//保存配制到本地
        editor=preferences.edit();
        Bmob.initialize(this, "8c90ef0312bfa9752d4d8ca72ef9ae03");

        // 设置登录表单。
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);//邮箱
        populateAutoComplete();
        mUserView = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView2 = (EditText) findViewById(R.id.password2);
        mPasswordView2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) { //应该是可以用来监听键盘的确认键的
                    attemptLogin();//点击注册，进行验证和注册
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);//圆圈进度条
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//sdk版本
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {//应该是确认正在登陆（不能点击进行第二次）
            return;
        }

        // 重置错误
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // 获取存储值进行登录尝试。
         name=mUserView.getText().toString();
         email = mEmailView.getText().toString();
         password = mPasswordView.getText().toString();
         password2 = mPasswordView2.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(name)){
            mUserView.setError("请输入用户名");
            focusView = mUserView;
            cancel = true;//取消登陆
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {//检查密码不等于空和长度大于4
            mPasswordView.setError(getString(R.string.error_invalid_password));//错误提示
            focusView = mPasswordView;
            cancel = true;//取消登陆
        }
        if(!password.equals(password2)){
            mPasswordView.setError(getString(R.string.error_invalid_password2)); //错误提示
            focusView = mPasswordView;
            cancel = true;//取消登陆
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {//检查是不是邮箱格式
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();//应该是焦点转移到focusView视图(即，输错的栏）
        } else {
     //       showProgress(true);//应该是登陆的进度条，后面会进行密码匹配，如下Task
     //       mAuthTask = new UserLoginTask(name,email, password);//另一异步线程进行密码匹配，这是初始化
      //      mAuthTask.execute((Void) null); //这是开始执行

            BmobUser bu = new BmobUser();
            bu.setUsername(name);//进行注册
            bu.setPassword(password);
            bu.setEmail(email);
            //注意：不能用save方法进行注册
            bu.signUp(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "注册成功",
                            Toast.LENGTH_SHORT).show();
                    Log.v("注册成功","====");

                    ReciteInfo.register=true;
                    ReciteInfo.userName=name;
                    editor.putString("user_name",name);
                    editor.commit();
                    Intent intent=new Intent(SignActivity.this,UserActivity.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                    NavigationDrawerFragment.userNameView.setText(name);
                    finish();
                }
                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "注册失败 "+msg,
                            Toast.LENGTH_SHORT).show();
                    Log.v("注册失败","====");
                    mEmailView.setError(getString(R.string.error_incorrect_email));//注册用户已存在
                    mEmailView.requestFocus();//重新获得输入密码焦点

                }
            });

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**  显示了进步UI和隐藏登录表单。
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {//确认版本，看能不能用动画
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);//隐藏整个登陆输入框
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(  //动画
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);//显示进度条
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {//实现异步操作,并提供接口反馈当前异步执行的程度

        private final String mName;
        private final String mEmail;
        private final String mPassword;
        private boolean sign;

        UserLoginTask(String name,String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
            sign=false;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Bmob.initialize(SignActivity.this, "8c90ef0312bfa9752d4d8ca72ef9ae03");

            MyUser bu=new MyUser();

            bu.setUsername(mName);//进行注册
            bu.setPassword(mPassword);
            bu.setEmail(mEmail);
            //注意：不能用save方法进行注册
            bu.signUp(SignActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    sign=true;
                    Toast.makeText(getApplicationContext(), "注册成功",
                            Toast.LENGTH_SHORT).show();
             //       signCallbacks=new NavigationDrawerFragment();
             //       signCallbacks.onSignRefresh(mName);
            }
                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    sign=true;
                    Toast.makeText(getApplicationContext(), "注册失败 "+msg,
                            Toast.LENGTH_SHORT).show();
                }
            });
            try {   //这里应该是访问网络，验证密码
                // Simulate network access.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;  //false验证不了
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {//当doInBackground（）完成后自动调用，并获得他的返回值
            mAuthTask = null;
            showProgress(false);

            if (success) {

                ReciteInfo.register=true;
                ReciteInfo.userName=mName;
                editor.putString("user_name",mName);
                editor.commit();
                Intent intent=new Intent(SignActivity.this,UserActivity.class);
                intent.putExtra("name",mName);
                startActivity(intent);
                NavigationDrawerFragment.userNameView.setText(mName);
                finish();
            } else {
                mEmailView.setError(getString(R.string.error_incorrect_email));//注册用户已存在
                mEmailView.requestFocus();//重新获得输入密码焦点
            }
        }

        @Override
        protected void onCancelled() {//取消
            mAuthTask = null;//后台异步栈置空
            showProgress(false);
        }
    }

}

