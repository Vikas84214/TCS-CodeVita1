import java.io.*;
import java.util.*;

public class Main {
    static final int[][] F = {
        {0,1,0, 1,0,0, 0,0,1},
        {0,0,1, 1,0,0, 0,-1,0},
        {0,-1,0, 1,0,0, 0,0,-1},
        {0,0,-1, 1,0,0, 0,1,0},
        {-1,0,0, 0,0,1, 0,-1,0},
        {1,0,0, 0,0,-1, 0,-1,0}
    };

    static int[][] moves = new int[18][24];
    static int[][] corners = {
        {0,15,16}, {1,14,21}, {2,4,17}, {3,5,20},
        {6,8,19}, {7,9,22}, {10,13,18}, {11,12,23}
    };

    static char[] rotate(char[] v, int axis, int sign) {
        char[] r = new char[3];
        int a = (axis + 1) % 3, b = (axis + 2) % 3;
        r[axis] = v[axis];
        r[a] = (char)(sign * (v[b] - '0') + '0');
        r[b] = (char)(-sign * (v[a] - '0') + '0');
        return r;
    }

    static int[] rot(int[] v, int axis, int sign) {
        int[] r = v.clone();
        int a = (axis + 1) % 3, b = (axis + 2) % 3;
        r[a] = sign * v[b];
        r[b] = -sign * v[a];
        return r;
    }

    static void buildMoves() {
        int[][] pos = new int[24][3];
        int[][] normal = new int[24][3];

        for (int f = 0; f < 6; f++) {
            int[] n = {F[f][0], F[f][1], F[f][2]};
            int[] right = {F[f][3], F[f][4], F[f][5]};
            int[] down = {F[f][6], F[f][7], F[f][8]};

            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 2; c++) {
                    int id = f * 4 + r * 2 + c;
                    int x = n[0] + right[0] * (2 * c - 1) + down[0] * (2 * r - 1);
                    int y = n[1] + right[1] * (2 * c - 1) + down[1] * (2 * r - 1);
                    int z = n[2] + right[2] * (2 * c - 1) + down[2] * (2 * r - 1);
                    pos[id] = new int[]{x, y, z};
                    normal[id] = n.clone();
                }
            }
        }

        for (int f = 0; f < 6; f++) {
            for (int type = 0; type < 3; type++) {
                int sign = type == 0 ? 1 : -1;
                if (type == 2) sign = 2;

                int[] p = F[f];
                int axis = p[0] != 0 ? 0 : p[1] != 0 ? 1 : 2;
                int dir = p[axis];

                for (int i = 0; i < 24; i++) {
                    moves[f * 3 + type][i] = i;

                    if (pos[i][axis] != dir) continue;

                    int[] np = pos[i].clone();
                    int[] nn = normal[i].clone();

                    if (type == 2) {
                        np = rot(np, axis, dir);
                        np = rot(np, axis, dir);
                        nn = rot(nn, axis, dir);
                        nn = rot(nn, axis, dir);
                    } else {
                        np = rot(np, axis, dir * sign);
                        nn = rot(nn, axis, dir * sign);
                    }

                    for (int j = 0; j < 24; j++) {
                        if (Arrays.equals(pos[j], np) && Arrays.equals(normal[j], nn)) {
                            moves[f * 3 + type][i] = j;
                            break;
                        }
                    }
                }
            }
        }
    }

    static char[] apply(char[] state, int[] move) {
        char[] next = new char[24];
        for (int i = 0; i < 24; i++)
            next[move[i]] = state[i];
        return next;
    }

    static char[] twist(char[] state, int[] c, boolean clockwise) {
        char[] s = state.clone();
        int a = c[0], b = c[1], d = c[2];

        if (clockwise) {
            s[a] = state[d];
            s[b] = state[a];
            s[d] = state[b];
        } else {
            s[a] = state[b];
            s[b] = state[d];
            s[d] = state[a];
        }

        return s;
    }

    static String key(char[] state, int[] c) {
        char[] x = {state[c[0]], state[c[1]], state[c[2]]};
        Arrays.sort(x);
        return new String(x);
    }

    static Map<String, String> possible = new HashMap<>();
    static char[] target;

    static boolean dfs(char[] state, int depth) {
        String k = possible.get(new String(state));
        if (k != null) {
            System.out.println(k);
            return true;
        }

        if (depth == 4) return false;

        for (int i = 0; i < 18; i++) {
            if (dfs(apply(state, moves[i]), depth + 1))
                return true;
        }

        return false;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        target = new char[24];

        int p = 0;
        while (st.hasMoreTokens())
            target[p++] = st.nextToken().charAt(0);

        while (p < 24) {
            st = new StringTokenizer(br.readLine());
            while (st.hasMoreTokens())
                target[p++] = st.nextToken().charAt(0);
        }

        buildMoves();

        char[] solved = new char[24];
        char[] colors = {'y', 'r', 'w', 'o', 'b', 'g'};

        for (int f = 0; f < 6; f++)
            Arrays.fill(solved, f * 4, f * 4 + 4, colors[f]);

        for (int[] c : corners) {
            for (boolean dir : new boolean[]{true, false}) {
                char[] twisted = twist(solved, c, dir);
                possible.put(new String(twisted), key(solved, c));
            }
        }

        if (!dfs(target, 0))
            System.out.println("Not enough clues");
    }
}
