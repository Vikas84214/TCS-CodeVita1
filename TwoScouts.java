import java.io.*;
import java.util.*;

public class Main {

    static int n, m, dest;
    static int[] graph;
    static boolean[][] seen;
    static boolean[] terminal;
    static int destBit;

    static void dfs(int start, int mask, int v) {
        if (v == dest) {
            terminal[mask & ~destBit] = true;
            return;
        }

        if (seen[mask][v])
            return;

        seen[mask][v] = true;

        int next = graph[v] & ~mask;

        while (next != 0) {
            int bit = next & -next;
            int u = Integer.numberOfTrailingZeros(bit);
            dfs(start, mask | bit, u);
            next -= bit;
        }
    }

    static boolean[] getPaths(int start) {
        terminal = new boolean[1 << n];
        seen = new boolean[1 << n][n];

        if (start == dest) {
            terminal[0] = true;
            return terminal;
        }

        int startBit = 1 << start;
        dfs(start, startBit, start);

        return terminal;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st =
            new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        graph = new int[n];

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken()) - 1;

            graph[a] |= 1 << b;
            graph[b] |= 1 << a;
        }

        st = new StringTokenizer(br.readLine());

        int s1 = Integer.parseInt(st.nextToken()) - 1;
        int s2 = Integer.parseInt(st.nextToken()) - 1;

        dest = Integer.parseInt(br.readLine().trim()) - 1;
        destBit = 1 << dest;

        boolean[] paths1 = getPaths(s1);
        boolean[] paths2 = getPaths(s2);

        int size = 1 << n;
        int INF = Integer.MAX_VALUE;

        int[] best = new int[size];
        Arrays.fill(best, INF);

        for (int mask = 0; mask < size; mask++) {
            if (paths2[mask])
                best[mask] = Integer.bitCount(mask);
        }

        for (int bit = 0; bit < n; bit++) {
            int b = 1 << bit;

            for (int mask = 0; mask < size; mask++) {
                if ((mask & b) != 0 &&
                    best[mask ^ b] < best[mask]) {
                    best[mask] = best[mask ^ b];
                }
            }
        }

        int answer = INF;
        int all = size - 1;

        for (int mask = 0; mask < size; mask++) {
            if (!paths1[mask])
                continue;

            int allowed = all ^ mask;
            int second = best[allowed];

            if (second != INF) {
                answer = Math.min(answer,
                    Integer.bitCount(mask) + second + 1);
            }
        }

        if (answer == INF)
            System.out.println("Impossible");
        else
            System.out.println(answer);
    }
}
