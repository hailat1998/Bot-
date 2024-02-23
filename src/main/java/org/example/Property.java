package org.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    private Properties properties = new Properties();

    public Property() throws FileNotFoundException, IOException {
        try (InputStream inputStream = new FileInputStream("config.properties")) {
            properties.load(inputStream);
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }
}