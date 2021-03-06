package com.stevewedig.blog.digraph.node_graph;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.stevewedig.blog.digraph.node_graph_partial.PartialGraph;
import com.stevewedig.blog.util.LambdaLib.Fn1;

/**
 * A digraph containing nodes.
 */
public interface Graph<Id, Node> extends PartialGraph<Id, Node> {

  // ===========================================================================
  // parents
  // ===========================================================================

  /**
   * Getting an id's parent node set.
   */
  ImmutableSet<Node> parentNodeSet(Id id);

  // ===========================================================================
  // children
  // ===========================================================================

  /**
   * Getting an id's child node set.
   */
  ImmutableSet<Node> childNodeSet(Id id);

  // ===========================================================================
  // ancestors
  // ===========================================================================

  /**
   * Getting an id's ancestor node iterable (its parents, parents' parents, and so on).
   */
  Iterable<Node> ancestorNodeIterable(Id id, boolean inclusive);

  /**
   * Getting an id set's ancestor node iterable (their parents, parents' parents, and so on).
   */
  Iterable<Node> ancestorNodeIterable(Set<Id> ids, boolean inclusive);

  /**
   * Getting an id's ancestor node set (its parents, parents' parents, and so on).
   */
  ImmutableSet<Node> ancestorNodeSet(Id id, boolean inclusive);

  /**
   * Getting an id set's ancestor node set (their parents, parents' parents, and so on).
   */
  ImmutableSet<Node> ancestorNodeSet(Set<Id> ids, boolean inclusive);

  // ===========================================================================
  // descendants
  // ===========================================================================

  /**
   * Getting an id's descendant node iterable (its children, childrens' children, and so on).
   */
  Iterable<Node> descendantNodeIterable(Id id, boolean inclusive);

  /**
   * Getting an id set's descendant node iterable (their children, childrens' children, and so on).
   */
  Iterable<Node> descendantNodeIterable(Set<Id> ids, boolean inclusive);

  /**
   * Getting an id's descendant node set (its children, childrens' children, and so on).
   */
  ImmutableSet<Node> descendantNodeSet(Id id, boolean inclusive);

  /**
   * Getting an id set's descendant node set (their children, childrens' children, and so on).
   */
  ImmutableSet<Node> descendantNodeSet(Set<Id> ids, boolean inclusive);

  // ===========================================================================
  // roots (sources)
  // ===========================================================================

  /**
   * The digraph's root (source) nodes, so the nodes without parents.
   */
  ImmutableSet<Node> rootNodeSet();

  // ===========================================================================
  // leaves (sinks)
  // ===========================================================================

  /**
   * The digraph's leaf (sink) nodes, so the nodes without children.
   */
  ImmutableSet<Node> leafNodeSet();

  // ===========================================================================
  // topological sort
  // ===========================================================================

  /**
   * A topologically sorted list of nodes, with roots (sources) first (will be absent if the digraph
   * is cyclic).
   */
  Optional<ImmutableList<Node>> optionalTopsortNodeList();

  // ===========================================================================
  // generic traversal
  // ===========================================================================

  /**
   * Generic node traversal as an iterable.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param inclusive Whether to include the start nodes in the traversal.
   * @param startId The initial id.
   * @param expand A function mapping a node to the next ids.
   * @return A node iterable corresponding to the traversal.
   */
  Iterable<Node> traverseNodeIterable(boolean depthFirst, boolean inclusive, Id startId,
      Fn1<Node, List<Id>> expand);

  /**
   * Generic node traversal as an iterable.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param inclusive Whether to include the start nodes in the traversal.
   * @param startIds The initial id list.
   * @param expand A function mapping a node to the next ids.
   * @return A node iterable corresponding to the traversal.
   */
  Iterable<Node> traverseNodeIterable(boolean depthFirst, boolean inclusive,
      ImmutableList<Id> startIds, Fn1<Node, List<Id>> expand);

  /**
   * Generic node traversal copied into a list.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param inclusive Whether to include the start nodes in the traversal.
   * @param startIds The initial id.
   * @param expand A function mapping a node to the next ids.
   * @return A node list corresponding to the traversal.
   */
  ImmutableList<Node> traverseNodeList(boolean depthFirst, boolean inclusive, Id startId,
      Fn1<Node, List<Id>> expand);

  /**
   * Generic node traversal copied into a list.
   * 
   * @param depthFirst Whether to traverse depth first or breadth first.
   * @param inclusive Whether to include the start nodes in the traversal.
   * @param startIds The initial id list.
   * @param expand A function mapping a node to the next ids.
   * @return A node list corresponding to the traversal.
   */
  ImmutableList<Node> traverseNodeList(boolean depthFirst, boolean inclusive,
      ImmutableList<Id> startIds, Fn1<Node, List<Id>> expand);

  // ===========================================================================
  // transforming ids into nodes
  // ===========================================================================

  /**
   * Converting ids to a node set.
   */
  ImmutableSet<Node> transformSet(Iterable<Id> ids);

  /**
   * Converting ids to a node list.
   */
  ImmutableList<Node> transformList(Iterable<Id> ids);

  /**
   * Converting optional id to an optional node.
   */
  Optional<Node> transformOptional(Optional<Id> id);

  /**
   * Converting an id iterable to a node iterable.
   */
  Iterable<Node> transformIterable(Iterable<Id> idIterable);

  /**
   * Converting an id iterator to a node iterator.
   */
  Iterator<Node> transformIterator(Iterator<Id> idIterator);

}
