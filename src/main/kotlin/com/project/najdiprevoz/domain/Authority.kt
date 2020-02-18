package com.project.najdiprevoz.domain

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "authorities")
class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    var id: Long? = null
    @Column(name = "authority", nullable = false, unique = true)
    var authority: String? = null
    // Inverse
    @OneToMany(mappedBy = "authority")
    var users: List<User> = ArrayList()
}