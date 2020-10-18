package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.enums.RideStatus
import org.springframework.data.jpa.domain.Specification
import java.time.ZonedDateTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root

// H@CK where 1 = 1
fun whereTrue() = Specification<Ride> { _, _, cb ->
    cb.and()
}

private fun getPath(root: Root<Ride>, attributeName: List<String>): Path<Ride> {
    var path: Path<Ride> = root
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

private fun valueLike(value: String, root: Root<Ride>, cb: CriteriaBuilder, properties: List<String>) =
        cb.like(getPath(root, properties).`as`(String::class.java), value)

fun likeSpecification(properties: List<String>, value: String): Specification<Ride> =
        Specification<Ride> { root, _, cb -> valueLike(value, root, cb, properties) }


private fun greaterThanOrEqualsPredicate(value: Int, properties: List<String>, root: Root<Ride>,
                                         cb: CriteriaBuilder) = cb.greaterThanOrEqualTo(
        getPath(root, properties).`as`(Int::class.java), value)

fun greaterThanOrEquals(properties: List<String>, value: Int) =
        Specification<Ride> { root, _, cb -> greaterThanOrEqualsPredicate(value, properties, root, cb) }


private fun equalsPredicate(value: Long, properties: List<String>, root: Root<Ride>,
                                         cb: CriteriaBuilder) = cb.equal(
        getPath(root, properties).`as`(Long::class.java), value)

fun equalLongSpecification(properties: List<String>, value: Long) =
        Specification<Ride> { root, _, cb -> equalsPredicate(value, properties, root, cb) }


private fun laterThanTimePredicate(value: ZonedDateTime, properties: List<String>, root: Root<Ride>,
                                   cb: CriteriaBuilder) =
        cb.and(cb.greaterThanOrEqualTo(getPath(root, properties).`as`(ZonedDateTime::class.java), value),
                cb.lessThanOrEqualTo(getPath(root, properties).`as`(ZonedDateTime::class.java), value.withHour(23).withMinute(59)))

fun laterThanTime(properties: List<String>, value: ZonedDateTime) =
        Specification<Ride> { root, _, cb -> laterThanTimePredicate(value, properties, root, cb) }


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

