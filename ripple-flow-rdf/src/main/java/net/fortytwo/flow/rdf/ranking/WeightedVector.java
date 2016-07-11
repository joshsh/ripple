package net.fortytwo.flow.rdf.ranking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class WeightedVector<T> {
    private final Map<T, WeightedValue<T>> valueToWeightedValue;

    public WeightedVector() {
        valueToWeightedValue = new HashMap<>();
    }

    public WeightedVector(final WeightedVector<T> other) {
        valueToWeightedValue = new HashMap<>(other.valueToWeightedValue);
    }

    public int size() {
        return valueToWeightedValue.size();
    }

    public Collection<WeightedValue<T>> values() {
        return valueToWeightedValue.values();
    }

    public double getWeight(final T v) {
        WeightedValue<T> wv = valueToWeightedValue.get(v);
        return (null == wv) ? 0 : wv.weight;
    }

    public void setWeight(final T v, final double weight) {
        if (0 == weight) {
            valueToWeightedValue.remove(v);
        } else {
            WeightedValue<T> wv = valueToWeightedValue.get(v);

            if (null == wv) {
                wv = new WeightedValue<>();
                wv.value = v;
                valueToWeightedValue.put(v, wv);
            }

            wv.weight = weight;
        }
    }

    // Convenience method.
    public void addWeight(final T v, final double weight) {
        WeightedValue<T> wv = valueToWeightedValue.get(v);

        if (null == wv) {
            wv = new WeightedValue<>();
            wv.value = v;
            wv.weight = weight;
            valueToWeightedValue.put(v, wv);
        } else {
            wv.weight += weight;
            if (0 == wv.weight) {
                valueToWeightedValue.remove(v);
            }
        }
    }

    public void clear() {
        valueToWeightedValue.clear();
    }

    /**
     * @param other another vector to add to this one
     * @return sum of this vector with another vector (set union)
     */
    public WeightedVector<T> add(final WeightedVector<T> other) {
        WeightedVector<T> result = new WeightedVector<>(this);

        for (WeightedValue<T> wv : other.valueToWeightedValue.values()) {
            T v = wv.value;
            double w1 = getWeight(v);
            double w2 = wv.weight;

            result.setWeight(v, w1 + w2);
        }

        return result;
    }

    /**
     * @return product of this vector with the transpose of another vector
     * (set intersection)
     */
    public WeightedVector<T> multiplyByTransposeOf(final WeightedVector<T> other) {
        WeightedVector<T> result = new WeightedVector<>(this);

        for (WeightedValue<T> wv : valueToWeightedValue.values()) {
            T v = wv.value;
            double w1 = wv.weight;
            double w2 = other.getWeight(v);

            result.setWeight(v, w1 * w2);
        }

        return result;
    }

    /**
     * Note: assumes all non-negative weights.
     */
    public void normalizeAsDist() {
        double sum = 0;

        for (WeightedValue<T> wv : valueToWeightedValue.values()) {
            sum += wv.weight;
        }

        if (0 == sum) {
            return;
        }

        for (WeightedValue wv : valueToWeightedValue.values()) {
            wv.weight /= sum;
        }
    }

    public WeightedVector<T> normalizedAsDist() {
        WeightedVector<T> copy = new WeightedVector<>(this);
        copy.normalizeAsDist();
        return copy;
    }
}
