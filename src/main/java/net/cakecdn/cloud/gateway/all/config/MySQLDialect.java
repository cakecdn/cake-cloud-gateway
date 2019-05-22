package net.cakecdn.cloud.gateway.all.config;

import org.hibernate.dialect.MySQL5Dialect;

public class MySQLDialect extends MySQL5Dialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    }

}