package com.project.najdiprevoz.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "cities")
data class City(
        @Column(name = "name")
        private val name: String
) : BaseEntity<Long>()