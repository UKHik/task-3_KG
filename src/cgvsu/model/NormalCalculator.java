package cgvsu.model;

import cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.List;


/*
* 1. Берём полигон (например, треугольник)
2. Получаем три его вершины: p0, p1, p2
3. Вычисляем два вектора:
   - u = p1 - p0
   - v = p2 - p0
4. Вычисляем векторное произведение: normal = u × v
5. Нормализуем результат (делаем длину = 1)
6. Сохраняем нормаль в модель
7. Присваиваем индекс этой нормали всем вершинам полигона
*/
public class NormalCalculator {

    /**
     * Вычисляет нормали для каждой грани.
     * Нормали записываются в model.normals,
     * индексы нормалей помещаются в polygon.normalIndices.
     */
    public static void computeNormals(Model model) {
        for (Polygon polygon : model.getPolygons()) {

            var vertexIds = polygon.getVertexIndices();
            if (vertexIds.size() < 3) {
                throw new IllegalArgumentException(
                        "Polygon must have at least 3 vertices."
                );
            }

            Vector3f p0 = model.getVertices().get(vertexIds.get(0));
            Vector3f p1 = model.getVertices().get(vertexIds.get(1));
            Vector3f p2 = model.getVertices().get(vertexIds.get(2));

            Vector3f normal = computeFaceNormal(p0, p1, p2).normalize();
            int normalIndex = model.getNormals().size();

            model.getNormals().add(normal);

            List<Integer> normalIndices = new ArrayList<>();
            for (var _ : vertexIds) {
                normalIndices.add(normalIndex);
            }

            polygon.setNormalIndices(normalIndices);
        }
    }

    private static Vector3f computeFaceNormal(Vector3f p0, Vector3f p1, Vector3f p2) {
        Vector3f u = p1.subtract(p0);
        Vector3f v = p2.subtract(p0);
        return u.cross(v);
    }
}