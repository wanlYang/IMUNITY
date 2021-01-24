/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> DATASOURCEKEY = new InheritableThreadLocal<String>();

    public static void setDataSourceKey(String dataSource) {
        DATASOURCEKEY.set(dataSource);
    }

    protected Object determineCurrentLookupKey() {
        return DATASOURCEKEY.get();
    }

    public static void removeDataSourceKey() {
        DATASOURCEKEY.remove();
    }
}
