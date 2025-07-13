package mirrormaze.pathfinder;

import java.awt.Point;
import java.util.*;

public class PathFinder {

    public static class Result {
        public final List<Point> path;
        public final int steps;

        public Result(List<Point> path) {
            this.path = path;
            this.steps = path.size() - 1;
        }
    }

    public static Result bfs(boolean[][] walls,
            int startX, int startY,
            int endX, int endY) {
        int cols = walls.length;
        int rows = walls[0].length;
        boolean[][] visited = new boolean[cols][rows];
        Point[][] prev = new Point[cols][rows];
        Queue<Point> queue = new LinkedList<>();

        queue.add(new Point(startX, startY));
        visited[startX][startY] = true;
        prev[startX][startY] = null;

        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (p.x == endX && p.y == endY) {
                List<Point> path = new ArrayList<>();
                for (Point cur = p; cur != null; cur = prev[cur.x][cur.y]) {
                    path.add(cur);
                }
                Collections.reverse(path);
                return new Result(path);
            }

            for (int[] d : dirs) {
                int nx = p.x + d[0];
                int ny = p.y + d[1];
                if (nx < 0 || nx >= cols || ny < 0 || ny >= rows)
                    continue;
                if (visited[nx][ny] || walls[nx][ny])
                    continue;
                visited[nx][ny] = true;
                prev[nx][ny] = p;
                queue.add(new Point(nx, ny));
            }
        }

        return null;
    }
}
