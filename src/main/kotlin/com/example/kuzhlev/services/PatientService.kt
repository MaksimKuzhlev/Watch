package com.example.kuzhlev.services

import com.example.kuzhlev.DTO.CheckUpdate
import com.example.kuzhlev.entitys.*
import com.example.kuzhlev.views.CreateWatchForm

interface PatientService {
    fun saveInfoPeople(patientEntity: PatientEntity)
    fun deletePeople(token:String)

    fun savePhysicPeople(physicalEntity: PatientPhysicalEntity)

    fun saveDiseasePeople(patientDiseaseEntity: PatientDiseaseEntity)
    fun saveMedicatePeople(medicateEntity: PatientMedicateEntity)
    fun deleteMedicatePeople(medicateEntity: PatientMedicateEntity)
    fun createToken():String
    fun check(checkUpdate: CheckUpdate)

    fun checkrq():Boolean

    fun idrq():Long

    fun tokenrq():String

    fun namerq(token:String):String

}