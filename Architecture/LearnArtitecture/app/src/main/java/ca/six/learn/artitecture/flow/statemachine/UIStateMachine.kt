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

    // 其实可以是viewmodel调用的方法
    fun onResult(state: UIState<String>) {
        when (state) {
            is Start -> println("start")
            is Success -> println(state.value)
        }
    }

}


sealed class UIState<out T>
object Start : UIState<Nothing>()
object Completion : UIState<Nothing>()
class Success<out T>(val value: T) : UIState<T>() //generics不能用于object, 得用于class
class Failure(val exception: Throwable) : UIState<Nothing>()


object Machine {
    operator fun <T> invoke(
        actionOn: CoroutineContext = Dispatchers.Default,
        action: suspend () -> T
    ): Flow<UIState<T>> =
        flow<UIState<T>> { emit(Success(action())) }
            .onStart { emit(Start) }
            .catch { exception -> emit(Failure(exception)) }
            .onCompletion { emit(Completion) }
            .flowOn(actionOn)
}

