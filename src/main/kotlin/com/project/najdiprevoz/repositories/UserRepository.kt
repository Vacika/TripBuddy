package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

    @Query("""
        SELECT u as rating FROM User u
        JOIN Rating r on
        r.reservationRequest.trip.driver = u
        GROUP BY u.id
        ORDER BY avg(rating)
        DESC
    """)
    fun findTopRatedDrivers(page: Pageable): List<User>?

    fun findByUsername(username: String): Optional<User>

    fun findByActivationToken(activationToken: String): Optional<User>

    @Modifying
    @Query("UPDATE User set isActivated = true where username = :username")
    fun activateUser(@Param("username") username: String)

}
