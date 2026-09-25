# MicroSpring RetailGo

Proyecto educativo en Java que implementa un contenedor ligero de **inversión de control (IoC)** e **inyección de dependencias (DI)**, sin utilizar Spring Framework. El caso de uso aplica una promoción de RetailGo validando inventario y enviando una notificación.

## Características

- Registro explícito de componentes anotados con `@RGComponent`.
- Inyección de dependencias por constructor mediante `@RGInject`.
- Resolución de implementaciones a través de interfaces.
- Componentes singleton por instancia de `MiniContainer`.
- Detección de dependencias circulares.
- Mensajes de error para dependencias no registradas, implementaciones ambiguas y constructores inválidos.

## Estructura

```text
src/
├── main/java/co/edu/retailgo/
│   ├── App.java                     # Punto de entrada
│   ├── minispring/                  # Contenedor y anotaciones propias
│   ├── domain/                      # Caso de uso y puertos
│   ├── adapters/                    # Implementaciones de infraestructura
│   └── legacy/                      # Ejemplo de acoplamiento fuerte
└── test/java/co/edu/retailgo/       # Pruebas del contenedor
```

## Requisitos

- JDK 17 o superior
- Maven 3.9 o superior

Docker es opcional para ejecutar el proyecto en un contenedor.

## Ejecución local

Desde la raíz del proyecto, ejecute las pruebas:

```bash
mvn test
```

Resultado esperado:

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

Para iniciar la aplicación:

```bash
mvn -q exec:java
```

La salida esperada incluye una notificación y el resultado de la promoción:

```text
Notificación a CLI-1042: Promoción RetailGo aplicada para SKU-100
PROMOTION_APPLIED
```

## Ejecución con Docker

Construya y ejecute la imagen:

```bash
docker compose up --build
```

El servicio ejecuta `App` y termina después de imprimir el resultado. Para detener y eliminar el contenedor creado:

```bash
docker compose down
```

## Cómo funciona el contenedor

La aplicación registra las clases candidatas al crear `MiniContainer`:

```java
MiniContainer container = new MiniContainer(
    PromotionService.class,
    InMemoryInventoryAdapter.class,
    ConsoleNotificationAdapter.class
);
```

Al solicitar `PromotionService`, el contenedor selecciona su constructor con `@RGInject`, resuelve automáticamente `InventoryPort` y `NotificationPort`, crea sus adaptadores y conserva las instancias para futuras solicitudes.

```java
PromotionService service = container.getBean(PromotionService.class);
```

Para que una clase sea administrada debe tener `@RGComponent`. Si posee más de un constructor, marque exactamente uno con `@RGInject`; si tiene un único constructor, la anotación es opcional.

## Pruebas incluidas

`MiniContainerTest` verifica que el contenedor:

- Cree e inyecte las dependencias de `PromotionService`.
- Reutilice instancias singleton.
- Informe una implementación faltante.
- Detecte una dependencia circular.

## Tecnologías

- Java 17
- Maven
- JUnit 5
- Docker / Docker Compose
