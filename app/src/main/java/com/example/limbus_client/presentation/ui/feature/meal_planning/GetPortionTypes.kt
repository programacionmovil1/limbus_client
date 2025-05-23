package com.example.limbus_client.presentation.ui.feature.meal_planning
// Agregar estos imports adicionales al archivo existente
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// Datos adicionales para el modal
data class PortionType(
    val name: String,
    val grams: Double,
    val description: String
)

data class FoodRegistration(
    val food: FoodOption,
    val quantity: Double,
    val portionType: PortionType,
    val mealType: String,
    val totalCalories: Int,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
    val totalFiber: Double
)

// Función para obtener tipos de porción según el alimento
fun getPortionTypes(food: FoodOption): List<PortionType> {
    return when {
        food.name.contains("Huevo", ignoreCase = true) -> listOf(
            PortionType("Unidad", 50.0, "1 huevo mediano"),
            PortionType("100 gramos", 100.0, "Medida estándar"),
            PortionType("Clara", 33.0, "Solo la clara"),
            PortionType("Yema", 17.0, "Solo la yema")
        )
        food.name.contains("Manzana", ignoreCase = true) -> listOf(
            PortionType("Unidad mediana", 182.0, "1 manzana mediana"),
            PortionType("Unidad pequeña", 138.0, "1 manzana pequeña"),
            PortionType("Unidad grande", 223.0, "1 manzana grande"),
            PortionType("100 gramos", 100.0, "Medida estándar"),
            PortionType("Rebanada", 25.0, "1 rebanada")
        )
        food.name.contains("Plátano", ignoreCase = true) -> listOf(
            PortionType("Unidad mediana", 118.0, "1 plátano mediano"),
            PortionType("Unidad pequeña", 81.0, "1 plátano pequeño"),
            PortionType("Unidad grande", 136.0, "1 plátano grande"),
            PortionType("100 gramos", 100.0, "Medida estándar")
        )
        food.name.contains("Yogur", ignoreCase = true) -> listOf(
            PortionType("Envase individual", 125.0, "1 envase típico"),
            PortionType("Taza", 245.0, "1 taza (8 oz)"),
            PortionType("Media taza", 123.0, "1/2 taza"),
            PortionType("100 gramos", 100.0, "Medida estándar"),
            PortionType("Cucharada", 15.0, "1 cucharada")
        )
        food.name.contains("Pechuga", ignoreCase = true) || food.name.contains("Pollo", ignoreCase = true) -> listOf(
            PortionType("Pechuga mediana", 150.0, "1 pechuga sin hueso"),
            PortionType("Pechuga pequeña", 100.0, "1 pechuga pequeña"),
            PortionType("Porción", 85.0, "Porción estándar"),
            PortionType("100 gramos", 100.0, "Medida estándar")
        )
        food.name.contains("Arroz", ignoreCase = true) -> listOf(
            PortionType("Taza cocido", 195.0, "1 taza de arroz cocido"),
            PortionType("Media taza", 98.0, "1/2 taza cocido"),
            PortionType("Porción", 150.0, "Porción estándar"),
            PortionType("100 gramos", 100.0, "Medida estándar")
        )
        food.name.contains("Pan", ignoreCase = true) || food.name.contains("Tostada", ignoreCase = true) -> listOf(
            PortionType("Rebanada", 25.0, "1 rebanada estándar"),
            PortionType("Rebanada gruesa", 35.0, "1 rebanada gruesa"),
            PortionType("Media rebanada", 12.5, "1/2 rebanada"),
            PortionType("100 gramos", 100.0, "Medida estándar")
        )
        else -> listOf(
            PortionType("100 gramos", 100.0, "Medida estándar"),
            PortionType("Porción", 85.0, "Porción típica"),
            PortionType("Media porción", 42.5, "Media porción"),
            PortionType("Taza", 120.0, "1 taza aproximada")
        )
    }
}

@Composable
fun AddFoodRegistrationModal(
    food: FoodOption,
    mealType: String,
    onDismiss: () -> Unit,
    onConfirm: (FoodRegistration) -> Unit
) {
    var quantity by remember { mutableDoubleStateOf(1.0) }
    var selectedPortionIndex by remember { mutableIntStateOf(0) }
    var customQuantity by remember { mutableStateOf("1.0") }

    val portionTypes = remember { getPortionTypes(food) }
    val selectedPortion = portionTypes[selectedPortionIndex]

    // Cálculos nutricionales
    val multiplier = (quantity * selectedPortion.grams) / 100.0
    val totalCalories = (food.calories * multiplier).roundToInt()
    val totalProtein = food.protein * multiplier
    val totalCarbs = food.carbs * multiplier
    val totalFat = food.fat * multiplier
    val totalFiber = food.fiber * multiplier

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header con imagen y nombre del alimento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                food.name.contains("Manzana", ignoreCase = true) -> "🍎"
                                food.name.contains("Plátano", ignoreCase = true) -> "🍌"
                                food.name.contains("Huevo", ignoreCase = true) -> "🥚"
                                food.name.contains("Yogur", ignoreCase = true) -> "🥛"
                                food.name.contains("Pollo", ignoreCase = true) -> "🍗"
                                food.name.contains("Salmón", ignoreCase = true) -> "🐟"
                                food.name.contains("Pan", ignoreCase = true) -> "🍞"
                                food.name.contains("Arroz", ignoreCase = true) -> "🍚"
                                else -> "🍽️"
                            },
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = mealType,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Selector de cantidad
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón para decrementar
                    OutlinedButton(
                        onClick = {
                            if (quantity > 0.25) {
                                quantity = (quantity - 0.25).coerceAtLeast(0.25)
                                customQuantity = String.format("%.2f", quantity).trimEnd('0').trimEnd('.')
                            }
                        },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir cantidad",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Campo de cantidad personalizada
                    OutlinedTextField(
                        value = customQuantity,
                        onValueChange = { newValue ->
                            customQuantity = newValue
                            newValue.toDoubleOrNull()?.let { value ->
                                if (value > 0) quantity = value
                            }
                        },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                    )

                    // Botón para incrementar
                    OutlinedButton(
                        onClick = {
                            quantity += 0.25
                            customQuantity = String.format("%.2f", quantity).trimEnd('0').trimEnd('.')
                        },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Selector de tipo de porción
                Text(
                    text = "Tipo de porción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(portionTypes) { index, portionType ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPortionIndex = index },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedPortionIndex == index)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            ),
                            border = if (selectedPortionIndex == index)
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            else
                                BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = portionType.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = if (selectedPortionIndex == index)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${portionType.grams}g - ${portionType.description}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (selectedPortionIndex == index)
                                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Resumen nutricional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Información nutricional",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NutritionalInfo(
                                label = "Calorías",
                                value = "$totalCalories",
                                unit = "kcal",
                                color = MaterialTheme.colorScheme.primary
                            )
                            NutritionalInfo(
                                label = "Proteínas",
                                value = String.format("%.1f", totalProtein),
                                unit = "g",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NutritionalInfo(
                                label = "Carbohidratos",
                                value = String.format("%.1f", totalCarbs),
                                unit = "g",
                                color = MaterialTheme.colorScheme.secondary
                            )
                            NutritionalInfo(
                                label = "Grasas",
                                value = String.format("%.1f", totalFat),
                                unit = "g",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (totalFiber > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                NutritionalInfo(
                                    label = "Fibra",
                                    value = String.format("%.1f", totalFiber),
                                    unit = "g",
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Total: ${String.format("%.0f", quantity * selectedPortion.grams)}g",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val registration = FoodRegistration(
                                food = food,
                                quantity = quantity,
                                portionType = selectedPortion,
                                mealType = mealType,
                                totalCalories = totalCalories,
                                totalProtein = totalProtein,
                                totalCarbs = totalCarbs,
                                totalFat = totalFat,
                                totalFiber = totalFiber
                            )
                            onConfirm(registration)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Agregar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionalInfo(
    label: String,
    value: String,
    unit: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value$unit",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}