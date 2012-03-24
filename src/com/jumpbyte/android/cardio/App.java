package com.jumpbyte.android.cardio;

import io.card.payment.CardIOActivity;
import io.card.payment.ChargeRequest;
import io.card.payment.ChargeResult;
import io.card.payment.ChargeStatus;
import io.card.payment.ChargeableItem;

import java.util.ArrayList;
import java.util.List;

import com.phonegap.DroidGap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class App extends DroidGap {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");
	}
	
    public void onCreate1(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.main);
        
     // Find the scan button and setup an onClickListener
        Button scanButton = (Button) findViewById(R.id.chargeCreditCardButton);
        scanButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent intent = new Intent(App.this, CardIOActivity.class);

                // EXTRA_APP_TOKEN - required
                // (failing to include this will cause an exception)
                intent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, "6a32bd3378404e978ec66854583d4626");

                // Create an example "shopping cart" here. Your code will likely be much more complex.
                List<ChargeableItem> myCart = new ArrayList<ChargeableItem>();
                myCart.add(new ChargeableItem("A one dollar item", 100));

                // Create the charge request with parameters:
                // 1. our example cart
                // 2. currency (currently only "USD" supported)
                // 3. whether the charge is live (you'll probably want to keep this false during development)
                ChargeRequest myRequest = new ChargeRequest(myCart, "USD", false);
                intent.putExtra(CardIOActivity.EXTRA_CHARGE_REQUEST, myRequest);

                BroadcastReceiver br = new BroadcastReceiver(){
                	@Override
                	public void onReceive(Context context, Intent intent) {
                		// TODO Auto-generated method stub
                		Log.i("RESULT BROADCAST", "got result: "
                                + intent.getParcelableExtra(CardIOActivity.EXTRA_CHARGE_RESULT));

                            // TODO: deliver purchased item.

                            // Receiver will be leaked if it is not unregistered.
                            // There should only be one active receiver, so this is a good place to unregister it.
                            unregisterReceiver(this);
                	}
                    
                };
                
                // Register the broadcast receiver.
                // IMPORTANT: purchased items MUST be delivered in response to this broadcast.
                // It is possible for the charge to complete without the user pressing "Done".
                App.this.registerReceiver(br, new IntentFilter(CardIOActivity.ACTION_CHARGE_COMPLETE)
                );

                // Start the activity. We're using 1 for our request code,
                // but you can use whatever value makes sense.
                App.this.startActivityForResult(intent, 1);
        	}
        });
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Ensure the requestCode is what specified when we started activity
		if (requestCode == 1) {

			if (data != null
					&& data.hasExtra(CardIOActivity.EXTRA_CHARGE_RESULT)) {
				// charge mode will return results via EXTRA_CHARGE_RESULT

				TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

				ChargeResult result = data
						.getParcelableExtra(CardIOActivity.EXTRA_CHARGE_RESULT);

				if (result.getChargeStatus() == ChargeStatus.CHARGE_CONFIRMED) {
					resultTextView.setText(String.format(
							"Charge %s was successful: %s",
							result.getChargeId(), result.getMessage()));
				} else {
					resultTextView.setText(String.format("Sorry! %s",
							result.getMessage()));
				}
			} else {
				// No data means that the user canceled
				Log.i("myApp", String.format("User canceled card scan."));
			}
		}
	}
}