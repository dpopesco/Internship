package org.example.requests;

import org.example.exceptions.ConversionJsonToModelException;

public interface APIContract<T, K, M> {
    T getItem(String itemId) throws ConversionJsonToModelException;

    M getItemWithFailure(String itemId) throws ConversionJsonToModelException;

    K getItems() throws ConversionJsonToModelException;

    M getItemsWithFailure() throws ConversionJsonToModelException;

    T createItem(T item) throws ConversionJsonToModelException;

    M createItemWithFailure(T item) throws ConversionJsonToModelException;

    M createItemWithoutBody() throws ConversionJsonToModelException;

    T updateItem(String itemId, T updatedItem) throws ConversionJsonToModelException;

    M updateItemWithFailure(String itemId, T updatedItem) throws ConversionJsonToModelException;

    T deleteItem(String itemId) throws ConversionJsonToModelException;

    M deleteItemWithFailure(String itemId) throws ConversionJsonToModelException;
}
