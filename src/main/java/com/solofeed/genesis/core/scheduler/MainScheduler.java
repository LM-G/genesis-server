package com.solofeed.genesis.core.scheduler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Genesis main scheduler
 */
@Log4j2
@Component
public class MainScheduler {
    private final static long TICK_RATE = 50L;
    private BehaviorSubject<Integer> counter$ = BehaviorSubject.createDefault(0);

    /**
     * 20Hz main loop, refreshing app state 20 times per second
     */
    @Scheduled(fixedRate = TICK_RATE)
    public void update() {
        counter$.onNext(counter$.getValue() + 1);
    }


    @PostConstruct
    public void process() {
        counter$.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                LOGGER.debug("Main observer onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                if(value % 100 == 0) {
                    LOGGER.debug(Thread.currentThread().getName()+"- Main: " + value/100 );
                }
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
