package cgvsu;

import cgvsu.model.Model;
import cgvsu.model.NormalCalculator;
import cgvsu.model.NormalComparator;
import cgvsu.objreader.ObjReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("=== Вычисление нормалей ===");
            Path fileName = Path.of("models/FinalBaseMesh.obj");
            String fileContent = Files.readString(fileName);

            System.out.println("Загрузка модели...");
            Model model = ObjReader.read(fileContent);

            System.out.println("Вычисление нормалей...");
            NormalCalculator.computeNormals(model);

            System.out.println("Вершины: " + model.getVertices().size());
            System.out.println("Полигоны: " + model.getPolygons().size());
            System.out.println("Нормали: " + model.getNormals().size());

            if (!model.getNormals().isEmpty()) {
                System.out.println("Первая нормаль: " + model.getNormals().get(0));
            }

            System.out.println("\n=== Сравнение с эталонными нормалями ===");
            Path referenceFile = Path.of("models/reference.obj");

            if (Files.exists(referenceFile)) {
                String refContent = Files.readString(referenceFile);
                Model refModel = ObjReader.read(refContent);

                var referenceNormals = List.copyOf(refModel.getNormals());

                refModel.getNormals().clear();
                NormalCalculator.computeNormals(refModel);

                var comparison = NormalComparator.compare(
                        refModel.getNormals(),
                        referenceNormals,
                        1.0
                );

                System.out.println(comparison);
            } else {
                System.out.println("Эталонный файл не найден.");
            }

            System.out.println("Готово.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}