# Android-Serialization-Test
Parcelable 과 커스텀 된 Serializable 의 속도를 비교합니다.</br>

이 예제는 블로그 포스트 ['Parcelable vs Serializable , 정말 Serializable은 느릴까?'](https://medium.com/@limgyumin/parcelable-vs-serializable-%EC%A0%95%EB%A7%90-serializable%EC%9D%80-%EB%8A%90%EB%A6%B4%EA%B9%8C-bc2b9a7ba810)
</br>
의 테스트를 구현한 안드로이드 프로젝트 샘플 앱 입니다.

</br>
AndroidSerialization 폴더는 코틀린 으로 구현된 테스트 앱을 제공합니다.
</br>
AndroidSerialization2 폴더는 자바 로 구현된 테스트 앱을 제공합니다.

</br></br>
해당 코드의 원소스 는 다음과 같습니다.
</br>
Reference Code : [Alexey Frishman AndroidSerializationTest](https://bitbucket.org/afrishman/androidserializationtest/src/default/)
</br></br>

# Testing Serialization Performance on Android

Usually it is stated that on Android you should use Parcelable instead of Serializable, because Parcelable is much faster. However, I have not found any in-depth tests of both solutions' performance. That's why I create very simple Android project to test it myself.

## Tests

I created a class TreeNode and a tree of objects of this type. This emulates relatively deep object graph of 5 levels. Each TreeNode at one of the first 4 levels has 10 child nodes. The last level is filled with leaf nodes. It is easy to calculate that total number of objects in the tree would be 1 + 10 + 100 + 1000 + 10000 = 11111. Except list of children field, each node has 3 string fields, 3 int fields and 3 boolean fields to emulate some real-world data.

After tree is created it is parceled/unparceled and serialized/deserialized. I've made 10 runs of each procedure on LG Nexus 5 and removed first ubnormally high values. Average results are listed below. You can run the application on any Android device, it would be good to see results on other devices.

## Test Results

Parcel: avg. 250 ms

Unparcel: avg. 232 ms

Serialize: avg. 70 ms

Deserialize: avg. 141 ms

## Conclusion

Usual Java serialization on an average Android device (if done right *) is about 3.6 times faster than Parcelable for writes and about 1.6 times faster for reads. Also it proves that Java Serialization (if done right) is fast storage mechanism that gives acceptable results even with relatively large object graphs of 11000 objects with 10 fields each.

\* The sidenote is that usually everybody who blindly states that "Parcelable is mush faster" compares it to default automatic serialization, which uses much reflection inside. This is unfair comparison, because Parcelable uses manual (and very complicated) procedure of writing data to the stream. What is usually not mentioned is that standard Java Serializable according to the docs can also be done in a manual way, using writeObject() and readObject() methods. For more info see [JavaDocs](http://developer.android.com/reference/java/io/Serializable.html). This is how it should be done for the best performance.
