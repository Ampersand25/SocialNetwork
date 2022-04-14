package main.utils;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static String databaseURL = "jdbc:postgresql://localhost:5432/socialnetwork";
    public static String databaseUsername = "postgres";
    public static String databasePassword = "postgres";
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
