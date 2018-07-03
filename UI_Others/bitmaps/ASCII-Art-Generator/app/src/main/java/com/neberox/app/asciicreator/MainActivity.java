/*
 * Copyright (c) 2018 Muhammad Umar (https://github.com/zelin)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.neberox.app.asciicreator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.neberox.library.asciicreator.ASCIIConverter;
import com.neberox.library.asciicreator.utilities.OnBitmapTaskListener;
import com.neberox.library.asciicreator.utilities.OnStringTaskListener;

public class MainActivity extends AppCompatActivity {
    private TextView fontText;

    private SeekBar seekBar;

    private ImageView ivShow;
    private ImageView iv;

    private ASCIIConverter converter;

    private Switch switchGrayScale;
    private Switch swtichReverseLum;

    private int SEEK_BAR_STEP = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivShow =  findViewById(R.id.mainImage);
        iv =  findViewById(R.id.imageView);

        switchGrayScale =  findViewById(R.id.switch2);
        swtichReverseLum =  findViewById(R.id.switch1);

        fontText =  findViewById(R.id.fontText);

        seekBar =  findViewById(R.id.fontSeekBar);
        seekBar.setMax(50); // We are going from 10 to 60
        seekBar.setProgress(8);

        fontText.setText("Font : " + String.valueOf(seekBar.getProgress() + SEEK_BAR_STEP));

        converter = new ASCIIConverter(MainActivity.this, seekBar.getProgress() + SEEK_BAR_STEP);

        converter.setFontSize(18);
        converter.setReversedLuminance(false);
        converter.setGrayScale(false);

        findViewById(R.id.convertBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmapSrc = BitmapFactory.decodeResource(getResources(), R.drawable.beauty);
                try {
                    converter.createASCIIImage(bitmapSrc, new OnBitmapTaskListener() {
                        @Override
                        public void onTaskCompleted(Bitmap data) {
                            iv.setImageBitmap(data);
                        }
                    });

                    converter.createASCIIString(bitmapSrc, new OnStringTaskListener() {
                        @Override
                        public void onTaskCompleted(String data) {
                            Log.d("ASCII-GENERATOR", data);
                        }
                    });


                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        switchGrayScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                converter.setGrayScale(isChecked);
            }
        });

        swtichReverseLum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                converter.setReversedLuminance(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                converter.setFontSize(progress + SEEK_BAR_STEP);
                fontText.setText("Font : " + String.valueOf(seekBar.getProgress() + SEEK_BAR_STEP));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {            }
        });

    }
}
