/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.modules.file.service;

import com.javase11.certification.productmanagement.modules.data.*;
import com.javase11.certification.productmanagement.modules.service.ProductManager;
import com.javase11.certification.productmanagement.modules.service.ProductManagerException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author oracle
 * @version 4.0
 */
public class ProductFileManager implements ProductManager {
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private static final Charset charset = Charset.forName("UTF-8");
    private final ResourceBundle config = ResourceBundle.getBundle(
            "com/javase11/certification/productmanagement/modules/file/service/config");
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private final Path dataFolder = Path.of(System.getProperty("user.home"), config.getString("data.folder"));
    private Map<Product, List<Review>> products = new HashMap<>();

    public ProductFileManager() {
        loadAllData();
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
                    Files.lines(dataFolder.resolve(file), charset).findFirst().orElseThrow());
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

    @Override
    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating,
                                 final LocalDate bestBefore) throws ProductManagerException {
        final Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    @Override
    public Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) throws ProductManagerException {
        final Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    @Override
    public Product findProduct(final int id) throws ProductManagerException {
        return products.keySet().stream().filter(p -> p.getId() == id).findFirst().orElseThrow(
                () -> new ProductManagerException("Product with id " + id + " not found"));
    }

    @Override
    public List<Product> findProducts(final Predicate<Product> filter) throws ProductManagerException {
        return products.keySet().stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public List<Review> findReviews(final int id) throws ProductManagerException {
        return products.get(findProduct(id));
    }

    @Override
    public Map<Rating, BigDecimal> getDiscounts() throws ProductManagerException {
        return products.keySet().stream().collect(Collectors.groupingBy(p -> p.getRating(),
                Collectors.collectingAndThen(
                        Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                        discount -> BigDecimal.valueOf(discount))));
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

    private Product reviewProduct(final Product product, final Rating rating, final String comments) {
        final List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        final Product newProduct = product.applyRating(Rateable.convert(
                (int) Math.round(reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))));
        products.put(newProduct, reviews);
        return newProduct;
    }

    @Override
    public Product reviewProduct(final int id, final Rating rating, final String comments) throws ProductManagerException {
        return reviewProduct(findProduct(id), rating, comments);
    }

}
