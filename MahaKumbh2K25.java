import java.io.*;
import java.util.*;

public class Main {
    static Map<String, Integer> map = new HashMap<>();
    static List<List<Integer>> graph = new ArrayList<>();
    static List<Query> queries = new ArrayList<>();
    static Map<Integer, Set<Integer>> blocked = new HashMap<>();

    static class Query {
        int u, v;
        String op;

        Query(int u, String op, int v) {
            this.u = u;
            this.op = op;
            this.v = v;
        }
    }

    static int id(String s) {
        Integer x = map.get(s);
        if (x != null) return x;
        int n = graph.size();
        map.put(s, n);
        graph.add(new ArrayList<>());
        return n;
    }

    static void connect(int u, int v) {
        if (!graph.get(u).contains(v)) {
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
    }

    static void disconnect(int u, int v) {
        graph.get(u).remove((Integer) v);
        graph.get(v).remove((Integer) u);
    }

    static boolean canTravel(int source, int destination) {
        Set<Integer> ban = blocked.get(source);

        if (ban != null && ban.contains(destination)) return false;

        boolean[] visited = new boolean[graph.size()];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        visited[source] = true;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();

            if (u == destination) return true;

            for (int v : graph.get(u)) {
                if (visited[v]) continue;
                if (ban != null && ban.contains(v)) continue;

                visited[v] = true;
                queue.offer(v);
            }
        }

        return false;
    }

    static String nextLine(BufferedReader br) throws IOException {
        String s;
        while ((s = br.readLine()) != null) {
            s = s.trim();
            if (!s.isEmpty()) return s;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(nextLine(br));

        for (int i = 0; i < n; i++) {
            String[] a = nextLine(br).split("\\s+");
            int u = id(a[0]);

            for (int j = 1; j < a.length; j++) {
                connect(u, id(a[j]));
            }
        }

        int q = Integer.parseInt(nextLine(br));

        for (int i = 0; i < q; i++) {
            String[] a = nextLine(br).split("\\s+");
            queries.add(new Query(id(a[0]), a[1], id(a[2])));
        }

        int r = Integer.parseInt(nextLine(br));

        for (int i = 0; i < r; i++) {
            String[] a = nextLine(br).split("\\s+");
            int source = id(a[0]);

            Set<Integer> set = blocked.computeIfAbsent(source, k -> new HashSet<>());

            for (int j = 1; j < a.length; j++) {
                set.add(id(a[j]));
            }
        }

        StringBuilder out = new StringBuilder();

        for (Query query : queries) {
            if (query.op.equals("connects")) {
                connect(query.u, query.v);
            } else if (query.op.equals("disconnects")) {
                disconnect(query.u, query.v);
            } else {
                out.append(canTravel(query.u, query.v) ? "yes\n" : "no\n");
            }
        }

        System.out.print(out);
    }
}
