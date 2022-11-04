package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name="medicate")
@Component
class PatientMedicateEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
    var token:String,
    var amount:String,
    var frequency:String,
    var nameMed:String,
        )