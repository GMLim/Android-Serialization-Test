package gmlim.sample.androidserialization2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout results;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = (LinearLayout) findViewById(R.id.results);
        scroll = (ScrollView) findViewById(R.id.scroll);
    }

    public void testParcelable(View view) {
        TreeNode root = createNode(0);
        Parcel parcel = Parcel.obtain();

        long start = System.nanoTime();

        root.writeToParcel(parcel, 0);

        long finish = System.nanoTime();

        int length = parcel.marshall().length;
        parcel.setDataPosition(0); // reset for reading

        long start2 = System.nanoTime();

        TreeNode restored = TreeNode.CREATOR.createFromParcel(parcel);

        long finish2 = System.nanoTime();

        addResult("parcel: " + (finish - start) / 1_000_000 + "ms; unparcel: " + (finish2 - start2)
                / 1_000_000 + "ms; size: " + length);

        System.out.println(restored);

        parcel.recycle();

    }

    public void testSerializable(View view) {
        TreeNode root = createNode(0);

        ObjectOutputStream out = null;
        ByteArrayOutputStream bas = new ByteArrayOutputStream(1_000_000);

        long start = System.nanoTime();
        try {
            out = new ObjectOutputStream(bas);
            out.writeObject(root);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        long finish = System.nanoTime();

        byte[] byteArray = bas.toByteArray();
        int length = byteArray.length;
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = null;
        TreeNode restored = null;

        long start2 = System.nanoTime();

        try {
            in = new ObjectInputStream(bis);

            restored = (TreeNode) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        long finish2 = System.nanoTime();

        addResult("serialize: " + (finish - start) / 1_000_000 + "ms; deserialize: "
                + (finish2 - start2) / 1_000_000 + "ms; size: " + length);

        System.out.println(restored);
    }

    private TreeNode createNode(int level) {
        if (level < 4) {
            return createRootNode(level + 1);
        } else {
            return createSimpleNode();
        }

    }

    private TreeNode createRootNode(int level) {
        TreeNode root = createSimpleNode();
        root.children = new ArrayList<TreeNode>(10);
        for (int i = 0; i < 10; i++) {
            root.children.add(createNode(level));
        }
        return root;
    }

    private TreeNode createSimpleNode() {
        TreeNode root = new TreeNode();
        root.string0 = "aaaaaaaaaa";
        root.string1 = "bbbbbbbbbb";
        root.string2 = "cccccccccc";
        root.int0 = 111111111;
        root.int1 = 222222222;
        root.int2 = 333333333;
        root.boolean0 = true;
        root.boolean1 = false;
        root.boolean2 = true;
        return root;
    }

    private void addResult(String message) {
        TextView result = new TextView(this);
        result.setText(message);
        result.setPadding(10, 10, 10, 10);
        results.addView(result);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.scrollTo(0, scroll.getBottom());
            }
        });
    }
}
