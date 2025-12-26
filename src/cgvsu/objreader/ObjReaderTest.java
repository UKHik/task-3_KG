package cgvsu.objreader;

import cgvsu.model.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjReaderTest {

    @Test
    void testReadingVerticesAndFace() {
        String objText = String.join("\n",
                "v 0 0 0",
                "v 1 0 0",
                "v 0 1 0",
                "f 1 2 3"
        );

        Model model = ObjReader.read(objText);

        assertEquals(3, model.getVertices().size());
        assertEquals(1, model.getPolygons().size());
        assertEquals(3, model.getPolygons().get(0).getVertexIndices().size());
    }

    @Test
    void testReadTextureVertices() {
        String objText = String.join("\n",
                "v 0 0 0",
                "v 1 1 1",
                "vt 0.5 0.5"
        );

        Model model = ObjReader.read(objText);

        assertEquals(2, model.getVertices().size());
        assertEquals(1, model.getTextureVertices().size());
    }
}