package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Authority

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AuthorityRepository : JpaRepository<Authority?, Long?> {
    fun findByAuthority(authority: String?): Authority?
}