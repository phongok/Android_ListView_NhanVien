package com.example.listview_nhanvien;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv_DS;
    ArrayList<NhanVien> nhanVienArrayList= new ArrayList<NhanVien>();
    MyArrayAdapter adapterNhanVien;
    String[] dv_List;
    String donvi;
    ImageView imgActivity;

    public  static ArrayList<Integer> vitri= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText et_Maso= findViewById(R.id.editText_MaSo);
        EditText et_Hoten= findViewById(R.id.editText_HoTen);
        lv_DS= findViewById(R.id.listView_NV);
        RadioGroup rg_Gioitinh= findViewById(R.id.radiofroup);
        RadioButton rg_Nam= findViewById(R.id.radioButton_Nam);
        RadioButton rg_Nu= findViewById(R.id.radioButton_Nu);
        imgActivity= findViewById(R.id.imageActivity);
        Button btnThoat= findViewById(R.id.button_thoat);


        Spinner spinner_DonVi= findViewById(R.id.spinner_DonVi);

        dv_List= getResources().getStringArray(R.array.donvi_list);

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dv_List);
        spinner_DonVi.setAdapter(adapter);
        spinner_DonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                donvi= dv_List[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        Button bt_ThemNV= findViewById(R.id.button_Them);
        bt_ThemNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maNV= Integer.parseInt(et_Maso.getText().toString());
                String tenNV= et_Hoten.getText().toString();
                String gioitinhNV= ((RadioButton)findViewById(rg_Gioitinh.getCheckedRadioButtonId())).getText().toString();
                byte[] imgBytes = imgByte(imgActivity);


                NhanVien nhanVien= new NhanVien(maNV, tenNV, gioitinhNV, donvi, imgBytes);
                nhanVienArrayList.add(nhanVien);


                adapterNhanVien= new MyArrayAdapter(MainActivity.this, R.layout.item_viewnhanvien, nhanVienArrayList);
                lv_DS.setAdapter(adapterNhanVien);
                adapter.notifyDataSetChanged();

            }
        });


        Button btn_ChupHinh = findViewById(R.id.button_ChupHinh);
        btn_ChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.CAMERA}, 20);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 20);

            }
        });

        Button btn_LayAnh = findViewById(R.id.button_LayAnh);
        btn_LayAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 7);
            }
        });


        Button bt_Xoa= findViewById(R.id.button_Xoa);
        bt_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!vitri.isEmpty()){
                    for(int k:vitri){
                        nhanVienArrayList.remove(k);
                    }
                    vitri.clear();
                    adapterNhanVien.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Chưa chọn!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button bt_Sua = findViewById(R.id.button_Sua);
        bt_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vitri.isEmpty()){
                    for(int k:vitri){
                        NhanVien nv = nhanVienArrayList.get(k);
                        et_Maso.setText(nv.getMaso() + "");
                        et_Hoten.setText(nv.getHoten());
                        if (nv.getGioitinh().equals("Nam"))
                            rg_Nam.setChecked(true);
                        else
                            rg_Nu.setChecked(true);

                        //Drawable drawable = Drawa;
                        //imgActivity.setImageDrawable(nv.getImg());

                        for (int j = 0;j <dv_List.length;j++)
                            if(dv_List[j].equals(nv.getDonvi()))
                                spinner_DonVi.setSelection(j);


                    }
                    vitri.clear();
                    adapterNhanVien.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Chưa chọn!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public byte[] imgByte(ImageView img) {
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        return  bytes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imgActivity.setImageBitmap(captureImage);
        }

        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            imgActivity.setImageURI(selectedImg);

        }
    }






}