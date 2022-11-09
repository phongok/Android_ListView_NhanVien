package com.example.listview_nhanvien;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<NhanVien> {
    //Màn hình sử dụng layout này
    Activity context;
    //Layout cho từng dòng muốn hiển thị
    int resource;
    //Danh sách dữ liệu muốn hiển thị lên màn hình
    @NonNull List<NhanVien> objects;

    public MyArrayAdapter(@NonNull Activity context, int resource, @NonNull List<NhanVien> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource= resource;
        this.objects= objects;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Đây là 1 lớp để build layout bình thường
        LayoutInflater inflater= this.context.getLayoutInflater();
        //this.resource chính là item.xml, truyền vào khi gọi MyArrayAdapter
        convertView = inflater.inflate(this.resource, null);

        ImageView imgItem=convertView.findViewById(R.id.imgView);
        TextView profile= convertView.findViewById(R.id.tv_view);
        CheckBox checkBoxTT= convertView.findViewById(R.id.checkbox_view);


        NhanVien nhanVien= this.objects.get(position);

        byte[] byteArray = nhanVien.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgItem.setImageBitmap(bitmap);


        profile.setText("  " +nhanVien.getMaso() + "\n  " + nhanVien.getHoten() + "\n  "+ nhanVien.getGioitinh() + "\n  "+
                nhanVien.getDonvi());

        checkBoxTT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBoxTT.isChecked()){
                    MainActivity.vitri.add(position);
                }else {
                    for(int x: MainActivity.vitri){
                        if(x == position){
                            MainActivity.vitri.remove(x);
                        }
                    }
                }
            }
        });




        return convertView;
    }



}

