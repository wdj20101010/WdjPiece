向 OnePiece 致敬!
WdjPiece,一个基于JVM的编程语言.
为了简单,以便构造复杂.

1.你不用申请变量,也不用管它的类型.
  变量本来就在那里,它是全类型.
    abc=123.456
    abc=true
    abc="Hello world"

2.你不用构造类,也不用创建实例.
  这里用片段,它是类也是实例,是模块也是容器.
    a=piece{
        b=piece{}
        c=proce{}
        d=funct(){}
    }
    e=a.b

3.这里除了函数还有过程,
  它从另一个层面反映程序的逻辑.
    main=proce{ runnow{a}}
    a=piece{
        X=0    Y=0
        this=proce{
            enter{this.X=    this.Y=}
            runnow{b}
        }
    }
    b=piece{
        R=0    θ=0
        b1=funct(x,y){ this=(x^2+y^2)^0.5}
        b2=funct(x,y){
            this=atan(y/x)
            if(x<0){ this=this+180}
            if(x>0 & y<0){ this=this+360}
        }
        this=proce{
            this.R=this.b1(a.X,a.Y)
            this.θ=this.b2(a.X,a.Y)
            runnow{c}
        }
    }
    c=proce{ result{a.X  a.Y  b.R  b.θ}}
