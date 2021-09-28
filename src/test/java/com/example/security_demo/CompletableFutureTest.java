package com.example.security_demo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class CompletableFutureTest {

    public static void main(String[] args) {

        Supplier<Integer> supplier1 = new Supplier<Integer>() {

            @Override
            public Integer get() {
                long val = (long) (Math.random() * 10000);
                System.out.println("wait " + val);
                try {
                    Thread.sleep(val);
                    if (val / 1000 < 5) {
                        throw new CompletionException(new Exception("ex " + val));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("return " + val);
                return (int)val;
            }
        };
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(supplier1);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(supplier1);
        CompletableFuture<Integer> completableFuture3 = CompletableFuture.supplyAsync(supplier1);
        CompletableFuture<Void> completableFutureAll = CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3);

        try {
            Void join = completableFutureAll.join();
        } catch (Exception e) {
            System.out.println("join Ex");
            e.printStackTrace();
            return;
        }

        try {
            Integer val1 = completableFuture1.get();
            Integer val2 = completableFuture2.get();
            Integer val3 = completableFuture3.get();
            System.out.println("val1 " + val1);
            System.out.println("val2 " + val2);
            System.out.println("val2 " + val3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
