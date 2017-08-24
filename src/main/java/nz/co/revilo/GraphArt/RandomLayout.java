package nz.co.revilo.GraphArt;

import java.util.List;
import java.util.Random;

public class RandomLayout extends LayoutManager {
    Graph graph;
    Random rnd = new Random();
    public RandomLayout(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void execute() {
        List<NodeItem> nodes = graph.getModel().getAllNodes();
        for (NodeItem node: nodes) {
            double x = rnd.nextDouble() * 800;
            double y = rnd.nextDouble() * 800;
            node.relocate(x, y);
        }
    }
}
