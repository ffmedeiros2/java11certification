/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.handleexceptions.data;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author oracle
 * @version 4.0
 */
public class ProductManager {
    private static final Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US), "fr-FR", new ResourceFormatter(Locale.FRANCE), "ru-RU",
            new ResourceFormatter(new Locale("ru", "RU")), "zh-CN", new ResourceFormatter(Locale.CHINA), "pt-BR",
            new ResourceFormatter(new Locale("pt", "BR")));
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private final Charset utf8Charset = Charset.forName("UTF-8");
    private final Map<Product, List<Review>> products = new HashMap<>();
    private final ResourceBundle config = ResourceBundle.getBundle(
            "com/javase11/certification/productmanagement/handleexceptions/data/config");
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private ResourceFormatter formatter;

    public ProductManager(final Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(final String laguageTag) {
        changeLocale(laguageTag);
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public void changeLocale(final String laguageTag) {
        formatter = formatters.getOrDefault(laguageTag, formatters.get("pt-BR"));
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating,
                                 final LocalDate bestBefore) {
        final Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) {
        final Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product findProduct(final int id) throws ProductManagerException {
        return products.keySet().stream().filter(p -> p.getId() == id).findFirst().orElseThrow(
                () -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public void printProductReport(final Product product) {
        final StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append("\n");

        final List<Review> reviews = products.get(product);
        if (reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews") + '\n');
        } else {
            txt.append(
                    reviews.stream().sorted().map(r -> formatter.formatReview(r) + '\n').collect(Collectors.joining()));
        }

        try {
            final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
            out.println(txt);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void printProductReport(final int id) {
        try {
            printProductReport(findProduct(id));
        } catch (final ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    public void printProducts(final Predicate<Product> filter, final Comparator<Product> sorter) {
        final StringBuilder txt = new StringBuilder();
        txt.append(products.keySet().stream().sorted(sorter).filter(filter).map(
                p -> formatter.formatProduct(p) + '\n').collect(Collectors.joining()));

        try {
            final PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
            out.println(txt);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void parseReview(final String text) {
        try {
            final Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt((String) values[0]), Rateable.convert(Integer.parseInt((String) values[1])),
                    (String) values[2]);
        } catch (final ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review: " + text);
        }
    }

    public void parseProduct(final String text) {
        try {
            final Object[] values = productFormat.parse(text);
            final int id = Integer.parseInt((String) values[1]);
            final String name = (String) values[2];
            final BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            final Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch ((String) values[0]) {
                case "D":
                    createProduct(id, name, price, rating);
                    break;
                case "F":
                    final LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    createProduct(id, name, price, rating, bestBefore);
            }
        } catch (final ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product: " + text + " " + e.getMessage());
        }
    }

    public Product reviewProduct(final Product product, final Rating rating, final String comments) {
        final List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        final Product newProduct = product.applyRating(Rateable.convert(
                (int) Math.round(reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))));
        products.put(newProduct, reviews);
        return newProduct;
    }

    public Product reviewProduct(final int id, final Rating rating, final String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (final ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public Map<String, String> getDiscounts() {
        return products.keySet().stream().collect(Collectors.groupingBy(p -> p.getRating().getStars(),
                Collectors.collectingAndThen(Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                        discount -> formatter.moneyFormat.format(discount))));
    }

    private static class ResourceFormatter {
        private final DateTimeFormatter dateFormat;
        private final NumberFormat moneyFormat;
        private final ResourceBundle resources;

        private ResourceFormatter(final Locale locale) {
            resources = ResourceBundle.getBundle(
                    "com/javase11/certification/productmanagement/handleexceptions/data/resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatReview(final Review review) {
            return MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
                    review.getComments());
        }

        private String formatProduct(final Product product) {
            return MessageFormat.format(resources.getString("product"), product.getName(),
                    moneyFormat.format(product.getPrice()), product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String getText(final String key) {
            return resources.getString(key);
        }
    }
}
