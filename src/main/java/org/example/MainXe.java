package org.example;

public class MainXe {
    public static void main(String[] args) {

        //Xe xe = new Xe("Vision", "Honda", 2026);
        Xe xe1 = new Xe(); //Thêm constructor không tham số Xe() ở Xe.java để new Xe() hoạt động.
        xe1.setHangXe("Honda");
        xe1.setTenXe("Vision");
        xe1.setNamsx(2027);
        xe1.showInfo();
    }
}
