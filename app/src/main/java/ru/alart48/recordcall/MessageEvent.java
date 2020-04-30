package ru.alart48.recordcall;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MessageEvent {

    private PublishSubject<MessageEvent> changeObservable = PublishSubject.create();
    private String message;

    public void setMessage(String message) {
        this.message = message;
        changeObservable.onNext(this);
    }

    public String getMessage() {
        return message;
    }


    public Observable<MessageEvent> getModelChanges() {
        return changeObservable;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "message='" + message + '\'' +
                '}';
    }


}
