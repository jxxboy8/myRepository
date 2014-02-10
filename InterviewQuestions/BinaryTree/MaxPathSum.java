package BinaryTree;

public class MaxPathSum {
	
	public int maxPathSum(TreeNode root) {
        // Start typing your Java solution below
        // DO NOT write main() function
		return (findMaxSum(root))[0];
    }
	
	public int[] findMaxSum(TreeNode root){
		if(root == null){
			int[] results = {Integer.MIN_VALUE, 0};
			return results;
		}
		int[] left = findMaxSum(root.left);
		int[] right = findMaxSum(root.right);
		int[] results = new int[2];
		
		if(left[1] > right[1])
			results[1] = Math.max(left[1] + root.val, root.val);
		else
			results[1] = Math.max(right[1] + root.val, root.val);
		
		int temp = root.val;
		if(left[1] > 0)
			temp = temp + left[1];
		if(right[1] > 0)
			temp = temp + right[1];
		if(temp > left[0]){
			if(temp > right[0])
				results[0] = temp;
			else
				results[0] = right[0];
		}
		else{
			if(left[0] > right[0])
				results[0] = left[0];
			else
				results[0] = right[0];
		}
		return results;
		
	}

}
