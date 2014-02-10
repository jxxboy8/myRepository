package BinaryTree;
import java.util.ArrayList;
import java.util.LinkedList;

public class BinaryTreeBFS {
	public ArrayList<ArrayList<Integer>> levelOrder(TreeNode root) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
		LinkedList<TreeNode> nodeList = new LinkedList<TreeNode>();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		if(root == null)
			return result;
		int length = 1;
		nodeList.add(root);
		ArrayList<Integer> firstlvl = new ArrayList<Integer>();
		firstlvl.add(root.val);
		result.add(firstlvl);
		int level = 1;
		while(!nodeList.isEmpty()){
			length = nodeList.size();
			level++;
			while(length > 0){
				length--;
				TreeNode curr = nodeList.removeFirst();
				if(curr.left != null){
					nodeList.add(curr.left);
					if(level > result.size()){
						ArrayList<Integer> newlvl = new ArrayList<Integer>();
						result.add(newlvl);
					}
					result.get(level-1).add(curr.left.val);
				}
				if(curr.right != null){
					nodeList.add(curr.right);
					if(level > result.size()){
						ArrayList<Integer> newlvl = new ArrayList<Integer>();
						result.add(newlvl);
					}
					result.get(level-1).add(curr.right.val);
				}
			}
		}
		
		return result;
    }

}
