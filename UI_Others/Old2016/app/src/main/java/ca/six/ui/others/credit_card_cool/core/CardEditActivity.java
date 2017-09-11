package ca.six.ui.others.credit_card_cool.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.six.ui.others.R;
import ca.six.ui.others.credit_card_cool.core.pager.CardFragmentAdapter;


public class CardEditActivity extends AppCompatActivity {
    int lastPageSelected = 0;
    private CreditCardView creditCardView;

    private String cardNumber, cvv, cardHolderName, expiry;
    private CardFragmentAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
                int max = pager.getAdapter().getCount();
                if(pager.getCurrentItem() == max -1) {
                    // if last card.
                    onDoneTapped();
                }
                else {
                    showNext();
                }
            }
        });
        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevious();
            }
        });

        setKeyboardVisibility(true);
        creditCardView = (CreditCardView) findViewById(R.id.credit_card_view);


        if(savedInstanceState != null) {
            checkParams(savedInstanceState);
        }
        else {
            checkParams(getIntent().getExtras());
        }

        loadPager();

    }

    private void checkParams(Bundle bundle) {
        if(bundle == null) {
            return;
        }
        cardHolderName = bundle.getString(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
        cvv = bundle.getString(CreditCardUtils.EXTRA_CARD_CVV);
        expiry = bundle.getString(CreditCardUtils.EXTRA_CARD_EXPIRY);
        cardNumber = bundle.getString(CreditCardUtils.EXTRA_CARD_NUMBER);

        creditCardView.setCVV(cvv);
        creditCardView.setCardHolderName(cardHolderName);
        creditCardView.setCardExpiry(expiry);
        creditCardView.setCardNumber(cardNumber);

        if(cardAdapter != null) {
            cardAdapter.notifyDataSetChanged();
        }
    }

    public void refreshNextButton() {
        ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        int max = pager.getAdapter().getCount();
        String text = "next";
        if(pager.getCurrentItem() == max -1) {
            text = "done";
        }
        ((TextView)findViewById(R.id.next)).setText(text);
    }

    public void loadPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                cardAdapter.focus(position);
                if (position == 2) {
                    creditCardView.showBack();
                } else if ((position == 1 && lastPageSelected == 2) || position == 3) {
                    creditCardView.showFront();
                }

                lastPageSelected = position;
                refreshNextButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        pager.setOffscreenPageLimit(4);

        cardAdapter = new CardFragmentAdapter(getSupportFragmentManager(),getIntent().getExtras());
        cardAdapter.setOnCardEntryCompleteListener(new CardFragmentAdapter.ICardEntryCompleteListener() {
            @Override
            public void onCardEntryComplete(int currentIndex) {
                showNext(); // show the next item in the ViewPager
            }

            @Override
            public void onCardEntryEdit(int currentIndex, String entryValue) {
                switch (currentIndex) {
                    case 0:
                        cardNumber = entryValue.replace(CreditCardUtils.SPACE_SEPERATOR, "");
                        creditCardView.setCardNumber(cardNumber);
                        break;
                    case 1:
                        expiry = entryValue;
                        creditCardView.setCardExpiry(entryValue);
                        break;
                    case 2:
                        cvv = entryValue;
                        creditCardView.setCVV(entryValue);
                        break;
                    case 3:
                        cardHolderName = entryValue;
                        creditCardView.setCardHolderName(entryValue);
                        break;
                }
            }
        });

        pager.setAdapter(cardAdapter);
    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString(CreditCardUtils.EXTRA_CARD_CVV, cvv);
        outState.putString(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
        outState.putString(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
        outState.putString(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);


        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        checkParams(inState);
    }


    public void showPrevious() {

        final ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        int currentIndex = pager.getCurrentItem();

        if (currentIndex - 1 >= 0) {
            pager.setCurrentItem(currentIndex - 1);
        }

        refreshNextButton();
    }

    public void showNext() {

        final ViewPager pager = (ViewPager) findViewById(R.id.card_field_container_pager);
        CardFragmentAdapter adapter = (CardFragmentAdapter) pager.getAdapter();

        int max = adapter.getCount();
        int currentIndex = pager.getCurrentItem();

        if (currentIndex + 1 < max) {
            pager.setCurrentItem(currentIndex + 1);
        } else {
//            onDoneTapped();
            // completed the card entry.
            setKeyboardVisibility(false);
        }

        refreshNextButton();
    }

    private void onDoneTapped() {

//        setKeyboardVisibility(false);
//        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        parent.setLayoutParams(layoutParams);


        Intent intent = new Intent();

        intent.putExtra(CreditCardUtils.EXTRA_CARD_CVV, cvv);
        intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
        intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
        intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);


        setResult(RESULT_OK,intent);
        finish();


    }

    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) parent.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
            parent.setLayoutParams(layoutParams);

        }
    }

    private void setKeyboardVisibility(boolean visible) {
        final EditText editText = (EditText) findViewById(R.id.card_number_field);
        if (!visible) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



}
