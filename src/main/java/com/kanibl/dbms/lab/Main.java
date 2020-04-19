package com.kanibl.dbms.lab;


public class Main {

    public static void main(String[] args) throws Exception {
        Application app = new Application();

        System.out.println("Print all users #1");
        long time = System.currentTimeMillis();
        System.out.println(app.getUserAllAsMap());
        System.out.printf("Execution time %d ms\n", System.currentTimeMillis() - time);
        System.out.println("========\n");

        System.out.println("Print all users #2");
        time = System.currentTimeMillis();
        System.out.println(app.getUserAllAsMap());
        System.out.printf("Execution time %d ms\n", System.currentTimeMillis() - time);
        System.out.println("========\n");

        System.out.println("Print one user #1");
        time = System.currentTimeMillis();
        System.out.println(app.getUserById(1));
        System.out.printf("Execution time %d ms\n", System.currentTimeMillis() - time);
        System.out.println("========\n");

        System.out.println("Print one user #2");
        time = System.currentTimeMillis();
        System.out.println(app.getUserById(1));
        System.out.printf("Execution time %d ms\n", System.currentTimeMillis() - time);
        System.out.println("========\n");

        app.closeConnection();
    }

}
