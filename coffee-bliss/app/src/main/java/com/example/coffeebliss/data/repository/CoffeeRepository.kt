package com.example.coffeebliss.data.repository

import com.example.coffeebliss.data.local.dao.MemberDao
import com.example.coffeebliss.data.local.dao.RedemptionDao
import com.example.coffeebliss.data.local.dao.TransaksiDao
import com.example.coffeebliss.data.local.entity.Member
import com.example.coffeebliss.data.local.entity.Redemption
import com.example.coffeebliss.data.local.entity.Transaksi
import kotlinx.coroutines.flow.Flow

class CoffeeRepository(
    private val memberDao: MemberDao,
    private val transaksiDao: TransaksiDao,
    private val redemptionDao: RedemptionDao
) {
    suspend fun registerMember(nama: String, email: String, noHp: String): Result<Member> {
        return try {
            val existing = memberDao.getMemberByEmail(email)
            if (existing != null) return Result.failure(Exception("Email sudah terdaftar"))
            val count = memberDao.getMemberCount()
            val nomorMember = "CB%04d".format(count + 1)
            val member = Member(nama = nama, email = email, noHp = noHp, nomorMember = nomorMember)
            val id = memberDao.insertMember(member)
            Result.success(member.copy(id = id.toInt()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String): Member? = memberDao.getMemberByEmail(email)

    fun getMemberById(id: Int): Flow<Member?> = memberDao.getMemberById(id)

    fun getTransaksi(memberId: Int): Flow<List<Transaksi>> = transaksiDao.getTransaksiByMember(memberId)

    fun getRedemptions(memberId: Int): Flow<List<Redemption>> = redemptionDao.getRedemptionsByMember(memberId)

    suspend fun addTransaksiAndUpdatePoin(member: Member, jumlahBelanja: Long, keterangan: String): Result<Int> {
        return try {
            val poin = (jumlahBelanja / 10000).toInt()
            transaksiDao.insertTransaksi(
                Transaksi(memberId = member.id, jumlahBelanja = jumlahBelanja, poinDapat = poin, keterangan = keterangan)
            )
            val newTotal = member.totalPoin + poin
            val newStatus = when {
                newTotal >= 500 -> "Gold"
                newTotal >= 200 -> "Silver Plus"
                else -> "Silver"
            }
            memberDao.updateMember(member.copy(totalPoin = newTotal, status = newStatus))
            Result.success(poin)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun redeemPoin(member: Member, reward: String, poinDibutuhkan: Int): Result<Unit> {
        return try {
            if (member.totalPoin < poinDibutuhkan) {
                return Result.failure(Exception("Poin tidak cukup. Poin Anda: ${member.totalPoin}, dibutuhkan: $poinDibutuhkan"))
            }
            redemptionDao.insertRedemption(
                Redemption(memberId = member.id, reward = reward, poinDipakai = poinDibutuhkan)
            )
            memberDao.updateMember(member.copy(totalPoin = member.totalPoin - poinDibutuhkan))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
