package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Ride
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface RideRepository : JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride>