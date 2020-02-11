package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long>