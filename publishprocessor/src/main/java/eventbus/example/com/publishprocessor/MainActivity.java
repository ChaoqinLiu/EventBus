package eventbus.example.com.publishprocessor;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private Disposable disposable;

    @InjectView(R.id.text1)
    TextView text1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        registerEvents();
    }

    private void initViews(){

        RxView.clicks(text1)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                        Intent i = new Intent(MainActivity.this,CrossActivity.class);
                        startActivity(i);
                    }
                });
    }

    private void registerEvents(){

        disposable = rxBus.toFlowable(CrossActivityEvent.class)
                .subscribe(new Consumer<CrossActivityEvent>() {
                    @Override
                    public void accept(@NonNull CrossActivityEvent crossActivityEvent) throws Exception {
                        Toast.makeText(MainActivity.this,"来自MainActivity的消息",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
