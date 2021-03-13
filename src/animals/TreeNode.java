package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class TreeNode {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String content;
    public String nodeType;
    public TreeNode no;
    public TreeNode yes;

    public TreeNode(String content, String nodeType) {
        this(content, nodeType, null, null);
    }

    public TreeNode() {

    }

    public TreeNode(String content, String nodeType, TreeNode no, TreeNode yes) {
        this.content = content;
        this.nodeType = nodeType;
        this.no = no;
        this.yes = yes;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getContent() {
        return content;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return yes == null && no == null;
    }
}


