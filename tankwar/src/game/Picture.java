package game;
//  12.图片父类抽取
//	模仿墙类,再做修改,draw方法抽象,
//  修改成员变量权限private为protected,
//  让子类继承直接使用,增加无参构造方法给子弹类使用
public abstract class Picture {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public Picture(){}

    public Picture(int x,int y){
        this.x = x;
        this.y = y;
    }
    public abstract void draw();
    public int getOrder(){
        return 0;
    }
}
