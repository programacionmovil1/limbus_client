package com.example.limbus_client.presentation.ui.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.limbus_client2.R

@Composable
fun Presentacion_2(
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Main Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Removed the status indicator/timestamp that was here

                Spacer(modifier = Modifier.height(16.dp))

                // Food plate image
                Image(
                    painter = painterResource(id = R.drawable.imagen2),
                    contentDescription = "Nutrition Plate",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Progress indicators - 3 bars
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = 1f,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp),
                        color = Color(0xFF3F51B5) // Blue indicator for completed
                    )
                    LinearProgressIndicator(
                        progress = 1f,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp),
                        color = Color(0xFF3F51B5) // Blue indicator for current
                    )
                    LinearProgressIndicator(
                        progress = 0f,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp),
                        color = Color.LightGray // Gray for upcoming
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title text
                Text(
                    text = "Adaptado a ti",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description text
                Text(
                    text = "Recibe planes y consejos nutricionales diseñados específicamente para tus metas y condición de salud.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onSkipClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Saltar")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onNextClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F51B5) // Blue button
                        ),
                        modifier = Modifier.width(120.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Siguiente")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}