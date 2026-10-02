package com.example.pertemuan3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pertemuan3.components.InfoItem
import com.example.pertemuan3.components.ProfileCard
import com.example.pertemuan3.components.ProfileHeader
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pertemuan3.shared.generated.resources.Res
import pertemuan3.shared.generated.resources.profile_photo

/**
 * Halaman Utama: My Profile App
 *
 * Menerapkan layout Jetpack Compose:
 * - Column, Row, Box, Card, Text, Button, Image, Icon
 * Serta menggunakan 3 Composable Reusable:
 * 1. ProfileHeader
 * 2. InfoItem
 * 3. ProfileCard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    // State data profil pengguna (dapat diedit secara interaktif)
    var name by remember { mutableStateOf("Ibrahim Budi Satria") }
    var role by remember { mutableStateOf("Mahasiswa Teknik Informatika ITERA") }
    var bio by remember {
        mutableStateOf(
            "Mahasiswa Teknik Informatika ITERA Semester 7. " +
            "Asal dari Jakarta Timur. Saya memiliki semangat tinggi dan antusias " +
            "akan hal baru."
        )
    }
    var email by remember { mutableStateOf("ibrahim.123140097@student.itera.ac.id") }
    var phone by remember { mutableStateOf("+62 812-1306-9669") }
    var location by remember { mutableStateOf("Pasarrebo, Jakarta, ID") }

    // State dialog edit profil & snackbar notifikasi
    var showEditDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profil",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        // Box utama sebagai latar belakang halaman dengan gradien halus
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        )
                    )
                )
        ) {
            // Column scrollable yang menampung seluruh komponen profil
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. REUSABLE COMPOSABLE: ProfileHeader (Header circular foto & nama)
                ProfileHeader(
                    name = name,
                    role = role,
                    avatarPainter = painterResource(Res.drawable.profile_photo),
                    isOnline = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Baris Statistik Profil Singkat (Menggunakan Row, Card, Box, Text)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = "3",
                        label = "Proyek"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = "3.21",
                        label = "IPK"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        value = "Sem 7",
                        label = "Semester"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2. REUSABLE COMPOSABLE: ProfileCard untuk Deskripsi/Bio Singkat
                ProfileCard(
                    title = "Bio / Tentang Saya",
                    headerIcon = Icons.Default.Person
                ) {
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. REUSABLE COMPOSABLE: ProfileCard untuk List Informasi Kontak
                ProfileCard(
                    title = "Informasi Kontak",
                    headerIcon = Icons.Default.Info
                ) {
                    // Item Informasi: Email
                    InfoItem(
                        icon = Icons.Default.Email,
                        label = "Alamat Email",
                        value = email,
                        iconTint = Color(0xFF2563EB),
                        iconContainerColor = Color(0xFF2563EB).copy(alpha = 0.12f),
                        actionIcon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Email: $email")
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )

                    // Item Informasi: Phone
                    InfoItem(
                        icon = Icons.Default.Phone,
                        label = "Nomor Telepon / WhatsApp",
                        value = phone,
                        iconTint = Color(0xFF16A34A),
                        iconContainerColor = Color(0xFF16A34A).copy(alpha = 0.12f),
                        actionIcon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Menghubungi: $phone")
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    )

                    // Item Informasi: Location
                    InfoItem(
                        icon = Icons.Default.LocationOn,
                        label = "Lokasi Domisili",
                        value = location,
                        iconTint = Color(0xFFEA580C),
                        iconContainerColor = Color(0xFFEA580C).copy(alpha = 0.12f),
                        actionIcon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Lokasi: $location")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Baris Tombol Aksi (Menggunakan Row, Button, OutlinedButton, Icon, Text)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tombol Utama: Edit Profil
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profil",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Profil",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Tombol Sekunder: Bagikan
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Tautan profil berhasil disalin!")
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan Profil",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bagikan",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }

    // Dialog Interaktif untuk Edit Data Profil
    if (showEditDialog) {
        EditProfileDialog(
            currentName = name,
            currentRole = role,
            currentBio = bio,
            currentEmail = email,
            currentPhone = phone,
            currentLocation = location,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newRole, newBio, newEmail, newPhone, newLocation ->
                name = newName
                role = newRole
                bio = newBio
                email = newEmail
                phone = newPhone
                location = newLocation
                showEditDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Profil berhasil diperbarui!")
                }
            }
        )
    }
}

/**
 * Kartu mini statistik profil
 */
@Composable
private fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Dialog Form untuk mengedit informasi profil
 */
@Composable
private fun EditProfileDialog(
    currentName: String,
    currentRole: String,
    currentBio: String,
    currentEmail: String,
    currentPhone: String,
    currentLocation: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String) -> Unit
) {
    var editName by remember { mutableStateOf(currentName) }
    var editRole by remember { mutableStateOf(currentRole) }
    var editBio by remember { mutableStateOf(currentBio) }
    var editEmail by remember { mutableStateOf(currentEmail) }
    var editPhone by remember { mutableStateOf(currentPhone) }
    var editLocation by remember { mutableStateOf(currentLocation) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profil",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Nama Lengkap") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editRole,
                    onValueChange = { editRole = it },
                    label = { Text("Role / Gelar") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editBio,
                    onValueChange = { editBio = it },
                    label = { Text("Bio / Deskripsi") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editEmail,
                    onValueChange = { editEmail = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editPhone,
                    onValueChange = { editPhone = it },
                    label = { Text("Nomor Telepon") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editLocation,
                    onValueChange = { editLocation = it },
                    label = { Text("Lokasi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(editName, editRole, editBio, editEmail, editPhone, editLocation)
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
@Preview
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}
