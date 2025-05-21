package com.example.limbus_client2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.limbus_client.presentation.ui.feature.auth.Formulario_1
import com.example.limbus_client.presentation.ui.feature.auth.Formulario_2
import com.example.limbus_client.presentation.ui.feature.auth.Formulario_3
import com.example.limbus_client.presentation.ui.feature.auth.Formulario_4
import com.example.limbus_client.presentation.ui.feature.auth.Formulario_5
import com.example.limbus_client.presentation.ui.feature.auth.LoginScreen
import com.example.limbus_client.presentation.ui.feature.auth.Presentacion_1
import com.example.limbus_client.presentation.ui.feature.auth.Presentacion_2
import com.example.limbus_client.presentation.ui.feature.auth.Presentacion_3
import com.example.limbus_client.presentation.ui.feature.auth.Presentacion_4
import com.example.limbus_client.presentation.ui.feature.auth.RegistrationCompletedScreen
import com.example.limbus_client.presentation.ui.feature.auth.WelcomeScreen
import com.example.limbus_client2.ui.theme.Limbus_client2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Limbus_client2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Usar un state hoisting para controlar la navegación
                    val currentScreenState = remember { mutableStateOf("welcome") }

                    when (currentScreenState.value) {
                        "welcome" -> {
                            WelcomeScreen(
                                onStartClicked = {
                                    currentScreenState.value = "presentacion1"
                                    println("Navegando a presentacion1")
                                },
                                onLoginClicked = {
                                    // Si el usuario ya tiene cuenta, ir a la pantalla de login
                                    currentScreenState.value = "login"
                                    println("Navegando a login")
                                }
                            )
                        }
                        "presentacion1" -> {
                            Presentacion_1(
                                onStartClicked = {
                                    // Navegar a la segunda pantalla de presentación
                                    currentScreenState.value = "presentacion2"
                                    println("Navegando a presentacion2")
                                },
                                onLoginClicked = {
                                    // Si el usuario hace clic en Saltar, ir directamente a la pantalla principal
                                    currentScreenState.value = "main"
                                    println("Saltar a pantalla principal")
                                }
                            )
                        }
                        "presentacion2" -> {
                            Presentacion_2(
                                onNextClicked = {
                                    // Navegar a la tercera pantalla de presentación
                                    currentScreenState.value = "presentacion3"
                                    println("Navegando a presentacion3")
                                },
                                onSkipClicked = {
                                    // Si el usuario hace clic en Saltar, ir directamente a la pantalla principal
                                    currentScreenState.value = "main"
                                    println("Saltar a pantalla principal")
                                }
                            )
                        }
                        "presentacion3" -> {
                            Presentacion_3(
                                onFinishClicked = {
                                    // Navegar a la cuarta pantalla de presentación
                                    currentScreenState.value = "presentacion4"
                                    println("Navegando a presentacion4")
                                },
                                onSkipClicked = {
                                    // Si el usuario hace clic en Saltar, ir directamente a la pantalla principal
                                    currentScreenState.value = "main"
                                    println("Saltar a pantalla principal")
                                }
                            )
                        }
                        "presentacion4" -> {
                            Presentacion_4(
                                onFinishClicked = {
                                    // Al terminar el onboarding, ir a la pantalla de registro/login
                                    currentScreenState.value = "login"
                                    println("Navegando a login")
                                },
                                onSkipClicked = {
                                    // Si el usuario hace clic en Saltar, ir directamente a la pantalla principal
                                    currentScreenState.value = "main"
                                    println("Saltar a pantalla principal")
                                }
                            )
                        }
                        "login" -> {
                            LoginScreen(
                                onLoginClicked = {
                                    // Ahora el login nos lleva al formulario de registro
                                    currentScreenState.value = "formulario1"
                                    println("Navegando a formulario de registro")
                                },
                                onRegisterClicked = {
                                    // El botón de registro también lleva al formulario
                                    currentScreenState.value = "formulario1"
                                    println("Navegando a formulario de registro")
                                },
                                onForgotPasswordClicked = {
                                    // Navegar a la pantalla de recuperación de contraseña
                                    currentScreenState.value = "forgot_password"
                                    println("Navegando a recuperación de contraseña")
                                }
                            )
                        }
                        "formulario1" -> {
                            Formulario_1(
                                onContinueClicked = {
                                    // Ahora al completar el formulario 1, vamos al formulario 2
                                    currentScreenState.value = "formulario2"
                                    println("Navegando a formulario 2")
                                },
                                onBackClicked = {
                                    // Volver a la pantalla anterior
                                    currentScreenState.value = "login"
                                    println("Volviendo a la pantalla de login")
                                },
                                onLoginClicked = {
                                    // Si ya tiene cuenta, volver a login
                                    currentScreenState.value = "login"
                                    println("Redirigiendo a login")
                                }
                            )
                        }
                        "formulario2" -> {
                            Formulario_2(
                                onContinueClicked = {
                                    // Al completar el formulario 2, vamos al formulario 3
                                    currentScreenState.value = "formulario3"
                                    println("Navegando a formulario 3")
                                },
                                onBackClicked = {
                                    // Volver al formulario 1
                                    currentScreenState.value = "formulario1"
                                    println("Volviendo al formulario 1")
                                },
                                onLoginClicked = {
                                    // Si ya tiene cuenta, volver a login
                                    currentScreenState.value = "login"
                                    println("Redirigiendo a login")
                                }
                            )
                        }
                        "formulario3" -> {
                            Formulario_3(
                                onFinishClicked = {
                                    // Al completar el formulario 3, vamos al formulario 4
                                    currentScreenState.value = "formulario4"
                                    println("Navegando a formulario 4")
                                },
                                onBackClicked = {
                                    // Volver al formulario 2
                                    currentScreenState.value = "formulario2"
                                    println("Volviendo al formulario 2")
                                },
                                onLoginClicked = {
                                    // Si ya tiene cuenta, volver a login
                                    currentScreenState.value = "login"
                                    println("Redirigiendo a login")
                                }
                            )
                        }
                        "formulario4" -> {
                            Formulario_4(
                                onFinishClicked = {
                                    // Modificado: Al completar el formulario 4, vamos al formulario 5
                                    currentScreenState.value = "formulario5"
                                    println("Navegando a formulario 5")
                                },
                                onBackClicked = {
                                    // Volver al formulario 3
                                    currentScreenState.value = "formulario3"
                                    println("Volviendo al formulario 3")
                                },
                                onLoginClicked = {
                                    // Si ya tiene cuenta, volver a login
                                    currentScreenState.value = "login"
                                    println("Redirigiendo a login")
                                }
                            )
                        }
                        "formulario5" -> {
                            // Formulario 5 para preferencias de la aplicación
                            Formulario_5(
                                onCompleteRegistrationClicked = {
                                    // MODIFICADO: Al completar el formulario 5, vamos a la pantalla de registro completado
                                    currentScreenState.value = "registration_completed"
                                    println("Registro completado, navegando a pantalla de confirmación")
                                },
                                onBackClicked = {
                                    // Volver al formulario 4
                                    currentScreenState.value = "formulario4"
                                    println("Volviendo al formulario 4")
                                }
                            )
                        }
                        "registration_completed" -> {
                            // NUEVO: Pantalla de registro completado
                            RegistrationCompletedScreen(
                                onContinueClicked = {
                                    // Al hacer clic en continuar, vamos a la pantalla principal
                                    currentScreenState.value = "main"
                                    println("Navegando a pantalla principal")
                                }
                            )
                        }
                        "register" -> {
                            // Esta ruta ya no se usaría, pero la dejamos por compatibilidad
                            // Redirigimos a nuestro nuevo formulario
                            currentScreenState.value = "formulario1"
                        }
                        "forgot_password" -> {
                            // Aquí iría tu pantalla de recuperación de contraseña
                            // Por ahora, mostramos algo simple
                            Text(
                                text = "Recuperación de Contraseña",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        "main" -> {
                            // Aquí iría tu pantalla principal de la aplicación
                            // Por ahora, mostramos algo simple para demostrar que llegamos a esta pantalla
                            Text(
                                text = "Pantalla Principal",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}