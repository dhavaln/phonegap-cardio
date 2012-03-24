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