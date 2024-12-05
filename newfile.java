import java.util.*;

public class RoadNetwork {

    private Map<String, Map<String, Integer>> graph; //Location, {neighbor,distance}

    public RoadNetwork() {
        graph = new HashMap<>();
    }

    public void addRoad(String location1, String location2, int distance) {
        graph.computeIfAbsent(location1, k -> new HashMap<>()).put(location2, distance);
        graph.computeIfAbsent(location2, k -> new HashMap<>()).put(location1, distance); //Undirected graph
    }


    public List<String> dijkstra(String start, String end) {
        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            return null; //Start or end location not found
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String location : graph.keySet()) {
            distances.put(location, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(end)) break;

            for (Map.Entry<String, Integer> neighbor : graph.get(current).entrySet()) {
                int distance = distances.get(current) + neighbor.getValue();
                if (distance < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), distance);
                    previous.put(neighbor.getKey(), current);
                    pq.remove(neighbor.getKey()); //Remove and re-add to maintain priority queue order.
                    pq.add(neighbor.getKey());
                }
            }
        }

        return reconstructPath(previous, end);
    }

    private List<String> reconstructPath(Map<String, String> previous, String end) {
        List<String> path = new ArrayList<>();
        String current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }

    public void printNetwork() {
        System.out.println("Road Network:");
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (Map.Entry<String, Integer> neighbor : entry.getValue().entrySet()) {
                System.out.print(neighbor.getKey() + "(" + neighbor.getValue() + ") ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        RoadNetwork network = new RoadNetwork();
        Scanner scanner = new Scanner(System.in);

        // Sample road network (expand as needed)
        network.addRoad("A", "B", 4);
        network.addRoad("A", "C", 2);
        network.addRoad("B", "C", 1);
        network.addRoad("B", "D", 5);
        network.addRoad("C", "D", 8);


        while (true) {
            System.out.println("\nRoad Network Application");
            System.out.println("1. Add Road");
            System.out.println("2. Find Shortest Path");
            System.out.println("3. Print Network");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter location 1: ");
                    String loc1 = scanner.nextLine();
                    System.out.print("Enter location 2: ");
                    String loc2 = scanner.nextLine();
                    System.out.print("Enter distance: ");
                    int dist = scanner.nextInt();
                    network.addRoad(loc1, loc2, dist);
                    break;
                case 2:
                    System.out.print("Enter start location: ");
                    String start = scanner.nextLine();
                    System.out.print("Enter end location: ");
                    String end = scanner.nextLine();
                    List<String> path = network.dijkstra(start, end);
                    if (path != null) {
                        System.out.println("Shortest path: " + path);
                    } else {
                        System.out.println("Invalid start or end location or no path found.");
                    }
                    break;
                case 3:
                    network.printNetwork();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
