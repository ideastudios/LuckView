# LuckView 抽奖转盘

# 效果图
<img src="/screenshots/screenshot.gif"/>

# 属性

```
<!--奇数扇形的填充色-->
<attr name="luck_odd_sector_color" format="color"/>

<!--偶数扇形的填充色-->
<attr name="luck_even_sector_color" format="color"/>

<!--奖品图片的宽度占转盘宽度的比-->
<attr name="luck_item_width_ratio" format="float"/>

<!--奖品图片的高度占转盘宽度的比-->
<attr name="luck_item_height_ratio" format="float"/>

<!--奖品图片距离圆心的偏移长度占圆盘半径长度的比-->
<attr name="luck_item_offset_ratio" format="float"/>

<!--奖项字体大小-->
<attr name="luck_text_size" format="dimension" />

<!--奖项字体颜色-->
<attr name="luck_text_color" format="color" />

<!--奖品名称文字距离圆心的长度和半径之比-->
<attr name="luck_text_offset_ratio" format="float"/>

<!--indicator的宽度和直径的比-->
<attr name="luck_indicator_width_ratio" format="float"/>

<!--indicator的高度和直径的比-->
<attr name="luck_indicator_height_ratio" format="float"/>

<!--indicator的高度和直径的比-->
<attr name="luck_indicator_drawable" format="reference"/>

<!--转一圈所需的时间-->
<attr name="luck_cycle_millis" format="integer"/>
```
# 声明-布局中
```java
    <tech.oom.luckpan.NewLuckView
        android:id="@+id/luck_view"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        app:luck_indicator_drawable="@mipmap/ic_launcher"
        app:luck_indicator_height_ratio="0.3"
        app:luck_indicator_width_ratio="0.208"
        app:luck_text_offset_ratio="0.8"
        app:luck_text_color="@color/colorAccent"
        app:luck_cycle_millis="1000"
        app:luck_even_sector_color="@color/colorPrimary"/>
```