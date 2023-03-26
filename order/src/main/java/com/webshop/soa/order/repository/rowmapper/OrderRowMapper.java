package com.webshop.soa.order.repository.rowmapper;

import com.webshop.soa.order.domain.Order;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Order}, with proper type conversions.
 */
@Service
public class OrderRowMapper implements BiFunction<Row, String, Order> {

    private final ColumnConverter converter;

    public OrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Order} stored in the database.
     */
    @Override
    public Order apply(Row row, String prefix) {
        Order entity = new Order();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrderID(converter.fromRow(row, prefix + "_order_id", Long.class));
        entity.setOrderPrice(converter.fromRow(row, prefix + "_order_price", Double.class));
        entity.setOrderQuantity(converter.fromRow(row, prefix + "_order_quantity", Long.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setTransportID(converter.fromRow(row, prefix + "_transport_id", Long.class));
        return entity;
    }
}
