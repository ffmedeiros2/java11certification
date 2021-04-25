package com.javase11.certification.productmanagement.modules.service;

import com.javase11.certification.productmanagement.modules.data.Product;
import com.javase11.certification.productmanagement.modules.data.Rating;
import com.javase11.certification.productmanagement.modules.data.Review;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface ProductManager {
    Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating) throws ProductManagerException;
    Product createProduct(final int id, final String name, final BigDecimal price, final Rating rating, final LocalDate bestBefore) throws ProductManagerException;
    Product reviewProduct(final int id, final Rating rating, final String comments) throws ProductManagerException;
    Product findProduct(final int id) throws ProductManagerException;
    List<Product> findProducts(Predicate<Product> filter) throws ProductManagerException;
    List<Review> findReviews(int id) throws ProductManagerException;
    Map<Rating, BigDecimal> getDiscounts() throws ProductManagerException;
}
