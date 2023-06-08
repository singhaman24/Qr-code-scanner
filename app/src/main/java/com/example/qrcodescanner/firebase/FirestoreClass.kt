package com.example.qrcodescanner.firebase

import com.example.qrcodescanner.Constents
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.QrDetail
import com.example.qrcodescanner.SignUpActivity
import com.example.qrcodescanner.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A custom class where we will add the operation performed for the firestore database.
 */

class FirestoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore= FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the firestore database.
     */
    fun resisterUser(activity: SignUpActivity, userInfo: User){

        mFireStore.collection(Constents.USERS)
            // Document ID for users fields. Here the document it is the User ID.

            .document(getCurrentUserId())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //   Here call a function of sign up activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
    }

    /**
     * A function for getting the user id of current logged user.
     */
    fun getCurrentUserId(): String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId =""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

// here we are making entry for qr details in the firebase.
fun getQrDetails(activity: MainActivity, qrDetails: QrDetail){
    mFireStore.collection(Constents.QR)
        .document().set(qrDetails)
        .addOnSuccessListener {
            activity.qrScanDetails()
        }
}

}