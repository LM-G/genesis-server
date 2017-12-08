package com.solofeed.genesis.core.scheduler;

import com.solofeed.genesis.core.event.UpdateEvent;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Genesis main scheduler
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class MainScheduler {
    private final static long TICK_RATE = 100L;
    private BehaviorSubject<Integer> counterSubject = BehaviorSubject.createDefault(0);
    private final ApplicationEventPublisher publisher;

    /**
     * 10Hz main loop, refreshing app state 10 times per second
     */
    @Scheduled(fixedRate = TICK_RATE)
    public void update() {
        counterSubject.onNext(counterSubject.getValue() + 1);
    }


    @PostConstruct
    public void process() {
        counterSubject.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                LOGGER.debug("Main observer onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                publisher.publishEvent(UpdateEvent.of(Long.valueOf(value)));
            }

            @Override
            public void onError(Throwable e) {
                LOGGER.debug("Main observer onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                LOGGER.debug("Main observer onComplete");
            }
        });
    }
}
