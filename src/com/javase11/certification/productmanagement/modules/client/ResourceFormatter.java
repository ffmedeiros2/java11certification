package com.javase11.certification.productmanagement.modules.client;

import com.javase11.certification.productmanagement.modules.data.Product;
import com.javase11.certification.productmanagement.modules.data.Review;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceFormatter {
    private static final Map<String, ResourceFormatter> formatters = Map.of("en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US), "fr-FR", new ResourceFormatter(Locale.FRANCE), "ru-RU",
            new ResourceFormatter(new Locale("ru", "RU")), "zh-CN", new ResourceFormatter(Locale.CHINA), "pt-BR",
            new ResourceFormatter(new Locale("pt", "BR")));
    private final DateTimeFormatter dateFormat;
    private final NumberFormat moneyFormat;
    private final ResourceBundle resources;

    private ResourceFormatter(final Locale locale) {
        resources = ResourceBundle.getBundle("com/javase11/certification/productmanagement/modules/client/resources",
                locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public static ResourceFormatter getResourceFormatter(final String languageTag) {
        return formatters.getOrDefault(languageTag, formatters.get("pt-BR"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public String formatReview(final Review review) {
        return MessageFormat.format(resources.getString("review"), review.getRating().getStars(),
                review.getComments());
    }

    public String formatProduct(final Product product) {
        return MessageFormat.format(resources.getString("product"), product.getName(),
                moneyFormat.format(product.getPrice()), product.getRating().getStars(),
                dateFormat.format(product.getBestBefore()));
    }

    public String formatProductReport(final Product product, final List<Review> reviews) {
        final StringBuilder txt = new StringBuilder();
        txt.append(formatProduct(product));
        txt.append("\n");

        if (reviews.isEmpty()) {
            txt.append(getText("no.reviews") + '\n');
        } else {
            txt.append(
                    reviews.stream().sorted().map(r -> formatReview(r) + '\n').collect(Collectors.joining()));
        }
        return txt.toString();
    }

    public String formatData(final String messageKey, final Object obj) {
        return System.getProperty("user.home")
                     .replace("\\", "/") + MessageFormat.format(resources.getString(messageKey), obj);
    }

    private String getText(final String key) {
        return resources.getString(key);
    }
}
