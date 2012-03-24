package com.jumpbyte.android.cardio.plugin;

import io.card.payment.CardIOActivity;
import io.card.payment.ChargeRequest;
import io.card.payment.ChargeableItem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.jumpbyte.android.cardio.App;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

public class AppComm extends Plugin {

	public static final String ACTION_SCAN = "scan";

	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		PluginResult result = new PluginResult(Status.OK);
		result.setKeepCallback(true);

		if (ACTION_SCAN.equals(action)) {
			// start scan here
			Log.i("AppComm", "start scanning");
			startScanning();
		}

		return result;
	}

	private void startScanning() {
		try {
			// TODO Auto-generated method stub
			Intent intent = new Intent(ctx.getApplicationContext(),
					CardIOActivity.class);

			// EXTRA_APP_TOKEN - required
			// (failing to include this will cause an exception)
			intent.putExtra(CardIOActivity.EXTRA_APP_TOKEN,
					"6a32bd3378404e978ec66854583d4626");

			// Create an example "shopping cart" here. Your code will likely be
			// much more complex.
			List<ChargeableItem> myCart = new ArrayList<ChargeableItem>();
			myCart.add(new ChargeableItem("A one dollar item", 100));

			// Create the charge request with parameters:
			// 1. our example cart
			// 2. currency (currently only "USD" supported)
			// 3. whether the charge is live (you'll probably want to keep this
			// false during development)
			ChargeRequest myRequest = new ChargeRequest(myCart, "USD", false);
			intent.putExtra(CardIOActivity.EXTRA_CHARGE_REQUEST, myRequest);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			BroadcastReceiver br = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					Log.i("RESULT BROADCAST",
							"got result: "
									+ intent.getParcelableExtra(CardIOActivity.EXTRA_CHARGE_RESULT));

					// TODO: deliver purchased item.

					// Receiver will be leaked if it is not unregistered.
					// There should only be one active receiver, so this is a
					// good place to unregister it.
					ctx.getApplication().unregisterReceiver(this);
				}

			};

			// Register the broadcast receiver.
			// IMPORTANT: purchased items MUST be delivered in response to this
			// broadcast.
			// It is possible for the charge to complete without the user
			// pressing "Done".
			ctx.getApplicationContext().registerReceiver(br,
					new IntentFilter(CardIOActivity.ACTION_CHARGE_COMPLETE));

			// Start the activity. We're using 1 for our request code,
			// but you can use whatever value makes sense.
			ctx.getApplicationContext().startActivity(intent);
		} catch (Exception e) {
			Log.e("AppComm", e.getMessage(), e);
		}
		Log.i("AppComm", "activity started");
	}
}
