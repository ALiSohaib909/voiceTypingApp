package com.my.newvoicetyping

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.my.newvoicetyping.Utils.checkIfKeyboardEnabled
import com.my.newvoicetyping.Utils.isInputMethodEnabled
import com.my.newvoicetyping.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        animation()
        binding.btnletsstart.setOnClickListener {
            startActivity(Intent(this,Secondactivity::class.java))
        }
    }
    private fun nextMove() {
        if (checkIfKeyboardEnabled()) {
            if (isInputMethodEnabled()) {
                startActivity(Intent(this@MainActivity, Appmainactivity::class.java))
            } else {
                startActivity(Intent(this@MainActivity, Secondactivity::class.java))
            }
        } else {
            startActivity(Intent(this@MainActivity, Secondactivity::class.java))
        }
    }

    private fun animation() {
        binding.btnletsstart.visibility = View.INVISIBLE
        binding.animationView.animate()
        binding.animationView.playAnimation()

        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                Log.e("Animation:", "end")
                binding.animationView.cancelAnimation()
                binding.animationView.visibility = View.INVISIBLE
                binding.btnletsstart.visibility = View.VISIBLE

            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

}