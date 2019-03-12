package eventbus.example.com.rxrelay.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.safframework.injectview.Injector;

import eventbus.example.com.rxrelay.RxBus;


public class BaseActivity extends AppCompatActivity {

    protected RxBus rxBus;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxBus = RxBus.get();
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Injector.injectInto(this);
    }
}
