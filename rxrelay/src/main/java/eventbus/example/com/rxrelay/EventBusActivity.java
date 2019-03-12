package eventbus.example.com.rxrelay;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;


import eventbus.example.com.rxrelay.app.BaseActivity;
import eventbus.example.com.rxrelay.domain.Fragment1Event;
import eventbus.example.com.rxrelay.domain.Fragment2Event;
import eventbus.example.com.rxrelay.fragment.Fragment1;
import eventbus.example.com.rxrelay.fragment.Fragment2;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class EventBusActivity extends BaseActivity {

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);

        initViews();
        registerEvents();
    }

    private void initViews(){

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout1, fragment1, fragment1.getClass().getName());
        fragmentTransaction.replace(R.id.layout2, fragment2, fragment2.getClass().getName());
        fragmentTransaction.commit();
    }

    private void registerEvents(){

        compositeDisposable.add(rxBus.toObservable(Fragment1Event.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                fragment2.getText2().setText("fragment2 已经接收到事件");
            }
        }));

        compositeDisposable.add(rxBus.toObservable(Fragment2Event.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        fragment1.getText1().setText("fragment1 已经接收到事件");
                    }
                }));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        compositeDisposable.clear();
    }
}
