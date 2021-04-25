/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.javaio.data;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
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
    final Charset utf8Charset = Charset.forName("UTF-8");
    private final ResourceBundle config = ResourceBundle.getBundle(
            "com/javase11/certification/productmanagement/javaio/data/config");
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private final Path reportsFolder = Path.of(System.getProperty("user.home"), config.getString("reports.folder"));
    private final Path dataFolder = Path.of(System.getProperty("user.home"), config.getString("data.folder"));
    private final Path tempFolder = Path.of(System.getProperty("user.home"), config.getString("temp.folder"));
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;

    public ProductManager(final Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(final String laguageTag) {
        changeLocale(laguageTag);
        loadAllData();
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    private void dumpData() {
        try {
            if (Files.notExists(tempFolder)) {
                Files.createDirectory(tempFolder);
            }
            final Path tempFile = tempFolder.resolve(
                    MessageFormat.format(config.getString("temp.file"), Instant.now().toEpochMilli()));
            try (final ObjectOutputStream out = new ObjectOutputStream(
                    Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                out.writeObject(products);
                products = new HashMap<>();
            }
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Error dumping data " + e.getMessage(), e);
        }
    }

    private void restoreData() {
        try {
            final Path tempFile = Files.list(tempFolder).filter(
                    path -> path.getFileName().toString().endsWith("tmp")).sorted(
                    Comparator.reverseOrder()).findFirst().orElseThrow();
            try (final ObjectInputStream in = new ObjectInputStream(
                    Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                products = (HashMap) in.readObject();
            }
        } catch (final Exception e) {
            logger.log(Level.SEVERE, "Error restoring data " + e.getMessage(), e);
        }
    }

    private void loadAllData() {
        try {
            products = Files.list(dataFolder).filter(file -> file.getFileName().toString().startsWith("product")).map(
                    file -> loadProduct(file)).filter(product -> product != null).collect(
                    Collectors.toMap(product -> product, product -> loadReviews(product)));
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Error loading data " + e.getMessage(), e);
        }
    }

    private Product loadProduct(final Path file) {
        Product product = null;
        try {
            product = parseProduct(
                    Files.lines(dataFolder.resolve(file), Charset.forName("UTF-8")).findFirst().orElseThrow());
        } catch (final Exception e) {
            logger.log(Level.WARNING, "Error loading product " + e.getMessage());
        }
        return product;
    }

    private List<Review> loadReviews(final Product product) {
        List<Review> reviews = null;
        final Path file = dataFolder.resolve(
                MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
        if (Files.notExists(file)) {
            reviews = new ArrayList<>();
        } else {
            try {
                reviews = Files.lines(file, Charset.forName("UTF-8")).map(text -> parseReview(text)).filter(
                        review -> review != null).collect(Collectors.toList());
            } catch (final IOException e) {
                logger.log(Level.WARNING, "Error loading reviews " + e.getMessage());
            }
        }
        return reviews;
    }

    public void changeLocale(final String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("pt-BR"));
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

    public void printProductReport(final Product product) throws IOException {
        final Path productFile = reportsFolder.resolve(
                MessageFormat.format(config.getString("report.file"), product.getId()));

        try (final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {
            out.append(formatter.formatProduct(product) + System.lineSeparator());
            final List<Review> reviews = products.get(product);
            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews") + System.lineSeparator());
            } else {
                out.append(
                        reviews.stream().sorted().map(r -> formatter.formatReview(r) + System.lineSeparator()).collect(
                                Collectors.joining()));
            }
        }
    }

    public void printProductReport(final int id) {
        try {
            printProductReport(findProduct(id));
        } catch (final ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (final IOException e) {
            logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
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

    private Review parseReview(final String text) {
        Review review = null;
        try {
            final Object[] values = reviewFormat.parse(text);
            review = new Review(Rateable.convert(Integer.parseInt((String) values[0])), (String) values[1]);
        } catch (final ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review: " + text);
        }
        return review;
    }

    private Product parseProduct(final String text) {
        Product product = null;
        try {
            final Object[] values = productFormat.parse(text);
            final int id = Integer.parseInt((String) values[1]);
            final String name = (String) values[2];
            final BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            final Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch ((String) values[0]) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    final LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
            }
        } catch (final ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product: " + text + " " + e.getMessage());
        }
        return product;
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
            resources = ResourceBundle.getBundle("com/javase11/certification/productmanagement/javaio/data/resources",
                    locale);
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
