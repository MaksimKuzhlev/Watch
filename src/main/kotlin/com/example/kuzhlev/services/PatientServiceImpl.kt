package com.example.kuzhlev.services

import com.example.kuzhlev.DTO.CheckUpdate
import com.example.kuzhlev.DTO.Sos
import com.example.kuzhlev.entitys.PatientDiseaseEntity
import com.example.kuzhlev.entitys.PatientEntity
import com.example.kuzhlev.entitys.PatientMedicateEntity

import com.example.kuzhlev.repositories.*
import org.springframework.stereotype.Service

@Service
class PatientServiceImpl(private val watchRepository: WatchRepository,
                         private val patientRepository: PatientRepository,
                         private val diseaseRepository: DiseaseRepository,
                         private val medicateRepository: MedicateRepository,
                         ):PatientService {

    var id:Long = 0
    var token = ""
    var check = false

    override fun saveInfoPeople(patientEntity: PatientEntity) {

        patientRepository.save(patientEntity)
    }

    override fun deletePeople(token: String) {
        patientRepository.delete(patientRepository.findByToken(token))
        diseaseRepository.delete(diseaseRepository.findByToken(token)!!)
        medicateRepository.findByToken(token).forEach { medicateRepository.delete(it) }
    }

    override fun saveMedicatePeople(medicateEntity: PatientMedicateEntity) {
        medicateRepository.save(medicateEntity)
    }

    override fun saveDiseasePeople(patientDiseaseEntity: PatientDiseaseEntity) {
        diseaseRepository.save(patientDiseaseEntity)
    }

    override fun deleteMedicatePeople(medicateEntity: PatientMedicateEntity) {
        medicateRepository.delete(medicateEntity)
    }


    override fun createToken(): String {
        var rand =""
        do {
            for (l in 0..7) {
                rand += ('a'..'z').random().toString()
            }
        } while(watchRepository.findByToken(rand)!=null)
        return rand
    }

    override fun check(checkUpdate: CheckUpdate) {
        check = checkUpdate.check
        id = checkUpdate.id
        token = checkUpdate.token
    }

    override fun checkrq(): Boolean {
        return check
    }

    override fun idrq(): Long {
        return id
    }

    override fun tokenrq(): String {
        return token
    }

    override fun namerq(token:String): String {
       return watchRepository.findByToken(token)?.name ?: ""
    }

    override fun tachycardiaCheck(token: String): Boolean {
        return diseaseRepository.findByToken(token)!!.tachycardia
    }

    override fun bradicardiaCheck(token: String): Boolean {
        return diseaseRepository.findByToken(token)!!.bradycardia
    }


}