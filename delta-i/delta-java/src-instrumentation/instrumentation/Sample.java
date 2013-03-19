package instrumentation;

class Foo {
  int u;
}

public class Sample {

  int info, info1, info2;
  Sample left, right;
  Foo foo;
  
  public void bla(Sample s) { // bla(DeltaRef<Sample> s)
    s.foo = foo; // -> s.set_foo(get_foo()); ok
    foo = s.foo; // -> set_foo(s.get_foo());
    this.foo = s.foo; // -> set_foo(s.get_foo());
  }

}