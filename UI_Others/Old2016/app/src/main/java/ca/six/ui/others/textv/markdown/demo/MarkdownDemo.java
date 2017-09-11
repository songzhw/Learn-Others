package ca.six.ui.others.textv.markdown.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import ca.six.ui.others.R;
import ca.six.ui.others.textv.markdown.MarkdownView;


public class MarkdownDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarkdownView markdownView = new MarkdownView(this);
        setContentView(markdownView);

        // TODO: 2017-03-11 title(#) is still not finished
        String markdown = "@andres ```this is awesome``` and **bold** \n" +
                "```\nfunction(){\n  return 1;\n}\n``` " +
                "cc @everyone. Here is a link www.google.com\n"+
                "\n*xie ti zi*\n"+
                "\n# Title 01\n"+
                "\n## Title 02";
        markdownView.setMarkdown(markdown);
    }
}