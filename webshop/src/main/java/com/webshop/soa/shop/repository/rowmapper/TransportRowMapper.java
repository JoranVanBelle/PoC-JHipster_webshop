package com.webshop.soa.shop.repository.rowmapper;

import com.webshop.soa.shop.domain.Transport;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Transport}, with proper type conversions.
 */
@Service
public class TransportRowMapper implements BiFunction<Row, String, Transport> {

    private final ColumnConverter converter;

    public TransportRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Transport} stored in the database.
     */
    @Override
    public Transport apply(Row row, String prefix) {
        Transport entity = new Transport();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTransportID(converter.fromRow(row, prefix + "_transport_id", Long.class));
        entity.setTransportName(converter.fromRow(row, prefix + "_transport_name", String.class));
        return entity;
    }
}
