package com.project.najdiprevoz.utils

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RideStatus
import org.springframework.data.jpa.domain.Specification
import java.time.ZonedDateTime
import javax.persistence.criteria.*


// H@CK where 1 = 1
fun whereTrue() = Specification<Ride> { _, _, cb ->
    cb.and()
}

 fun<T> getPath(root: Root<T>, attributeName: List<String>): Path<T> {
    var path: Path<T> = root
    for (part in attributeName) {
        path = path.get(part)
    }
    return path
}

private fun tripStatusEqualsPredicate(properties: List<String>, value: RideStatus, root: Root<Ride>,
                                      cb: CriteriaBuilder) =
        cb.equal(getPath(root, properties), value)

fun tripStatusEqualsSpecification(properties: List<String>, value: RideStatus): Specification<Ride> =
        Specification<Ride> { root, _, cb -> tripStatusEqualsPredicate(properties, value, root, cb) }

private fun <T>  valueLike(value: String, root: Root<T>, cb: CriteriaBuilder, properties: List<String>) =
        cb.like(getPath(root, properties).`as`(String::class.java), value)

fun <T>likeSpecification(properties: List<String>, value: String): Specification<T> =
        Specification { root, _, cb -> valueLike(value, root, cb, properties) }


inline fun <T, reified E:Comparable<E>>greaterThanOrEqualsPredicate(value: E, properties: List<String>, root: Root<T>,
                                                              cb: CriteriaBuilder): Predicate = cb.greaterThanOrEqualTo(
        getPath(root, properties).`as`(E::class.java), value)

inline fun <T, reified E:Comparable<E>> greaterThanOrEquals(properties: List<String>, value: E) =
        Specification<T> { root, _, cb -> greaterThanOrEqualsPredicate(value, properties, root, cb) }


private fun <T, E> equalsPredicate(value: E, properties: List<String>, root: Root<T>,
                                         cb: CriteriaBuilder) = cb.equal(
        getPath(root, properties), value)

fun <T> equalSpecification(properties: List<String>, value: Long) =
        Specification<T> { root, _, cb -> equalsPredicate(value, properties, root, cb) }

private fun <T> laterThanTimePredicate(value: ZonedDateTime, properties: List<String>, root: Root<T>,
                                   cb: CriteriaBuilder) =
        cb.and(cb.greaterThanOrEqualTo(getPath(root, properties).`as`(ZonedDateTime::class.java), value),
                cb.lessThanOrEqualTo(getPath(root, properties).`as`(ZonedDateTime::class.java), value.withHour(23).withMinute(59)))

fun <T> laterThanTime(properties: List<String>, value: ZonedDateTime) =
        Specification<T> { root, _, cb -> laterThanTimePredicate(value, properties, root, cb) }



fun <T> equals(propertyNames: List<String>, value: Any?): Specification<T>? {
    if (value == null) {
        return Specification.where(null)
    }
    val result = Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder -> criteriaBuilder.equal(getPath(root , propertyNames), value) }
    return result.and(isNotNull(propertyNames))
}

fun <T> like(propertyName: String?, value: String): Specification<T>? {
    return if (value=="") {
        Specification.where(null)
    } else Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get(propertyName)),
                "%" + value.toLowerCase() + "%")
    }
}

fun <T> isNotNull(propertyName: String?): Specification<T>? {
    return Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder -> criteriaBuilder.isNotNull(root.get<Any>(propertyName)) }
}

fun <T> isNotNull(propertyName: List<String>): Specification<T>? {
    return Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder -> criteriaBuilder.isNotNull(getPath(root, propertyName)) }
}


fun <T> notEquals(propertyNames: List<String>, value: Any?): Specification<T>? {
    return if (value == null || propertyNames.isEmpty()) {
        Specification.where(null)
    } else Specification { root: Root<T>, query: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder -> criteriaBuilder.notEqual(getPath(root, propertyNames), value) }
}

