package eventbus.example.com.rxrelay;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RxBus {

    private Relay<Object> bus = null;
    private static RxBus instance;

    private RxBus(){
        bus = PublishRelay.create().toSerialized();
    }

    public static RxBus get(){
        return Holder.BUS;
    }

    public void post(Object event) {
        bus.accept(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType){
        return bus.ofType(eventType);
    }

    public boolean hasObservable(){
        return bus.hasObservers();
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError, Action onComplete, Consumer onSubscribe){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext,onError,onComplete,onSubscribe);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError, Action onComplete){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext,onError,onComplete);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext, Consumer onError){
        return toObservable(eventType).observeOn(scheduler).subscribe(onNext,onError);
    }

    public <T> Disposable register(Class<T> eventType,  Consumer<T> onNext){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError, Action onComplete, Consumer onSubscribe){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,onError,onComplete,onSubscribe);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError, Action onComplete){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,onError,onComplete);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer onError){
        return toObservable(eventType).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext,onError);
    }

    public void unregister(Disposable disposable){
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }
}
