package ca.six.learn.artitecture.flow.statemachine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ca.six.learn.artitecture.R
import kotlinx.coroutines.launch

class UIStateMachineDemo : AppCompatActivity(R.layout.activity_btn_tv) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

        }

    }

}