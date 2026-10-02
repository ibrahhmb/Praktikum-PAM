package com.example.pertemuan3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 1. Reusable Composable: ProfileHeader
 * Menampilkan avatar foto profil berbentuk circular, badge status aktif,
 * nama pengguna, serta role/status dengan desain modern dan minimalis.
 */
@Composable
fun ProfileHeader(
    name: String,
    role: String,
    avatarPainter: Painter,
    modifier: Modifier = Modifier,
    isOnline: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box pembungkus foto profil circular dengan border dan badge status
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Lingkaran foto profil
            Image(
                painter = avatarPainter,
                contentDescription = "Foto Profil $name",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .border(
                        BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                        CircleShape
                    )
            )

            // Badge status online/aktif di sudut kanan bawah avatar
            if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .padding(3.dp)
                        .background(Color(0xFF22C55E), CircleShape) // Warna hijau aktif
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Nama Pengguna
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Badge / Tag Role
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 1.dp
        ) {
            Text(
                text = role,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * 2. Reusable Composable: InfoItem
 * Menampilkan baris informasi yang terdiri dari Icon berlatar belakang rounded,
 * Label deskripsi, dan Nilai informasi (Email, Telepon, Lokasi, dsb).
 */
@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    iconContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    actionIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onClick() }
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                } else {
                    Modifier.padding(vertical = 8.dp)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box wadah icon bernuansa modern & minimalis
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconContainerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Kolom teks label dan nilai info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Action icon opsional (misal: panah, copy, atau tombol aksi)
        if (actionIcon != null) {
            Icon(
                imageVector = actionIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * 3. Reusable Composable: ProfileCard
 * Container Card serbaguna dengan sudut rounded dan elevasi lembut
 * untuk mengelompokkan konten profil seperti Bio, Info Kontak, dan Statistik.
 */
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    headerIcon: ImageVector? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header opsional pada kartu
            if (title != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    if (headerIcon != null) {
                        Icon(
                            imageVector = headerIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Isi konten kartu
            content()
        }
    }
}
