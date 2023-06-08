package com.example.qrcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.qrcodescanner.databinding.ActivityIntroBinding
import com.example.qrcodescanner.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_intro.btnSignInIntro
import kotlinx.android.synthetic.main.activity_intro.btnSignUpIntro

class IntroActivity : AppCompatActivity() {
    private var binding: ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //button click response for sign up button
        binding?.btnSignUpIntro?.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        //button response for sign in button
       binding?.btnSignInIntro?.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}