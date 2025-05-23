package com.example.limbus_client.presentation.ui.feature.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt

// Modelos de datos
data class FoodOption(
    val id: String,
    val name: String,
    val calories: Int,
    val imageUrl: String? = null,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0,
    val mealTypes: List<String> = emptyList() // Nuevo campo para tipos de comida
)

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val calories: Int,
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    val mealTypes: List<String> = emptyList() // Nuevo campo para tipos de comida
)

data class CookbookRecipe(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val prepTime: String,
    val difficulty: String,
    val isPersonal: Boolean = true,
    val mealTypes: List<String> = emptyList() // Nuevo campo para tipos de comida
)

data class PortionType(
    val name: String,
    val grams: Double,
    val description: String
)

data class FoodRegistration(
    val id: String = System.currentTimeMillis().toString(),
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

// Constantes actualizadas - solo 4 tipos de comida
val mealTypes = listOf("Desayuno", "Almuerzo", "Cena", "Snack")

enum class FoodTab { COOKBOOK, RECIPES, FOODS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    onBackClicked: () -> Unit,
    onFoodSelected: (String) -> Unit,
    onFoodRegistered: (FoodRegistration) -> Unit = {} // Callback para manejar el registro
) {
    // Estados
    var selectedMealTypeIndex by remember { mutableStateOf(0) }
    var isFabExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(FoodTab.FOODS) }

    // Lista de alimentos registrados
    var registeredFoods by remember { mutableStateOf<List<FoodRegistration>>(emptyList()) }

    // Estado para el modal de registro
    var showRegistrationModal by remember { mutableStateOf(false) }
    var selectedFoodForRegistration by remember { mutableStateOf<FoodOption?>(null) }

    // Función para agregar alimento a la lista
    val addFoodToList = { registration: FoodRegistration ->
        registeredFoods = registeredFoods + registration
        onFoodRegistered(registration)
    }

    // Función para eliminar alimento de la lista
    val removeFoodFromList = { foodId: String ->
        registeredFoods = registeredFoods.filter { it.id != foodId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = mealTypes[selectedMealTypeIndex])
                        if (registeredFoods.isNotEmpty()) {
                            Text(
                                text = "${registeredFoods.size} alimento(s) añadido(s)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = isFabExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        MealTypeFab(type = "Crear receta personalizada", onClick = {
                            onFoodSelected("Receta personalizada")
                            isFabExpanded = false
                        })
                        MealTypeFab(type = "Buscar alimento común", onClick = {
                            onFoodSelected("Alimento común")
                            isFabExpanded = false
                        })
                        MealTypeFab(type = "Escanear código de barras", onClick = {
                            onFoodSelected("Escanear código")
                            isFabExpanded = false
                        })
                        MealTypeFab(type = "Importar de API", onClick = {
                            onFoodSelected("Importar API")
                            isFabExpanded = false
                        })
                    }
                }
                FloatingActionButton(
                    onClick = { isFabExpanded = !isFabExpanded },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = if (isFabExpanded) "Cerrar opciones" else "Añadir alimento"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Selector de tipo de comida
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                mealTypes.forEachIndexed { index, type ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = mealTypes.size
                        ),
                        onClick = { selectedMealTypeIndex = index },
                        selected = index == selectedMealTypeIndex
                    ) {
                        Text(type)
                    }
                }
            }

            // Lista de alimentos registrados (si hay alguno)
            AnimatedVisibility(visible = registeredFoods.isNotEmpty()) {
                RegisteredFoodsList(
                    registeredFoods = registeredFoods,
                    onRemoveFood = removeFoodFromList,
                    mealType = mealTypes[selectedMealTypeIndex]
                )
            }

            // Barra de búsqueda
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { isSearchActive = false },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = {
                    Text(when(selectedTab) {
                        FoodTab.COOKBOOK -> "Buscar en tu libro de cocina"
                        FoodTab.RECIPES -> "Buscar recetas online"
                        FoodTab.FOODS -> "Buscar alimentos y productos"
                    })
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        "Resultados para '$searchQuery'",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    when(selectedTab) {
                        FoodTab.COOKBOOK -> Text("Buscando en recetas guardadas...", style = MaterialTheme.typography.bodyMedium)
                        FoodTab.RECIPES -> Text("Buscando en bases de datos de recetas...", style = MaterialTheme.typography.bodyMedium)
                        FoodTab.FOODS -> Text("Buscando alimentos y productos...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Pestañas
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                Tab(
                    selected = selectedTab == FoodTab.COOKBOOK,
                    onClick = { selectedTab = FoodTab.COOKBOOK },
                    text = { Text("LIBRO DE COCINA") }
                )
                Tab(
                    selected = selectedTab == FoodTab.RECIPES,
                    onClick = { selectedTab = FoodTab.RECIPES },
                    text = { Text("RECETAS") }
                )
                Tab(
                    selected = selectedTab == FoodTab.FOODS,
                    onClick = { selectedTab = FoodTab.FOODS },
                    text = { Text("ALIMENTOS") }
                )
            }

            // Contenido según la pestaña seleccionada
            when(selectedTab) {
                FoodTab.COOKBOOK -> CookbookContent(mealTypes[selectedMealTypeIndex])
                FoodTab.RECIPES -> RecipesContent(mealTypes[selectedMealTypeIndex])
                FoodTab.FOODS -> FoodsContent(
                    mealType = mealTypes[selectedMealTypeIndex],
                    onFoodClick = { food ->
                        selectedFoodForRegistration = food
                        showRegistrationModal = true
                    }
                )
            }
        }
    }

    // Modal de registro de alimento
    if (showRegistrationModal && selectedFoodForRegistration != null) {
        AddFoodRegistrationModal(
            food = selectedFoodForRegistration!!,
            mealType = mealTypes[selectedMealTypeIndex],
            onDismiss = {
                showRegistrationModal = false
                selectedFoodForRegistration = null
            },
            onConfirm = { registration ->
                addFoodToList(registration)
                showRegistrationModal = false
                selectedFoodForRegistration = null
            }
        )
    }
}

@Composable
fun RegisteredFoodsList(
    registeredFoods: List<FoodRegistration>,
    onRemoveFood: (String) -> Unit,
    mealType: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alimentos añadidos a $mealType",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${registeredFoods.size} item(s)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Resumen nutricional total
            val totalCalories = registeredFoods.sumOf { it.totalCalories }
            val totalProtein = registeredFoods.sumOf { it.totalProtein }
            val totalCarbs = registeredFoods.sumOf { it.totalCarbs }
            val totalFat = registeredFoods.sumOf { it.totalFat }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NutritionSummary("Cal", "${totalCalories}")
                NutritionSummary("Prot", "${String.format("%.1f", totalProtein)}g")
                NutritionSummary("Carb", "${String.format("%.1f", totalCarbs)}g")
                NutritionSummary("Gras", "${String.format("%.1f", totalFat)}g")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de alimentos individuales
            registeredFoods.forEach { registration ->
                RegisteredFoodItem(
                    registration = registration,
                    onRemove = { onRemoveFood(registration.id) }
                )
                if (registration != registeredFoods.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun NutritionSummary(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RegisteredFoodItem(
    registration: FoodRegistration,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji del alimento
            Text(
                text = getFoodEmoji(registration.food.name),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = registration.food.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${String.format("%.1f", registration.quantity)} ${registration.portionType.name.lowercase()} (${String.format("%.0f", registration.quantity * registration.portionType.grams)}g)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${registration.totalCalories} cal",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar alimento",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun MealTypeFab(type: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = type,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun CookbookContent(mealType: String) {
    val cookbookRecipes = getCookbookRecipes(mealType)

    if (cookbookRecipes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📖",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay recetas guardadas para $mealType",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cookbookRecipes) { recipe ->
                CookbookRecipeCard(recipe)
            }
        }
    }
}

@Composable
fun RecipesContent(mealType: String) {
    val recipes = getOnlineRecipes(mealType)

    if (recipes.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌐",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay recetas online para $mealType",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes) { recipe ->
                OnlineRecipeCard(recipe)
            }
        }
    }
}

@Composable
fun FoodsContent(
    mealType: String,
    onFoodClick: (FoodOption) -> Unit
) {
    val foods = getFoodOptions(mealType)

    if (foods.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🍽️",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay alimentos disponibles para $mealType",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foods) { food ->
                FoodItemCard(
                    food = food,
                    onClick = { onFoodClick(food) }
                )
            }
        }
    }
}

@Composable
fun FoodItemCard(
    food: FoodOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del alimento
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getFoodEmoji(food.name),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${food.calories} cal por 100g",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "P: ${food.protein}g | C: ${food.carbs}g | G: ${food.fat}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir alimento",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun CookbookRecipeCard(recipe: CookbookRecipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { /* Navegar a detalle */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍳",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                Text(
                    text = recipe.difficulty,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = "${recipe.calories} cal",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "⏱️ ${recipe.prepTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun OnlineRecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { /* Navegar a detalle */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍽️",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                IconButton(
                    onClick = { /* Toggle favorito */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = if (recipe.isFavorite) Color.Red else Color.White
                    )
                }

                if (recipe.rating > 0) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp).background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", recipe.rating),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = "${recipe.calories} cal",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "⏱️ ${recipe.readyInMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "👥 ${recipe.servings}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
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

    val portionTypes = listOf(
        PortionType("Gramos", 1.0, "g"),
        PortionType("Porción", 100.0, "porción estándar"),
        PortionType("Taza", 240.0, "1 taza"),
        PortionType("Cucharada", 15.0, "1 cucharada"),
        PortionType("Cucharadita", 5.0, "1 cucharadita"),
        PortionType("Pieza", 50.0, "1 pieza mediana"),
        PortionType("Rebanada", 30.0, "1 rebanada")
    )

    val selectedPortion = portionTypes[selectedPortionIndex]
    val totalGrams = quantity * selectedPortion.grams

    // Cálculos nutricionales
    val multiplier = totalGrams / 100.0
    val totalCalories = (food.calories * multiplier).roundToInt()
    val totalProtein = food.protein * multiplier
    val totalCarbs = food.carbs * multiplier
    val totalFat = food.fat * multiplier
    val totalFiber = food.fiber * multiplier

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Añadir a $mealType",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Información del alimento
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getFoodEmoji(food.name),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = food.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${food.calories} cal por 100g",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Control de cantidad
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (quantity > 0.1) quantity = (quantity - 0.1).coerceAtLeast(0.1) },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir cantidad",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    OutlinedTextField(
                        value = String.format("%.1f", quantity),
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let {
                                if (it > 0) quantity = it
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )

                    OutlinedButton(
                        onClick = { quantity += 0.1 },
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de tipo de porción
                Text(
                    text = "Tipo de porción",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.height(120.dp)
                ) {
                    itemsIndexed(portionTypes) { index, portion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPortionIndex = index }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.RadioButton(
                                selected = selectedPortionIndex == index,
                                onClick = { selectedPortionIndex = index }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = portion.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = portion.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Información nutricional calculada
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Información nutricional",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Total: ${String.format("%.0f", totalGrams)}g",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            NutritionInfo("Calorías", "${totalCalories}")
                            NutritionInfo("Proteína", "${String.format("%.1f", totalProtein)}g")
                            NutritionInfo("Carbohidratos", "${String.format("%.1f", totalCarbs)}g")
                            NutritionInfo("Grasas", "${String.format("%.1f", totalFat)}g")
                        }
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Añadir")
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionInfo(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// Funciones helper
fun getFoodEmoji(foodName: String): String {
    return when {
        foodName.contains("manzana", ignoreCase = true) -> "🍎"
        foodName.contains("banana", ignoreCase = true) || foodName.contains("plátano", ignoreCase = true) -> "🍌"
        foodName.contains("pan", ignoreCase = true) -> "🍞"
        foodName.contains("huevo", ignoreCase = true) -> "🥚"
        foodName.contains("pollo", ignoreCase = true) -> "🐔"
        foodName.contains("carne", ignoreCase = true) -> "🥩"
        foodName.contains("pescado", ignoreCase = true) -> "🐟"
        foodName.contains("leche", ignoreCase = true) -> "🥛"
        foodName.contains("queso", ignoreCase = true) -> "🧀"
        foodName.contains("yogur", ignoreCase = true) -> "🥛"
        foodName.contains("arroz", ignoreCase = true) -> "🍚"
        foodName.contains("pasta", ignoreCase = true) -> "🍝"
        foodName.contains("ensalada", ignoreCase = true) -> "🥗"
        foodName.contains("tomate", ignoreCase = true) -> "🍅"
        foodName.contains("zanahoria", ignoreCase = true) -> "🥕"
        foodName.contains("brócoli", ignoreCase = true) -> "🥦"
        foodName.contains("papa", ignoreCase = true) || foodName.contains("patata", ignoreCase = true) -> "🥔"
        else -> "🍽️"
    }
}

fun getFoodOptions(mealType: String): List<FoodOption> {
    return when (mealType) {
        "Desayuno" -> listOf(
            FoodOption("1", "Avena", 389, protein = 16.9, carbs = 66.3, fat = 6.9, fiber = 10.6, mealTypes = listOf("Desayuno")),
            FoodOption("2", "Huevos revueltos", 155, protein = 10.6, carbs = 1.6, fat = 11.1, fiber = 0.0, mealTypes = listOf("Desayuno")),
            FoodOption("3", "Pan integral", 247, protein = 13.0, carbs = 41.0, fat = 4.2, fiber = 7.0, mealTypes = listOf("Desayuno")),
            FoodOption("4", "Yogur griego", 59, protein = 10.0, carbs = 3.6, fat = 0.4, fiber = 0.0, mealTypes = listOf("Desayuno")),
            FoodOption("5", "Plátano", 89, protein = 1.1, carbs = 22.8, fat = 0.3, fiber = 2.6, mealTypes = listOf("Desayuno", "Snack"))
        )
        "Almuerzo" -> listOf(
            FoodOption("6", "Pechuga de pollo", 165, protein = 31.0, carbs = 0.0, fat = 3.6, fiber = 0.0, mealTypes = listOf("Almuerzo", "Cena")),
            FoodOption("7", "Arroz integral", 123, protein = 2.6, carbs = 23.0, fat = 1.0, fiber = 1.8, mealTypes = listOf("Almuerzo", "Cena")),
            FoodOption("8", "Brócoli", 34, protein = 2.8, carbs = 7.0, fat = 0.4, fiber = 2.6, mealTypes = listOf("Almuerzo", "Cena")),
            FoodOption("9", "Ensalada mixta", 20, protein = 1.4, carbs = 3.6, fat = 0.2, fiber = 1.8, mealTypes = listOf("Almuerzo", "Cena")),
            FoodOption("10", "Quinoa", 368, protein = 14.1, carbs = 64.2, fat = 6.1, fiber = 7.0, mealTypes = listOf("Almuerzo", "Cena"))
        )
        "Cena" -> listOf(
            FoodOption("11", "Salmón", 208, protein = 25.4, carbs = 0.0, fat = 12.4, fiber = 0.0, mealTypes = listOf("Cena")),
            FoodOption("12", "Verduras al vapor", 35, protein = 1.5, carbs = 7.0, fat = 0.3, fiber = 3.0, mealTypes = listOf("Cena")),
            FoodOption("13", "Batata", 86, protein = 1.6, carbs = 20.1, fat = 0.1, fiber = 3.0, mealTypes = listOf("Cena")),
            FoodOption("14", "Ensalada de espinacas", 23, protein = 2.9, carbs = 3.6, fat = 0.4, fiber = 2.2, mealTypes = listOf("Cena"))
        )
        "Snack" -> listOf(
            FoodOption("15", "Almendras", 579, protein = 21.2, carbs = 21.6, fat = 49.9, fiber = 12.5, mealTypes = listOf("Snack")),
            FoodOption("16", "Manzana", 52, protein = 0.3, carbs = 13.8, fat = 0.2, fiber = 2.4, mealTypes = listOf("Snack")),
            FoodOption("17", "Yogur natural", 61, protein = 3.5, carbs = 4.7, fat = 3.3, fiber = 0.0, mealTypes = listOf("Snack")),
            FoodOption("18", "Nueces", 654, protein = 15.2, carbs = 13.7, fat = 65.2, fiber = 6.7, mealTypes = listOf("Snack"))
        )
        else -> emptyList()
    }
}

fun getCookbookRecipes(mealType: String): List<CookbookRecipe> {
    return when (mealType) {
        "Desayuno" -> listOf(
            CookbookRecipe("1", "Pancakes de avena", null, 320, "15 min", "Fácil", mealTypes = listOf("Desayuno")),
            CookbookRecipe("2", "Smoothie verde", null, 180, "5 min", "Fácil", mealTypes = listOf("Desayuno"))
        )
        "Almuerzo" -> listOf(
            CookbookRecipe("3", "Ensalada César", null, 280, "20 min", "Medio", mealTypes = listOf("Almuerzo")),
            CookbookRecipe("4", "Pasta con vegetales", null, 420, "25 min", "Fácil", mealTypes = listOf("Almuerzo"))
        )
        "Cena" -> listOf(
            CookbookRecipe("5", "Salmón al horno", null, 350, "30 min", "Medio", mealTypes = listOf("Cena")),
            CookbookRecipe("6", "Pollo con verduras", null, 310, "35 min", "Fácil", mealTypes = listOf("Cena"))
        )
        else -> emptyList()
    }
}

fun getOnlineRecipes(mealType: String): List<Recipe> {
    return when (mealType) {
        "Desayuno" -> listOf(
            Recipe("1", "Tostadas francesas", null, 20, 2, 280, 4.5, false, listOf("Desayuno")),
            Recipe("2", "Bowl de açai", null, 10, 1, 320, 4.8, true, listOf("Desayuno"))
        )
        "Almuerzo" -> listOf(
            Recipe("3", "Tacos de pescado", null, 25, 4, 380, 4.2, false, listOf("Almuerzo")),
            Recipe("4", "Buddha bowl", null, 15, 2, 420, 4.6, true, listOf("Almuerzo"))
        )
        "Cena" -> listOf(
            Recipe("5", "Risotto de champiñones", null, 40, 4, 450, 4.3, false, listOf("Cena")),
            Recipe("6", "Curry de lentejas", null, 35, 6, 380, 4.7, true, listOf("Cena"))
        )
        else -> emptyList()
    }
}