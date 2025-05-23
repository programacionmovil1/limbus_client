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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

// Modelos de datos mejorados
data class FoodCategory(
    val name: String,
    val icon: Int
)

data class FoodOption(
    val id: String,
    val name: String,
    val calories: Int,
    val imageUrl: String? = null,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0
)

data class Recipe(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val calories: Int,
    val rating: Double = 0.0,
    val isFavorite: Boolean = false
)

data class CookbookRecipe(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val prepTime: String,
    val difficulty: String,
    val isPersonal: Boolean = true
)

// Lista de categorías de comidas
val mealTypes = listOf(
    "Desayuno",
    "Almuerzo",
    "Cena",
    "Snack"
)

// Pestañas disponibles
enum class FoodTab {
    COOKBOOK, RECIPES, FOODS
}

// APIs de recetas con información detallada
val recipeApis = listOf(
    "Spoonacular API - Más de 5000 recetas con imágenes HD y análisis nutricional completo",
    "Edamam Recipe API - Recetas con certificación nutricional y filtros dietéticos",
    "TheMealDB - API gratuita con 300+ recetas internacionales e imágenes",
    "Tasty API (RapidAPI) - Recetas de BuzzFeed con videos paso a paso",
    "Recipe Puppy API - Búsqueda simple por ingredientes con imágenes",
    "Forkify API - Recetas curadas con imágenes de alta calidad",
    "BigOven API - 350,000+ recetas profesionales con fotos",
    "Yummly API - Recomendaciones personalizadas con análisis nutricional"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    onBackClicked: () -> Unit,
    onFoodSelected: (String) -> Unit
) {
    // Estados existentes
    var selectedMealTypeIndex by remember { mutableStateOf(0) }
    var isFabExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    // Nuevo estado para las pestañas
    var selectedTab by remember { mutableStateOf(FoodTab.COOKBOOK) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = mealTypes[selectedMealTypeIndex]) },
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

            // Barra de búsqueda mejorada
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
                // Sugerencias de búsqueda según la pestaña
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        "Resultados para '$searchQuery'",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Aquí podrías mostrar sugerencias específicas por pestaña
                    when(selectedTab) {
                        FoodTab.COOKBOOK -> Text("Buscando en recetas guardadas...", style = MaterialTheme.typography.bodyMedium)
                        FoodTab.RECIPES -> Text("Buscando en bases de datos de recetas...", style = MaterialTheme.typography.bodyMedium)
                        FoodTab.FOODS -> Text("Buscando alimentos y productos...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Pestañas mejoradas
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
                FoodTab.FOODS -> FoodsContent(mealTypes[selectedMealTypeIndex])
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

// Contenido del Libro de Cocina (recetas guardadas/personales)
@Composable
fun CookbookContent(mealType: String) {
    val cookbookRecipes = getCookbookRecipes(mealType)

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

// Contenido de Recetas (APIs externas)
@Composable
fun RecipesContent(mealType: String) {
    val recipes = getOnlineRecipes(mealType)

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

// Contenido de Alimentos
@Composable
fun FoodsContent(mealType: String) {
    val foods = getFoodOptions(mealType)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(foods) { food ->
            FoodItemCard(food)
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
                // Placeholder para imagen - aquí usarías AsyncImage con Coil
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

                // Badge de dificultad
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

                // Calorías
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
                // Placeholder para imagen - aquí usarías AsyncImage con Coil
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

                // Botón favorito
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

                // Rating
                if (recipe.rating > 0) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", recipe.rating),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // Calorías
                Text(
                    text = "${recipe.calories} cal",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
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
                        text = "🍽️ ${recipe.servings} porciones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(food: FoodOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Añadir alimento */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder para imagen del alimento
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍎",
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
                // Información nutricional adicional
                Text(
                    text = "P: ${food.protein}g | C: ${food.carbs}g | G: ${food.fat}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = { /* Añadir */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir alimento"
                )
            }
        }
    }
}

// Funciones para obtener datos (simuladas - aquí conectarías con APIs reales)
fun getCookbookRecipes(mealType: String): List<CookbookRecipe> {
    return when (mealType) {
        "Desayuno" -> listOf(
            CookbookRecipe("1", "Magdalenas de Avena Caseras", null, 180, "25 min", "Fácil"),
            CookbookRecipe("2", "Pancakes Proteicos", null, 210, "15 min", "Fácil"),
            CookbookRecipe("3", "Bowl de Açaí", null, 320, "10 min", "Muy Fácil")
        )
        "Almuerzo" -> listOf(
            CookbookRecipe("4", "Ensalada César Premium", null, 320, "20 min", "Fácil"),
            CookbookRecipe("5", "Pollo Teriyaki", null, 380, "30 min", "Medio"),
            CookbookRecipe("6", "Wrap Mediterráneo", null, 310, "15 min", "Fácil")
        )
        "Cena" -> listOf(
            CookbookRecipe("7", "Salmón Glaseado", null, 350, "25 min", "Medio"),
            CookbookRecipe("8", "Risotto de Champiñones", null, 420, "35 min", "Difícil"),
            CookbookRecipe("9", "Tacos de Pescado", null, 280, "20 min", "Fácil")
        )
        else -> listOf(
            CookbookRecipe("10", "Energy Balls", null, 150, "10 min", "Muy Fácil"),
            CookbookRecipe("11", "Smoothie Verde", null, 180, "5 min", "Muy Fácil")
        )
    }
}

fun getOnlineRecipes(mealType: String): List<Recipe> {
    return when (mealType) {
        "Desayuno" -> listOf(
            Recipe("r1", "Avocado Toast Gourmet", null, 15, 2, 280, 4.8, false),
            Recipe("r2", "French Toast Stuffed", null, 20, 4, 420, 4.6, true),
            Recipe("r3", "Chia Pudding Parfait", null, 10, 2, 250, 4.7, false),
            Recipe("r4", "Breakfast Quinoa Bowl", null, 25, 3, 340, 4.5, false)
        )
        "Almuerzo" -> listOf(
            Recipe("r5", "Buddha Bowl Mediterráneo", null, 30, 2, 450, 4.9, true),
            Recipe("r6", "Pasta Pesto con Pollo", null, 25, 4, 520, 4.4, false),
            Recipe("r7", "Quinoa Stuffed Bell Peppers", null, 35, 4, 380, 4.6, false),
            Recipe("r8", "Asian Fusion Salad", null, 20, 2, 320, 4.3, false)
        )
        "Cena" -> listOf(
            Recipe("r9", "Lemon Herb Salmon", null, 30, 4, 480, 4.8, true),
            Recipe("r10", "Mushroom Risotto Creamy", null, 40, 6, 420, 4.5, false),
            Recipe("r11", "Thai Curry Coconut", null, 35, 4, 380, 4.7, false),
            Recipe("r12", "Stuffed Zucchini Boats", null, 45, 4, 290, 4.4, false)
        )
        else -> listOf(
            Recipe("r13", "Protein Smoothie Bowl", null, 10, 1, 280, 4.6, false),
            Recipe("r14", "Dark Chocolate Truffles", null, 60, 12, 150, 4.9, true),
            Recipe("r15", "Homemade Granola Bars", null, 30, 8, 200, 4.3, false)
        )
    }
}
fun getFoodOptions(mealType: String): List<FoodOption> {
    return when (mealType) {
        "Desayuno" -> listOf(
            FoodOption("f1", "Avena integral", 389, null, 16.9, 66.3, 6.9, 10.6),
            FoodOption("f2", "Plátano maduro", 89, null, 1.1, 22.8, 0.3, 2.6),
            FoodOption("f3", "Yogur griego natural", 59, null, 10.0, 3.6, 0.4, 0.0),
            FoodOption("f4", "Huevos enteros", 155, null, 13.0, 1.1, 11.0, 0.0),
            FoodOption("f5", "Leche de almendras", 17, null, 0.6, 0.6, 1.1, 0.5),
            FoodOption("f6", "Tostada integral", 247, null, 13.0, 41.0, 4.2, 7.0),
            FoodOption("f7", "Mantequilla de maní", 588, null, 25.8, 20.0, 50.4, 8.5),
            FoodOption("f8", "Café negro", 2, null, 0.3, 0.0, 0.0, 0.0),
            FoodOption("f9", "Miel natural", 304, null, 0.3, 82.4, 0.0, 0.2),
            FoodOption("f10", "Granola casera", 471, null, 10.1, 68.0, 20.0, 8.0)
        )
        "Almuerzo" -> listOf(
            FoodOption("f11", "Pechuga de pollo", 165, null, 31.0, 0.0, 3.6, 0.0),
            FoodOption("f12", "Arroz integral", 123, null, 2.6, 23.0, 1.0, 1.8),
            FoodOption("f13", "Brócoli fresco", 34, null, 2.8, 7.0, 0.4, 2.6),
            FoodOption("f14", "Aguacate", 160, null, 2.0, 8.5, 14.7, 6.7),
            FoodOption("f15", "Quinoa cocida", 120, null, 4.4, 21.3, 1.9, 2.8),
            FoodOption("f16", "Ensalada mixta", 20, null, 1.4, 4.0, 0.2, 2.0),
            FoodOption("f17", "Aceite de oliva", 884, null, 0.0, 0.0, 100.0, 0.0),
            FoodOption("f18", "Tomate cherry", 18, null, 0.9, 3.9, 0.2, 1.2),
            FoodOption("f19", "Pasta integral", 124, null, 5.0, 25.0, 1.1, 3.2),
            FoodOption("f20", "Pavo en rebanadas", 135, null, 25.0, 0.0, 3.2, 0.0)
        )
        "Cena" -> listOf(
            FoodOption("f21", "Salmón fresco", 208, null, 25.4, 0.0, 12.4, 0.0),
            FoodOption("f22", "Batata dulce", 86, null, 1.6, 20.1, 0.1, 3.0),
            FoodOption("f23", "Espinacas frescas", 23, null, 2.9, 3.6, 0.4, 2.2),
            FoodOption("f24", "Tofu firme", 76, null, 8.1, 1.9, 4.8, 0.3),
            FoodOption("f25", "Champiñones", 22, null, 3.1, 3.3, 0.3, 1.0),
            FoodOption("f26", "Carne de res magra", 250, null, 26.1, 0.0, 15.0, 0.0),
            FoodOption("f27", "Calabacín", 17, null, 1.2, 3.1, 0.3, 1.0),
            FoodOption("f28", "Pimientos rojos", 31, null, 1.0, 7.0, 0.3, 2.5),
            FoodOption("f29", "Coliflor", 25, null, 1.9, 5.0, 0.3, 2.0),
            FoodOption("f30", "Lentejas cocidas", 116, null, 9.0, 20.0, 0.4, 7.9)
        )
        else -> listOf(
            FoodOption("f31", "Almendras naturales", 579, null, 21.2, 21.6, 49.9, 12.5),
            FoodOption("f32", "Manzana roja", 52, null, 0.3, 13.8, 0.2, 2.4),
            FoodOption("f33", "Yogur con frutos secos", 150, null, 8.0, 12.0, 8.0, 2.0),
            FoodOption("f34", "Mix de frutos secos", 607, null, 20.0, 20.0, 54.0, 10.0),
            FoodOption("f35", "Chocolate negro 70%", 546, null, 7.8, 45.9, 31.3, 10.9),
            FoodOption("f36", "Zanahoria baby", 41, null, 0.9, 9.6, 0.2, 2.8),
            FoodOption("f37", "Hummus tradicional", 166, null, 8.0, 14.3, 9.6, 6.0),
            FoodOption("f38", "Crackers integrales", 395, null, 11.0, 67.0, 11.0, 10.0),
            FoodOption("f39", "Batido de proteínas", 120, null, 24.0, 3.0, 1.5, 1.0),
            FoodOption("f40", "Té verde", 1, null, 0.0, 0.0, 0.0, 0.0)
        )
    }
}