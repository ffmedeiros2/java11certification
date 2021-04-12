/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.streamapi.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author oracle
 * @version 4.0
 */
public class ProductManager {
	private static final Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
			"en-US", new ResourceFormatter(Locale.US), "fr-FR", new ResourceFormatter(Locale.FRANCE), "ru-RU",
			new ResourceFormatter(new Locale("ru", "RU")), "zh-CN", new ResourceFormatter(Locale.CHINA), "pt-BR",
			new ResourceFormatter(new Locale("pt", "BR")));
	private final Map<Product, List<Review>> products = new HashMap<>();

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

	public Product findProduct(final int id) {
		return products.keySet().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
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

		System.out.println(txt);
	}

	public void printProductReport(final int id) {
		printProductReport(findProduct(id));
	}

	public void printProducts(final Predicate<Product> filter, final Comparator<Product> sorter) {
		final StringBuilder txt = new StringBuilder();
		txt.append(products.keySet().stream().sorted(sorter).filter(filter).map(
				p -> formatter.formatProduct(p) + '\n').collect(Collectors.joining()));
		System.out.println(txt);
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
		return reviewProduct(findProduct(id), rating, comments);
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
					"com/javase11/certification/productmanagement/streamapi/data/resources", locale);
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
