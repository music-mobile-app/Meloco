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

import jp.excd.meloco.R;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;

public class DevelopSubMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_sub_menu);
        //GUIの設定
        initGui();
    }
    //--------------------------------------------------------------------------
    // GUIの初期設定
    //--------------------------------------------------------------------------
    private void initGui() {
        WLog.d(this,"initGui()");

        //アダプターの設定
        Resources res = getResources();
        String[] arr = res.getStringArray(R.array.menu_list_devlop_sub);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                arr);

        final ListView listView = (ListView)this.findViewById(R.id.menu_list_devlop_sub);
        listView.setAdapter(adapter);

        //アイテム選択時の処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WLog.d(this,"onItemClick");

                String str = (String)((TextView)view).getText();
                WLog.d(this,"onItemClick" + "selected=" + str);

                if (str.equals("オーディオ設定")) {
                    //オーディオ設定画面

                    //インテントの作成
                    Intent intent = new Intent(getApplication(), DevelopConfigSetActivity.class);

                    //画面遷移
                    startActivity(intent);

                }
                if (str.equals("オーディオ実験")) {
                    //オーディオ実験画面

                    //インテントの作成
                    Intent intent = new Intent(getApplication(), AudioTestActivity.class);

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
                WLog.d(this,"閉じるボタン押下");
                //閉じる。
                finish();
            }
        });


    }
}
