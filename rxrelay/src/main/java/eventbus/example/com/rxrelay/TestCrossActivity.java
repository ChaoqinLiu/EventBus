package eventbus.example.com.rxrelay;

import android.os.Bundle;

import eventbus.example.com.rxrelay.app.BaseActivity;
import eventbus.example.com.rxrelay.domain.CrossActivityEvent;


public class TestCrossActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxBus.post(new CrossActivityEvent());
    }
}
