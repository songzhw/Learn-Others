package ca.six.learn.artitecture.flow.statemachine

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import ca.six.learn.artitecture.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UIStateMachineDemo : AppCompatActivity(R.layout.activity_btn_tv) {
    // 使用这个viewModels就得让kotlinOptions的target是java8 (默认是java6)
    val vm by viewModels<UIStateMachineViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.getUser()
                .collect { event ->  //collect得在被coroutine或suspect函数之中才行!
                    when (event) {
                        is Start -> println("szw start : ${Thread.currentThread().name}") //=> 09:15:05, main线程上
                        is Success -> println("szw succ = ${event.value} : ${Thread.currentThread().name}") //=> 09:15:07, main线程上
                    }
                }
        }

    }


}

class UIStateMachineViewModel : ViewModel() {
    fun getUser() = Machine(Dispatchers.IO) {
        // http result
        Thread.sleep(2000)
        println("szw getUser : ${Thread.currentThread().name}") //=> szw getUser : DefaultDispatcher-worker-1
        "200"
    }
}


sealed class UIState<out T>
object Start : UIState<Nothing>()
object Completion : UIState<Nothing>()
class Success<T>(val value: T) : UIState<T>() //generics不能用于object, 得用于class
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
