package com.example.qrcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.qrcodescanner.firebase.FirestoreClass
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_sign_up.setOnClickListener{
            registerUser()
        }
    }


    /**
     * A function to register a user to our app using the Firebase.
     */

    private fun registerUser(){
        // Here we get the text from editText and trim the space
        val name: String = et_name_sign_up.text.toString().trim{it <= ' '}
        val email: String = et_email_sign_up.text.toString().trim{it <= ' '}
        val password: String = et_password_sign_up.text.toString().trim{it <= ' '}

        if(validateForm(name, email,password)){
            // Show the progress dialog.
            showProgressDialog(resources.
            getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // If the registration is successfully done
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser? = task.result!!.user
                        val registerEmail = firebaseUser?.email
                       val user = registerEmail?.let { User(firebaseUser.uid, name, it) }
                        // call the registerUser function of FirestoreClass to make an entry in the database.
                        if (user != null) {
                            FirestoreClass().resisterUser(this,user)
                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))

                        }

                    }else{
                        Toast.makeText(
                            this@SignUpActivity,
                            "Registration Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

    }

    /**
     * A function to validate the entries of a new user.
     */

    private fun validateForm(name: String, email: String, password: String): Boolean{
      return when {
          TextUtils.isEmpty(name) -> {
              showErrorSnackBar("Please enter a name")
              false
          }
          TextUtils.isEmpty(email) -> {
              showErrorSnackBar("Please enter a E-mail address")
              false
          }
          TextUtils.isEmpty(password) -> {
              showErrorSnackBar("Please enter password")
              false
          }else ->{
              true
          }
      }
    }

    /**
     * A function to be called the user is registered successfully and entry is made in the firestore database.
     */

    fun userRegisteredSuccess(){
        Toast.makeText(
            this,
            "you have registered succesfully",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        finish()
    }

}