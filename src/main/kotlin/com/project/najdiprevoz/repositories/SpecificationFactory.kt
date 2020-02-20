package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.Ride
import org.springframework.data.jpa.domain.Specification
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root


//search by city name
private fun fromCityNameLike(cityName: String, root: Root<Ride>, cb: CriteriaBuilder) =
        cb.equal(root.get<City>("from").get<String>("name"), cityName)

fun fromCityNameLike(cityName: String) = Specification<Ride> { root, _, cb ->
    fromCityNameLike(cityName, root, cb)
}

fun <T> getPath(root: Root<T>, attributeName: List<String>): Path<T> {
    var path: Path<T> = root
    for (part in attributeName) {
        path = path.get(part)
    }
    return path
}

private fun cityToLikePredicate(cityName: String, root: Root<Ride>, cb: CriteriaBuilder) =
        cb.equal(root.get<City>("to").get<String>("name"), cityName)

fun cityToLike(cityName: String) =
        Specification<Ride> { root, _, cb -> cityToLikePredicate(cityName, root, cb) }

private fun <T> valueLike(value: String, root: Root<T>, cb: CriteriaBuilder, properties: List<String>) =
        cb.like(getPath(root, properties).`as`(String::class.java), value)

fun <T> likeSpecification(properties: List<String>, value: String) {
    Specification<T> { root, _, cb -> valueLike(value, root, cb, properties) }
}


private fun <T> dateOnSpecificationPredicate(value: Date, root: Root<T>, cb: CriteriaBuilder, properties: List<String>) =
        cb.equal(getPath(root, properties).`as`(Date::class.java), value)

fun <T> dateOnSpecification(properties: List<String>, value: Date) =
        Specification<T> { root, _, cb -> dateOnSpecificationPredicate(value, root, cb, properties) }


private fun <T> greaterThanOrEqualsPredicate(value: Int, properties: List<String>, root: Root<T>, cb: CriteriaBuilder) = cb.greaterThanOrEqualTo(getPath(root, properties).`as`(Int::class.java), value)

fun <T> greaterThanOrEquals(properties: List<String>, value: Int) =
        Specification<T> { root, _, cb -> greaterThanOrEqualsPredicate(value, properties, root, cb) }


private fun <T> laterThanTimePredicate (value: ZonedDateTime, properties: List<String>, root: Root<T>, cb: CriteriaBuilder) =
        cb.greaterThan(getPath(root, properties).`as`(ZonedDateTime::class.java), value)

fun <T> laterThanTime(properties: List<String>, value: ZonedDateTime) =
        Specification<T> { root, _, cb -> laterThanTimePredicate(value,properties,root,cb) }


//private fun <T> valueLike(value: String, root: Root<T>, cb: CriteriaBuilder, properties: List<String>) =
//        cb.like(getPath(root, properties).`as`(String::class.java), value)
//
//search by city name


//
//fun nameLike(name: String): Specification<Ride> =
//        Specification { root, _, cb ->
//            searchByNameSpecification(root, cb, name)
//        }
//
//private fun searchByNameSpecification(root: Root<Ride>, criteriaBuilder: CriteriaBuilder, name: String) =
//        criteriaBuilder.like(root.get<String>(name), "%$name%")
//
//fun <T> isNull(fieldName: String) =
//        Specification<T> { root, _, cb ->
//            isNullPredicate(cb, root, fieldName)
//        }
//
//private fun <T> isNullPredicate(cb: CriteriaBuilder, root: Root<T>, fieldName: String) =
//        cb.isNull(root.get<Any>(fieldName))
//
//
////search after given date, inclusive
//private fun afterDatePredicate(date: Date, root: Root<Ride>, cb: CriteriaBuilder) =
//        cb.greaterThan(root.get<Date>("startDate"), date)
//
//fun afterDate(date: Date) = Specification<Ride> { root, _, cb ->
//    afterDatePredicate(date, root, cb)
//}
//
////search before date, inclusive
//private fun beforeDatePredicate(date: Date, root: Root<Ride>, cb: CriteriaBuilder) =
//        cb.lessThanOrEqualTo(root.get<Date>("dueDate"), date)
//
//fun beforeDate(date: Date) = Specification<Ride> { root, _, cb ->
//    beforeDatePredicate(date, root, cb)
//}
//
////search where project name in [1, 2, 3]
//
//private fun statusNameInPredicate(names: Collection<String>, root: Root<Ride>) =
//        root.get<String>("status").`in`(names)
//
//private fun subjectLikePredicate(subject: String, root: Root<Ride>, cb: CriteriaBuilder) =
//        cb.like(cb.lower(root.get<String>("subject")), "%$subject%")
//
//fun subjectLike(subject: String) = Specification<Ride> { root, _, cb ->
//    subjectLikePredicate(subject.trim().toLowerCase(), root, cb)
//}
//
//fun statusNameIn(names: Collection<String>) = Specification<Ride> { root, _, _ ->
//    statusNameInPredicate(names, root)
//}
//
//private fun projectEquals(projectId: Int, root: Root<Ride>, cb: CriteriaBuilder) =
//        cb.equal(root.get<Int>("projectId"), projectId)
//
//fun projectIdEquals(projectId: Int) = Specification<Ride> { root, _, cb ->
//    projectEquals(projectId, root, cb)
//}
//
//private fun ticketNumberEqualsPredicate(ticketNumber: Int, root: Root<Ride>, cb: CriteriaBuilder) =
//        cb.equal(root.get<Int>("id"), ticketNumber)
//
//
//fun ticketNumberEquals(ticketNumber: Int) = Specification<Ride> { root, _, cb ->
//    ticketNumberEqualsPredicate(ticketNumber, root, cb)
//}
//
//private fun trackerSearch(trackers: List<String>, root: Root<Ride>) =
//        root.get<String>("tracker").`in`(trackers)
//
//fun trackerIn(trackers: List<String>) = Specification<Ride> { root, _, _ ->
//    trackerSearch(trackers, root)
//}
//
// H@CK where 1 = 1
fun whereTrue() = Specification<Ride> { _, _, cb ->
    cb.and()
}
//
