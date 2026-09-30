import java.io.*;
import java.util.*;

public class Main {
    static int n, m, t, count;
    static char[][] grid;
    static boolean[][][] valid;
    static boolean[][] used;
    static String answer;

    static void dfs(int r, int c, int time, StringBuilder word) {
        if (count > 1) return;
        if (time == t - 1) {
            String s = word.toString();
            if (answer == null) {
                answer = s;
                count = 1;
            } else if (!answer.equals(s)) count = 2;
            return;
        }

        used[r][c] = true;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k], nc = c + dc[k];
            if (nr >= 0 && nr < n && nc >= 0 && nc < m &&
                !used[nr][nc] && valid[time + 1][nr][nc]) {
                word.append(grid[nr][nc]);
                dfs(nr, nc, time + 1, word);
                word.deleteCharAt(word.length() - 1);
            }
        }
        used[r][c] = false;
    }

    static String next(BufferedReader br) throws IOException {
        String s;
        while ((s = br.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) return s;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] a = next(br).split("\\s+");
        n = Integer.parseInt(a[0]);
        m = Integer.parseInt(a[1]);
        grid = new char[n][m];

        for (int i = 0; i < n; i++) {
            a = next(br).split("\\s+");
            for (int j = 0; j < m; j++) grid[i][j] = a[j].charAt(0);
        }

        t = Integer.parseInt(next(br));
        int clues = Integer.parseInt(next(br));
        valid = new boolean[t][n][m];

        for (int k = 0; k < t; k++)
            for (int i = 0; i < n; i++)
                Arrays.fill(valid[k][i], true);

        for (int k = 0; k < clues; k++) {
            int time = Integer.parseInt(next(br)) - 1;
            a = next(br).split("\\s+");
            int x1 = Integer.parseInt(a[0]) - 1;
            int y1 = Integer.parseInt(a[1]) - 1;
            int x2 = Integer.parseInt(a[2]) - 1;
            int y2 = Integer.parseInt(a[3]) - 1;

            for (int i = x1; i <= x2; i++)
                for (int j = y1; j <= y2; j++) valid[time][i][j] = false;
        }

        for (int time = 0; time < t; time++) {
            boolean exists = false;
            for (int i = 0; i < n && !exists; i++)
                for (int j = 0; j < m; j++)
                    if (valid[time][i][j]) {
                        exists = true;
                        break;
                    }
            if (!exists) {
                System.out.println("Not enough clues");
                return;
            }
        }

        used = new boolean[n][m];
        StringBuilder word = new StringBuilder(t);

        for (int i = 0; i < n && count <= 1; i++) {
            for (int j = 0; j < m && count <= 1; j++) {
                if (valid[0][i][j]) {
                    word.append(grid[i][j]);
                    dfs(i, j, 0, word);
                    word.setLength(0);
                }
            }
        }

        System.out.println(count == 1 ? answer : "Not enough clues");
    }
}
