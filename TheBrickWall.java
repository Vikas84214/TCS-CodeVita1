import java.io.*;
import java.util.*;

public class Main {

    static class Node {
        int r, c, cost;

        Node(int r, int c, int cost) {
            this.r = r;
            this.c = c;
            this.cost = cost;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        char[][] wall = new char[n][n];

        int sr = -1, sc = -1, dr = -1, dc = -1;

        for (int r = 0; r < n; r++) {
            String s = br.readLine().trim();
            int col = 0;
            int i = 0;

            while (i < s.length()) {
                int len = 0;

                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    len = len * 10 + (s.charAt(i) - '0');
                    i++;
                }

                char type = s.charAt(i++);

                for (int k = 0; k < len; k++) {
                    wall[r][col] = type;

                    if (type == 'S') {
                        sr = r;
                        sc = col;
                    } else if (type == 'D') {
                        dr = r;
                        dc = col;
                    }

                    col++;
                }
            }
        }

        int[][] dist = new int[n][n];
        for (int[] row : dist)
            Arrays.fill(row, Integer.MAX_VALUE);

        ArrayDeque<Node> deque = new ArrayDeque<>();
        dist[sr][sc] = 0;
        deque.addFirst(new Node(sr, sc, 0));

        int[] rr = {-1, 1, 0, 0};
        int[] cc = {0, 0, -1, 1};

        while (!deque.isEmpty()) {
            Node cur = deque.pollFirst();

            if (cur.cost != dist[cur.r][cur.c])
                continue;

            if (cur.r == dr && cur.c == dc)
                break;

            for (int k = 0; k < 4; k++) {
                int nr = cur.r + rr[k];
                int nc = cur.c + cc[k];

                if (nr < 0 || nr >= n || nc < 0 || nc >= n)
                    continue;

                if (wall[nr][nc] == 'R')
                    continue;

                int add = (wall[nr][nc] == 'G') ? 1 : 0;
                int newCost = cur.cost + add;

                if (newCost < dist[nr][nc]) {
                    dist[nr][nc] = newCost;

                    if (add == 0)
                        deque.addFirst(new Node(nr, nc, newCost));
                    else
                        deque.addLast(new Node(nr, nc, newCost));
                }
            }
        }

        System.out.println(dist[dr][dc]);
    }
}
