package cgvsu.model;

import cgvsu.math.Vector3f;

import java.util.List;

public class NormalComparator {

    public static class ComparisonResult {
        public final int totalNormals;
        public final int matchingNormals;
        public final double averageAngleDifference;
        public final double maxAngleDifference;

        public ComparisonResult(int total, int matching, double avgAngle, double maxAngle) {
            this.totalNormals = total;
            this.matchingNormals = matching;
            this.averageAngleDifference = avgAngle;
            this.maxAngleDifference = maxAngle;
        }

        @Override
        public String toString() {
            return String.format("""
                            Результаты сравнения:
                            Всего нормалей: %d
                            Совпадающих: %d (%.1f%%)
                            Средняя разница углов: %.4f градусов
                            Максимальная разница углов: %.4f градусов
                            """,
                    totalNormals,
                    matchingNormals,
                    (100.0 * matchingNormals / totalNormals),
                    averageAngleDifference,
                    maxAngleDifference
            );
        }
    }

    /**
     * Сравнивает два списка нормалей и возвращает статистику.
     *
     * @param computed       вычисленные нормали
     * @param reference      эталонные нормали из файла
     * @param angleTolerance допустимое отклонение в градусах
     */
    public static ComparisonResult compare(
            List<Vector3f> computed,
            List<Vector3f> reference,
            double angleTolerance) {

        if (computed.size() != reference.size()) {
            throw new IllegalArgumentException(
                    "Количество нормалей не совпадает: computed=" + computed.size() +
                            ", reference=" + reference.size()
            );
        }

        int total = computed.size();
        int matching = 0;
        double sumAngleDiff = 0;
        double maxAngleDiff = 0;

        for (int i = 0; i < total; i++) {
            Vector3f c = computed.get(i);
            Vector3f r = reference.get(i);

            double angleDiff = angleBetween(c, r);
            sumAngleDiff += angleDiff;
            maxAngleDiff = Math.max(maxAngleDiff, angleDiff);

            if (angleDiff <= angleTolerance) {
                matching++;
            }
        }

        double avgAngleDiff = sumAngleDiff / total;
        return new ComparisonResult(total, matching, avgAngleDiff, maxAngleDiff);
    }

    private static double angleBetween(Vector3f a, Vector3f b) {
        float dotProduct = a.dot(b);
        float lengthProduct = a.length() * b.length();

        if (lengthProduct < Vector3f.EPS) {
            return 0;
        }

        double cosAngle = Math.max(-1.0, Math.min(1.0, dotProduct / lengthProduct));
        return Math.toDegrees(Math.acos(cosAngle));
    }
}