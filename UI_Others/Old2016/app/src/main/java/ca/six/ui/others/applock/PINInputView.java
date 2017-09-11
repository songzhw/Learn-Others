package ca.six.ui.others.applock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.WeakHashMap;

import ca.six.ui.others.R;


public class PINInputView extends LinearLayout implements TextWatcher {

    private static final String TAG = "PIN";

    private Paint itemTextPaint;
    private Paint itemBackgroundPaint;

    private int inputViewsCount = 10;
    private PINItemView[] pinItemViews;

    private EditText editText;
    private String lastText = "";

    private boolean passwordCharactersEnabled = true;

    private WeakHashMap<PINItemView, PINItemAnimator> animators = new WeakHashMap<PINItemView, PINItemAnimator>();

    public PINInputView(Context ctx){
        this(ctx, null);
    }

    public PINInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);
        setupEditText();

        itemTextPaint = new Paint();
        itemBackgroundPaint = new Paint();
        inputViewsCount = 4;

        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PINInputView);
            itemTextPaint.setColor(a.getColor(R.styleable.PINInputView_pinTextColor, getResources().getColor(R.color.pin__default_item_text)));
            itemBackgroundPaint.setColor(a.getColor(R.styleable.PINInputView_pinBackgroundColor, getResources().getColor(R.color.pin__default_item_background)));
            a.recycle();
        } else{
            itemTextPaint.setColor(getResources().getColor(R.color.pin__default_item_text));
            itemBackgroundPaint.setColor( getResources().getColor(R.color.pin__default_item_background));
        }
    }

    protected void setupEditText() {
        removeAllViews();

        editText = new EditText(getContext());
        editText.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        editText.setTextColor(getResources().getColor(android.R.color.transparent));
        editText.setCursorVisible(false);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.addTextChangedListener(this);

        addView(editText);
    }

    public PINInputView setInputViewsCount(int inputViewsCount) {
        this.inputViewsCount = inputViewsCount;
        reset();
        return this;
    }

    public void reset() {
        this.lastText = "";
        this.editText.setText("");

        if(pinItemViews != null)
            animateLastOut();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            ensureKeyboardVisible();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void ensureKeyboardVisible() {
        editText.requestFocus();
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, 0);
    }

    @Override
         public void onDraw(Canvas canvas) {
        if(pinItemViews == null || pinItemViews.length != inputViewsCount)
            setupItemViews(canvas);

        String text = editText.getText().toString();
        for(int i = 0; i < pinItemViews.length; i++)
            pinItemViews[i].draw(canvas, i < text.length() ? (passwordCharactersEnabled ? "*" : text.substring(i, i + 1)) : "");
    }

    private void setupItemViews(Canvas canvas) {
        pinItemViews = new PINItemView[inputViewsCount];

        int cellWidth = canvas.getWidth() / inputViewsCount;
        int desiredRadius = Math.min(cellWidth / 2, canvas.getHeight() / 2);

        itemTextPaint.setTextSize((int)(desiredRadius * .85));

        for(int i = 0; i < pinItemViews.length; i++)
            pinItemViews[i] = new PINItemView(getPositionInCanvas(canvas, i, cellWidth), desiredRadius, itemTextPaint, itemBackgroundPaint);
    }

    private float[] getPositionInCanvas(Canvas canvas, int position, int cellWidth) {
        return new float[]{
                (cellWidth * position) + (cellWidth / 2),
                canvas.getHeight() / 2
        };
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable == null || lastText == null)
            return;

        String text = editable.toString();
        if(inputViewsCount < text.length()){
            // EditText已经满了， 如已经有了”2352“的数字。 这时仍继续输入”6“， 这时就进入此分支。 text = 23526, lastText= 2352
            editText.setText(lastText);
            editText.setSelection(editText.getText().length());
        }
        else if(text.length() < lastText.length()){
            // 按退格键进入此分支。 如原来有“233”， 现在按退格， text= 23, lastText=233
            animateLastOut(); // 有字样的大圆，做动画变小
            this.lastText = text;
        }
        else if(lastText.length() < text.length()){
            // 开始，直到没有输满之前，全是走这个分支。这时text, lastText， 可能分别是：“”2， null", "23, 2", "235, 23“这样的
            animateLastIn(); // 原来的小圆心，现在做动画变大，并带上字样
            this.lastText = text;
        }
    }

    private void animateLastOut() {
        int startingIndex = editText.getText().toString().length(); // One after current length, only happens on backspace

        for(int i = pinItemViews.length - 1; startingIndex <= i; i--)
            if(!pinItemViews[i].isAnimatedOut())
                animate(pinItemViews[i], PINItemAnimator.ItemAnimationDirection.OUT);
    }

    private void animateLastIn() {
        PINItemView item = pinItemViews[editText.getText().toString().length() - 1];
        animate(item, PINItemAnimator.ItemAnimationDirection.IN);
    }

    private void animate(PINItemView view, PINItemAnimator.ItemAnimationDirection direction) {
        cancelPreviousAnimation(view);
        view.setAnimationDirection(direction);
        PINItemAnimator animator = new PINItemAnimator(this, view, direction);
        animators.put(view, animator);
        animator.start();
    }

    private void cancelPreviousAnimation(PINItemView view) {
        PINItemAnimator animator = animators.get(view);
        try{
            animator.cancel();
            animators.put(view, null);
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener actionListener) {
        editText.setOnEditorActionListener(actionListener);
    }

    public void setPasswordCharactersEnabled(boolean passwordCharactersEnabled) {
        this.passwordCharactersEnabled = passwordCharactersEnabled;
    }

}
