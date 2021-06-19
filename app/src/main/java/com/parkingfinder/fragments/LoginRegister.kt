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
import com.parkingfinder.activities.MainActivity
import com.parkingfinder.activities.SecondActivity
import com.parkingfinder.interfaces.ActivityFragmentCommunication


class LoginRegister : Fragment() {
    private var activityFragmentCommunication: ActivityFragmentCommunication? = null
    private var btn_register: Button? = null
    private var btn_login: Button? = null
    private var et_email: EditText? = null
    private var et_password: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun register(email: String, password: String) {
        if (email.isNullOrEmpty()) {
            et_email?.setError("E-mail can't be empty!");
            et_email?.requestFocus();
        } else if (password.isNullOrEmpty()) {
            et_password?.setError("Password can't be empty!");
            et_password?.requestFocus();
        } else {
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
                            et_password?.setError("Password is too weak! Minimum 6 characters required.");
                            et_password?.requestFocus();
                        } catch (e: FirebaseAuthUserCollisionException) {
                            et_email?.setError("E-mail is already in use!");
                            et_email?.requestFocus();
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            et_email?.setError("Invalid e-mail!");
                            et_email?.requestFocus();
                        }
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        if (email.isNullOrEmpty()) {
            et_email?.setError("E-mail can't be empty!");
            et_email?.requestFocus();
        } else if (password.isNullOrEmpty()) {
            et_password?.setError("Password can't be empty!");
            et_password?.requestFocus();
        } else {
            val tag = "Login"
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(tag, "signInWithEmail:success")
                        openSecondActivity()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidUserException) {
                            et_email?.setError("This account does not exist!");
                            et_email?.requestFocus();
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

    private fun openSecondActivity() {
        val intent = Intent(context, SecondActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login_register, container, false)
        btn_register = view.findViewById(R.id.btn_register)
        btn_login = view.findViewById(R.id.btn_login)
        et_email = view.findViewById(R.id.et_email)
        et_password = view.findViewById(R.id.et_password)
        return view;
    }

    companion object {
        fun newInstance() = LoginRegister()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_register?.setOnClickListener {
            register(et_email?.text.toString(), et_password?.text.toString())
        }
        btn_login?.setOnClickListener {
            login(et_email?.text.toString(), et_password?.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityFragmentCommunication) {
            activityFragmentCommunication = context
        }
    }
}