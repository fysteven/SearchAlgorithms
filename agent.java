import java.io.*;
import java.nio.Buffer;
import java.util.*;
/**
 * Created by Frank on 9/24/14.
 */
public class agent {

    public Vertex[] vertices;
    public String sourcePerson;
    public String destinationPerson;
    public int[][] matrix;



    public ArrayList<Vertex> log = new ArrayList<Vertex>();
    public ArrayList<Vertex> path = new ArrayList<Vertex>();

    public StringBuilder builder = new StringBuilder();

    public static void main(String[] args) throws IOException {
        ShortestPathProject project = new ShortestPathProject();
        FileReader fileReader = new FileReader("input.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        project.readInputFile(bufferedReader);

        agent g = new agent(project.numberOfPeople);

        for (int i = 0; i < project.numberOfPeople ; ++i) {
            g.vertices[i].personName = project.nameOfPeople[i];
            g.vertices[i].nodeNumber = i;
//            g.ver[i].personName = project.nameOfPeople[i];
        }



        for (int i = 0; i < project.numberOfPeople; i++) {

            for (int j = 0; j < project.numberOfPeople; j++) {
                g.matrix[i][j] = project.matrix[i][j];
                if (g.matrix[i][j] > 0) {
                //    g.vertices[i].adjacentVertices[j].cost = g.matrix[i][j];
                }
                //System.out.print(g.matrix[i][j] + " ");
            }
        }


        if (project.algorithm.equals("1")) {
            g.bfs_new(g.vertices[project.source], g.vertices[project.destination], project.numberOfPeople);
        }
        if (project.algorithm.equals("2")) {
            g.dfs_new(g.vertices[project.source], g.vertices[project.destination], project.numberOfPeople);

        }
        if (project.algorithm.equals("3")) {
            g.ucs_new(g.vertices[project.source], g.vertices[project.destination], project.numberOfPeople);
        }


       // System.out.println(outputPath(g.vertices[project.destination]));
        List<Vertex> vertexlist = outputPath(g.vertices[project.destination]);
    //    System.out.print(g.log.get(0).personName);
        g.builder.append(g.log.get(0).personName);
        for (int i = 1; i < g.log.size(); i++) {
    //        System.out.print("-" + g.log.get(i).personName);
            g.builder.append("-" + g.log.get(i).personName);
        }
    //    System.out.println();
        g.builder.append("\n");
    //    System.out.print(vertexlist.get(0).personName);
        g.builder.append(vertexlist.get(0).personName);
        for (int i = 1; i < vertexlist.size(); i++) {
    //        System.out.print("-" + vertexlist.get(i).personName);
            g.builder.append("-" + vertexlist.get(i).personName);
        }
    //    System.out.println();
      //  System.out.print(g.vertices[project.destination].pathCost);
    //    System.out.println();
        g.builder.append("\n");
    //    System.out.println(giveCost(g.vertices[project.destination], g.matrix));
        g.builder.append(giveCost(g.vertices[project.destination], g.matrix));
        if (giveCost(g.vertices[project.destination], g.matrix) > 0) {
            project.writeOutputFile(g.builder);
            System.out.println(g.builder);
        } else {
            g.builder = new StringBuilder();
            g.builder.append("NoPathAvailable");
            project.writeOutputFile(g.builder);
            System.out.println(g.builder.toString());
        }


    }

    public agent(int n) {
        vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex();
            vertices[i].personName = new String();
            //this.vertices[i].adjacentVertices = new Edge[n];
        }
        this.matrix = new int[n][n];

    }

    private int bfs_new(Vertex source, Vertex destination, int numberOfPeople) {
        NodeComparatorBFS nodeComparator = new NodeComparatorBFS();
        PriorityQueue<Vertex> openQueue = new PriorityQueue<Vertex>(15, nodeComparator);

        source.pathCost = 0;
        source.depth = 0;
        source.parentNode = null;

        openQueue.offer(source);

        Queue<Vertex> closedQueue = new LinkedList<Vertex>();

        Vertex currentNode;

        while (true) {
            if (openQueue.isEmpty()) {
                return -1;
            }
            currentNode = openQueue.poll();

           this.log.add(currentNode);

            if (currentNode == destination) {
                return 0;
            }

            int currentNumber = currentNode.nodeNumber;
            for (int i = 0; i < numberOfPeople; i++) {
                if (matrix[i][currentNumber] > 0) {
                    Vertex child = vertices[i];
                    int cost = vertices[i].pathCost;
                    //System.out.println(child.personName);
                    //child.pathCost = currentNode.pathCost + cost;

                        if (!openQueue.contains(child) && !closedQueue.contains(child)) {

                            //child.parentNode = currentNode;
                            child.depth = currentNode.depth + 1;
                            child.pathCost = currentNode.pathCost + 1;
                            child.parentNode = currentNode;
                            openQueue.offer(child);



                        }
                      /*else if (openQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                child.pathCost = currentNode.pathCost + child.pathCost;

                                child.depth = currentNode.depth + 1;
                                openQueue.remove(currentNode);
                                openQueue.offer(child);
                            }
                        } else if (closedQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                closedQueue.remove(currentNode);
                                openQueue.offer(child);
                            }
                        }*/

                }


            }

            closedQueue.offer(currentNode);
            // Collection.sort(openQueue);
        }
    }

    private int dfs_new(Vertex source, Vertex destination, int numberOfPeople) {
        NodeComparatorDFS nodeComparator = new NodeComparatorDFS();
        PriorityQueue<Vertex> openQueue = new PriorityQueue<Vertex>(15, nodeComparator);

        source.pathCost = 0;
        source.depth = 0;
        source.parentNode = null;

        openQueue.offer(source);

        Queue<Vertex> closedQueue = new LinkedList<Vertex>();

        Vertex currentNode;

        while (true) {
            if (openQueue.isEmpty()) {
                return -1;
            }
            currentNode = openQueue.poll();

            this.log.add(currentNode);

            if (currentNode == destination) {
                return 0;
            }

            int currentNumber = currentNode.nodeNumber;
            for (int i = 0; i < numberOfPeople; i++) {
                if (matrix[i][currentNumber] > 0) {
                    Vertex child = vertices[i];
                    int cost = vertices[i].pathCost;
                    //System.out.println(child.personName);
                    //child.pathCost = currentNode.pathCost + cost;

                    if (!openQueue.contains(child) && !closedQueue.contains(child)) {


                        child.depth = currentNode.depth + 1;
                        child.pathCost = currentNode.pathCost + child.pathCost;
                        //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                        child.parentNode = currentNode;
                        openQueue.offer(child);



                    } else if (openQueue.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            child.pathCost = currentNode.pathCost + child.pathCost;
                            //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];

                            child.depth = currentNode.depth + 1;
                            openQueue.remove(currentNode);
                            openQueue.offer(child);
                        }

                    } else if (closedQueue.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            child.pathCost = currentNode.pathCost + child.pathCost;
                            //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                            closedQueue.remove(currentNode);
                            openQueue.offer(child);
                        }
                    }

                }


            }

            closedQueue.offer(currentNode);
            // Collection.sort(openQueue);
        }
    }

    private int ucs_new(Vertex source, Vertex destination, int numberOfPeople) {
        NodeComparatorUCS nodeComparator = new NodeComparatorUCS();
        PriorityQueue<Vertex> openQueue = new PriorityQueue<Vertex>(15, nodeComparator);

        source.pathCost = 0;
        source.depth = 0;
        source.parentNode = null;

        openQueue.offer(source);

        Queue<Vertex> closedQueue = new LinkedList<Vertex>();

        Vertex currentNode;

        while (true) {
            if (openQueue.isEmpty()) {
                return -1;
            }
            currentNode = openQueue.poll();

            this.log.add(currentNode);

            if (currentNode == destination) {
                return 0;
            }

            int currentNumber = currentNode.nodeNumber;
            for (int i = 0; i < numberOfPeople; i++) {
                if (matrix[i][currentNumber] > 0) {
                    Vertex child = vertices[i];
                    int cost = vertices[i].pathCost;
                    //System.out.println(child.personName);
                    //child.pathCost = currentNode.pathCost + cost;

                    if (!openQueue.contains(child) && !closedQueue.contains(child)) {


                        child.depth = currentNode.depth + 1;
                        child.pathCost = currentNode.pathCost + child.pathCost;
                        //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                        child.parentNode = currentNode;
                        openQueue.offer(child);



                    } else if (openQueue.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            child.pathCost = currentNode.pathCost + child.pathCost;
                            //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];

                            child.depth = currentNode.depth + 1;
                            openQueue.remove(currentNode);
                            openQueue.offer(child);
                        }

                    } else if (closedQueue.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            child.pathCost = currentNode.pathCost + child.pathCost;
                            //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                            closedQueue.remove(currentNode);
                            openQueue.offer(child);
                        }
                    }

                }


            }

            closedQueue.offer(currentNode);
            // Collection.sort(openQueue);
        }
    }

    private int dfs_new_stack(Vertex source, Vertex destination, int numberOfPeople) {
        NodeComparatorDFS nodeComparator = new NodeComparatorDFS();
        Stack<Vertex> openQueue = new Stack<Vertex>();

        source.pathCost = 0;
        source.parentNode = null;

        openQueue.push(source);

        Queue<Vertex> closedQueue = new LinkedList<Vertex>();

        Vertex currentNode;

        while (true) {
            if (openQueue.isEmpty()) {
                return -1;
            }
            BufferComparator bufferComparator = new BufferComparator();
            PriorityQueue<Vertex> buffer = new PriorityQueue<Vertex>(15, bufferComparator);

            /*Vertex tempVertex = new Vertex();
            tempVertex = openQueue.pop();*/
            buffer.add(openQueue.pop());



            while (openQueue.isEmpty() == false && openQueue.peek().depth == buffer.peek().depth) {
                buffer.offer(openQueue.pop());
                System.out.println("asdjfowejf");
            }

            while (buffer.isEmpty() == false) {
                openQueue.push(buffer.poll());
            }


      /*      NameComparator nameComparator = new NameComparator();
            Collections.sort(buffer, nameComparator);
*/


            while (openQueue.isEmpty() == false) {
                currentNode = openQueue.pop();

                this.log.add(currentNode);

                if (currentNode == destination) {
                    return 0;
                }

                int currentNumber = currentNode.nodeNumber;
                for (int i = 0; i < numberOfPeople; i++) {
                    if (matrix[i][currentNumber] > 0) {
                        Vertex child = vertices[i];
                        //int cost = vertices[i].pathCost;
                        //child.pathCost = currentNode.pathCost + matrix[i][currentNumber];

                        if (!openQueue.contains(child) && !closedQueue.contains(child)) {

                            //child.parentNode = currentNode;
                            child.depth = currentNode.depth + 1;
                            child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                            //child.pathCost = currentNode.pathCost + child.pathCost;
                            child.parentNode = currentNode;


                            openQueue.push(child);


                        } else if (openQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                                //child.pathCost = currentNode.pathCost + child.pathCost;

                                child.depth = currentNode.depth + 1;
                                child.parentNode = currentNode;
                                openQueue.remove(currentNode);
                                openQueue.push(child);
                            }
                        } else if (closedQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                closedQueue.remove(currentNode);
                                openQueue.push(child);
                            }
                        }

                    }


                }
              /*  Queue<Vertex> children = new LinkedList<Vertex>();
                for (int index = 0; index < numberOfPeople; index++) {

                    if (matrix[currentNode.nodeNumber][index] > 0) {
                        children.add(this.vertices[index]);
                    }

                }

                while (children.isEmpty() == false) {
                    Vertex child = children.poll();
                    if (!buffer.contains(child) && !closedQueue.contains(child)) {
                        child.depth = currentNode.depth + 1;
                        child.pathCost = currentNode.pathCost + matrix[i][currentNumber];
                        //child.pathCost = currentNode.pathCost + child.pathCost;
                        child.parentNode = currentNode;
                        buffer.offer(child);
                    } else if (buffer.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            child.pathCost = currentNode.pathCost + matrix[i][currentNumber];

                            child.depth = currentNode.depth + 1;
                            child.parentNode = currentNode;
                            buffer.remove(currentNode);
                            buffer.offer(child);
                        }
                    } else if (closedQueue.contains(child)) {
                        if (child.pathCost < currentNode.pathCost) {
                            closedQueue.remove(currentNode);
                            buffer.offer(child);
                        }
                    }

                }*/

                closedQueue.offer(currentNode);


                // Collection.sort(openQueue);
            }
        }
    }

    private int dfs_new_backup(Vertex source, Vertex destination, int numberOfPeople) {
        NodeComparatorDFS nodeComparator = new NodeComparatorDFS();
        Stack<Vertex> openQueue = new Stack<Vertex>();

        source.pathCost = 0;
        source.parentNode = null;

        openQueue.push(source);

        Queue<Vertex> closedQueue = new LinkedList<Vertex>();

        Vertex currentNode;

        while (true) {
            if (openQueue.isEmpty()) {
                return -1;
            }

            ArrayList<Vertex> buffer = new ArrayList<Vertex>();

            buffer.add(openQueue.pop());

            while (openQueue.isEmpty() == false && openQueue.peek().pathCost >= buffer.get(0).pathCost) {
                buffer.add(openQueue.pop());
            }

            NameComparator nameComparator = new NameComparator();
            Collections.sort(buffer, nameComparator);

            while (buffer.isEmpty() == false) {
                currentNode = buffer.remove(0);

                this.log.add(currentNode);

                if (currentNode == destination) {
                    return 0;
                }

                int currentNumber = currentNode.nodeNumber;
                for (int i = 0; i < numberOfPeople; i++) {
                    if (matrix[i][currentNumber] > 0) {
                        Vertex child = vertices[i];
                        int cost = vertices[i].pathCost;
                        //child.pathCost = currentNode.pathCost + cost;

                        if (!openQueue.contains(child) && !closedQueue.contains(child)) {

                            //child.parentNode = currentNode;
                            child.depth = currentNode.depth + 1;
                            child.pathCost = currentNode.pathCost + child.pathCost;
                            child.parentNode = currentNode;
                            openQueue.push(child);


                        } else if (openQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                child.pathCost = currentNode.pathCost + child.pathCost;

                                child.depth = currentNode.depth + 1;
                                openQueue.remove(currentNode);
                                openQueue.push(child);
                            }
                        } else if (closedQueue.contains(child)) {
                            if (child.pathCost < currentNode.pathCost) {
                                closedQueue.remove(currentNode);
                                openQueue.push(child);
                            }
                        }

                    }


                }


                closedQueue.offer(currentNode);
                // Collection.sort(openQueue);
            }
        }
    }

    public class NodeComparatorDFS implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.depth < v2.depth) {
                return 1;
            }
            if (v1.depth > v2.depth) {
                return -1;
            }
            if (v1.depth == v2.depth) {

                if (v1.personName.compareToIgnoreCase(v2.personName) > 0) {
                    return 1;
                }
                if (v1.personName.compareToIgnoreCase(v2.personName) < 0) {
                    return -1;
                }

            }
            return 0;
        }
    }

    public class NodeComparatorBFS implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.depth < v2.depth) {
                return -1;
            }
            if (v1.depth > v2.depth) {
                return 1;
            }
            if (v1.depth == v2.depth) {

                if (v1.personName.compareToIgnoreCase(v2.personName) > 0) {
                    return 1;
                }
                if (v1.personName.compareToIgnoreCase(v2.personName) < 0) {
                    return -1;
                }

            }
            return 0;
        }
    }
    public class NodeComparatorUCS implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.pathCost < v2.pathCost) {
                return -1;
            }
            if (v1.pathCost > v2.pathCost) {
                return 1;
            }
            if (v1.pathCost == v2.pathCost) {

                if (v1.personName.compareToIgnoreCase(v2.personName) > 0) {
                    return 1;
                }
                if (v1.personName.compareToIgnoreCase(v2.personName) < 0) {
                    return -1;
                }

            }
            return 0;
        }
    }

    public class BufferComparator implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.personName.compareToIgnoreCase(v2.personName) > 0) {
                return 1;
            }
            if (v1.personName.compareToIgnoreCase(v2.personName) < 0) {
                return -1;
            }
            return 0;
        }
    }
    public class NameComparator implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            if (v1.personName.compareToIgnoreCase(v2.personName) > 0) {
                return 1;
            }
            else {
                return -1;
            }
        }

    }

    public static List<Vertex> outputPath(Vertex goal) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = goal; vertex != null; vertex = vertex.getParentNode()) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public static int giveCost(Vertex goal, int[][] matrix) {
        int cost = 0;
        for (Vertex vertex = goal; vertex != null; vertex = vertex.getParentNode()) {
            if (vertex.getParentNode() != null) {
                cost = cost + matrix[vertex.nodeNumber][vertex.getParentNode().nodeNumber];

            }
        }
        return cost;
    }
}

class Vertex implements Comparator<Vertex> {
    public int nodeNumber;
    public String personName;

    public int depth;
    public int pathCost;
    public Vertex parentNode;
    public Edge[] adjacentVertices;

    public Vertex(String personName, int nodeNumber) {
        this.personName = personName;
        this.nodeNumber = nodeNumber;
    }

    public Vertex() {

    }

    public int compareAlphabeticalOrder(Vertex v) {
        return (this.getPersonName().compareToIgnoreCase(v.getPersonName()));
    }
    /*
        public int compare(Vertex vertex1, Vertex vertex2) {
            if (vertex1.pathCost < vertex2.pathCost) {
                return -1;
            }
            if (vertex1.pathCost > vertex2.pathCost) {
                return 1;
            }
            if (vertex1.nodeNumber < vertex2.nodeNumber) {
                return -1;
            }
            return 0;
        }
    */
    public String getPersonName() {
        return personName;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public int getDepth() {
        return depth;
    }

    public Vertex getParentNode() {
        return parentNode;
    }

    public void setParentNode(Vertex parentNode) {
        this.parentNode = parentNode;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    
    public int compare(Vertex o1, Vertex o2) {
        return (o1.getPersonName().compareToIgnoreCase(o2.getPersonName()));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex vertex = (Vertex) obj;
            if (this.nodeNumber == vertex.nodeNumber) {
                return true;
            }
        }
        return false;
    }


}
class Edge {
    public int cost;
    public Vertex target;

    public Edge(Vertex targetVertex, int cost) {
        this.cost = cost;
        this.target = targetVertex;
    }

    public Edge() {

    }

}
class ShortestPathProject {
    public String algorithm = "";
    public String sourcePerson = "";
    public int source;
    public String destinationPerson = "";
    public int destination;
    public int numberOfPeople;
    private int maximumNumberOfPeople = 50;
    public String[] nameOfPeople;// = new String[maximumNumberOfPeople];
    public int[][] matrix;// = new int[maximumNumberOfPeople][];
    private static final int MAX_VALUE = 99999;
    private Vertex[] vertices;
    private Vertex root;

    public boolean readInputFile(BufferedReader reader) throws IOException {
        Scanner sc = new Scanner(reader).useDelimiter("\\s*\n\\s*");
        algorithm = sc.next();
        System.out.println(algorithm);

        sourcePerson = sc.next();
        System.out.println(sourcePerson);

        destinationPerson = sc.next();
        System.out.println(destinationPerson);

        numberOfPeople = Integer.parseInt(sc.next());
        System.out.println(numberOfPeople);

        nameOfPeople = new String[numberOfPeople];
        matrix = new int[numberOfPeople][numberOfPeople];

        for (int i = 0; i < numberOfPeople; ++i) {
            nameOfPeople[i] = sc.next();
            System.out.println(nameOfPeople[i]);
        }

        for (int i = 0; i < numberOfPeople; ++i) {
            if (sourcePerson.equals(nameOfPeople[i])) {
                source = i;
                break;
            }
        }
        System.out.println(source);

        for (int i = 0; i < numberOfPeople; ++i) {
            if (destinationPerson.equals(nameOfPeople[i])) {
                destination = i;
                break;
            }
        }
        System.out.println(destination);

        String[] somePeople;
        for (int i = 0; i < numberOfPeople; ++i) {

            somePeople = sc.next().split(" ");
            for (int j = 0; j < numberOfPeople; j++) {
                matrix[i][j] = Integer.parseInt(somePeople[j]);
                //matrix[i][j] = Integer.parseInt(sc.next());
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        return true;
    }

    public boolean readFile(FileReader fileReader) throws IOException {
        return true;
    }
    public void readFile() throws IOException {
        String string[] = new String[50];
        Scanner sc = new Scanner("buzhidao \n s zenmeban 1 \n\f 2").useDelimiter("\\s*\n\\s*");

        int i = 0;
        while (sc.hasNext()) {
            string[i] = "=" + sc.next() + "=";
            i++;
            //System.out.println(sc.next());
        }
        System.out.println(string[0]);
        System.out.println(string[1]);
        System.out.println(string[2]);
        System.out.println(string[3]);
    }

    public void readFile2() throws IOException {
        String string;
        int number;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //read a line of String
        string = br.readLine();
        //number = Integer.parseInt(string);
        //System.out.println(number);

    }

    public boolean determineWhichAlgorithm(String algorithmNumber) {
        String string;
        if (algorithmNumber.equals("1")) {
            string = "We are using Breadth-First Search.";
        } else if (algorithmNumber.equals("2")) {
            string = "We are using Depth-First Search";
        } else if (algorithmNumber.equals("3")) {
            string = "We are using Uniform-Cost Search.";
        } else {
            string = "Algorithm Error.";
        }
        System.out.println(string);
        return true;
    }

    public boolean determineWhichAlgorithm() {
        determineWhichAlgorithm(this.algorithm);
        return true;
    }

    public boolean readInputFile2(String file) throws IOException{
        file = "input.txt";
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            System.out.println();
        }
        return true;
    }

    public boolean writeOutputFile2(String str) throws IOException {
        String outputFile = "output.txt";
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(str);
        bufferedWriter.close();

        return true;
    }

    public boolean writeOutputFile(StringBuilder builder) throws IOException{
        //System.out.print(builder.toString());
        String outputFile = "output.txt";
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(builder.toString());
        bufferedWriter.close();
        return true;
    }
    public static void main(String[] args) throws IOException {
        ShortestPathProject project = new ShortestPathProject();
        FileReader fileReader = new FileReader("input.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        project.readInputFile(bufferedReader);
        project.determineWhichAlgorithm();
        /*
        FileReader fileReader = new FileReader("d://input.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        FileWriter fileWriter = new FileWriter("d://output.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            bufferedWriter.write(string + "\r");
            bufferedWriter.write('\n');
            System.out.println(string);

        }
        bufferedReader.close();
        bufferedWriter.close();
        */


    }

    public boolean initialize(int sourceNumber) {
        this.root.setNodeNumber(sourceNumber);
        initializePeople();
        return true;
    }

    public boolean initializePeople() {
        this.vertices = new Vertex[this.numberOfPeople];
        for (int i = 0; i < this.numberOfPeople; i++) {
            vertices[i].setNodeNumber(i);
            vertices[i].setPersonName(this.nameOfPeople[i]);
        }

        return true;
    }




}
