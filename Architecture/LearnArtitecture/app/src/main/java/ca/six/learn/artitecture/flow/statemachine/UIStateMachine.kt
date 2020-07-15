package ca.six.learn.artitecture.flow.statemachine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ca.six.learn.artitecture.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UIStateMachineDemo : AppCompatActivity(R.layout.activity_btn_tv) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

        }

    }

}

object BizState {
    operator fun <T> invoke(
        actionOn: CoroutineContext = Dispatchers.Default,
        action: () -> T
    ): Flow<T> {
        return flow<T> { emit(action()) }
            .onStart { emit() }
            .catch { exception -> emit(exception) }
            .onCompletion { emit() }
            .flowOn(actionOn)
    }
}

sealed class UIState<T>
object Start : UIState<Unit>()
object Completion: UIState<Unit>()
object Success<T>: UIState<T>() //generics不能用于object, 得用于class