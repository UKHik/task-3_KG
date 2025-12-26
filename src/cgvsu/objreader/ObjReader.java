package cgvsu.objreader;

import cgvsu.math.Vector2f;
import cgvsu.math.Vector3f;
import cgvsu.model.Model;
import cgvsu.model.Polygon;

import java.util.Scanner;

public class ObjReader {

	public static Model read(String objText) {
		Model model = new Model();

		Scanner scanner = new Scanner(objText);

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();

			if (line.isEmpty() || line.startsWith("#")) continue;

			if (line.startsWith("vn ")) parseNormal(line, model);
			else if (line.startsWith("vt ")) parseTexture(line, model);
			else if (line.startsWith("v ")) parseVertex(line, model);
			else if (line.startsWith("f ")) parseFace(line, model);
		}

		return model;
	}

	private static void parseVertex(String line, Model model) {
		String[] tokens = line.split("\\s+");
		float x = Float.parseFloat(tokens[1]);
		float y = Float.parseFloat(tokens[2]);
		float z = Float.parseFloat(tokens[3]);
		model.getVertices().add(new Vector3f(x, y, z));
	}

	private static void parseTexture(String line, Model model) {
		String[] t = line.split("\\s+");
		model.getTextureVertices().add(
				new Vector2f(Float.parseFloat(t[1]),
						Float.parseFloat(t[2]))
		);
	}

	private static void parseNormal(String line, Model model) {
		String[] tokens = line.split("\\s+");
		float x = Float.parseFloat(tokens[1]);
		float y = Float.parseFloat(tokens[2]);
		float z = Float.parseFloat(tokens[3]);
		model.getNormals().add(new Vector3f(x, y, z));
	}

	private static void parseFace(String line, Model model) {
		String[] tokens = line.substring(2).split("\\s+");
		Polygon polygon = new Polygon();

		for (String group : tokens) {
			String[] parts = group.split("/");

			int vertexIndex = Integer.parseInt(parts[0]) - 1;
			polygon.getVertexIndices().add(vertexIndex);

			if (parts.length > 1 && !parts[1].isEmpty()) {
				int texIndex = Integer.parseInt(parts[1]) - 1;
				polygon.getTextureVertexIndices().add(texIndex);
			}

			if (parts.length > 2 && !parts[2].isEmpty()) {
				int normalIndex = Integer.parseInt(parts[2]) - 1;
				polygon.getNormalIndices().add(normalIndex);
			}
		}

		model.getPolygons().add(polygon);
	}
}