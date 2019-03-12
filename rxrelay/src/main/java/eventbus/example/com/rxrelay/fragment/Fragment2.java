package eventbus.example.com.rxrelay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;

import java.util.concurrent.TimeUnit;

import eventbus.example.com.rxrelay.R;
import eventbus.example.com.rxrelay.app.BaseFragment;
import eventbus.example.com.rxrelay.domain.Fragment2Event;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class Fragment2 extends BaseFragment {

    @InjectView(R.id.text2)
    TextView text2;

    @InjectView(R.id.button2)
    Button button2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_2, container, false);

        Injector.injectInto(this, v);

        initView();

        return v;
    }

    private void initView(){
        RxView.clicks(button2)
                .throttleFirst(500,TimeUnit.MILLISECONDS) //间隔500毫秒再发送下一个点击事件
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(@NonNull Object o){
                        rxBus.post(new Fragment2Event());
                    }
                });

    }

    public TextView getText2(){
        return text2;
    }
}
