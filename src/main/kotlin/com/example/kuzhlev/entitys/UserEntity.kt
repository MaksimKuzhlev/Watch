package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table (name="usr")
@Component
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    var firstName:String,
    var secondName:String,
    var token:String?,
    var username:String,
    var password:String,
    var role: String,
    var active:Boolean
        )