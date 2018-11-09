package gmlim.sample.androidserialization

import android.os.Parcel
import android.os.Parcelable
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList

class TreeNode : Serializable, Parcelable {

    var children: MutableList<TreeNode>? = null

    var string0: String? = null
    var string1: String? = null
    var string2: String? = null

    var int0: Int = 0
    var int1: Int = 0
    var int2: Int = 0

    var boolean0: Boolean = false
    var boolean1: Boolean = false
    var boolean2: Boolean = false

    constructor() {}

    protected constructor(`in`: Parcel) {
        if (`in`.readByte().toInt() == 0x01) {
            children = ArrayList()
            `in`.readList(children, TreeNode::class.java.classLoader)
        } else {
            children = null
        }
        string0 = `in`.readString()
        string1 = `in`.readString()
        string2 = `in`.readString()
        int0 = `in`.readInt()
        int1 = `in`.readInt()
        int2 = `in`.readInt()
        boolean0 = `in`.readByte().toInt() != 0x00
        boolean1 = `in`.readByte().toInt() != 0x00
        boolean2 = `in`.readByte().toInt() != 0x00
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeUTF(string0)
        out.writeUTF(string1)
        out.writeUTF(string2)
        out.writeInt(int0)
        out.writeInt(int1)
        out.writeInt(int2)
        out.writeBoolean(boolean0)
        out.writeBoolean(boolean1)
        out.writeBoolean(boolean2)
        if (children != null) {
            out.writeInt(children!!.size)
            for (child in children!!) {
                child.writeObject(out)
            }
        } else {
            out.writeInt(0)
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        string0 = `in`.readUTF()
        string1 = `in`.readUTF()
        string2 = `in`.readUTF()
        int0 = `in`.readInt()
        int1 = `in`.readInt()
        int2 = `in`.readInt()
        boolean0 = `in`.readBoolean()
        boolean1 = `in`.readBoolean()
        boolean2 = `in`.readBoolean()
        val childCount = `in`.readInt()
        if (childCount > 0) {
            children = ArrayList(childCount)
            for (i in 0 until childCount) {
                val child = TreeNode()
                child.readObject(`in`)
                children!!.add(child)
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (children == null) {
            dest.writeByte(0x00.toByte())
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeList(children)
        }
        dest.writeString(string0)
        dest.writeString(string1)
        dest.writeString(string2)
        dest.writeInt(int0)
        dest.writeInt(int1)
        dest.writeInt(int2)
        dest.writeByte((if (boolean0) 0x01 else 0x00).toByte())
        dest.writeByte((if (boolean1) 0x01 else 0x00).toByte())
        dest.writeByte((if (boolean2) 0x01 else 0x00).toByte())
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmField
        val CREATOR: Parcelable.Creator<TreeNode> = object : Parcelable.Creator<TreeNode> {
            override fun createFromParcel(`in`: Parcel): TreeNode {
                return TreeNode(`in`)
            }

            override fun newArray(size: Int): Array<TreeNode?> {
                return arrayOfNulls(size)
            }
        }
    }
}