package eventbus.example.com.sticky;

import android.os.Bundle;
import android.widget.Toast;

import eventbus.example.com.sticky.app.BaseActivity;
import eventbus.example.com.sticky.domain.NormalEvent;
import eventbus.example.com.sticky.domain.StickyEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class StickyActivity extends BaseActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initData();
        registerEvents();
    }

    private void initData(){

        rxBus.postSticky(new StickyEvent());
        rxBus.post(new NormalEvent());
    }

    private void registerEvents(){
        compositeDisposable.add(rxBus.registerSticky(StickyEvent.class, AndroidSchedulers.mainThread(), new Consumer<StickyEvent>() {
            @Override
            public void accept(@NonNull StickyEvent stickyEvent) throws Exception {
                Toast.makeText(StickyActivity.this,"this is StickEvent",Toast.LENGTH_SHORT).show();
            }
        }));

        compositeDisposable.add(rxBus.register(NormalEvent.class, AndroidSchedulers.mainThread(), new Consumer<NormalEvent>() {
            @Override
            public void accept(@NonNull NormalEvent normalEvent) throws Exception {
                Toast.makeText(StickyActivity.this, "this is NormalEvent", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        rxBus.removeSticky(StickyEvent.class);
        compositeDisposable.clear();
    }
}
