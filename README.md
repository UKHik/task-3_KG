# OBJ Model Normal Calculator

Приложение для расчета нормалей к граням 3D моделей в формате OBJ с поддержкой проверки корректности вычисленных значений.

## Описание проекта

Проект реализует функционал загрузки 3D моделей из OBJ-файлов, вычисления нормалей к полигонам геометрии и сравнения вычисленных нормалей с эталонными значениями. Код включает полный набор unit-тестов для обеспечения корректности работы алгоритмов.

## Структура проекта

```
src/main/java/cgvsu/
    ├── Main.java                          # Точка входа приложения
    ├── math/
    │   ├── Vector2f.java                  # Класс для 2D векторов
    │   └── Vector3f.java                  # Класс для 3D векторов
    ├── model/
    │   ├── Model.java                     # Модель, содержащая вершины, полигоны, нормали
    │   ├── Polygon.java                   # Класс полигона (грань)
    │   ├── NormalCalculator.java          # Вычисление нормалей к граням
    │   └── NormalComparator.java          # Сравнение нормалей с эталоном
    └── objreader/
        ├── ObjReader.java                 # Парсер OBJ-файлов
        └── ObjReaderException.java        # Исключение для ошибок парсинга

src/test/java/cgvsu/
    ├── NormalCalculatorTest.java          # Unit-тесты вычисления нормалей
    └── ObjReaderTest.java                 # Unit-тесты парсера OBJ
```

## Основные компоненты

### Vector3f — 3D вектор
Класс для работы с трёхмерными векторами с поддержкой основных операций:
```java
import cgvsu.math.Vector3f;

// Создание вектора
Vector3f v1 = new Vector3f(1.0f, 2.0f, 3.0f);
Vector3f v2 = new Vector3f(4.0f, 5.0f, 6.0f);

// Операции
Vector3f diff = v1.subtract(v2);          // Вычитание векторов
Vector3f normal = v1.cross(v2);           // Векторное произведение (cross product)
float dotProduct = v1.dot(v2);            // Скалярное произведение (dot product)
float length = v1.length();               // Длина вектора
Vector3f normalized = v1.normalize();     // Нормализация (длина = 1)
```

### Vector2f — 2D вектор
Класс для работы с двумерными векторами:
```java
import cgvsu.math.Vector2f;

Vector2f uv = new Vector2f(0.5f, 0.75f);  // Координаты текстуры
```

### Model — 3D модель
Основной класс, представляющий 3D модель:
```java
import cgvsu.model.Model;

Model model = new Model();

List<Vector3f> vertices = model.getVertices();           // Вершины модели
List<Vector2f> textureVertices = model.getTextureVertices(); // Координаты текстур
List<Vector3f> normals = model.getNormals();            // Нормали к граням
List<Polygon> polygons = model.getPolygons();           // Полигоны (грани)
```

### ObjReader — парсер OBJ-файлов
Класс для чтения 3D моделей в формате OBJ:
```java
import cgvsu.objreader.ObjReader;
import java.nio.file.Files;
import java.nio.file.Path;

String fileContent = Files.readString(Path.of("model"));
Model model = ObjReader.read(fileContent);
```

Поддерживаемые типы данных в OBJ-файлах:
- `v x y z` — вершина 3D координаты
- `vt u v` — координаты текстуры
- `vn x y z` — нормаль вектор (из файла)
- `f v1/vt1/vn1 v2/vt2/vn2 ...` — полигон с индексами вершин, текстур и нормалей

### NormalCalculator — вычисление нормалей
Вычисляет нормали к граням модели используя векторное произведение:
```java
import cgvsu.model.NormalCalculator;

NormalCalculator.computeNormals(model);
// После вызова model.getNormals() содержит вычисленные нормали
// Каждой вершине полигона присваивается индекс соответствующей нормали
```

**Алгоритм вычисления нормали к грани:**
1. Берём полигон (треугольник или многоугольник)
2. Получаем первые три его вершины: p0, p1, p2
3. Вычисляем два вектора: u = p1 - p0, v = p2 - p0
4. Вычисляем векторное произведение: normal = u × v
5. Нормализуем результат (делаем длину = 1)
6. Сохраняем нормаль в модель
7. Присваиваем индекс нормали всем вершинам полигона

### NormalComparator — сравнение нормалей
Сравнивает вычисленные нормали с эталонными значениями:
```java
import cgvsu.model.NormalComparator;

var result = NormalComparator.compare(
    computedNormals,      // Вычисленные нормали
    referenceNormals,     // Эталонные нормали из файла
    1.0                   // Допустимое отклонение в градусах
);

System.out.println(result);
// Выведет статистику: количество совпадений, среднее и максимальное отклонение углов
```

## Использование

### Загрузка модели и вычисление нормалей
```java
import cgvsu.model.Model;
import cgvsu.model.NormalCalculator;
import cgvsu.objreader.ObjReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Example {
    public static void main(String[] args) throws Exception {
        // Загрузить модель из файла
        String fileContent = Files.readString(Path.of("models/model"));
        Model model = ObjReader.read(fileContent);
        
        System.out.println("Вершины: " + model.getVertices().size());
        System.out.println("Полигоны: " + model.getPolygons().size());
        
        // Вычислить нормали
        NormalCalculator.computeNormals(model);
        System.out.println("Нормали: " + model.getNormals().size());
        
        // Получить первую нормаль
        if (!model.getNormals().isEmpty()) {
            System.out.println("Первая нормаль: " + model.getNormals().get(0));
        }
    }
}
```

### Сравнение с эталонными нормалями
```java
import cgvsu.model.NormalComparator;
import java.util.List;

// Загрузить эталонный файл с нормалями
Model refModel = ObjReader.read(Files.readString(Path.of("models/reference")));
List<Vector3f> referenceNormals = List.copyOf(refModel.getNormals());

// Очистить и пересчитать нормали
refModel.getNormals().clear();
NormalCalculator.computeNormals(refModel);

// Сравнить результаты
var comparison = NormalComparator.compare(
    refModel.getNormals(),
    referenceNormals,
    1.0  // Допуск 1 градус
);
System.out.println(comparison);
```

## Запуск

### Компиляция
```bash
javac -d out $(find src/main -name "*.java")
```

### Запуск main приложения
```bash
java -cp out cgvsu.Main
```

### Запуск тестов (требуется JUnit 5)
```bash
javac -cp out:lib/junit-jupiter-api.jar:lib/junit-jupiter-engine.jar \
      -d out $(find src/test -name "*.java")

java -cp out:lib/junit-jupiter-api.jar:lib/junit-jupiter-engine.jar \
     org.junit.platform.console.ConsoleLauncher --scan-classpath
```

## Тестирование

Проект включает полный набор unit-тестов для проверки корректности:

### NormalCalculatorTest — тесты вычисления нормалей
- `testSimpleTriangleNormal()` — вычисление нормали простого треугольника
- `testReversedTriangleNormal()` — проверка обработки полигонов в обратном порядке
- `testNormalIndicesAssignedCorrectly()` — правильное присвоение индексов нормалей
- `testDegenerateTriangleProducesZeroNormal()` — обработка вырожденных треугольников
- `testLessThanThreeVerticesThrows()` — валидация минимума вершин в полигоне
- `testMultiplePolygonsHaveIndependentNormals()` — независимость нормалей разных полигонов
- `testQuadrilateralNormal()` — вычисление нормали четырёхугольника

### ObjReaderTest — тесты парсера OBJ-файлов
- `testReadingVerticesAndFace()` — чтение вершин и полигонов
- `testReadTextureVertices()` — чтение координат текстур

## Основные особенности

✓ Полная поддержка формата OBJ (вершины, текстуры, нормали, полигоны)

✓ Эффективное вычисление нормалей с использованием векторного произведения

✓ Нормализация векторов нормалей (единичной длины)

✓ Обработка полигонов с произвольным числом вершин (>= 3)

✓ Сравнение нормалей с допуском по углам

✓ Подробная статистика по результатам сравнения

✓ Полное покрытие unit-тестами (9+ тестов)

✓ Обработка ошибок парсинга с указанием номера строки

## Обработка ошибок

Приложение генерирует `ObjReaderException` при ошибках парсинга OBJ-файла:
```java
try {
    Model model = ObjReader.read(fileContent);
} catch (ObjReaderException e) {
    System.err.println("Ошибка парсинга: " + e.getMessage());
}
```

`NormalCalculator` выбрасывает `IllegalArgumentException` если полигон содержит менее 3 вершин.
