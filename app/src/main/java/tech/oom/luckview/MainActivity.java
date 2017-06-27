package tech.oom.luckview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import tech.oom.luckpan.LuckBean;
import tech.oom.luckpan.LuckItemInfo;
import tech.oom.luckpan.NewLuckView;

public class MainActivity extends AppCompatActivity {
    private NewLuckView luckView;
    private int[] images = new int[]{R.drawable.huawei, R.drawable.image_one, R.drawable.iphone, R.drawable.image_one, R.drawable.macbook, R.drawable.image_one, R.drawable.meizu, R.drawable.xiaomi};
    private String[] strs = {"华为手机", "谢谢惠顾", "iPhone 6s", "谢谢惠顾", "mac book", "谢谢惠顾", "魅族手机", "小米手机"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
