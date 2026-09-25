package co.edu.retailgo;

import co.edu.retailgo.minispring.RGComponent;
import co.edu.retailgo.minispring.RGInject;

@RGComponent
public class CircularA {
    @RGInject
    public CircularA(CircularB ignored) { }
}
