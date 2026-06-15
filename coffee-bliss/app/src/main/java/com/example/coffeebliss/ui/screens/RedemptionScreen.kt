package com.example.coffeebliss.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coffeebliss.data.local.entity.Redemption
import com.example.coffeebliss.viewmodel.CoffeeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RewardItem(val nama: String, val poinDibutuhkan: Int, val emoji: String, val deskripsi: String)

val availableRewards = listOf(
    RewardItem("Espresso", 50, "☕", "Secangkir espresso panas"),
    RewardItem("Cappuccino", 100, "🍵", "Cappuccino creamy dengan foam sempurna"),
    RewardItem("Free Latte", 150, "🥛", "Latte premium pilihan barista")
)

@Composable
fun RedemptionScreen(viewModel: CoffeeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var rewardToRedeem by remember { mutableStateOf<RewardItem?>(null) }

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        val msg = uiState.successMessage ?: uiState.errorMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Tukar Poin", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            uiState.currentMember?.let { member ->
                Text("Poin saat ini: ${member.totalPoin} poin", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Pilih Reward", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            availableRewards.forEach { reward ->
                val canRedeem = (uiState.currentMember?.totalPoin ?: 0) >= reward.poinDibutuhkan
                RewardCard(
                    reward = reward,
                    canRedeem = canRedeem,
                    onRedeem = { rewardToRedeem = reward }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Riwayat Penukaran", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.redemptionList.isEmpty()) {
                Text("Belum ada penukaran poin", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.redemptionList) { redemption ->
                        RedemptionHistoryCard(redemption)
                    }
                }
            }
        }
    }

    rewardToRedeem?.let { reward ->
        AlertDialog(
            onDismissRequest = { rewardToRedeem = null },
            title = { Text("Tukar Poin") },
            text = { Text("Tukar ${reward.poinDibutuhkan} poin dengan ${reward.emoji} ${reward.nama}?\n\nPoin Anda: ${uiState.currentMember?.totalPoin ?: 0}") },
            confirmButton = {
                Button(onClick = {
                    viewModel.redeemReward(reward.nama, reward.poinDibutuhkan)
                    rewardToRedeem = null
                }) { Text("Tukar") }
            },
            dismissButton = { TextButton(onClick = { rewardToRedeem = null }) { Text("Batal") } }
        )
    }
}

@Composable
fun RewardCard(reward: RewardItem, canRedeem: Boolean, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (canRedeem) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(reward.emoji, style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reward.nama, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(reward.deskripsi, style = MaterialTheme.typography.bodySmall)
                Text("${reward.poinDibutuhkan} poin", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(8.dp)
            ) { Text("Tukar") }
        }
    }
}

@Composable
fun RedemptionHistoryCard(redemption: Redemption) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(redemption.reward, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(dateFormat.format(Date(redemption.tanggal)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("-${redemption.poinDipakai} poin", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
    }
}
