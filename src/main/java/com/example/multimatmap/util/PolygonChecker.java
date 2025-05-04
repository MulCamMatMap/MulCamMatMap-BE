package com.example.multimatmap.util;

public class PolygonChecker {
    // 오각형의 꼭짓점 (위도, 경도) 좌표
    private static final double[][] POLYGON = {
            { 37.55886433, 127.07398042 },
            { 37.54231671, 127.046426804 },
            { 37.53995426, 127.07067062 },
            { 37.54519382, 127.08052601 },
            { 37.55712283, 127.07949356 }
    };

    /**
     * 경도와 위도가 주어졌을 때, Polygon 내부에 존재하는지 확인하는 함수
     * @param lat 경도
     * @param lon 위도
     * @return
     */
    public static boolean isValidLocation(double lat, double lon) {
        boolean inside = false;
        int n = POLYGON.length;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = POLYGON[i][1], yi = POLYGON[i][0]; // 경도, 위도
            double xj = POLYGON[j][1], yj = POLYGON[j][0];

            boolean intersect = ((yi > lat) != (yj > lat)) &&
                    (lon < (xj - xi) * (lat - yi) / (yj - yi + 1e-10) + xi);

            if (intersect) inside = !inside;
        }

        return inside;
    }
}
