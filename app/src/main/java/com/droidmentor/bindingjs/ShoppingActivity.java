package com.droidmentor.bindingjs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.goshiv.gochatlibrary.Webgochat;

public class ShoppingActivity extends AppCompatActivity {

    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ordersIntent=new Intent(ShoppingActivity.this,Webgochat.class);
                ordersIntent.putExtra("GMID", "YWU2YzYwMGU4");
                ordersIntent.putExtra("name","Faisal Ali");
                ordersIntent.putExtra("email","faisalazmee1@gmail.com");
                ordersIntent.putExtra("code","91");
                ordersIntent.putExtra("phone","9833173457");
                ordersIntent.putExtra("type","9833173457");
                startActivity(ordersIntent);
            }
        });
    }
}
