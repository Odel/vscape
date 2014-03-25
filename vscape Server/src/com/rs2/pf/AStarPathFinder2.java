package com.rs2.pf;

import java.util.HashSet;
import java.util.Set;

import com.rs2.model.Position;

/**
 * An implementation of a <code>PathFinder</code> which uses the A* search
 * algorithm. Unlike the <code>DumbPathFinder</code>, this will attempt to find
 * a possible path and is more suited for player following.
 * 
 * @author Graham Edgecombe
 * 
 */
public class AStarPathFinder2 implements PathFinder2 {

	/**
	 * The cost of moving in a straight line.
	 */
	private static final int COST_STRAIGHT = 10;

	/**
	 * Represents a node used by the A* algorithm.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	private static class Node implements Comparable<Node> {

		/**
		 * The parent node.
		 */
		private Node parent;

		/**
		 * The cost.
		 */
		private int cost;

		/**
		 * The heuristic.
		 */
		private int heuristic;

		/**
		 * The depth.
		 */
		private int depth;

		/**
		 * The x coordinate.
		 */
		private final int x;

		/**
		 * The y coordinate.
		 */
		private final int y;

		/**
		 * Creates a node.
		 * 
		 * @param x
		 *            The x coordinate.
		 * @param y
		 *            The y coordinate.
		 */
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Sets the parent.
		 * 
		 * @param parent
		 *            The parent.
		 */
		public void setParent(Node parent) {
			this.parent = parent;
		}

		/**
		 * Gets the parent node.
		 * 
		 * @return The parent node.
		 */
		public Node getParent() {
			return parent;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int getCost() {
			return cost;
		}

		/**
		 * Gets the X coordinate.
		 * 
		 * @return The X coordinate.
		 */
		public int getX() {
			return x;
		}

		/**
		 * Gets the Y coordinate.
		 * 
		 * @return The Y coordinate.
		 */
		public int getY() {
			return y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cost;
			result = prime * result + depth;
			result = prime * result + heuristic;
			result = prime * result + ((parent == null) ? 0 : parent.hashCode());
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (cost != other.cost)
				return false;
			if (depth != other.depth)
				return false;
			if (heuristic != other.heuristic)
				return false;
			if (parent == null) {
				if (other.parent != null)
					return false;
			} else if (!parent.equals(other.parent))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public int compareTo(Node arg0) {
			return cost - arg0.cost;
		}

	}

	private Node current;
	private Node[][] nodes;
	private Set<Node> closed = new HashSet<Node>();
	private Set<Node> open = new HashSet<Node>();

	@Override
	public Path findPath(Position location, int radius, TileMap map, int srcX, int srcY, int dstX, int dstY) {
		if (dstX < 0 || dstY < 0 || dstX >= map.getWidth() || dstY >= map.getHeight()) {
			//System.out.println("shit");
			return null; // out of range
		}

		nodes = new Node[map.getWidth()][map.getHeight()];
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				nodes[x][y] = new Node(x, y);
			}
		}

		open.add(nodes[srcX][srcY]);

		while (open.size() > 0) {
			current = getLowestCost();
			if (current == nodes[dstX][dstY]) {
				break;
			}
			open.remove(current);
			closed.add(current);
			//System.out.println(current.getX());
			//System.out.println(current.getY());
			int x = current.getX(), y = current.getY();

			// north west
			if (x > 0 && map.getTile(x - 1, y).isEasternTraversalPermitted() && map.getTile(x, y).isWesternTraversalPermitted()) {
				if (y < (map.getHeight() - 1) && map.getTile(x, y + 1).isSouthernTraversalPermitted() && map.getTile(x, y).isNorthernTraversalPermitted()) {
					Node n = nodes[x - 1][y + 1];
					examineNode(n);
				}
			}
			// north east
			if (x < (map.getWidth() - 1) && map.getTile(x + 1, y).isWesternTraversalPermitted() && map.getTile(x, y).isEasternTraversalPermitted()) {
				if (y < (map.getHeight() - 1) && map.getTile(x, y + 1).isSouthernTraversalPermitted() && map.getTile(x, y).isNorthernTraversalPermitted()) {
					Node n = nodes[x + 1][y + 1];
					examineNode(n);
				}
			}
			// south west
			if (y > 0 && map.getTile(x, y - 1).isNorthernTraversalPermitted() && map.getTile(x, y).isSouthernTraversalPermitted()) {
				if (x > 0 && map.getTile(x - 1, y).isEasternTraversalPermitted() && map.getTile(x, y).isWesternTraversalPermitted()) {
					Node n = nodes[x - 1][y - 1];
					examineNode(n);
				}
			}
			// south east
			if (y > 0 && map.getTile(x, y - 1).isNorthernTraversalPermitted() && map.getTile(x, y).isSouthernTraversalPermitted()) {
				if (x < (map.getWidth() - 1) && map.getTile(x + 1, y).isWesternTraversalPermitted() && map.getTile(x, y).isEasternTraversalPermitted()) {
					Node n = nodes[x + 1][y - 1];
					examineNode(n);
				}
			}
			// west
			if (x > 0 && map.getTile(x - 1, y).isEasternTraversalPermitted() && map.getTile(x, y).isWesternTraversalPermitted()) {
				Node n = nodes[x - 1][y];
				examineNode(n);
			}
			// east
			if (x < (map.getWidth() - 1) && map.getTile(x + 1, y).isWesternTraversalPermitted() && map.getTile(x, y).isEasternTraversalPermitted()) {
				Node n = nodes[x + 1][y];
				examineNode(n);
			}
			// south
			if (y > 0 && map.getTile(x, y - 1).isNorthernTraversalPermitted() && map.getTile(x, y).isSouthernTraversalPermitted()) {
				Node n = nodes[x][y - 1];
				examineNode(n);
			}
			// north
			if (y < (map.getHeight() - 1) && map.getTile(x, y + 1).isSouthernTraversalPermitted() && map.getTile(x, y).isNorthernTraversalPermitted()) {
				Node n = nodes[x][y + 1];
				examineNode(n);
			}
		}

		if (nodes[dstX][dstY].getParent() == null) {
			//System.out.println("shit3");
			return null;
		}

		Path p = new Path();
		Node n = nodes[dstX][dstY];
		//System.out.println("shit1");
		while (n != nodes[srcX][srcY]) {
			p.addPoint(new Point(n.getX() + location.getX() - radius, n.getY() + location.getY() - radius));
			n = n.getParent();
		}
		p.addPoint(new Point(srcX + location.getX() - radius, srcY + location.getY() - radius));


		return p;
	}

	private Node getLowestCost() {
		Node curLowest = null;
		for (Node n : open) {
			if (curLowest == null) {
				curLowest = n;
			} else {
				if (n.getCost() < curLowest.getCost()) {
					curLowest = n;
				}
			}
		}
		return curLowest;
	}

	private void examineNode(Node n) {
		int heuristic = estimateDistance(current, n);
		int nextStepCost = current.getCost() + heuristic;
		if (nextStepCost < n.getCost()) {
			open.remove(n);
			closed.remove(n);
		}
		if (!open.contains(n) && !closed.contains(n)) {
			n.setParent(current);
			n.setCost(nextStepCost);
			open.add(n);
		}
	}

	/**
	 * Estimates a distance between the two points.
	 * 
	 * @param src
	 *            The source node.
	 * @param dst
	 *            The distance node.
	 * @return The distance.
	 */
	public int estimateDistance(Node src, Node dst) {
		int deltaX = src.getX() - dst.getX();
		int deltaY = src.getY() - dst.getY();
		return (Math.abs(deltaX) + Math.abs(deltaY)) * COST_STRAIGHT;
	}

}
