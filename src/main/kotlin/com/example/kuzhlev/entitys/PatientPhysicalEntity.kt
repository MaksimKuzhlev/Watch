package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name="patientPhysic")
@Component
class PatientPhysicalEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,
    var token:String,
    var weight:Int,
    var growth:Int,
    var old:Int,
    var birthday: LocalDate
    )