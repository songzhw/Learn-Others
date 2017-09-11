package ca.six.ui.others.credit_card_cool.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import ca.six.ui.others.R;
import ca.six.ui.others.credit_card_cool.core.CardEditActivity;
import ca.six.ui.others.credit_card_cool.core.CreditCardUtils;
import ca.six.ui.others.credit_card_cool.core.CreditCardView;


public class CreditCardDemoActivity extends AppCompatActivity {
    private CreditCardDemoActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_view);
        self = this;

        findViewById(R.id.add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, CardEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            LinearLayout cardContainer = (LinearLayout) findViewById(R.id.card_container);


            String name = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            String cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER);
            String expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY);
            String cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV);

            if(reqCode == 0) {
                CreditCardView creditCardView = new CreditCardView(this);
                creditCardView.setCVV(cvv);
                creditCardView.setCardHolderName(name);
                creditCardView.setCardExpiry(expiry);
                creditCardView.setCardNumber(cardNumber);

                int index = cardContainer.getChildCount();
                cardContainer.addView(creditCardView);
                initListener(index, creditCardView);
            }
            else {
                CreditCardView creditCardView = (CreditCardView) cardContainer.getChildAt(reqCode);
                creditCardView.setCardExpiry(expiry);
                creditCardView.setCardNumber(cardNumber);
                creditCardView.setCardHolderName(name);
                creditCardView.setCVV(cvv);

            }
        }
    }


    private void initListener(final int index, CreditCardView creditCardView) {
        creditCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditCardView creditCardView = (CreditCardView) v;
                String cardNumber = creditCardView.getCardNumber();
                String expiry = creditCardView.getExpiry();
                String cardHolderName = creditCardView.getCardHolderName();

                Intent intent = new Intent(self, CardEditActivity.class);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, cardHolderName);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_NUMBER, cardNumber);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_EXPIRY, expiry);
                intent.putExtra(CreditCardUtils.EXTRA_CARD_SHOW_CARD_SIDE, CreditCardUtils.CARD_SIDE_FRONT);

                startActivityForResult(intent, index);

            }
        });

    }

}
