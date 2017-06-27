# LuckView 抽奖转盘

# 效果图
<img src="/screenshots/screenshot.gif"/>

# 属性

```
        <!--奇数扇形的填充色-->
        <attr name="luck_odd_sector_color" format="color"/>

        <!--偶数扇形的填充色-->
        <attr name="luck_even_sector_color" format="color"/>

        <!--奖品图片的宽度占转盘直径的比-->
        <attr name="luck_item_width_ratio" format="float"/>

        <!--奖品图片的高度占转盘直径的比-->
        <attr name="luck_item_height_ratio" format="float"/>

        <!--奖品图片距离圆心的偏移长度占圆盘半径长度的比-->
        <attr name="luck_item_offset_ratio" format="float"/>

        <!--奖项字体大小-->
        <attr name="luck_text_size" format="dimension" />

        <!--奖项字体颜色-->
        <attr name="luck_text_color" format="color" />

        <!--奖品名称文字距离圆心的长度和半径之比-->
        <attr name="luck_text_offset_ratio" format="float"/>

        <!--indicator的宽度和转盘直径的比-->
        <attr name="luck_indicator_width_ratio" format="float"/>

        <!--indicator的高度和转盘直径的比-->
        <attr name="luck_indicator_height_ratio" format="float"/>

        <!--indicator的高度和转盘直径的比-->
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
# Activity中的使用

```java
        luckView = (NewLuckView) findViewById(R.id.luck_view);
        luckView.setIndicatorResourceId(R.drawable.node);
        ArrayList<LuckItemInfo> items = new ArrayList<>();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            LuckItemInfo luckItem = new LuckItemInfo();
            luckItem.prize_name = strs[i];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images[i]);
            bitmaps.add(bitmap);
            items.add(luckItem);
        }

        LuckBean luck = new LuckBean();
        luck.details = items;
//        load数据
        luckView.loadData(luck, bitmaps);
//        luckView.setEnable(false); 设置是否可用 默认为true
        //添加监听 当luckview检测到indicator所在位置被点击时，会自动开始旋转
        luckView.setLuckViewListener(new NewLuckView.LuckViewListener() {
            @Override
            public void onStart() {

                //模拟网络请求获取抽奖结果，然后设置选中项的index值
                luckView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random();
                        int i = random.nextInt(6);
                        luckView.setStop(i);
                    }
                }, 3000);
            }

            @Override
            public void onStop(int index) {

            }
        });

    }
```

# Gradle
[![](https://jitpack.io/v/ideastudios/LuckView.svg)](https://jitpack.io/#ideastudios/LuckView)

1. Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the dependency
```
	dependencies {
	        compile 'com.github.ideastudios:LuckView:0.0.1'
	}

```

# 注意
* LuckView属性中，只有奖品图片的偏移量 和 文字的偏移量 是相对于圆盘半径的，其他的相关属性都是相对于圆盘的直径
* LuckView中奖项的数量大小应该设置为可以能被360整除的数，如果不能被360整除，则会出现相应bug
* LuckView draw不同奖项图片 draw不同奖项名称是通过canvas.rotate(sectorAnger)的方式实现的



# 感谢
该工程参考了[Nipuream/LuckPan](https://github.com/Nipuream/LuckPan) 的相关代码和UI,感谢这位小伙伴