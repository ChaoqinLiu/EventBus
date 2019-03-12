package eventbus.example.com.sticky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;

import java.util.concurrent.TimeUnit;

import eventbus.example.com.sticky.app.BaseActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.text4)
    TextView text4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){

        RxView.clicks(text4)
                .throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {

                    public void accept(@NonNull Object o){
                        Intent intent = new Intent(MainActivity.this,StickyActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
