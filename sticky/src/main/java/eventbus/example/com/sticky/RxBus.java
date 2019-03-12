package eventbus.example.com.sticky;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RxBus {

    private Relay<Object> bus = null;
    private static RxBus instance;
    private final Map<Class<?>, Object> mStickyEventMap;

    //禁用构造方法
    public RxBus(){
        bus = PublishRelay.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus get(){
        return Holder.BUS;
    }

    public void post(Object event){
        bus.accept(event);
    }

    public void postSticky(Object event){
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        bus.accept(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType){
        return bus.ofType(eventType);
    }

    /**
     * 根据传递的eventType 类型返回特定类型(eventType)的被观察者
     */

    public <T> Observable<T> toObservableSticky(final Class<T> eventType){
        synchronized (mStickyEventMap){
            Observable<T> observable = bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        emitter.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    public boolean hasObservable(){
        return bus.hasObservers();
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError, Action onComplete, Consumer onSubseribe){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext, onError, onComplete, onSubseribe);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError, Action onComplete){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext, onError, onComplete);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext, onError);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError, Action onComplete, Consumer onSubseribe){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete, onSubseribe);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError, Action onComplete){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    public <T> Disposable registerSticky(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext){
        return toObservableSticky(eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable registerSticky(Class<T> eventType, Consumer<T> onNext){
        return toObservableSticky(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> Disposable registerSticky(Class<T> eventType, Consumer<T> onNext, Consumer onError){
        return toObservableSticky(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeSticky(Class<T> eventType){
        synchronized (mStickyEventMap){
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents(){
        synchronized (mStickyEventMap){
            mStickyEventMap.clear();
        }
    }

    public void unregister(Disposable disposable){
        if (disposable != null && disposable.isDisposed()){
            disposable.dispose();
        }
    }

    private static class Holder{
        private static final RxBus BUS = new RxBus();
    }
}
