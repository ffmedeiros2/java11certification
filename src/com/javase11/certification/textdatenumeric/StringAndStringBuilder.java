package com.javase11.certification.textdatenumeric;

public class StringAndStringBuilder {

    public static void main(String[] args) {
        // Strings
        String teaTxt = "Tea";
        String b = "Tea";
        String c = new String("Tea");
        System.out.println(teaTxt == b);
        System.out.println(teaTxt == c);
        System.out.println(teaTxt == c.intern());
        c = teaTxt + ' ' + b;
        System.out.println(c.toLowerCase());
        System.out.println(c);
        c = c.toUpperCase();
        System.out.println(c);
        System.out.println(c.length());


        StringBuilder txt = new StringBuilder(c);
        System.out.println(txt);
        System.out.println(txt.length());
        System.out.println(txt.capacity());
        txt.replace(0, 3, "What is the price of");
        System.out.println(txt);
        System.out.println(txt.length());
        System.out.println(txt.capacity());
    }

}
