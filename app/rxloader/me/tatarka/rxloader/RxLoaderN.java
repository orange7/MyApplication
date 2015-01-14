package me.tatarka.rxloader;

import rx.Observable;
import rx.functions.FuncN;

public class RxLoaderN<T> extends BaseRxLoader<T> {
	private FuncN<Observable<T>> observableFunc;

	RxLoaderN(RxLoaderBackend manager, String tag,
			FuncN<Observable<T>> observableFunc, RxLoaderObserver<T> observer) {
		super(manager, tag, observer);
		this.observableFunc = observableFunc;
	}

	/**
	 * Starts the {@link rx.Observable} with the given argument.
	 * 
	 * @param arg1
	 *            the argument
	 * @return the {@code RxLoader1} for chaining
	 * @see RxLoader#start()
	 */
	public RxLoaderN<T> start(Object... args) {
		start(observableFunc.call(args));
		return this;
	}

	/**
	 * Restarts the {@link rx.Observable} with the given argument.
	 * 
	 * @param arg1
	 *            the argument
	 * @return the {@code RxLoader1} for chaining
	 * @see RxLoader#restart()
	 */
	public RxLoaderN<T> restart(Object... args) {
		restart(observableFunc.call(args));
		return this;
	}

	/**
	 * Saves the last value that the {@link rx.Observable} returns in
	 * {@link rx.Observer#onNext(Object)} in the Activities'ss ore Fragment's
	 * instanceState bundle. When the {@code Activity} or {@code Fragment} is
	 * recreated, then the value will be redelivered.
	 * 
	 * The value <b>must</b> implement {@link android.os.Parcelable}. If not,
	 * you should use {@link me.tatarka.rxloader.RxLoader#save(SaveCallback)} to
	 * save and restore the value yourself.
	 * 
	 * @return the {@code RxLoader1} for chaining
	 */
	public RxLoaderN<T> save() {
		super.save();
		return this;
	}

	/**
	 * Saves the last value that the {@link rx.Observable} returns in
	 * {@link rx.Observer#onNext(Object)} in the Activities's ore Fragment's
	 * instanceState bundle. When the {@code Activity} or {@code Fragment} is
	 * recreated, then the value will be redelivered.
	 * 
	 * @param saveCallback
	 *            the callback to handle saving and restoring the value
	 * @return the {@code RxLoader1} for chaining
	 */
	public RxLoaderN<T> save(SaveCallback<T> saveCallback) {
		super.save(saveCallback);
		return this;
	}
}
