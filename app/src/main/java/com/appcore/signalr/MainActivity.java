package com.appcore.signalr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        HubConnection connection = new HubConnection("http://localhost/apptest/hub");
        HubProxy proxy = connection.createHubProxy("mydemohub");
        SignalRFuture<Void> awaitConnection = connection.start();

        try {
            awaitConnection.get();
        }
        catch (InterruptedException interruptException)
        {
            Log.v("", interruptException.getLocalizedMessage());
        }
        catch (ExecutionException executionException)
        {
            Log.v("", executionException.getLocalizedMessage());
        }

        proxy.invoke("JoinGroup", "Group1");
        proxy.on("livefeed", new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                Log.d("", s);
            }
        }, String.class);
    }
}
