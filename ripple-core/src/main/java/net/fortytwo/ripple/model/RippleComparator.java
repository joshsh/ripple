package net.fortytwo.ripple.model;

import net.fortytwo.flow.Collector;
import net.fortytwo.ripple.RippleException;
import net.fortytwo.ripple.model.types.NumericType;
import net.fortytwo.ripple.util.ModelConnectionHelper;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

// TODO: this comparator currently ignores the equivalence of RDF lists with native lists,
// and of numeric values with numeric-typed literals

/**
 * @author Joshua Shinavier (http://fortytwo.net)
 */
public class RippleComparator implements Comparator<Object> {
    private static final Logger logger = LoggerFactory.getLogger(RippleComparator.class.getName());

    private final ModelConnection modelConnection;
    private final Comparator<RippleList> listComparator;

    public RippleComparator(final ModelConnection mc) {
        this.modelConnection = mc;
        this.listComparator = new RippleListComparator();
    }

    public Comparator<RippleList> getListComparator() {
        return listComparator;
    }

    public int compare(final Object o1,
                       final Object o2) {
        try {
            RippleType o1Type = modelConnection.getModel().getTypeOf(o1);
            RippleType o2Type = modelConnection.getModel().getTypeOf(o2);

            // if the objects have the same type, use the type's internal comparison
            if (o1Type == o2Type) {
                return o1Type.compare(o1, o2, modelConnection);
            }

            // if the objects have different types, look for equivalence relations
            RippleType.Category o1Cat = findCategory(o1, o1Type);
            RippleType.Category o2Cat = findCategory(o2, o2Type);

            // recognize URIs or blank nodes representing lists as equivalent to native lists
            if (o1Cat == RippleType.Category.OTHER_RESOURCE
                    && ModelConnectionHelper.isRDFList(o1, modelConnection)) {
                o1Cat = RippleType.Category.LIST;
            }
            if (o2Cat == RippleType.Category.OTHER_RESOURCE
                    && ModelConnectionHelper.isRDFList(o2, modelConnection)) {
                o2Cat = RippleType.Category.LIST;
            }

            int c = o1Cat.compareTo(o2Cat);

            if (0 == c) {
                switch (o1Cat) {
                    case PLAIN_LITERAL_WITHOUT_LANGUAGE_TAG:
                        return comparePlainLiteralWithoutLanguageTag(o1, o2);
                    case PLAIN_LITERAL_WITH_LANGUAGE_TAG:
                        return comparePlainLiteralWithLanguageTag(o1, o2);
                    case NUMERIC_TYPED_LITERAL:
                        return compareNumericTypedLiteral(o1, o2, o1Type, o2Type);
                    case OTHER_TYPED_LITERAL:
                        return compareOtherTypedLiteral(o1, o2);
                    case LIST:
                        return compareList(o1, o2);
                    case KEYVALUE_VALUE:
                        return compareKeyValueValue(o1, o2, o1Type, o2Type);
                    case OPERATOR:
                        return compareOperator(o1, o2, o1Type, o2Type);
                    case OTHER_RESOURCE:
                        return compareOtherResource(o1, o2);
                    case STRING_TYPED_LITERAL:
                        return compareStringTypedLiteral(o1, o2);
                    default:
                        throw new IllegalStateException();
                }
            } else {
                return c;
            }
        } catch (RippleException e) {
            logger.warn("failed to compare " + o1 + " and " + o2, e);
            return 0;
        }
    }

    // default comparison where there are no equivalence relations between classes in a category
    private int compareDefaultInSameCategory(final Object o1, final Object o2,
                                             final RippleType o1Type, final RippleType o2Type) {
        if (o1Type == o2Type) {
            return o1Type.compare(o1, o2, modelConnection);
        } else {
            return o1Type.getClass().getName().compareTo(o2Type.getClass().getName());
        }
    }

    private RippleType.Category findCategory(final Object value, final RippleType type) throws RippleException {
        if (null != type) {
            return type.getCategory();
        }

        if (value instanceof Value) {
            Value sesameValue = (Value) value;

            if (sesameValue instanceof Literal) {
                URI datatype = ((Literal) sesameValue).getDatatype();

                if (null == datatype) {
                    Optional<String> language = ((Literal) sesameValue).getLanguage();

                    if (language.isPresent()) {
                        return RippleType.Category.PLAIN_LITERAL_WITH_LANGUAGE_TAG;
                    } else {
                        return RippleType.Category.PLAIN_LITERAL_WITHOUT_LANGUAGE_TAG;
                    }
                } else {
                    if (NumericType.isNumericLiteral((Literal) sesameValue)) {
                        return RippleType.Category.NUMERIC_TYPED_LITERAL;
                    } else {
                        return RippleType.Category.OTHER_TYPED_LITERAL;
                    }
                }
            } else if (sesameValue instanceof Resource) {
                if (ModelConnectionHelper.isRDFList(sesameValue, modelConnection)) {
                    return RippleType.Category.LIST;
                } else {
                    return RippleType.Category.OTHER_RESOURCE;
                }
            } else {
                throw new RippleException("Sesame value has unrecognized class: " + sesameValue);
            }
        } else {
            throw new IllegalStateException("no category for value: " + value);
        }
    }

    private int comparePlainLiteralWithoutLanguageTag(final Object o1,
                                                      final Object o2) throws RippleException {
        return modelConnection.toRDF(o1).stringValue().compareTo(
                modelConnection.toRDF(o2).stringValue());
    }

    private int comparePlainLiteralWithLanguageTag(final Object o1,
                                                   final Object o2) throws RippleException {
        Literal o1Lit = (Literal) modelConnection.toRDF(o1);
        Literal o2Lit = (Literal) modelConnection.toRDF(o2);

        int c = o1Lit.getLanguage().get().compareTo(
                o2Lit.getLanguage().get());
        if (0 == c) {
            return o1Lit.getLabel().compareTo(
                    o2Lit.getLabel());
        } else {
            return c;
        }
    }

    private int compareNumericTypedLiteral(final Object o1,
                                           final Object o2,
                                           final RippleType o1Type,
                                           final RippleType o2Type) {
        if (!(o1Type instanceof NumericType && o2Type instanceof NumericType)) {
            throw new IllegalStateException();
        }

        return NumericType.compare(((NumericType) o1Type).findNumber(o1), ((NumericType) o2Type).findNumber(o2));
    }

    private int compareStringTypedLiteral(final Object o1,
                                          final Object o2) throws RippleException {
        Literal o1Lit = (Literal) modelConnection.toRDF(o1);
        Literal o2Lit = (Literal) modelConnection.toRDF(o2);

        return o1Lit.getLabel().compareTo(
                o2Lit.getLabel());
    }

    private int compareOtherTypedLiteral(final Object o1,
                                         final Object o2) throws RippleException {
        Literal o1Lit = (Literal) modelConnection.toRDF(o1);
        Literal o2Lit = (Literal) modelConnection.toRDF(o2);

        int c = o1Lit.getDatatype().stringValue().compareTo(
                o2Lit.getDatatype().stringValue());
        if (0 == c) {
            return o1Lit.getLabel().compareTo(
                    o2Lit.getLabel());
        } else {
            return c;
        }
    }

    // TODO: list comparison is vulnerable to infinite loops due to circular lists
    private int compareList(final Object o1,
                            final Object o2) throws RippleException {
        if (o1 instanceof RippleList) {
            if (o2 instanceof RippleList) {
                return compareNativeLists((RippleList) o1, (RippleList) o2);
            } else {
                Collector<RippleList> o1Lists
                        = new Collector<>();
                Collector<RippleList> o2Lists
                        = new Collector<>();
                o1Lists.accept((RippleList) o1);
                modelConnection.toList(o2, o2Lists);
                return compareListCollectors(o1Lists, o2Lists);
            }
        } else {
            if (o2 instanceof RippleList) {
                Collector<RippleList> o1Lists
                        = new Collector<>();
                Collector<RippleList> o2Lists
                        = new Collector<>();
                modelConnection.toList(o1, o1Lists);
                o2Lists.accept((RippleList) o2);
                return compareListCollectors(o1Lists, o2Lists);
            } else {
                Collector<RippleList> o1Lists
                        = new Collector<>();
                Collector<RippleList> o2Lists
                        = new Collector<>();
                modelConnection.toList(o1, o1Lists);
                modelConnection.toList(o2, o2Lists);
                return compareListCollectors(o1Lists, o2Lists);
            }
        }
    }

    private int compareKeyValueValue(final Object o1,
                                     final Object o2,
                                     final RippleType o1Type,
                                     final RippleType o2Type) {
        if (o1Type == o2Type) {
            return o1Type.compare(o1, o2, modelConnection);
        } else {
            return o1Type.getClass().getName().compareTo(o2Type.getClass().getName());
        }
    }

    private int compareOperator(final Object o1,
                                final Object o2,
                                final RippleType o1Type,
                                final RippleType o2Type) {
        if (o1 instanceof PrimitiveStackMapping && o2 instanceof PrimitiveStackMapping) {
            return o1Type.compare(o1, o2, modelConnection);
        } else {
            return compareDefaultInSameCategory(o1, o2, o1Type, o2Type);
        }
    }

    private int compareOtherResource(final Object o1,
                                     final Object o2) throws RippleException {
        Value o1Rdf = modelConnection.toRDF(o1);
        Value o2Rdf = modelConnection.toRDF(o2);
        return null == o1Rdf
                ? null == o2Rdf ? 0
                : -1
                : null == o2Rdf ? 1
                : o1Rdf.stringValue().compareTo(
                o2Rdf.stringValue());
    }

    private int compareNativeLists(final RippleList l1,
                                   final RippleList l2) {
        RippleList cur1 = l1;
        RippleList cur2 = l2;

        while (!cur1.isNil()) {
            if (cur2.isNil()) {
                return 1;
            }

            int cmp = compare(cur1.getFirst(), cur2.getFirst());

            if (0 != cmp) {
                return cmp;
            }

            cur1 = cur1.getRest();
            cur2 = cur2.getRest();
        }

        if (cur2.isNil()) {
            return 0;
        } else {
            return -1;
        }
    }

    private int compareListCollectors(final Collector<RippleList> l1,
                                      final Collector<RippleList> l2) throws RippleException {
        int l1Size = l1.size();
        int l2Size = l2.size();

        if (l1Size < l2Size) {
            return -1;
        } else if (l2Size < l1Size) {
            return 1;
        }

        // This is common enough to probably be worth a special case.
        else if (1 == l1Size) {
            return compareNativeLists(l1.iterator().next(),
                    l2.iterator().next());
        } else {
            RippleList[] a1 = new RippleList[l1Size];
            RippleList[] a2 = new RippleList[l2Size];

            int i = 0;
            for (RippleList l : l1) {
                a1[i] = l;
            }
            i = 0;
            for (RippleList secondList : l2) {
                a2[i] = secondList;
            }

            Arrays.sort(a1, listComparator);
            Arrays.sort(a2, listComparator);

            for (int j = 0; j < l1Size; j++) {
                int c = compareNativeLists(a1[j], a2[j]);
                if (0 != c) {
                    return c;
                }
            }

            return 0;
        }
    }

    private class RippleListComparator implements Comparator<RippleList> {
        public int compare(final RippleList l1,
                           final RippleList l2) {
            return compareNativeLists(l1, l2);
        }
    }
}
