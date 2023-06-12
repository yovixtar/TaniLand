package com.ykstar.bangkit.taniland.pages.startup

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.ActivityAuthBinding
import com.ykstar.bangkit.taniland.pages.MainActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.UserViewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var progressDialog: Dialog

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences = UserPreference(this)
        val token = userPreferences.getToken()
        if (token != null) {
            gotoMainPage()
        }

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        }

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        binding.btnGoogle.setOnClickListener {
            authenticaion()
        }
    }

    private fun authenticaion() {

        if (!InternetActive.isOnline(this)) {
            InternetActive.showNoInternetDialog(this)
        } else {
            progressDialog.show()
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }

    }

    private fun gotoMainPage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                progressDialog.dismiss()
                showPrimaryToast(getString(R.string.kesalahan_authentikasi_coba_lagi), false)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userViewModel = UserViewModel(this)
                    userViewModel.authenticate(user?.displayName, user?.email)
                    userViewModel.authenticationState.observe(this) { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                progressDialog.dismiss()
                                gotoMainPage()
                            }

                            is Resource.Error -> {
                                showPrimaryToast(getString(R.string.kesalahan_authentikasi_coba_lagi), false)
                                progressDialog.dismiss()
                                auth.signOut()
                                googleSignInClient.signOut()
                            }
                        }
                    }
                } else {
                    showPrimaryToast(getString(R.string.kesalahan_authentikasi_coba_lagi), false)
                    auth.signOut()
                    googleSignInClient.signOut()
                }
            }
    }

}