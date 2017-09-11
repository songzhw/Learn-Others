package ca.six.ui.others.applock;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

public class PINInputController implements TextView.OnEditorActionListener {

    public interface InputEventListener {
        public void onInputEntered(String input);
    }

    private PINInputView inputView;
    private InputEventListener eventListener;

    /**
     * If you want to actually receive events after this, make sure you call setInputEventListener
     */
    public PINInputController(PINInputView inputView) {
        this(inputView, null);
    }

    public PINInputController(PINInputView inputView, InputEventListener eventListener) {
        this.inputView = inputView;
        this.inputView.setOnEditorActionListener(this);
        this.eventListener = eventListener;

        inputView.postDelayed(new Runnable(){
            public void run(){
                PINInputController.this.inputView.ensureKeyboardVisible();
            }
        }, 300);
    }

    public PINInputController setPasswordCharactersEnabled(boolean passwordCharacterEnabled) {
        this.inputView.setPasswordCharactersEnabled(passwordCharacterEnabled);
        return this;
    }

    public PINInputController setInputNumbersCount(int inputNumbersCount) {
        this.inputView.setInputViewsCount(inputNumbersCount);
        return this;
    }

    public PINInputController setInputEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        return this;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(isSoftKeyboardFinishedAction(textView, i, keyEvent)){
            if(eventListener != null) eventListener.onInputEntered(inputView.getText().toString());
            inputView.reset();
            return true;
        }
        return false;
    }

    private boolean isSoftKeyboardFinishedAction(TextView view, int action, KeyEvent event) {
        // Some devices return null event on editor actions for Enter Button
        return (action == EditorInfo.IME_ACTION_DONE || action == EditorInfo.IME_ACTION_GO || action == EditorInfo.IME_ACTION_SEND) && (event == null || event.getAction() == KeyEvent.ACTION_DOWN);
    }

}
