package jp.excd.meloco.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import jp.excd.meloco.BuildConfig;
import jp.excd.meloco.R;
import jp.excd.meloco.utility.CommonUtil;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //画面表示時のアニメーション指定
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_left);

        //メニューに表示する項目を、resから取得する。
        Resources res = getResources();
        String[] arr = res.getStringArray(R.array.menu_list);

        //ビルドモードが開発の場合は、開発メニューを追加する。
        if (BuildConfig.DEBUG) {

            Log.d(CommonUtil.tag(this),"debugMode");
            //追加するメニューを取得
            String addMenu = res.getString(R.string.menu_list_add_for_develop);

            String[] arr2 = new String[arr.length + 1];
            for (int i = 0; i < arr2.length; i++) {
                if (i == arr.length) {
                    //最終要素にメニューを追加
                    arr2[i] = addMenu;
                } else {
                    //最終要素以外は、元のメニューをそのままコピー
                    arr2[i] = arr[i];
                }
            }
            //メニューの入れ替え
            arr = arr2;
        }

        //アダプター設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                arr);

        final ListView listView = (ListView)this.findViewById(R.id.listMain);
        listView.setAdapter(adapter);

        //アイテム選択時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(CommonUtil.tag(this), "onItemClick");

                String str = (String)((TextView)view).getText();
                Log.d(CommonUtil.tag(this), "onItemClick" + "selected=" + str);

                Resources res = getResources();
                //開発メニュー文字列の取得
                String addMenu = res.getString(R.string.menu_list_add_for_develop);

                if (str.equals(addMenu)) {
                    //開発メニューオープン

                    //インテントの作成
                    Intent intent = new Intent(getApplication(), DevelopConfigSetActivity.class);

                    //画面遷移
                    startActivity(intent);


                }

            }
        });
        //閉じるボタンの設定
        final Button closeButton = (Button)this.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               Log.d(CommonUtil.tag(this), "閉じるボタン押下");
               //閉じる。
               finish();
           }
        });
    }
}
