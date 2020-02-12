package dohieu.com.pagesize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Item> itemList = new ArrayList<>();
    private AdapterRecycler adapterRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.RecycleviewList);
        random10Data();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycler = new AdapterRecycler(this, itemList, recyclerView);
        recyclerView.setAdapter(adapterRecycler);
        adapterRecycler.setLoadMore(new ILoadmore() {
            @Override
            public void onLoadMore() {
                if (itemList.size() <= 50)// Bạn có thể change max giá trị load ở đây , load tới số lượng như này thì có kéo nữa cũng không load nữa , bỏ điều kiện này đi thì nó cứ thế mà load
                {
                    itemList.add(null); // Add 1 cái null , để làm gì ? Quay lại cái Adapter của chúng ta mà thấy , nếu gặp item null thì nó sẽ coi đó là Loading View
                    adapterRecycler.notifyItemInserted(itemList.size() - 1);// Báo với adapter là có sự thay đổi
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {// Cái này là mình giả lập , bạn có thể replace cái Handler này với hàm fetch tới Web API hoặc database của các bạn để load dữ liệu
                            itemList.remove(itemList.size() - 1);//Gỡ bỏ thằng null vừa thêm vào khi nãy
                            adapterRecycler.notifyItemRemoved(itemList.size());// Báo với adapter là có sự thay đổi

                            // Thêm dữ liệu ngẫu nhiên
                            int index = itemList.size();
                            int end = index + 10;
                            for (int i = index; i < end; i++) {
                                String name = UUID.randomUUID().toString();
                                Item item = new Item(name, name.length());
                                itemList.add(item);
                            }
                            adapterRecycler.notifyDataSetChanged();
                            adapterRecycler.setLoader();
                        }
                    }, 3000);// Thời gian load dữ liệu
                } else {
                    // Khi đã load hết toàn bộ dữ liệu ta thông báo đã tải xong
                    Toast.makeText(MainActivity.this, "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void random10Data() {
        for (int i = 0; i < 10; i++) {
            String name = UUID.randomUUID().toString(); // Tạo 1 chuỗi UUID ngẫu nhiên, UUID là gì thì hỏi google-sensei
            Item item = new Item(name, name.length()); // Tạo mới 1 item model
            itemList.add(item);// Thêm vào danh sách =))
        }
    }
}
