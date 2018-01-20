package kq.cn.customkeyboard;

import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_input_price)
    EditText etInputPrice;
    @BindView(R.id.et_input_num_buy)
    EditText etInputNumBuy;
    @BindView(R.id.et_input_num_sell)
    EditText etInputNumSell;
    @BindView(R.id.layout_opt_container)
    LinearLayout optContainer;
    @BindView(R.id.view_under)
    View underView;

    Unbinder mUnBinder;
    CustomKeyboardManager customKeyboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnBinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mUnBinder) mUnBinder.unbind();
    }

    private void initView() {
        customKeyboardManager = new CustomKeyboardManager(this);

        CustomBaseKeyboard priceKeyboard = new CustomBaseKeyboard(this, R.xml.stock_price_num_keyboard) {
            @Override
            public boolean handleSpecialKey(EditText etCurrent, int primaryCode) {
                if (primaryCode == getKeyCode(R.integer.keycode_cur_price)) {
                    etCurrent.setText("9.999$");
                    return true;
                }
                return false;
            }
        };

        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);

        CustomBaseKeyboard numKeyboard = new CustomBaseKeyboard(this, R.xml.stock_trade_num_keyboard) {
            @Override
            public boolean handleSpecialKey(EditText etCurrent, int primaryCode) {
                Editable editable = etCurrent.getText();
                int start = etCurrent.getSelectionEnd();
                if (primaryCode == getKeyCode(R.integer.keycode_stocknum_000)) {
                    editable.insert(start, "000");
                    return true;
                } else if (primaryCode == getKeyCode(R.integer.keycode_stocknum_half)) {
                    etCurrent.setText(1000 / 2 + "");
                    return true;
                } else if (primaryCode == getKeyCode(R.integer.keycode_stocknum_all)) {
                    setStockNumAll(etCurrent);
                    return true;
                } else if (primaryCode == getKeyCode(R.integer.keycode_stock_sell)) {
                    etCurrent.setText("999999");
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        };

        numKeyboard.setCustomKeyStyle(new CustomBaseKeyboard.SimpleCustomKeyStyle() {
            @Override
            public Drawable getKeyBackground(Keyboard.Key key, EditText etCur) {
                if (getKeyCode(etCur.getContext(), R.integer.keycode_stock_sell) == key.codes[0]) {
                    if (R.id.et_input_num_sell == etCur.getId()) {
                        return getDrawable(etCur.getContext(), R.drawable.bg_custom_key_blue);
                    } else if (R.id.et_input_num_buy == etCur.getId()) {
                        return getDrawable(etCur.getContext(), R.drawable.bg_custom_key_red);
                    }
                }
                return super.getKeyBackground(key, etCur);
            }

            @Override
            public CharSequence getKeyLabel(Keyboard.Key key, EditText etCur) {
                if (getKeyCode(etCur.getContext(), R.integer.keycode_stock_sell) == key.codes[0]) {
                    if (R.id.et_input_num_sell == etCur.getId()) {
                        return "卖出";
                    } else if (R.id.et_input_num_buy == etCur.getId()) {
                        return "买入";
                    }
                }
                return super.getKeyLabel(key, etCur);
            }

            @Override
            public Integer getKeyTextColor(Keyboard.Key key, EditText etCur) {
                if (getKeyCode(etCur.getContext(), R.integer.keycode_stock_sell) == key.codes[0]) {
                    return getResources().getColor(R.color.default_white);
                }
                return super.getKeyTextColor(key, etCur);
            }
        });
        customKeyboardManager.attachTo(etInputNumBuy, numKeyboard);
        customKeyboardManager.attachTo(etInputNumSell, numKeyboard);

        customKeyboardManager.setShowUnderView(underView);
    }

    @OnClick(R.id.tv_stock_all)
    public void setStockNumAll(EditText view) {
        view.setText("10000");
    }
}
