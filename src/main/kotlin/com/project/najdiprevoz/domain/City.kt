package com.project.najdiprevoz.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "cities")
data class City(
        @Column(name = "name")
        val name: String
) : BaseEntity<Long>()