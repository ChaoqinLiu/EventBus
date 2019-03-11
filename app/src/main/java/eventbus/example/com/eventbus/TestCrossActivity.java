package eventbus.example.com.eventbus;

import android.os.Bundle;

import eventbus.example.com.eventbus.app.BaseActivity;
import eventbus.example.com.eventbus.domain.CrossActivityEvent;


public class TestCrossActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxBus.post(new CrossActivityEvent());
    }
}
