package com.govindadasu.androidboilerplate.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.govindadasu.androidboilerplate.R;
import com.govindadasu.androidboilerplate.activity.LoginSignupActivity;
import com.govindadasu.androidboilerplate.bo.User;

import java.io.InputStream;

public class Profile_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile__view);
        TextView tv = (TextView)findViewById(R.id.userInfo);

        tv.setText("Welcome "+(User.getLoggedInUser().getLoginType()==User.LOGIN_TYPE_FACEBOOK?"Facebook":"Google")+" User "+User.getLoggedInUser().getFirstName());

        if(User.getLoggedInUser().getLoginType()==User.LOGIN_TYPE_FACEBOOK){
            ProfilePictureView pv = (ProfilePictureView) findViewById(R.id.fb_profile_pic_view);
            pv.setVisibility(View.VISIBLE);
            pv.setProfileId(User.getLoggedInUser().getUserId());
        } else if(User.getLoggedInUser().getLoginType()==User.LOGIN_TYPE_GOOGLE){
            ImageView pv = (ImageView) findViewById(R.id.google_profile_pic);
            new LoadProfileImage(pv).execute(User.getLoggedInUser().getProfilePictureUrl());
        }

    }

    public void logoutUser(View view) {
        Intent intent = new Intent(this,LoginSignupActivity.class);
        intent.putExtra(LoginSignupActivity.TAG_LOGOUT, true);
        startActivity(intent);
        finish();
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
        }
    }
}