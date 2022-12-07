package com.example.kuzhlev.entitys

import org.springframework.stereotype.Component
import javax.persistence.*

@Entity
@Table(name="disease")
@Component
class PatientDiseaseEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long,
    var token:String,
    var stroke:Boolean,
    var tachycardia:Boolean,
    var bradycardia:Boolean,
    var arrhythmia:Boolean,
    var disease:String?,
        )