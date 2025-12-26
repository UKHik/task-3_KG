package cgvsu.model;

import cgvsu.math.Vector3f;
import cgvsu.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<Vector3f> vertices = new ArrayList<>();
    private final List<Vector2f> textureVertices = new ArrayList<>();
    private final List<Vector3f> normals = new ArrayList<>();
    private final List<Polygon> polygons = new ArrayList<>();


    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}