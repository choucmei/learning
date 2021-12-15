package chouc.java.thread.completable;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.function.*;

public class CompletableFutureTest {

    final ExecutorService executorService =
            Executors.newFixedThreadPool(
                    4);


    public static String executeError() {
        return "".substring(0, 1);
    }

    public static String executeA() {
        return executeWithReturn("A", 2);
    }

    public static String executeB() {
        return executeWithReturn("B", 4);
    }


    public static String executeWithReturn(String jobName, Integer sleep) {
        System.out.println("execute " + jobName + " start");
        System.out.println("executeWithReturn " + jobName + " thread:" + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("execute " + jobName + " end");
        return jobName + " result";
    }


    @Test
    public void supplyAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(() -> {
            System.out.println("execute A");
            return "A result";
        });

        try {
            System.out.println(executeA.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("execute B");
                return "B result";
            }
        });


        CompletableFuture<String> executeC = CompletableFuture.supplyAsync(CompletableFutureTest::executeA);

        try {
            System.out.println(executeA.get());
            System.out.println(executeB.get());
            System.out.println(executeC.get());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void supplyAsyncTestWithThreadPool() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);
        try {
            System.out.println(executeA.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncTest() {
        CompletableFuture<Void> executeA = CompletableFuture.runAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<Void> executeB = CompletableFuture.runAsync(CompletableFutureTest::executeB);

        try {
            System.out.println(executeA.get());
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void runAsyncWithThenApplyTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.thenApply((a) -> {
            System.out.println("execute B");
            System.out.println("thenApply:" + Thread.currentThread().getName());
            System.out.println("parameter a :" + a);
            return a + " thenApply";
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void runAsyncWiththenApplyAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.thenApplyAsync((a) -> {
            System.out.println("execute B");
            System.out.println("thenApplyAsync:" + Thread.currentThread().getName());
            System.out.println("parameter a :" + a);
            return a + " thenApplyAsync";
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void runAsyncWiththenAcceptTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<Void> executeB = executeA.thenAccept((a) -> {
            System.out.println("execute B");
            System.out.println("thenAccept:" + Thread.currentThread().getName());
            System.out.println("parameter a :");
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void runAsyncWiththenAcceptAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<Void> executeB = executeA.thenAcceptAsync((a) -> {
            System.out.println("execute B");
            System.out.println("thenAcceptAsync:" + Thread.currentThread().getName());
            System.out.println("parameter a :");
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenRunTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<Void> executeB = executeA.thenRun(() -> {
            System.out.println("execute B");
            System.out.println("thenRun:" + Thread.currentThread().getName());
            System.out.println("parameter a :");
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenRunAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<Void> executeB = executeA.thenRunAsync(() -> {
            System.out.println("execute B");
            System.out.println("thenRunAsync:" + Thread.currentThread().getName());
            System.out.println("parameter a :");
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("main method:" + Thread.currentThread().getName());
            System.out.println("watingB");
            System.out.println(executeB.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenCombineTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<String> executeC = executeA.thenCombine(executeB, new BiFunction<String, String, String>() {
            @Override
            public String apply(String a, String b) {
                System.out.println("thread thenCombine" + Thread.currentThread().getName());
                return a + " - " + b;
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main" + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runAsyncWiththenCombineAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<String> executeC = executeA.thenCombineAsync(executeB, new BiFunction<String, String, String>() {
            @Override
            public String apply(String a, String b) {
                System.out.println("thread thenCombineAsync" + Thread.currentThread().getName());
                return a + " - " + b;
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main" + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenAcceptBothTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.thenAcceptBoth(executeB, new BiConsumer<String, String>() {
            @Override
            public void accept(String a, String b) {
                System.out.println("a:" + a + " b：" + b);
                System.out.println("thread thenCombineAsync" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main" + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenAcceptBothAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.thenAcceptBothAsync(executeB, new BiConsumer<String, String>() {
            @Override
            public void accept(String a, String b) {
                System.out.println("a:" + a + " b：" + b);
                System.out.println("thread thenCombineAsync" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main" + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithrunAfterBothTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.runAfterBoth(executeB, new Runnable() {
            @Override
            public void run() {
                System.out.println("thread thenCombineAsync " + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runAsyncWithrunAfterBothAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.runAfterBothAsync(executeB, new Runnable() {
            @Override
            public void run() {
                System.out.println("thread thenCombineAsync " + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithapplyToEitherTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<String> executeC = executeA.applyToEither(executeB, new Function<String, String>() {
            @Override
            public String apply(String s) {
                System.out.println("execute C");
                System.out.println(" s :" + s);
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
                return s + " execute C";
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithapplyToEitherAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<String> executeC = executeA.applyToEitherAsync(executeB, new Function<String, String>() {
            @Override
            public String apply(String s) {
                System.out.println("execute C");
                System.out.println(" s :" + s);
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
                return s + " execute C";
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithacceptEitherTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.acceptEither(executeB, new Consumer<String>() {

            @Override
            public void accept(String s) {
                System.out.println("execute C");
                System.out.println(" s :" + s);
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithacceptEitherAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.acceptEitherAsync(executeB, new Consumer<String>() {

            @Override
            public void accept(String s) {
                System.out.println("execute C");
                System.out.println(" s :" + s);
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithrunAfterEitherTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.runAfterEither(executeB, new Runnable() {
            @Override
            public void run() {
                System.out.println("execute C");
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithrunAfterEitherAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = CompletableFuture.supplyAsync(CompletableFutureTest::executeB, executorService);

        CompletableFuture<Void> executeC = executeA.runAfterEitherAsync(executeB, new Runnable() {
            @Override
            public void run() {
                System.out.println("execute C");
                System.out.println("thread applyToEither" + Thread.currentThread().getName());
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());
            System.out.println("watingC");
            System.out.println(executeC.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenComposeTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.thenCompose(new Function<String, CompletionStage<String>>() {
            @Override
            public CompletionStage<String> apply(String s) {
                System.out.println("execute B s:" + s);
                System.out.println("thread thenCompose " + Thread.currentThread().getName());
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("thread thenCompose + supplyAsync " + Thread.currentThread().getName());
                    return s + " return B";
                });
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWiththenComposeAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.thenComposeAsync(new Function<String, CompletionStage<String>>() {
            @Override
            public CompletionStage<String> apply(String s) {
                System.out.println("execute B s:" + s);
                System.out.println("thread thenCompose " + Thread.currentThread().getName());
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("thread thenCompose + supplyAsync " + Thread.currentThread().getName());
                    return s + " return B";
                });
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void runAsyncWithwhenCompleteTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);

                }
            }
        });


        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("---- split ------");


        CompletableFuture<String> executeAA = CompletableFuture.supplyAsync(CompletableFutureTest::executeError, executorService);

        CompletableFuture<String> executeAB = executeAA.whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());
                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeAA.get());
            System.out.println("watingB");
            System.out.println(executeAB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void runAsyncWithwhenCompleteAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
            }
        });


        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("---- split ------");


        CompletableFuture<String> executeAA = CompletableFuture.supplyAsync(CompletableFutureTest::executeError, executorService);

        CompletableFuture<String> executeAB = executeAA.whenCompleteAsync(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());
                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
            }
        });

        try {
            System.out.println("watingA");
            System.out.println(executeAA.get());
            System.out.println("watingB");
            System.out.println(executeAB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void runAsyncWithhandleTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.handle(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
                return s + " - " + " Execute B";
            }
        });


        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("---- split ------");


        CompletableFuture<String> executeAA = CompletableFuture.supplyAsync(CompletableFutureTest::executeError, executorService);

        CompletableFuture<String> executeAB = executeAA.handle(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
                return s + " - " + " Execute BB";
            }
        });

        try {
            System.out.println("watingA");
//            System.out.println(executeAA.get());
            System.out.println("watingB");
            System.out.println(executeAB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void runAsyncWithhandleAsyncTest() {
        CompletableFuture<String> executeA = CompletableFuture.supplyAsync(CompletableFutureTest::executeA, executorService);

        CompletableFuture<String> executeB = executeA.handleAsync(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
                return s + " - " + " Execute B";
            }
        });


        try {
            System.out.println("watingA");
            System.out.println(executeA.get());
            System.out.println("watingB");
            System.out.println(executeB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("---- split ------");


        CompletableFuture<String> executeAA = CompletableFuture.supplyAsync(CompletableFutureTest::executeError, executorService);

        CompletableFuture<String> executeAB = executeAA.handleAsync(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String s, Throwable throwable) {
                System.out.println("thread whenComplete " + Thread.currentThread().getName());

                if (throwable != null) {
                    System.out.println(" execute ERROR:" + throwable.getMessage());
                } else {
                    System.out.println(" execte SUCCESS Result:" + s);
                }
                return s + " - " + " Execute BB";
            }
        });

        try {
            System.out.println("watingA");
//            System.out.println(executeAA.get());
            System.out.println("watingB");
            System.out.println(executeAB.get());
            System.out.println("thread main " + Thread.currentThread().getName());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


}
