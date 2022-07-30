package com.bottomnavcomp.ui.auth

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bottomnavcomp.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var verificationID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRequest.setOnClickListener {
            requestSMS()
        }

        binding.verifyCodeBtn.setOnClickListener {
            val text = binding.editCode.text.toString()
            if (text.isNotEmpty() && text.length == 6 && isNumeric(text)) {
                verifyCode(text)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Incorrect value. Try again",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.e("AUTH", "onVerificationCompleted")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("AUTH", "onVerificationFailed: " + e.message)
            }

            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, p1)
                verificationID = s
            }

        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, code)
        signIn(credential)

        Log.e("AUTH", "verifyCode method: $verificationID")
    }

    //Method to register user into the database
    private fun signIn(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successfully!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                } else {
                    Log.e("AUTH", "signIn failed: " + it.exception?.message)
                    Toast.makeText(
                        requireContext(),
                        "signIn failed: " + it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        Log.e("AUTH", "SIGN in method: $credential")
    }

    private fun requestSMS() {
        val phone = binding.editPhone.text.toString()
        if (phone.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "The field is empty. Enter a number",
                Toast.LENGTH_SHORT
            ).show()
        } else if (isNumeric(phone) && phone.length == 10) {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber("+996$phone")       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity())                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

            hideKeyboard()
            isNumEntered(true)
            startTimeCounter()

            Log.e("AUTH", "request SMS method: $phone")
        }
    }

    //Method to show and hide necessary views
    private fun isNumEntered(b: Boolean) {
        if (b) {
            binding.editPhone.visibility = View.GONE
            binding.btnRequest.visibility = View.GONE
            binding.editCode.visibility = View.VISIBLE
            binding.verifyCodeBtn.visibility = View.VISIBLE
            binding.counter.visibility = View.VISIBLE
        } else {
            binding.editPhone.visibility = View.VISIBLE
            binding.btnRequest.visibility = View.VISIBLE
            binding.editCode.visibility = View.GONE
            binding.verifyCodeBtn.visibility = View.GONE
            binding.counter.visibility = View.INVISIBLE
        }
    }

    //Embedded method to implement a counter
    private fun startTimeCounter() {
        binding.editCode.setText("")
        binding.editPhone.setText("")

        var counter = 59
        var zeros = "00:"
        object : CountDownTimer(counter.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (counter == 9) {
                    zeros += "0"
                }
                binding.counter.text = zeros + counter.toString()
                counter--
            }

            override fun onFinish() {
                isNumEntered(false)
                hideKeyboard()
            }
        }.start()
    }

    //Method to manually hide mobile keyboard
    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //Method to check if string has only digits
    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}