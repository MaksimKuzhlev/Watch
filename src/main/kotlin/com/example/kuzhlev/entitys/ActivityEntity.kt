package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name="activity")
@Component
class ActivityEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    var calories:Int,

        )