/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * Copyright 2018 Mozilla
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. */

package org.mozilla.mentat;

import com.sun.jna.Pointer;

import java.util.Date;
import java.util.UUID;

/**
 * Wraps a `Tuple` result from a Mentat query.
 * A `Tuple` result is a single row {@link TypedValue}s.
 * Values for individual fields can be fetched as {@link TypedValue} or converted into a requested type.
 * <p>
 * Field values can be fetched as one of the following types:
 * <ul>
 * <li>{@link TypedValue}</li>
 * <li>long</li>
 * <li>Entid (as long)</li>
 * <li>Keyword (as String)</li>
 * <li>boolean</li>
 * <li>double</li>
 * <li>{@link Date}</li>
 * <li>{@link String}</li>
 * <li>{@link UUID}</li>
 * </ul>
 * <p>
 * To iterate over the result set use standard iteration flows.
 */
public class TupleResult extends RustObject<JNA.TypedValueList> {

    private int size;

    public TupleResult(JNA.TypedValueList pointer) {
        super(pointer);
        this.size = JNA.INSTANCE.typed_value_list_size(super.validPointer());
    }

    /**
     * Return the {@link TypedValue} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link TypedValue} at that index.
     */
    public TypedValue get(Integer index) {
        JNA.TypedValue pointer = JNA.INSTANCE.value_at_index(this.validPointer(), index);
        if (pointer == null) {
            return null;
        }
        return new TypedValue(pointer);
    }

    /**
     * Return the {@link Long} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Long` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link Long} at that index.
     */
    public Long asLong(Integer index) {
        return JNA.INSTANCE.value_at_index_into_long(this.validPointer(), index);
    }

    /**
     * Return the Entid at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Ref` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The Entid at that index.
     */
    public Long asEntid(Integer index) {
        return JNA.INSTANCE.value_at_index_into_entid(this.validPointer(), index);
    }

    /**
     * Return the keyword {@link String} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Keyword` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The keyword at that index.
     */
    public String asKeyword(Integer index) {
        return getAndConsumeMentatString(JNA.INSTANCE.value_at_index_into_kw(this.validPointer(), index));
    }

    /**
     * Return the {@link Boolean} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Boolean` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link Boolean} at that index.
     */
    public Boolean asBool(Integer index) {
        return JNA.INSTANCE.value_at_index_into_boolean(this.validPointer(), index) == 0 ? false : true;
    }

    /**
     * Return the {@link Double} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Double` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link Double} at that index.
     */
    public Double asDouble(Integer index) {
        return JNA.INSTANCE.value_at_index_into_double(this.validPointer(), index);
    }

    /**
     * Return the {@link Date} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Instant` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link Date} at that index.
     */
    public Date asDate(Integer index) {
        return new Date(JNA.INSTANCE.value_at_index_into_timestamp(this.validPointer(), index) * 1_000);
    }

    /**
     * Return the {@link String} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `String` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link String} at that index.
     */
    public String asString(Integer index) {
        return getAndConsumeMentatString(
            JNA.INSTANCE.value_at_index_into_string(this.validPointer(), index));
    }

    /**
     * Return the {@link UUID} at the specified index.
     * If the index is greater than the number of values then this function will crash.
     * If the value type if the {@link TypedValue} at this index is not `Uuid` then this function will crash.
     * @param index The index of the value to fetch.
     * @return  The {@link UUID} at that index.
     */
    public UUID asUUID(Integer index) {
        return getAndConsumeUUIDPointer(
                JNA.INSTANCE.value_at_index_into_uuid(this.validPointer(), index));
    }

    /**
     * Return the size of the tuple.
     * @return The number of items in this tuple
     */
    public int size () {
      return this.size;
    }

    @Override
    protected void destroyPointer(JNA.TypedValueList p) {
        JNA.INSTANCE.typed_value_list_destroy(p);
    }
}
