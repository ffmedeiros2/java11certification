/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.concurrencymultithreading.app;

import com.javase11.certification.productmanagement.concurrencymultithreading.data.Product;
import com.javase11.certification.productmanagement.concurrencymultithreading.data.ProductManager;
import com.javase11.certification.productmanagement.concurrencymultithreading.data.Rating;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code Shop} class represents an application that manages Products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {
    public static void main(final String[] args) {
        final Charset utf8Charset = Charset.forName("UTF-8");
        final ProductManager pm = ProductManager.getInstance();
        final AtomicInteger clientCount = new AtomicInteger(0);

        final Callable<String> client = () -> {
            final String clientId = "Client " + clientCount.incrementAndGet();
            final String threadName = Thread.currentThread().getName();
            final int productId = ThreadLocalRandom.current().nextInt(6) + 101;
            final String languageTag = ProductManager.getSupportedLocales().stream().skip(
                    ThreadLocalRandom.current().nextInt(6)).findFirst().get();
            final StringBuilder log = new StringBuilder();
            log.append(clientId + " " + threadName + "\n-\tstart of log\t-\n");
            log.append(pm.getDiscounts(languageTag).entrySet().stream().map(
                    entry -> entry.getKey() + "\t" + entry.getValue()).collect(Collectors.joining("\n")));
            final Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
            log.append((product != null) ? "\nProduct: " + productId + " reviewed\n"
                    : "\nProduct: " + productId + " not reviewed\n");
            pm.printProductReport(productId, languageTag, clientId);
            log.append(clientId + " generated report for " + productId + " product");
            log.append("\n-\tend of log\t-\n");
            return log.toString();
        };
        final List<Callable<String>> clients = Stream.generate(() -> client).limit(5).collect(Collectors.toList());
        final ExecutorService executorService = Executors.newFixedThreadPool(3);

        try {
            final List<Future<String>> results = executorService.invokeAll(clients);
            executorService.shutdown();
            results.stream().forEach(result -> {
                try {
                    final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
                    System.out.println(result.get());
                } catch (final InterruptedException | ExecutionException | UnsupportedEncodingException e) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error retrieving client log " + e);
                }
            });
        } catch (final InterruptedException e) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients " + e);
        }
    }
}
