package com.mynotes.spring.reactor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

//https://bsideup.github.io/posts/daily_reactive/thread_locals/
public class ThreadLocalReactive {

    static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    // Fix hook in reactor
    static {
        Function<Runnable, Runnable> decorator = task -> {
            // Capture
            String userId = USER_ID.get();

            return () -> {
                String previous = USER_ID.get();
                // Restore
                USER_ID.set(userId);
                try {
                    // Call the original task
                    task.run();
                }
                finally {
                    // Cleanup
                    USER_ID.set(previous);
                }
            };
        };
        Schedulers.onScheduleHook("my-hook", decorator);
    }


    @Test
    public void testThreadLocals_reactor() {
        USER_ID.set("bsideup");

        Mono.just("Hello %s")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(greeting -> {
                    // WIll print "Hello null". Bummer!
                    System.out.println(String.format(greeting, USER_ID.get()));
                })
                .block();
    }

    @Test
    public void reactor_context() {
        Mono.just("Hello %s")
                .delayElement(Duration.ofSeconds(1))
                .transform(flux -> Mono.deferWithContext(ctx -> {
                    return flux.doOnNext(greeting -> {
                        // Get it from the Context
                        String userId = ctx.get("userId");
                        System.out.println(String.format(greeting, userId));
                    });
                }))
                .subscriberContext(Context.of("userId", "bsideup"))
                .block();
    }

}
