//package com.project.najdiprevoz;
//
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import javax.persistence.criteria.Path;
//import javax.persistence.criteria.Root;
//import java.time.ZonedDateTime;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//@Service
//public class SpecificationFactory {
//
//    /**
//     * In order to use this method, you need to set your "spring.jpa.properties.hibernate.dialect" property to {@link
//     * PingaPostgreSQLDialect} because this dialect is the one that registers the function so that jpa can use it.
//     * You can also register the function in your own dialect, if you prefer to use something else than {@link
//     * PingaPostgreSQLDialect}
//     * <p>
//     * Performs AND matching for given collection of words against the vector( list of lexeme tokens in the underlying db system ).
//     *
//     * @param propertyName the name of the field property on which full text search will be executed
//     * @param collection   the collection of words for querying the {@code propertyName} vector for occurrences of certain words or phrases
//     * @param <T>          the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> containsAnd(String propertyName, Collection<String> collection) {
//        if (collection == null || collection.isEmpty()) {
//            return Specification.where(null);
//        }
//        Specification<T> result = (root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(criteriaBuilder.function(com.project.najdiprevoz.PingaPostgreSQLDialect.FULL_TEXT_SEARCH, Boolean.class, root.get(propertyName), criteriaBuilder.literal(String.join(" & ", collection))));
//        return result;
//    }
//
//
//    /**
//     * In order to use this method, you need to set your "spring.jpa.properties.hibernate.dialect" property to {@link
//     * PingaPostgreSQLDialect} because this dialect is the one that registers the function so that jpa can use it.
//     * You can also register the function in your own dialect, if you prefer to use something else than {@link
//     * PingaPostgreSQLDialect}
//     * <p>
//     * Performs OR matching for given collection of words against the vector( list of lexeme tokens in the underlying db system ).
//     *
//     * @param collection the collection of words for querying the {@code propertyName} vector for occurrences of certain words or phrases
//     * @param <T>        the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> containsOr(Collection<String> propertyNames, Collection<String> collection) {
//        if (collection == null || collection.isEmpty()) {
//            return Specification.where(null);
//        }
//        Specification<T> result = (root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(criteriaBuilder.function(com.project.najdiprevoz.PingaPostgreSQLDialect.FULL_TEXT_SEARCH, Boolean.class, generatePropertyPath(propertyNames, root), criteriaBuilder.literal(String.join(" | ", collection))));
//        return result;
//    }
//
//    /**
//     * In order to use this method, you need to set your "spring.jpa.properties.hibernate.dialect" property to {@link
//     * PingaPostgreSQLDialect} because this dialect is the one that registers the function so that jpa can use it.
//     * You can also register the function in your own dialect, if you prefer to use something else than {@link
//     * PingaPostgreSQLDialect}
//     * <p>
//     * Performs NOT matching for given collection of words against the vector( list of lexeme tokens in the underlying db system ).
//     *
//     * @param propertyName the name of the field property on which full text search will be executed
//     * @param collection   the collection of words for querying the {@code propertyName} vector for occurrences of certain words or phrases
//     * @param <T>          the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> containsNot(String propertyName, Collection<String> collection) {
//        if (collection == null || collection.isEmpty()) {
//            return Specification.where(null);
//        }
//        Specification<T> result = (root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(criteriaBuilder.function(PingaPostgreSQLDialect.FULL_TEXT_SEARCH, Boolean.class, root.get(propertyName), criteriaBuilder.literal("!(" + String.join(" | ", collection) + ")")));
//        return result;
//    }
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#equals(Collection, Object)}}
//     */
//    @Deprecated
//    public <T> Specification<T> equals(String propertyName, Object value) {
//
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(propertyName), value);
//        return result.and(isNotNull(propertyName));
//    }
//
//
//    public <T> Specification<T> notEquals(String propertyName, Object value) {
//
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(propertyName), value);
//        return result;
//    }
//
//    public <T> Specification<T> notEquals(Collection<String> propertyNames, Object value) {
//        if (value == null || CollectionUtils.isEmpty(propertyNames)) {
//            return Specification.where(null);
//        }
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.notEqual(generatePropertyPath(propertyNames, root), value);
//    }
//
//    /**
//     * This method creates a specification that checks the equality between 2 fields that the entity contains.
//     * For example, lets say that we have the following class:
//     * class User {
//     * String firstName;
//     * String lastName;
//     * }
//     * if we call this method with in the following way: fieldEquals("firstName", "lastName")
//     * the specification will create the following query:
//     * SELECT * FROM USERS
//     * WHERE firstName = lastName.
//     */
//    public <T> Specification<T> fieldsEquals(String propertyName1, String propertyName2) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get(propertyName1), root.get(propertyName2));
//    }
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#equals(Collection, Object)}}
//     */
//    @Deprecated
//    public <T> Specification<T> equals(String propertyName, String propertyName2, Object value) {
//
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(propertyName).get(propertyName2), value);
//        return result.and(isNotNull(propertyName));
//    }
//
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#equals(Collection, Object)}}
//     */
//    @Deprecated
//    public <T> Specification<T> equals(String propertyName, String propertyName2, String propertyName3, Object value) {
//
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(propertyName).get(propertyName2).get(propertyName3), value);
//        return result.and(isNotNull(propertyName));
//    }
//
//    public <T> Specification<T> equals(Collection<String> propertyNames, Object value) {
//
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.equal(generatePropertyPath(propertyNames, root), value);
//        return result.and(isNotNull(propertyNames));
//    }
//
//    public <T> Specification<T> like(String propertyName, String value) {
//        if (value == "" || value == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(propertyName)),
//                "%" + value.toLowerCase() + "%");
//    }
//
//    public <T> Specification<T> likeAsString(String propertyName, String value) {
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(propertyName).as(String.class)),
//                "%" + value.toLowerCase() + "%");
//    }
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#in(Collection, Collection)}
//     */
//    @Deprecated
//    public <T> Specification<T> in(String propertyName, Collection collection) {
//        if (collection == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(propertyName)).value(collection);
//    }
//
//    public <T> Specification<T> in(Collection<String> propertyNames, Collection collection) {
//        if (collection == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.in(generatePropertyPath(propertyNames, root)).value(collection);
//    }
//
//    /**
//     * Create a predicate for testing whether the given {@code ZonedDateTime } value is
//     * between from and to values.
//     * Also if one of the values is null it will create a predicate for {@link SpecificationFactory#lessThanOrEquals} or {@link SpecificationFactory#greaterThanOrEqual}
//     *
//     * @param propertyNames list of property names in order from the root one
//     * @param from          starting range point; inclusive
//     * @param to            ending range point; inclusive
//     * @param <T>           the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> between(Collection<String> propertyNames, ZonedDateTime from, ZonedDateTime to) {
//        if (from == null && to == null)
//            return Specification.where(null);
//        else if (from == null)
//            return lessThanOrEquals(propertyNames, to);
//        else if (to == null)
//            return greaterThanOrEqual(propertyNames, from);
//
//        Specification<T> result = (root, query, criteriaBuilder) -> criteriaBuilder.between(generatePropertyPath(propertyNames, root), from, to);
//        return result;
//    }
//
//    /**
//     * Create a predicate for testing whether the given field's value is
//     * less than or equal to second argument - value.
//     *
//     * @param propertyNames list of property names in order from the root one
//     * @param value         value to be matched against
//     * @param <T>           the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> lessThanOrEquals(Collection<String> propertyNames, ZonedDateTime value) {
//        if (value == null)
//            return Specification.where(null);
//        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(generatePropertyPath(propertyNames, root), value);
//    }
//
//    /**
//     * Create a predicate for testing whether the given field's value is
//     * greater than or equal to second argument.
//     *
//     * @param propertyNames list of property names in order from the root one
//     * @param value         value to be matched against
//     * @param <T>           the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> greaterThanOrEqual(Collection<String> propertyNames, ZonedDateTime value) {
//        if (value == null)
//            return Specification.where(null);
//        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(generatePropertyPath(propertyNames, root), value);
//    }
//
//    /**
//     * The good old "Utils.safeIn" from MojTermin/MojDoktor :D
//     * If the collection is null or empty, returns an expression that is always false,
//     * otherwise it calls the regular {@link SpecificationFactory#in(String, Collection)}
//     */
//    public <T> Specification<T> safeIn(String propertyName, Collection collection) {
//        if (collection == null || collection.size() == 0)
//            return (root, query, criteriaBuilder) -> criteriaBuilder.or();
//        return in(propertyName, collection);
//    }
//
//    public <T> Specification<T> safeIn(Collection<String> propertyNames, Collection collection) {
//        if (collection == null || collection.size() == 0)
//            return (root, query, criteriaBuilder) -> criteriaBuilder.or();
//        return in(propertyNames, collection);
//    }
//
//    public <T> Specification<T> in(String propertyName1, String propertyName2, Collection collection) {
//        if (collection == null) {
//            return Specification.where(null);
//        }
//        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(propertyName1).get(propertyName2)).value(collection);
//    }
//
//    public <T> Specification<T> in(String propertyName1, String propertyName2, String propertyName3, String propertyName4, Collection collection) {
//        if (collection == null) {
//            return Specification.where(null);
//        }
//        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(propertyName1).get(propertyName2).get(propertyName3).get(propertyName4)).value(collection);
//    }
//
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#like(Collection, String)}}
//     */
//    @Deprecated
//    public <T> Specification<T> like(String propertyName1, String propertyName2, String value) {
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(propertyName1).get(propertyName2)),
//                "%" + value.toLowerCase() + "%");
//    }
//
//    /**
//     * Create a predicate for testing whether the property's value
//     * satisfies the given pattern value.
//     *
//     * @param propertyNames list of property names in order from the root one
//     * @param value         value to be matched against
//     * @param <T>           the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    public <T> Specification<T> like(Collection<String> propertyNames, String value) {
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(generatePropertyPath(propertyNames, root)),
//                "%" + value.toLowerCase() + "%");
//    }
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#like(Collection, String)}}
//     */
//    @Deprecated
//    public <T> Specification<T> like(String propertyName1, String propertyName2, String propertyName3, String value) {
//        if (value == null) {
//            return Specification.where(null);
//        }
//
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(propertyName1).get(propertyName2).get(propertyName3)),
//                "%" + value.toLowerCase() + "%");
//    }
//
//
//    public <T> Specification<T> isNull(String propertyName) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(propertyName));
//    }
//
//    /**
//     * @deprecated Use collection based property names implementation. {@link SpecificationFactory#isNotNull(Collection)}}
//     */
//    @Deprecated
//    public <T> Specification<T> isNotNull(String propertyName) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(propertyName));
//    }
//
//    public <T> Specification<T> isNotNull(Collection<String> propertyName) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(generatePropertyPath(propertyName, root));
//    }
//
//    /**
//     * In order to use this method, you need to set your "spring.jpa.properties.hibernate.dialect" property to {@link
//     * PingaPostgreSQLDialect} because this dialect is the one that registers the function so that jpa can use it.
//     * You can also register the function in your own dialect, if you prefer to use something else than {@link
//     * PingaPostgreSQLDialect}
//     *
//     * @param propertyName the name of the array property
//     * @param items        the array for which we want to check if it intersects with the {@code propertyName} array.
//     *                     If this array is null or empty, the method returns a specification that is always false.
//     * @param <T>          the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    final public <T> Specification<T> stringArrayIntersection(String propertyName, Collection<String> items) {
//        if (CollectionUtils.isEmpty(items))
//            return (root, query, criteriaBuilder) -> criteriaBuilder.or();
//
//        String itemsArray = items.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(",", "{", "}"));
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(
//                        criteriaBuilder.function(
//                                PingaPostgreSQLDialect.STRING_ARRAY_INTERSECTION,
//                                Boolean.class,
//                                root.get(propertyName),
//                                criteriaBuilder.literal(itemsArray)
//                        )
//                );
//    }
//
//    /**
//     * In order to use this method, you need to set your "spring.jpa.properties.hibernate.dialect" property to {@link
//     * PingaPostgreSQLDialect} because this dialect is the one that registers the function so that jpa can use it.
//     * You can also register the function in your own dialect, if you prefer to use something else than {@link
//     * PingaPostgreSQLDialect}
//     *
//     * @param propertyName the name of the array property
//     * @param items        the array for which we want to check if it intersects with the {@code propertyName} array.
//     *                     If this array is null or empty, the method returns a specification that is always false.
//     * @param <T>          the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    final public <T> Specification<T> longArrayIntersection(String propertyName, Collection<Long> items) {
//        if (CollectionUtils.isEmpty(items))
//            return (root, query, criteriaBuilder) -> criteriaBuilder.or();
//
//        String itemsArray = items.stream()
//                .map(Object::toString)
//                .collect(Collectors.joining(",", "{", "}"));
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(
//                        criteriaBuilder.function(
//                                PingaPostgreSQLDialect.LONG_ARRAY_INTERSECTION,
//                                Boolean.class,
//                                root.get(propertyName),
//                                criteriaBuilder.literal(itemsArray)
//                        )
//                );
//    }
//
//    @SafeVarargs
//    final public <T> Specification<T> chainOrSpecifications(Specification<T>... specifications) {
//        return Arrays.stream(specifications)
//                .reduce(Specification::or)
//                .orElseThrow(() -> new RuntimeException("The chain of specifications cannot be null"));
//    }
//
//    @SafeVarargs
//    final public <T> Specification<T> chainAndSpecifications(Specification<T>... specifications) {
//        return Arrays.stream(specifications)
//                .reduce(Specification::and)
//                .orElseThrow(() -> new RuntimeException("The chain of specifications cannot be null"));
//    }
//
//    /**
//     * This specification means that no filtering will be done on the rows
//     */
//    public <T> Specification<T> nullSpec() {
//        return Specification.where(null);
//    }
//
//    /**
//     * Create a path corresponding to the referenced attribute
//     *
//     * @param propertyNames list of property names in order from the root one
//     * @param root          root type in the from clause.
//     * @param <T>           the type of entity on which this specification will be executed
//     * @return a specification
//     */
//    private <T, X> Path<X> generatePropertyPath(Collection<String> propertyNames, Root<T> root) {
//        Path<X> actualPropertyPath = null;
//        for (String propertyName : propertyNames)
//            if (actualPropertyPath == null)
//                actualPropertyPath = root.get(propertyName);
//            else
//                actualPropertyPath = actualPropertyPath.get(propertyName);
//
//        return actualPropertyPath;
//    }
//
//}
