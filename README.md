# Sistema de Registro de Participantes de Natación
**EPN – ESFOT | Programación Orientada a Objetos**

---

## Estructura del proyecto

```
natacion/
├── pom.xml                            ← Dependencias Maven (JavaFX + MySQL)
├── sql/
│   └── natacion_db.sql                ← Script SQL (BD + tabla + datos de prueba)
└── src/main/
    ├── java/
    │   ├── Main.java                  ← Punto de entrada JavaFX
    │   ├── modelo/
    │   │   └── Participante.java      ← Clase modelo (JavaBean)
    │   ├── dao/
    │   │   ├── ParticipanteDAO.java   ← Interfaz CRUD
    │   │   ├── ParticipanteDAOImpl.java ← Implementación JDBC
    │   │   └── UsuarioDAO.java        ← Autenticación Login
    │   ├── util/
    │   │   └── Conexion.java          ← Singleton – conexión a MySQL
    │   └── controlador/
    │       ├── LoginController.java   ← Lógica ventana Login
    │       └── ParticipanteController.java ← Lógica CRUD + validaciones
    └── resources/
        └── vista/
            ├── login.fxml             ← Vista Login
            └── participante.fxml      ← Vista CRUD (TableView + formulario)
```

---

## Configuración rápida

### 1. Base de datos
```sql
-- Ejecuta el script en MySQL Workbench o terminal:
source sql/natacion_db.sql
```

### 2. Ajustar credenciales
En `src/main/java/util/Conexion.java` cambia:
```java
private static final String URL     = "jdbc:mysql://localhost:3306/natacion_db";
private static final String USUARIO = "root";
private static final String CLAVE   = "tu_clave_aqui";  // ← pon tu contraseña
```

### 3. Ejecutar con Maven
```bash
mvn javafx:run
```

---

## Patrones de diseño aplicados

| Patrón | Dónde se aplica | Qué hace |
|--------|----------------|----------|
| **Singleton** | `util/Conexion.java` | Garantiza una única instancia de la conexión a BD |
| **Factory / DAO** | `dao/ParticipanteDAO.java` + `ParticipanteDAOImpl.java` | Separa la interfaz de la implementación; se puede cambiar MySQL por PostgreSQL sin tocar el controlador |
| **Strategy** | Validaciones en `ParticipanteController.java` | Cada regla de validación es un algoritmo intercambiable (se puede extraer a clases `ValidadorEdad`, `ValidadorCorreo`, etc.) |

---

## Credenciales de prueba (Login)
| Usuario   | Contraseña |
|-----------|-----------|
| admin     | admin123  |
| profesor  | epn2024   |

---

## Validaciones implementadas
- ✔ Ningún campo obligatorio vacío
- ✔ Cédula: solo dígitos numéricos
- ✔ Edad: solo números y mayor a 5 años
- ✔ Correo: debe contener @
- ✔ Correo no repetido en la BD
- ✔ Cédula no repetida en la BD
- ✔ Eliminación con confirmación (Alert CONFIRMATION)
- ✔ Todos los errores mostrados con Alert

---

## Controles JavaFX usados
`Label` · `TextField` · `PasswordField` · `ComboBox` · `RadioButton` · `ToggleGroup` · `TextArea` · `Button` · `TableView` · `Alert`
