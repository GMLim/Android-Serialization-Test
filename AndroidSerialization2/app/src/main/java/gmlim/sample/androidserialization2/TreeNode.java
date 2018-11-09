package gmlim.sample.androidserialization2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TreeNode implements Serializable, Parcelable {
    private static final long serialVersionUID = 1L;

    public List<TreeNode> children;

    public String string0;
    public String string1;
    public String string2;

    public int int0;
    public int int1;
    public int int2;

    public boolean boolean0;
    public boolean boolean1;
    public boolean boolean2;

    public TreeNode() {
    }

    protected TreeNode(Parcel in) {
        if (in.readByte() == 0x01) {
            children = new ArrayList<TreeNode>();
            in.readList(children, TreeNode.class.getClassLoader());
        } else {
            children = null;
        }
        string0 = in.readString();
        string1 = in.readString();
        string2 = in.readString();
        int0 = in.readInt();
        int1 = in.readInt();
        int2 = in.readInt();
        boolean0 = in.readByte() != 0x00;
        boolean1 = in.readByte() != 0x00;
        boolean2 = in.readByte() != 0x00;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(string0);
        out.writeUTF(string1);
        out.writeUTF(string2);
        out.writeInt(int0);
        out.writeInt(int1);
        out.writeInt(int2);
        out.writeBoolean(boolean0);
        out.writeBoolean(boolean1);
        out.writeBoolean(boolean2);
        if (children != null) {
            out.writeInt(children.size());
            for (TreeNode child : children) {
                child.writeObject(out);
            }
        } else {
            out.writeInt(0);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        string0 = in.readUTF();
        string1 = in.readUTF();
        string2 = in.readUTF();
        int0 = in.readInt();
        int1 = in.readInt();
        int2 = in.readInt();
        boolean0 = in.readBoolean();
        boolean1 = in.readBoolean();
        boolean2 = in.readBoolean();
        int childCount = in.readInt();
        if (childCount > 0) {
            children = new ArrayList<TreeNode>(childCount);
            for (int i = 0; i < childCount; i++) {
                TreeNode child = new TreeNode();
                child.readObject(in);
                children.add(child);
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (children == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(children);
        }
        dest.writeString(string0);
        dest.writeString(string1);
        dest.writeString(string2);
        dest.writeInt(int0);
        dest.writeInt(int1);
        dest.writeInt(int2);
        dest.writeByte((byte) (boolean0 ? 0x01 : 0x00));
        dest.writeByte((byte) (boolean1 ? 0x01 : 0x00));
        dest.writeByte((byte) (boolean2 ? 0x01 : 0x00));
    }

    public static final Creator<TreeNode> CREATOR = new Creator<TreeNode>() {
        @Override
        public TreeNode createFromParcel(Parcel in) {
            return new TreeNode(in);
        }

        @Override
        public TreeNode[] newArray(int size) {
            return new TreeNode[size];
        }
    };
}
