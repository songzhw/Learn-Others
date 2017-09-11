package ca.six.ui.others.textv.markdown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import ca.six.ui.others.R;
import ca.six.ui.others.textv.spanable.Spanny;

public class MarkdownView extends LinearLayout {

    private static final String TAG = MarkdownView.class.getSimpleName();

    public MarkdownView(Context context) {
        super(context);
        init(context, null);
    }

    public MarkdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMarkdown(String markdown) {
        removeAllViews();

        Spanny currentText = null;

        MarkdownParser parser = new MarkdownParser(markdown);
        while (parser.next()) {
            String markdownPart = parser.contentString();

            if (parser.contentIsMultiLineCode()) {
                addCurrentTextView(currentText);

                currentText = new Spanny(markdownPart);
                addMultiLineCodeView(currentText);

                currentText = null;
            } else {
                if (currentText == null) {
                    currentText = new Spanny();
                }

                if (parser.contentIsBold())
                    currentText.append(markdownPart, new StyleSpan(Typeface.BOLD));

                if (parser.contentIsItalics())
                    currentText.append(markdownPart, new StyleSpan(Typeface.ITALIC));

                if (parser.contentIsStrikeThrough())
                    currentText.append(markdownPart, new StrikethroughSpan());

                if (parser.contentIsSingleLineCode())
                    currentText.append(markdownPart, new BackgroundColorSpan(Color.LTGRAY));

                if (parser.contentIsMention())
                    currentText.append("@" + markdownPart, new ForegroundColorSpan(Color.BLUE));

                if (parser.contentWithNoStyle()) {
                    currentText.append(markdownPart);
                }

            }
        }

        addCurrentTextView(currentText);
    }

    private void addCurrentTextView(Spanny text) {
        if (text == null)
            return;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.markdown_viewer_simple_view, this, false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        addView(view);
    }

    private void addMultiLineCodeView(Spanny text) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.markdown_viewer_multi_line_code_view, this, false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        addView(view);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setOrientation(VERTICAL);
    }

}
