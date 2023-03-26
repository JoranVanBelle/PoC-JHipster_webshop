package com.webshop.soa.shop.repository.rowmapper;

import com.webshop.soa.shop.domain.Pricing;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Pricing}, with proper type conversions.
 */
@Service
public class PricingRowMapper implements BiFunction<Row, String, Pricing> {

    private final ColumnConverter converter;

    public PricingRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Pricing} stored in the database.
     */
    @Override
    public Pricing apply(Row row, String prefix) {
        Pricing entity = new Pricing();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPricingID(converter.fromRow(row, prefix + "_pricing_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        return entity;
    }
}
