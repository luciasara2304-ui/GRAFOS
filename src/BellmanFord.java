import java.util.*;

public class BellmanFord {

    static final int INF = Integer.MAX_VALUE;

    // Arista dirigida u -> v con peso w
    static class Arista {
        int u, v, w;
        Arista(int u, int v, int w) { this.u = u; this.v = v; this.w = w; }
    }

    // true si NO hay ciclo negativo; llena dist[] y pred[]
    static boolean bellmanFord(int n, List<Arista> aristas, int origen,
                               int[] dist, int[] pred) {
        Arrays.fill(dist, INF);
        Arrays.fill(pred, -1);
        dist[origen] = 0;

        for (int i = 1; i <= n - 1; i++) {
            boolean huboCambio = false;
            for (Arista a : aristas) {
                if (dist[a.u] != INF && dist[a.u] + a.w < dist[a.v]) {
                    dist[a.v] = dist[a.u] + a.w;
                    pred[a.v] = a.u;
                    huboCambio = true;
                }
            }
            System.out.println("Iteracion " + i + ": " + formato(dist));
            if (!huboCambio) break;   // optimización: ya convergió
        }

        // Pasada extra: detección de ciclo negativo
        for (Arista a : aristas) {
            if (dist[a.u] != INF && dist[a.u] + a.w < dist[a.v]) {
                return false;
            }
        }
        return true;
    }

    // Mejorar el formato
    static String formato(int[] dist) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < dist.length; i++) {
            sb.append(dist[i] == INF ? "INF" : String.valueOf(dist[i]));
            if (i < dist.length - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // Reconstruye la ruta origen -> destino usando pred[]
    static String camino(int[] pred, int destino, String[] nombres) {
        LinkedList<String> ruta = new LinkedList<>();
        for (int x = destino; x != -1; x = pred[x]) ruta.addFirst(nombres[x]);
        return String.join(" -> ", ruta);
    }

    // Grafo 1
    public static void main(String[] args) {
        String[] nombres = {"A", "B", "C", "D", "E"};
        List<Arista> aristas = Arrays.asList(
                new Arista(0,1,6), new Arista(0,2,7), new Arista(1,2,8), new Arista(1,3,5),
                new Arista(1,4,-4), new Arista(2,3,-3), new Arista(2,4,9), new Arista(3,1,-2),
                new Arista(4,0,2), new Arista(4,3,7));

        int n = 5;
        int[] dist = new int[n], pred = new int[n];
        if (!bellmanFord(n, aristas, 0, dist, pred)) {
            System.out.println("Ciclo negativo detectado!");
        } else {
            for (int i = 0; i < n; i++) {
                System.out.println(nombres[i] + ": distancia = " + dist[i]
                        + ", ruta = " + camino(pred, i, nombres));
            }
        }

        // Demostración de ciclo negativo: B->C->D->B suma 1 + 2 - 5 = -2
        System.out.println("\n--- Grafo con ciclo negativo ---");
        List<Arista> aristas2 = Arrays.asList(
                new Arista(0,1,1), new Arista(1,2,1), new Arista(2,3,2), new Arista(3,1,-5));
        int[] d2 = new int[4], p2 = new int[4];
        boolean ok = bellmanFord(4, aristas2, 0, d2, p2);
        System.out.println("Resultado: " + (ok ? formato(d2) : "ciclo negativo"));
    }
}