package com.learning.seckill.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

/**
 *
 */
public class MessageProducer<T extends EventFactory, K extends EventHandler> {

    public static <T extends EventFactory, K extends EventHandler> void publish(T event, K eventHandler, Consumer<RingBuffer> consumer) {
        Disruptor<T> disruptor = getDisruptor(event, new AtomicInteger(0));

        disruptor.handleEventsWith(eventHandler);

        RingBuffer<T> ringBuffer = disruptor.start();

        consumer.accept(ringBuffer);

        disruptor.shutdown();
    }

    public static <T extends EventFactory, K extends EventHandler> void publish(T event, List<K> eventHandler, Consumer<RingBuffer> consumer) {
        Disruptor<T> disruptor = getDisruptor(event, new AtomicInteger(0));

        disruptor.handleEventsWith(eventHandler.toArray(new EventHandler[0]));

        RingBuffer<T> ringBuffer = disruptor.start();

        consumer.accept(ringBuffer);

        disruptor.shutdown();
    }

    private static <T extends EventFactory> Disruptor<T> getDisruptor(T event, AtomicInteger atomic){
        return new Disruptor<>(event, 512, runnable -> {
            return new Thread(runnable, "message-producer-thread-" + atomic.decrementAndGet());
        });
    }
}
