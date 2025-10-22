package com.nhom12.arkanoid.model;

import com.nhom12.arkanoid.utils.Constants;


public class BrickGroup extends Brick{

    public enum Type{
        NORMAL,
        STRONG,
        UNBREAKABLE,
        EXPLOSIVE
    }

    private final Type type;


    public BrickGroup(double x, double y, double width, double height, Type type) {
        super(x, y, width, height, getDefaultHealth(type));
        this.type = type;
    }

    @Override
    public void hit (){
        //Loại gạch này sẽ bao giờ bị phá
        if(type == Type.UNBREAKABLE){
            return;
        }

        super.hit();
    }


    // Có 4 loại gạch,
    private static  int getDefaultHealth(Type type) {
        switch(type){
            case STRONG : return 3; // loại này có máu:3
            case UNBREAKABLE: return Integer.MAX_VALUE; // loại này ko thể bị phá
            case EXPLOSIVE:// 2 loại cuối có chung 1 loại máu là 1
            case NORMAL:
            default: return 1;
        }
    }
    public Type getType() {
        return type;
    }
}
