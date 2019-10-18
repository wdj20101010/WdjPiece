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
      c=proce(){}
      d=funct(){}
    }
    e=a.b

3.这里除了函数还有过程,过程之间没有调用关系,
  它们会在合适的时候自发执行.
    X=0 Y=0 R=0 θ=0
    main=0
    main=proce(true){main=main+1
      enter{X= Y=}
    }
    step1=0
    step1=proce(main>step1){step1=step1+1
      R=(X^2+Y^2)^0.5
      θ=atan(Y/X)
      if(X<0){θ=θ+180}
      if(X>0&Y<0){θ=θ+360}
    }
    step2=0
    step2=proce(step1>step2){step2=step2+1
      result{X Y R θ}
    }
