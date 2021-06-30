package com.parkingfinder.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parkingfinder.R
import com.parkingfinder.activities.ParkingActivity
import com.parkingfinder.helper.PrefConfig
import com.parkingfinder.interfaces.ActivityFragmentCommunication

class LoginRegister : Fragment() {
    private var activityFragmentCommunication: ActivityFragmentCommunication? = null
    private var btnRegister: Button? = null
    private var btnLogin: Button? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var emailString: String = ""

    private fun register(email: String, password: String) {
        when {
            email.isEmpty() -> {
                etEmail?.error = "E-mail can't be empty!"
                etEmail?.requestFocus()
            }
            password.isEmpty() -> {
                etPassword?.error = "Password can't be empty!"
                etPassword?.requestFocus()
            }
            else -> {
                val tag = "Register"
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(tag, "createUserWithEmail:success")
                            openSecondActivity()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthWeakPasswordException) {
                                etPassword?.error =
                                    "Password is too weak! Minimum 6 characters required."
                                etPassword?.requestFocus()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                etEmail?.error = "E-mail is already in use!"
                                etEmail?.requestFocus()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                etEmail?.error = "Invalid e-mail!"
                                etEmail?.requestFocus()
                            }
                        }
                    }
            }
        }
    }

    private fun login(email: String, password: String) {
        when {
            email.isEmpty() -> {
                etEmail?.error = "E-mail can't be empty!"
                etEmail?.requestFocus()
            }
            password.isEmpty() -> {
                etPassword?.error = "Password can't be empty!"
                etPassword?.requestFocus()
            }
            else -> {
                val tag = "Login"
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            PrefConfig.saveEmailInPref(requireContext(), email)
                            Log.d(tag, "signInWithEmail:success")
                            openSecondActivity()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidUserException) {
                                etEmail?.error = "This account does not exist!"
                                etEmail?.requestFocus()
                            } catch (e: FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    context,
                                    "Invalid e-mail/password!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } catch (e: FirebaseTooManyRequestsException) {
                                Toast.makeText(
                                    context,
                                    "We have blocked all requests from this device due to unusual activity. Try again later.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
            }
        }
    }

    private fun openSecondActivity() {
        val intent = Intent(context, ParkingActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_login_register, container, false)
        btnRegister = view.findViewById(R.id.btn_register)
        btnLogin = view.findViewById(R.id.btn_login)
        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)
        if (!PrefConfig.loadEmailFromPref(requireContext()).isNullOrBlank()) {
            emailString = PrefConfig.loadEmailFromPref(requireContext())!!
            etEmail?.append(emailString)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegister?.setOnClickListener {
            register(etEmail?.text.toString(), etPassword?.text.toString())
        }
        btnLogin?.setOnClickListener {
            login(etEmail?.text.toString(), etPassword?.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }
}