package cgvsu.model;

import cgvsu.math.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NormalCalculatorTest {

    private Model createModel(Vector3f... vertices) {
        Model model = new Model();
        model.getVertices().addAll(List.of(vertices));
        return model;
    }

    private void assertNormalized(Vector3f v) {
        assertEquals(1.0f, v.length(), 1e-5, "Normal must be normalized");
    }

    private void assertNonZero(Vector3f v) {
        assertTrue(v.length() > 1e-5, "Normal must not be zero vector");
    }

    @Test
    void testSimpleTriangleNormal() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().add(p);

        NormalCalculator.computeNormals(model);

        Vector3f n = model.getNormals().get(0);

        assertTrue(n.length() > 0.99 && n.length() < 1.01, "Normal must be normalized");

        Vector3f u = new Vector3f(1, 0, 0);
        assertEquals(0f, n.dot(u), 1e-6, "Normal must be perpendicular to edge");

        Vector3f v = new Vector3f(0, 1, 0);
        assertEquals(0f, n.dot(v), 1e-6, "Normal must be perpendicular to second edge");
    }

    @Test
    void testReversedTriangleNormal() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(2, 1, 0));
        model.getPolygons().add(p);

        NormalCalculator.computeNormals(model);

        Vector3f n = model.getNormals().get(0);

        assertNonZero(n);
        assertNormalized(n);
    }

    @Test
    void testNormalIndicesAssignedCorrectly() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().add(p);

        NormalCalculator.computeNormals(model);

        List<Integer> normals = p.getNormalIndices();
        assertEquals(3, normals.size());
        assertEquals(0, normals.get(0));
        assertEquals(0, normals.get(1));
        assertEquals(0, normals.get(2));
    }

    @Test
    void testDegenerateTriangleProducesZeroNormal() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                new Vector3f(2, 2, 2)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(0, 1, 2));
        model.getPolygons().add(p);

        NormalCalculator.computeNormals(model);

        Vector3f n = model.getNormals().get(0);

        assertEquals(0f, n.x, 1e-6);
        assertEquals(0f, n.y, 1e-6);
        assertEquals(0f, n.z, 1e-6);
    }

    @Test
    void testLessThanThreeVerticesThrows() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(0, 1));
        model.getPolygons().add(p);

        assertThrows(IllegalArgumentException.class,
                () -> NormalCalculator.computeNormals(model));
    }

    @Test
    void testMultiplePolygonsHaveIndependentNormals() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(0, 1, 0),
                new Vector3f(0, 0, 1)
        );

        Polygon p1 = new Polygon();
        p1.getVertexIndices().addAll(List.of(0, 1, 2));

        Polygon p2 = new Polygon();
        p2.getVertexIndices().addAll(List.of(0, 1, 3));

        model.getPolygons().add(p1);
        model.getPolygons().add(p2);

        NormalCalculator.computeNormals(model);

        assertEquals(2, model.getNormals().size());

        Vector3f n1 = model.getNormals().get(0);
        Vector3f n2 = model.getNormals().get(1);

        assertNonZero(n1);
        assertNonZero(n2);

        assertNormalized(n1);
        assertNormalized(n2);

        assertFalse(
                Math.abs(n1.x - n2.x) < 1e-6 &&
                        Math.abs(n1.y - n2.y) < 1e-6 &&
                        Math.abs(n1.z - n2.z) < 1e-6,
                "Normals of different polygons must differ"
        );
    }

    @Test
    void testQuadrilateralNormal() {
        Model model = createModel(
                new Vector3f(0, 0, 0),
                new Vector3f(1, 0, 0),
                new Vector3f(1, 1, 0),
                new Vector3f(0, 1, 0)
        );

        Polygon p = new Polygon();
        p.getVertexIndices().addAll(List.of(0, 1, 2, 3));
        model.getPolygons().add(p);

        NormalCalculator.computeNormals(model);

        Vector3f n = model.getNormals().get(0);

        assertNormalized(n);

        List<Integer> normalIndices = p.getNormalIndices();
        assertEquals(4, normalIndices.size());
        for (int idx : normalIndices) {
            assertEquals(0, idx);
        }
    }
}