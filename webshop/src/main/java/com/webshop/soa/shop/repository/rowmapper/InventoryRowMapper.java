package com.webshop.soa.shop.repository.rowmapper;

import com.webshop.soa.shop.domain.Inventory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Inventory}, with proper type conversions.
 */
@Service
public class InventoryRowMapper implements BiFunction<Row, String, Inventory> {

    private final ColumnConverter converter;

    public InventoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Inventory} stored in the database.
     */
    @Override
    public Inventory apply(Row row, String prefix) {
        Inventory entity = new Inventory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setInventoryID(converter.fromRow(row, prefix + "_inventory_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Long.class));
        return entity;
    }
}
