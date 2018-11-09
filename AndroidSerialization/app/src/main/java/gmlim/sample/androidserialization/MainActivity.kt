package gmlim.sample.androidserialization

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun testParcelable(view: View) {
        val root = createNode(0)
        val parcel = Parcel.obtain()

        val start = System.nanoTime()

        root.writeToParcel(parcel, 0)

        val finish = System.nanoTime()

        val length = parcel.marshall().size
        parcel.setDataPosition(0) // reset for reading

        val start2 = System.nanoTime()

        val restored = TreeNode.CREATOR.createFromParcel(parcel)

        val finish2 = System.nanoTime()

        addResult("parcel: " + (finish - start) / 1000000 + "ms; unparcel: " + (finish2 - start2) / 1000000 + "ms; size: " + length)

        Log.d("stephen.lim", restored.toString())

        parcel.recycle()

    }

    fun testSerializable(view: View) {
        val root = createNode(0)

        var out: ObjectOutputStream? = null
        val bas = ByteArrayOutputStream(1000000)

        val start = System.nanoTime()
        try {
            out = ObjectOutputStream(bas)
            out!!.writeObject(root)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                try {
                    out!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        val finish = System.nanoTime()

        val byteArray = bas.toByteArray()
        val length = byteArray.size
        val bis = ByteArrayInputStream(byteArray)
        var `in`: ObjectInputStream? = null
        var restored: TreeNode? = null

        val start2 = System.nanoTime()

        try {
            `in` = ObjectInputStream(bis)

            restored = `in`!!.readObject() as TreeNode

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        val finish2 = System.nanoTime()

        addResult(
            "serialize: " + (finish - start) / 1000000 + "ms; deserialize: "
                    + (finish2 - start2) / 1000000 + "ms; size: " + length
        )

        Log.d("stephen.lim", restored.toString())
    }

    private fun createNode(level: Int): TreeNode {
        return if (level < 4) {
            createRootNode(level + 1)
        } else {
            createSimpleNode()
        }

    }

    private fun createRootNode(level: Int): TreeNode {
        val root = createSimpleNode()
        root.children = ArrayList<TreeNode>(10)
        for (i in 0..9) {
            (root.children as ArrayList<TreeNode>).add(createNode(level))
        }
        return root
    }

    private fun createSimpleNode(): TreeNode {
        val root = TreeNode()
        root.string0 = "aaaaaaaaaa"
        root.string1 = "bbbbbbbbbb"
        root.string2 = "cccccccccc"
        root.int0 = 111111111
        root.int1 = 222222222
        root.int2 = 333333333
        root.boolean0 = true
        root.boolean1 = false
        root.boolean2 = true
        return root
    }

    private fun addResult(message: String) {
        val result = TextView(this)
        result.text = message
        result.setPadding(10, 10, 10, 10)
        results.addView(result)
        scroll.post(Runnable { scroll.scrollTo(0, scroll.getBottom()) })
    }
}
