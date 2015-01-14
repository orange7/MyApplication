package me.tatarka.rxloader;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

abstract class BaseRxLoader<T> {
	private RxLoaderBackend manager;
	private String tag;
	private RxLoaderObserver<T> observer;
	private Scheduler scheduler;
	private SaveCallback<T> saveCallback;

	BaseRxLoader(RxLoaderBackend manager, String tag,
			RxLoaderObserver<T> observer) {
		scheduler = AndroidSchedulers.mainThread();
		this.manager = manager;
		this.tag = tag;
		this.observer = observer;

		CachingWeakRefSubscriber<T> subscription = manager.get(tag);
		if (subscription != null) {
			subscription.set(observer);
		}
	}

	protected BaseRxLoader<T> start(Observable<T> observable) {
		CachingWeakRefSubscriber<T> subscriber = manager.get(tag);
		if (subscriber == null) {
			manager.put(tag, createSubscriber(observable));
		}
		return this;
	}

	protected BaseRxLoader<T> restart(Observable<T> observable) {
		CachingWeakRefSubscriber<T> subscriber = manager.get(tag);
		if (subscriber != null) {
			subscriber.unsubscribe();
		}
		manager.put(tag, createSubscriber(observable));
		if (saveCallback != null) {
			manager.setSave(tag, observer, new WeakReference<SaveCallback<T>>(
					saveCallback));
		}
		return this;
	}

	protected BaseRxLoader<T> save(SaveCallback<T> saveCallback) {
		this.saveCallback = saveCallback;
		manager.setSave(tag, observer, new WeakReference<SaveCallback<T>>(
				saveCallback));
		return this;
	}

	protected BaseRxLoader<T> save() {
		return save(new ParcelableSaveCallback<T>());
	}

	private CachingWeakRefSubscriber<T> createSubscriber(
			Observable<T> observable) {
		CachingWeakRefSubscriber<T> subscriber = new CachingWeakRefSubscriber<T>(
				observer);
		observer.onStarted();
		subscriber.setSubscription(observable.observeOn(scheduler).subscribe(
				subscriber));
		return subscriber;
	}

	/**
	 * Cancels the task.
	 * 
	 * @return true if the task was started, false otherwise
	 */
	public boolean unsubscribe() {
		CachingWeakRefSubscriber<T> subscriber = manager.get(tag);
		if (subscriber != null) {
			subscriber.unsubscribe();
			return true;
		}
		return false;
	}
}
