package com.solofeed.genesis.echo.service;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Log4j2
@Service
@RequiredArgsConstructor
public class CounterService {
    private final SimpMessagingTemplate broker;
    private BehaviorSubject<Integer> counter$ = BehaviorSubject.createDefault(0);


    @Scheduled(fixedRate = 1000)
    public void increment() {
        counter$.onNext(counter$.getValue() + 1);
    }

    @PostConstruct
    public void process() {
        counter$.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                LOGGER.debug("Counter observer onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                LOGGER.debug(Thread.currentThread().getName() +"- Counter : " + value);
                broker.convertAndSend("/channel/public", value);
            }

            @Override
            public void onError(Throwable e) {
                LOGGER.debug("Counter observer onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                LOGGER.debug("Counter observer onComplete");
            }
        });
    }
}
